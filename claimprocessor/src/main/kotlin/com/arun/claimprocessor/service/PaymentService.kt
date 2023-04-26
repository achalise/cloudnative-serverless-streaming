package com.arun.claimprocessor.service

import com.arun.claimprocessor.models.FraudCheckResult
import com.arun.claimprocessor.models.PaymentResult
import com.arun.claimprocessor.models.Status
import com.arun.claimprocessor.models.StatusCode

class PaymentService {
    fun performPayment(fraudCheckResult: FraudCheckResult): PaymentResult {
        val (correlationId, claimRequest) = fraudCheckResult
        return PaymentResult(correlationId, claimRequest, Status(StatusCode.PAYMENT_PASSED, "Payment Successful"))
    }
}