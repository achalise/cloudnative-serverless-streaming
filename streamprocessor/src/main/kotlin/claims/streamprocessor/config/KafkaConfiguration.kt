package claims.streamprocessor.config

import claims.streamprocessor.ClaimCount
import claims.streamprocessor.ClaimCreatedEvent
import claims.streamprocessor.claims
import claims.streamprocessor.claimsStream
import claims.streamprocessor.processClaim
import claims.streamprocessor.processPayment
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.streams.StreamsBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.core.publisher.Sinks


@Configuration
@EnableKafka
@EnableKafkaStreams
class KafkaConfiguration {
    @Value(value = "\${spring.kafka.streams.bootstrap-servers}")
    private val bootstrapAddress: String? = null

    @Bean
    fun admin() = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress))

    @Bean
    fun claimProcessor(): (ByteArray) -> ClaimCreatedEvent {
        return processClaim()
    }

    @Bean
    fun paymentProcessor(): (ByteArray) -> String {
        return processPayment()
    }

    @Bean
    fun claimEvents(): (ByteArray) -> ClaimCount {
        return claims()
    }

    @Bean
    fun kStream(builder: StreamsBuilder): String {
        claimsStream(builder)
        return ""
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
    fun claimsConsumerFactory(): ConsumerFactory<String, ClaimCount> {
        return DefaultKafkaConsumerFactory(configPropertiesForClass(ClaimCount::class.java.name))
    }

    @Bean
    fun claimCountKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, ClaimCount>? {
        val factory: ConcurrentKafkaListenerContainerFactory<String, ClaimCount> =
            ConcurrentKafkaListenerContainerFactory<String, ClaimCount>()
        factory.consumerFactory = claimsConsumerFactory()
        return factory
    }

    @Bean
    fun claimCountListener(claimCountSinks: Sinks.Many<ClaimCount>): ClaimCountListener {
        return ClaimCountListener(claimCountSinks)
    }

}

@KafkaListener(
    topics = ["event-count"], groupId = "CONSUMER_ONE",
    containerFactory = "claimCountKafkaListenerContainerFactory"
)
class ClaimCountListener(private val sink: Sinks.Many<ClaimCount>) {
    private val logger = LoggerFactory.getLogger(ClaimCountListener::class.java)

    @KafkaHandler
    fun processMessage(claimCount: ClaimCount) {
        logger.info("Received claim count $claimCount")
        sink.emitNext(claimCount, { s,e -> true})
    }
}

