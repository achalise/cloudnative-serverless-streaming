package com.arun.claimservice

import com.arun.claimservice.functions.submitClaim
import com.arun.claimservice.messaging.MessageService
import com.arun.claimservice.service.EligibilityService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import reactor.core.publisher.Mono

@SpringBootApplication
class ClaimserviceApplication {
	private val logger = LoggerFactory.getLogger(ClaimserviceApplication::class.java)

	@Bean
	fun eligibilityService(): EligibilityService {
		return EligibilityService()
	}

	@Bean
	fun submitClaimRequest(eligibilityService: EligibilityService, messageService: MessageService): (request:ClaimRequest) -> Mono<ClaimResponse> {
		return submitClaim(eligibilityService, messageService)
	}
}

fun main(args: Array<String>) {
	runApplication<ClaimserviceApplication>(*args)

}


