package com.arun.claimprocessor

import com.arun.claimprocessor.models.ClaimRequest
import com.arun.claimprocessor.models.FraudCheckResult
import com.arun.claimprocessor.models.PaymentResult
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


@Configuration
@EnableKafka
class KafkaConfig {

    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String? = null

    @Bean
    fun admin() = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress))

    @Bean
    fun topic1() =
        TopicBuilder.name("CLAIMS")
            .build()

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory())
    }

    fun configPropertiesForClass(messageClass: String): Map<String, Any> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        configProps[JsonDeserializer.TRUSTED_PACKAGES] = "*"
        configProps[JsonDeserializer.VALUE_DEFAULT_TYPE] = messageClass
        configProps[JsonDeserializer.USE_TYPE_INFO_HEADERS] = false
        return configProps
    }

    @Bean
    fun claimsConsumerFactory(): ConsumerFactory<String, ClaimRequest> {
        return DefaultKafkaConsumerFactory(configPropertiesForClass(ClaimRequest::class.java.name))
    }

    @Bean
    fun paymentRequestConsumerFactory(): ConsumerFactory<String, FraudCheckResult> {
        return DefaultKafkaConsumerFactory(configPropertiesForClass(FraudCheckResult::class.java.name))
    }

    @Bean
    fun claimsKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, ClaimRequest>? {
        val factory: ConcurrentKafkaListenerContainerFactory<String, ClaimRequest> =
            ConcurrentKafkaListenerContainerFactory<String, ClaimRequest>()
        factory.consumerFactory = claimsConsumerFactory()
        return factory
    }

    @Bean
    fun paymentRequestKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, FraudCheckResult>? {
        val factory: ConcurrentKafkaListenerContainerFactory<String, FraudCheckResult> =
            ConcurrentKafkaListenerContainerFactory<String, FraudCheckResult>()
        factory.consumerFactory = paymentRequestConsumerFactory()
        return factory
    }

    @Bean
    fun paymentMessageListener(fraudCheckFunction: (ClaimRequest) -> FraudCheckResult,
                        kafkaTemplate: KafkaTemplate<String, Any>,
                        paymentFunction: (FraudCheckResult) -> PaymentResult): PaymentMessageListener {
        return PaymentMessageListener(fraudCheckFunction, kafkaTemplate, paymentFunction)
    }

    @Bean
    fun messageListener(fraudCheckFunction: (ClaimRequest) -> FraudCheckResult,
                        kafkaTemplate: KafkaTemplate<String, Any>,
                        paymentFunction: (FraudCheckResult) -> PaymentResult): ClaimsMessageListener {
       return ClaimsMessageListener(fraudCheckFunction, kafkaTemplate, paymentFunction)
    }

    @KafkaListener(topics = ["PAYMENTS"], groupId = "CONSUMER_ONE",
        containerFactory = "paymentRequestKafkaListenerContainerFactory")
    class PaymentMessageListener(val fraudchrckFN: (ClaimRequest) -> FraudCheckResult,
                          val kafkaTemplate: KafkaTemplate<String, Any>,
                          val paymentFunction: (FraudCheckResult) -> PaymentResult) {
        private val logger = LoggerFactory.getLogger(PaymentMessageListener::class.java)

        @KafkaHandler
        fun processPaymentMessage(fraudCheckResult: FraudCheckResult) {
            logger.info("Received payment request: $fraudCheckResult")
            val res = paymentFunction(fraudCheckResult)
            logger.info("Payment Result $res")
            val result = kafkaTemplate.send("PAYMENT_RESULT", res)
            result.thenAccept{
                logger.info("Published PaymentResult $it")
            }
        }
    }

    @KafkaListener(topics = ["CLAIMS"], groupId = "CONSUMER_ONE",
        containerFactory = "claimsKafkaListenerContainerFactory")
    class ClaimsMessageListener(val fraudchrckFN: (ClaimRequest) -> FraudCheckResult,
                          val kafkaTemplate: KafkaTemplate<String, Any>,
                          val paymentFunction: (FraudCheckResult) -> PaymentResult) {
        private val logger = LoggerFactory.getLogger(ClaimsMessageListener::class.java)

        @KafkaHandler(isDefault = true)
        fun procesClaiMessage(claimRequest: ClaimRequest) {
            logger.info("Received claim $claimRequest")
            val res = fraudchrckFN(claimRequest)
            logger.info("Fraudcheck result $res")
            val result = kafkaTemplate.send("PAYMENTS", res)
            result.thenAccept{
                logger.info("Successfully published payment request $it")
            }
        }
    }
}