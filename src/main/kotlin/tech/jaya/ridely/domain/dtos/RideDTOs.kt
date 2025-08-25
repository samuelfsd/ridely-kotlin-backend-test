package tech.jaya.ridely.domain.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.ridely.domain.Driver
import tech.jaya.ridely.domain.Ride
import tech.jaya.ridely.domain.enums.Status
import java.math.BigDecimal

class PassengerRequest(
    @JsonProperty(required = true)
    val name: String,
    @JsonProperty(required = true)
    val email: String
)

data class LocationDto(
    val address: String,
    val latitude: Double,
    val longitude: Double
)


data class RequestDriver(
    @JsonProperty(required = true)
    val passenger: PassengerRequest,

    @JsonProperty(required = true)
    val pickUp: LocationDto,

    @JsonProperty(required = true)
    val dropOff: LocationDto,

    val requestId: String? = null
) {
    fun toRide(driver: Driver) = Ride(
        pickUpAddress = this.pickUp.address,
        pickUpLatitude = this.pickUp.latitude,
        pickUpLongitude = this.pickUp.longitude,
        dropOffAddress = this.dropOff.address,
        dropOffLatitude = this.dropOff.latitude,
        dropOffLongitude = this.dropOff.longitude,
        passengerName = passenger.name,
        passengerEmail = passenger.email,
        driver = driver
    )
}

data class FinishRideRequest(
    @JsonProperty(required = true)
    val id: Long,
    @JsonProperty(required = true)
    val price: BigDecimal
)

data class ActionRideRequest(
    @JsonProperty(required = true)
    val id: Long
)

class PassengerResponse(
    val name: String,
    val email: String
)

class RequestDriverResponse private constructor(
    val id: Long,
    val driver: DriverDto,
    val status: Status,
    val dropOff: String,
    val pickUp: String,
) {
    data class DriverDto(
        val name: String,
        val car: CarDto
    ) {
        data class CarDto(
            val licensePlate: String,
            val model: String,
            val color: String
        )
    }

    companion object {
        fun fromRide(ride: Ride) = RequestDriverResponse(
            id = ride.id!!,
            dropOff = ride.dropOffAddress!!,
            pickUp = ride.pickUpAddress!!,
            status = ride.status!!,
            driver = DriverDto(
                name = ride.driver!!.name,
                car = DriverDto.CarDto(
                    licensePlate = ride.driver!!.carLicensePlate,
                    model = ride.driver!!.carModel,
                    color = ride.driver!!.carColor
                )
            ),
        )
    }
}

data class RideRequestStatusResponse(
    val requestId: String,
    val status: String,
    val message: String
)

class FinishResponse private constructor(
    val id: Long,
    val passenger: PassengerResponse,
    val dropOff: String,
    val status: Status,
    val price: BigDecimal
) {
    companion object {
        fun fromRide(ride: Ride): FinishResponse {
            return FinishResponse(
                id = ride.id!!,
                passenger = PassengerResponse(ride.passengerName!!, ride.passengerEmail!!),
                dropOff = ride.dropOffAddress!!,
                status = ride.status!!,
                price = ride.price!!
            )
        }
    }
}

class RefuseResponse private constructor(
    val id: Long,
    val passenger: PassengerResponse,
    val pickUp: String,
    val dropOff: String,
    val status: Status
) {
    companion object {
        fun fromRide(ride: Ride) = RefuseResponse(
            id = ride.id!!,
            passenger = PassengerResponse(ride.passengerName!!, ride.passengerEmail!!),
            pickUp = ride.pickUpAddress!!,
            dropOff = ride.dropOffAddress!!,
            status = ride.status!!
        )
    }
}

class CancelResponse private constructor(
    val id: Long,
    val pickUp: String,
    val dropOff: String,
    val status: Status
) {
    companion object {
        fun fromRide(ride: Ride) = CancelResponse(
            id = ride.id!!,
            pickUp = ride.pickUpAddress!!,
            dropOff = ride.dropOffAddress!!,
            status = ride.status!!
        )
    }
}

class AcceptResponse private constructor(
    val id: Long,
    val passenger: PassengerResponse,
    val pickUp: String,
    val dropOff: String,
    val status: Status,
) {
    companion object {
        fun fromRide(ride: Ride) = AcceptResponse(
            id = ride.id!!,
            passenger = PassengerResponse(ride.passengerName!!, ride.passengerEmail!!),
            pickUp = ride.pickUpAddress!!,
            dropOff = ride.dropOffAddress!!,
            status = ride.status!!
        )
    }
}

data class EstimateRideRequest(val pickUp: LocationDto, val dropOff: LocationDto)
data class EstimateRideResponse(
    val estimatedPrice: BigDecimal,
    val distanceKm: BigDecimal,
    val estimatedTimeMinutes: BigDecimal,
    val nearbyDrivers: List<NearbyDriverDto>
)

data class NearbyDriverDto(val id: Long, val name: String, val distanceToPassengerKm: Double) {
    companion object {
        fun from(driver: Driver, distance: Double) = NearbyDriverDto(
            id = driver.id!!,
            name = driver.name,
            distanceToPassengerKm = distance
        )
    }
}