package functions

import com.aun.streamprocessor.ClaimCount
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import java.util.Random

private val logger: Logger = LoggerFactory.getLogger("RetrieveClaimCount")

fun retrieveClaimCountFunction() : () -> Flux<ClaimCount> {
    return {
        val random = Random(10)
        Flux.fromArray(arrayOf("A", "B", "A", "C", "D", "A")).map {
            ClaimCount(it, random.nextInt())
        }
    }
}