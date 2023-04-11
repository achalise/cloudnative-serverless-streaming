package com.arun.claimservice.functions

import com.arun.claimservice.ClaimRequest
import com.arun.claimservice.ClaimResponse
import com.arun.claimservice.Status
import com.arun.claimservice.events.ClaimCreatedEvent
import com.arun.claimservice.messaging.MessageService
import com.arun.claimservice.service.EligibilityService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.util.UUID

fun log(name: String): Logger {
    return LoggerFactory.getLogger(name)
}
val logger = log("SubmitClaim")
fun submitClaim(eligibilityService: EligibilityService, messageService: MessageService): (request: ClaimRequest) -> Mono<ClaimResponse> {
   return { claimRequest ->
       log("SubmitClaimFN").info("Received claim request $claimRequest")
       val isEligible = eligibilityService.isEligible(claimRequest)
       logger.info("User is eligible for claim $isEligible")
       val event = ClaimCreatedEvent(UUID.randomUUID().toString(), claimRequest)
       messageService.publishClaimCreatedEvent(event).map {
           ClaimResponse(Status("UNDER_REVIEW", "Application received"), event.correlationId)
       }
   }
}
