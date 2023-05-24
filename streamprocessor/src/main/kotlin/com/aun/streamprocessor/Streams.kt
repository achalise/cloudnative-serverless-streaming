package com.aun.streamprocessor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.Produced


private val objectMapper = ObjectMapper().registerModule(kotlinModule())

fun claimsStream(streamsBuilder: StreamsBuilder) {
    val stream = streamsBuilder
        .stream("CLAIMS", Consumed.with(Serdes.String(), Serdes.String()))
        .peek { _, v ->
            println("received claim $v")
        }
        .map { k, v ->
            KeyValue(objectMapper.readValue(v, ClaimCreatedEvent::class.java).claimRequest.claimType, 1)
        }.groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
        .count()
        .toStream()
        .peek { k, v ->
            println("mapped stream $k and $v")
        }
        .map { k, v ->
            KeyValue("count", objectMapper.writeValueAsString(ClaimCount(k, v)))
        }.to("event-count", Produced.with(Serdes.String(), Serdes.String()))
}