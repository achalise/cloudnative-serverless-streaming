package com.aun.streamprocessor

import functions.retrieveClaimCountFunction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks


@SpringBootApplication
class StreamprocessorApplication {

	@Bean
	fun claimCountSink(): Sinks.Many<ClaimCount> {
		val replaySink = Sinks.many().multicast().onBackpressureBuffer<ClaimCount>()
		return replaySink
	}

	@Bean
	fun retrieveClaimCount(claimCountSinks: Sinks.Many<ClaimCount>): () -> Flux<ClaimCount> {
		return retrieveClaimCountFunction(claimCountSinks)
	}
}

fun main(args: Array<String>) {
	runApplication<StreamprocessorApplication>(*args)
}

