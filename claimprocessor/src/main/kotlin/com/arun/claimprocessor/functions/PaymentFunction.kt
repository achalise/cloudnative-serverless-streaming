package com.arun.claimprocessor.functions

import com.arun.claimprocessor.models.FraudCheckResult
import com.arun.claimprocessor.models.PaymentResult
import com.arun.claimprocessor.service.PaymentService

fun performPayment(paymentService: PaymentService): (FraudCheckResult) -> PaymentResult {
    return {
        paymentService.performPayment(it)
    }
}