package claims.streamprocessor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Branched
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.kstream.TimeWindows
import java.time.Duration


private val objectMapper = ObjectMapper().registerModule(kotlinModule())

fun claimsStream(streamsBuilder: StreamsBuilder) {
    val windowSize: Duration = Duration.ofSeconds(30)
    val tumblingWindow = TimeWindows.ofSizeWithNoGrace(windowSize)

    val stream = streamsBuilder
        .stream("CLAIMS", Consumed.with(Serdes.String(), Serdes.String()))
        .peek { _, v ->
            println("received claim $v")
        }
        .map { k, v ->
            KeyValue(objectMapper.readValue(v, ClaimCreatedEvent::class.java).claimRequest.claimType, 1)
        }.groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
        .windowedBy(tumblingWindow)
        .count()
        .toStream()
        .peek { k, v ->
            println("mapped stream $k and $v")
        }
        .map { k, v ->
            KeyValue("count", objectMapper.writeValueAsString(ClaimCount(k.key(), v)))
        }.to("event-count", Produced.with(Serdes.String(), Serdes.String()))
}
