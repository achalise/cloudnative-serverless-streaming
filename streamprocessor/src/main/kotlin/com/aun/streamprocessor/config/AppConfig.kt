package com.aun.streamprocessor.config

import com.aun.streamprocessor.ClaimCount
import functions.retrieveClaimCountFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Configuration
class AppConfig {
    @Bean
    fun claimCountSink(): Sinks.Many<ClaimCount> {
        val replaySink = Sinks.many().multicast().onBackpressureBuffer<ClaimCount>()
        return replaySink
    }

    @Bean
    fun retrieveClaimCount(claimCountSinks: Sinks.Many<ClaimCount>): () -> Flux<ClaimCount> {
        return retrieveClaimCountFunction(claimCountSinks)
    }

    @Bean
    fun corsWebFilter(): CorsWebFilter? {
        val corsConfig = CorsConfiguration()
        corsConfig.allowedOrigins = mutableListOf("*")
        corsConfig.maxAge = 8000L
        corsConfig.addAllowedMethod("*")
        corsConfig.addAllowedHeader("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsWebFilter(source)
    }
}