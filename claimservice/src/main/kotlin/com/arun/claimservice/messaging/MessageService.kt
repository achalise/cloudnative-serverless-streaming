package com.arun.claimservice.messaging

import com.arun.claimservice.events.ClaimCreatedEvent
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import reactor.core.publisher.Mono

interface MessageService {
    fun publishClaimCreatedEvent(event: ClaimCreatedEvent): Mono<SendMessageResult> {
        TODO()
    }
}

class KafkaMessageService(private val kafkaTemplate: KafkaTemplate<String, String>, private val claimTopic: String): MessageService {
    private val logger = LoggerFactory.getLogger(MessageService::class.java)
    private val objectMapper = jacksonObjectMapper()
    override fun publishClaimCreatedEvent(event: ClaimCreatedEvent): Mono<SendMessageResult> {
        return Mono.fromFuture(kafkaTemplate.send(claimTopic, objectMapper.writeValueAsString(event)))
            .map { SendMessageResult("SUCCESS") }
            .doOnSuccess {
                logger.info("Successfully published event $event")
            }
    }
}

data class SendMessageResult(var status: String)