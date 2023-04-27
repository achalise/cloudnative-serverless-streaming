package com.aun.streamprocessor

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG
import org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG
import org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.core.KafkaAdmin


@Configuration
@EnableKafka
@EnableKafkaStreams
class KafkaConfiguration {
    //@Value(value = "\${spring.kafka.bootstrap-servers}")
    @Value(value = "\${spring.kafka.streams.bootstrap-servers}")
    private val bootstrapAddress: String? = null

    @Bean
    fun admin() = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress))

    @Bean
    fun claimProcessor(): (ByteArray) -> ClaimCreatedEvent {
        return processClaim()
    }

    @Bean
    fun claimRequestProcessor(): (ByteArray) -> String {
        return processClaimRequest()
    }

    @Bean
    fun claimEvents(): (ByteArray) -> Unit  {
        return claims()
    }

    @Bean
    fun kStream(builder: StreamsBuilder): String {
        claimsStream(builder)
        return ""
    }
}
