package com.arun.claimprocessor.models

data class ClaimRequest(var firstName: String, var lastName: String, var email: String,
                        var amount: Long, var claimType: String,
    var status: Status = Status( StatusCode.NEW, "New Application")
)
data class Address(var street: String, var suburb: String, var postcode: String, var state: String)
data class Status(var code: StatusCode, var message: String)

data class ClaimResponse(var status: Status, var correlationId: String)
enum class StatusCode { NEW, FRAUD_CHECK_INIT, FRAUD_CHECK_PASSED, PAYMENT_INIT, PAYMENT_PASSED}
data class FraudCheckResult(var claimRequest: ClaimRequest, var status: Status)
data class PaymentResult(var claimRequest: ClaimRequest, var status: Status)