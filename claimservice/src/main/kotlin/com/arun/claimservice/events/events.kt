package com.arun.claimservice.events

import com.arun.claimservice.ClaimRequest
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class ClaimCreatedEvent(var correlationId: String, var claimRequest: ClaimRequest)
