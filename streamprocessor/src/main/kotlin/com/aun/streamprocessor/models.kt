package com.aun.streamprocessor

data class ClaimRequest(var firstName: String, var lastName: String, var email: String,
                        var amount: Long, var claimType: String)
