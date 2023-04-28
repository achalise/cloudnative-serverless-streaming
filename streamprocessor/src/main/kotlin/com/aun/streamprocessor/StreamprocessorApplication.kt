package com.aun.streamprocessor

import functions.retrieveClaimCountFunction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux

@SpringBootApplication
class StreamprocessorApplication {
	@Bean
	fun retrieveClaimCount(): () -> Flux<ClaimCount> {
		return retrieveClaimCountFunction()
	}
}

fun main(args: Array<String>) {
	runApplication<StreamprocessorApplication>(*args)
}

