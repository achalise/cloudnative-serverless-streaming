package claims.claimprocessor.functions

import claims.claimprocessor.models.FraudCheckResult
import claims.claimprocessor.models.PaymentResult
import claims.claimprocessor.service.PaymentService

fun performPayment(paymentService: PaymentService): (FraudCheckResult) -> PaymentResult {
    return {
        paymentService.performPayment(it)
    }
}