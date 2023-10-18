package claims.claimprocessor.functions

import claims.claimprocessor.models.ClaimCreatedEvent
import claims.claimprocessor.models.ClaimRequest
import claims.claimprocessor.models.FraudCheckResult
import claims.claimprocessor.service.FraudCheckService

fun performFraudCheck(fraudCheckService: FraudCheckService): (ClaimCreatedEvent) -> FraudCheckResult {
    return {
       fraudCheckService.performFraudCheck(it)
    }
}