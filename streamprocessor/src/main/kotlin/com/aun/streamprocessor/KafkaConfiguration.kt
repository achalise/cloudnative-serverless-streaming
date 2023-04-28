package com.aun.streamprocessor

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.streams.StreamsBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.core.KafkaAdmin


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
