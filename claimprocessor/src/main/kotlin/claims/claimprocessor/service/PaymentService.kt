package claims.claimprocessor.service

import claims.claimprocessor.models.FraudCheckResult
import claims.claimprocessor.models.PaymentResult
import claims.claimprocessor.models.Status
import claims.claimprocessor.models.StatusCode

class PaymentService {
    fun performPayment(fraudCheckResult: FraudCheckResult): PaymentResult {
        val (correlationId, claimRequest) = fraudCheckResult
        return PaymentResult(correlationId, claimRequest, Status(StatusCode.PAYMENT_PASSED, "Payment Successful"))
    }
}