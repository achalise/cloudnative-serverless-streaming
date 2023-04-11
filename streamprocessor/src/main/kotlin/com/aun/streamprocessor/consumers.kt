package com.aun.streamprocessor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.kafka.streams.kstream.KStream
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("ClaimsConsumer")

fun claims(): (KStream<String, ClaimRequest>) -> Unit {
    return {
        it.peek { key, value ->
            logger.info("Received $key and value: $value")
        }
    }
}

fun processClaim(): (ByteArray) -> ClaimRequest {
    return {
        logger.info("Processed claim $it")
        val stringValue = String(it)
        logger.info("String value ${stringValue}")
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
        mapper.readValue<ClaimRequest>(stringValue)
    }
}

fun processClaimRequest(): (ByteArray) -> String {
    return {
        logger.info("Processed Claim Request $it")
        "PROCESSED"
    }
}