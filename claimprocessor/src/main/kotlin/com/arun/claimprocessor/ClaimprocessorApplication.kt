package com.arun.claimprocessor

import com.arun.claimprocessor.functions.performFraudCheck
import com.arun.claimprocessor.functions.performPayment
import com.arun.claimprocessor.models.ClaimRequest
import com.arun.claimprocessor.models.FraudCheckResult
import com.arun.claimprocessor.models.PaymentResult
import com.arun.claimprocessor.service.FraudCheckService
import com.arun.claimprocessor.service.PaymentService
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
	fun fraudCheckFunction(fraudCheckService: FraudCheckService): (ClaimRequest) -> FraudCheckResult {
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
