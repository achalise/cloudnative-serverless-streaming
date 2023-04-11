package com.arun.claimprocessor.service

import com.arun.claimprocessor.models.ClaimRequest
import com.arun.claimprocessor.models.FraudCheckResult
import com.arun.claimprocessor.models.Status
import com.arun.claimprocessor.models.StatusCode

class FraudCheckService {
    fun performFraudCheck(claimRequest: ClaimRequest): FraudCheckResult {
        return FraudCheckResult(claimRequest, Status(StatusCode.FRAUD_CHECK_PASSED, "Fraud check successful"))
    }
}