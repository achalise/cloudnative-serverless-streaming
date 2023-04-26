package com.arun.claimprocessor.functions

import com.arun.claimprocessor.models.ClaimCreatedEvent
import com.arun.claimprocessor.models.ClaimRequest
import com.arun.claimprocessor.models.FraudCheckResult
import com.arun.claimprocessor.service.FraudCheckService

fun performFraudCheck(fraudCheckService: FraudCheckService): (ClaimCreatedEvent) -> FraudCheckResult {
    return {
       fraudCheckService.performFraudCheck(it)
    }
}