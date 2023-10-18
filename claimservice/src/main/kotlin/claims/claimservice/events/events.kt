package claims.claimservice.events

import claims.claimservice.ClaimRequest
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class ClaimCreatedEvent(var correlationId: String, var claimRequest: ClaimRequest)
