package functions

import claims.streamprocessor.ClaimCount
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

private val logger: Logger = LoggerFactory.getLogger("RetrieveClaimCount")

fun retrieveClaimCountFunction(claimCountSinks: Sinks.Many<ClaimCount>) : () -> Flux<ClaimCount> {
    return {
        claimCountSinks.asFlux().doOnEach {
            logger.info("emitted item $it")
        }
    }
}