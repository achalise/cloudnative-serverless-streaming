package com.aun.streamprocessor

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.streams.kstream.KStream
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.support.serializer.JsonDeserializer
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Configuration
class KafkaConfiguration {
    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String? = null

    @Bean
    fun admin() = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress))

    @Bean
    fun claimProcessor(): (ByteArray) -> ClaimRequest {
        return processClaim()
    }

    @Bean
    fun claimRequestProcessor(): (ByteArray) -> String {
        return processClaimRequest()
    }
}