package claims.claimprocessor.service

import claims.claimprocessor.models.ClaimCreatedEvent
import claims.claimprocessor.models.FraudCheckResult
import claims.claimprocessor.models.Status
import claims.claimprocessor.models.StatusCode

class FraudCheckService {
    fun performFraudCheck(claimCreatedEvent: ClaimCreatedEvent): FraudCheckResult {
        return FraudCheckResult(claimCreatedEvent.correlationId, claimCreatedEvent.claimRequest, Status(StatusCode.FRAUD_CHECK_PASSED, "Fraud check successful"))
    }
}