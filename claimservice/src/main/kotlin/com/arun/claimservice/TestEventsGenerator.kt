package com.arun.claimservice

import com.arun.claimservice.events.ClaimCreatedEvent
import com.arun.claimservice.messaging.MessageService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.*
import kotlin.random.Random

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = arrayOf("generate.testevents"), havingValue = "true")
class ScheduledJobsConfig(private val messageService: MessageService) {
    @Scheduled(fixedRate = 300)
    fun scheduleFixedRateTask() {
       generateRandomClaimRequest(messageService)
    }
}

fun generateRandomClaimRequest(messageService: MessageService) {
    val claimRequest = ClaimRequest("firstName", "lastName", "email@email.com", claimAmount(), claimType())
    val event = ClaimCreatedEvent(UUID.randomUUID().toString(), claimRequest)
    messageService.publishClaimCreatedEvent(event).subscribe()
}

fun claimType(): String {
    val claimTypes = mutableListOf("A", "B", "C")
    val rand = Random.nextInt(3)
    return claimTypes[rand]
}

fun claimAmount(): Long {
    return Random.nextLong(300, 1000);
}