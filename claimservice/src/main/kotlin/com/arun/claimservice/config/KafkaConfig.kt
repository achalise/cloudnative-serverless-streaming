package com.arun.claimservice.config

import com.arun.claimservice.messaging.KafkaMessageService
import com.arun.claimservice.messaging.MessageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate


@Configuration
class KafkaConfig {

    @Value("\${application.claim.topic}")
    private val claimTopic: String = ""

    @Bean
    fun topic1() =
        TopicBuilder.name("CLAIMS")
            .build()

    @Bean
    fun topic2() =
        TopicBuilder.name("ClaimCreated")
            .build()

    @Bean
    fun messageService(kafkaTemplate: KafkaTemplate<String, String>): MessageService {
        return KafkaMessageService(kafkaTemplate, claimTopic)
    }

}