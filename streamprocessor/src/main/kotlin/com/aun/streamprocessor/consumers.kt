package com.aun.streamprocessor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("ClaimsConsumer")

fun claims(): (ByteArray) -> ClaimCount {
    return {
        val str = String(it)
        logger.info("Received ClaimCount $str")
        val claimCount = ObjectMapper().registerModule(kotlinModule()).readValue<ClaimCount>(str)
        logger.info("Received count $claimCount")
        claimCount
    }
}

fun processClaim(): (ByteArray) -> ClaimCreatedEvent {
    return {
        logger.info("Processed claim ${String(it)}")
        val stringValue = String(it)
        val mapper = ObjectMapper().registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        mapper.readValue<ClaimCreatedEvent>(stringValue)
    }
}

fun processClaimRequest(): (ByteArray) -> String {
    return {
        logger.info("Processed Claim Request ${String(it)}")
        "PROCESSED"
    }
}