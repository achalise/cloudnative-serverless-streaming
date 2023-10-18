package claims.claimprocessor

import claims.claimprocessor.functions.performFraudCheck
import claims.claimprocessor.functions.performPayment
import claims.claimprocessor.models.ClaimCreatedEvent
import claims.claimprocessor.models.ClaimRequest
import claims.claimprocessor.models.FraudCheckResult
import claims.claimprocessor.models.PaymentResult
import claims.claimprocessor.service.FraudCheckService
import claims.claimprocessor.service.PaymentService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ClaimprocessorApplication {

	@Bean
	fun fraudCheckService(): FraudCheckService {
		return FraudCheckService()
	}

	@Bean
	fun paymentService(): PaymentService {
		return PaymentService()
	}

	@Bean
	fun fraudCheckFunction(fraudCheckService: FraudCheckService): (ClaimCreatedEvent) -> FraudCheckResult {
		return performFraudCheck(fraudCheckService)
	}

	@Bean
	fun processPaymentFunction(paymentService: PaymentService) : (FraudCheckResult) -> PaymentResult {
		return performPayment(paymentService)
	}
}

fun main(args: Array<String>) {
	runApplication<ClaimprocessorApplication>(*args)
}
