package tech.jaya.ridely.domain.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.ridely.domain.Passenger

data class PassengerCreation(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty(required = true)
    val email: String,
) {

    fun toPassenger(): Passenger {
        return Passenger(
            name = this.name,
            email = this.email,
            inTraveling = false
        )
    }
}

data class PassengerCreationResponse(
    val id: Long,
    val name: String,
    val email: String,
    val inTraveling: Boolean,
    val activationDate: String
)

fun Passenger.toResponse(): PassengerCreationResponse {
    return PassengerCreationResponse(
        id = this.id!!,
        name = this.name,
        email = this.email,
        inTraveling = this.inTraveling,
        activationDate = this.activationDate.toString(),
    )
}