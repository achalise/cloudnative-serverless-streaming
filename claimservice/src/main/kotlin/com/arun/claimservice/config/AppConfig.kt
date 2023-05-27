package com.arun.claimservice.config

import com.arun.claimservice.ClaimRequest
import com.arun.claimservice.ClaimResponse
import com.arun.claimservice.functions.submitClaim
import com.arun.claimservice.messaging.MessageService
import com.arun.claimservice.service.EligibilityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

@Configuration
class AppConfig {
    @Bean
    fun eligibilityService(): EligibilityService {
        return EligibilityService()
    }

    @Bean
    fun submitClaimRequest(eligibilityService: EligibilityService, messageService: MessageService): (request: ClaimRequest) -> Mono<ClaimResponse> {
        return submitClaim(eligibilityService, messageService)
    }
}