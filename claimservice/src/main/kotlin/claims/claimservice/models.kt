package claims.claimservice

data class ClaimRequest(var firstName: String, var lastName: String, var email: String, var amount: Long, var claimType: String)
data class Address(var street: String, var suburb: String, var postcode: String, var state: String)
data class Status(var code: String, var message: String)

data class ClaimResponse(var status: Status, var correlationId: String)