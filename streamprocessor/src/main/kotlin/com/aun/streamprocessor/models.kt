package com.aun.streamprocessor

import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class ClaimRequest(var firstName: String, var lastName: String, var email: String,
                        var amount: Long, var claimType: String)
@JsonSerialize
data class ClaimCreatedEvent(var correlationId: String, var claimRequest: ClaimRequest)
data class ClaimCount(var claimType: String, var count: Int)