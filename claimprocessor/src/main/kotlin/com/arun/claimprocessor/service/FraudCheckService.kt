package com.arun.claimprocessor.service

import com.arun.claimprocessor.models.ClaimCreatedEvent
import com.arun.claimprocessor.models.FraudCheckResult
import com.arun.claimprocessor.models.Status
import com.arun.claimprocessor.models.StatusCode

class FraudCheckService {
    fun performFraudCheck(claimCreatedEvent: ClaimCreatedEvent): FraudCheckResult {
        return FraudCheckResult(claimCreatedEvent.correlationId, claimCreatedEvent.claimRequest, Status(StatusCode.FRAUD_CHECK_PASSED, "Fraud check successful"))
    }
}