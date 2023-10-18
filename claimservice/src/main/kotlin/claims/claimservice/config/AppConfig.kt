package claims.claimservice.config

import claims.claimservice.ClaimRequest
import claims.claimservice.ClaimResponse
import claims.claimservice.functions.submitClaim
import claims.claimservice.messaging.MessageService
import claims.claimservice.service.EligibilityService
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