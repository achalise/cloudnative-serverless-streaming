package claims.streamprocessor

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ClaimRequest(var firstName: String, var lastName: String, var email: String,
                        var amount: Long, var claimType: String)
@JsonSerialize
data class ClaimCreatedEvent(var correlationId: String, var claimRequest: ClaimRequest)
data class ClaimCount(var claimType: String, var count: Long, var timeStamp: String = LocalTime.now().format(
    DateTimeFormatter.ofPattern("hh:mm:ss")))