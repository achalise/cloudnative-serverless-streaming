package functions

import com.aun.streamprocessor.ClaimCount
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.*

private val logger: Logger = LoggerFactory.getLogger("RetrieveClaimCount")

fun retrieveClaimCountFunction(claimCountSinks: Sinks.Many<ClaimCount>) : () -> Flux<ClaimCount> {
    return {
        claimCountSinks.asFlux().doOnEach {
            println("emitted item $it")
        }
    }
}