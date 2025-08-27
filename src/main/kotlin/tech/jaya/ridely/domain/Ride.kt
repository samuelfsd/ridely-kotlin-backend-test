package tech.jaya.ridely.domain

import jakarta.persistence.*
import tech.jaya.ridely.domain.enums.Status
import tech.jaya.ridely.domain.exceptions.RideInvalidState
import java.math.BigDecimal

/**
 * A data class representing a Ride in the system.
 *
 * @property id The unique identifier of the ride. It's nullable because it can be absent when a Ride object is created before being saved to the database.
 * @property pickUp The pickup address for the ride. It's a non-nullable Address object.
 * @property dropOff The drop-off address for the ride. It's a non-nullable Address object.
 * @property status The status of the ride. It's a non-nullable Status object.
 * @property driver The driver assigned to the ride. It's a nullable Driver object.
 * @property passengerName The name of the passenger requesting the ride. It's a non-nullable String.
 * @property passengerEmail The email of the passenger requesting the ride. It's a non-nullable String.
 * @property price The price of the ride. It's a nullable BigDecimal object.
 */
@Entity
@Table(name = "ride")
class Ride(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "pick_up_address", nullable = false)
    var pickUpAddress: String? = null,

    @Column(name = "pick_up_latitude", nullable = false)
    var pickUpLatitude: Double = 0.0,

    @Column(name = "pick_up_longitude", nullable = false)
    var pickUpLongitude: Double = 0.0,

    @Column(name = "drop_off_address", nullable = false)
    var dropOffAddress: String? = null,

    @Column(name = "drop_off_latitude", nullable = false)
    var dropOffLatitude: Double = 0.0,

    @Column(name = "drop_off_longitude", nullable = false)
    var dropOffLongitude: Double = 0.0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: Status? = Status.REQUESTED,

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    var driver: Driver? = null,

    @Column(name = "passenger_name", nullable = false)
    var passengerName: String? = null,

    @Column(name = "passenger_email", nullable = false)
    var passengerEmail: String? = null,

    var price: BigDecimal? = BigDecimal.ZERO,
) {
    fun request(driver: Driver) {
        if (status == Status.COMPLETED) {
            throw RideInvalidState("Ride cannot be requested because it is already completed")
        }

        driver.becomeBusy()
        this.driver = driver
        status = Status.REQUESTED
    }

    fun cancel() {
        if (status == Status.COMPLETED) {
            throw RideInvalidState("Ride cannot be canceled because it is already completed")
        }
        if (driver == null) {
            throw RideInvalidState("Ride cannot be cancelled without a driver")
        }
        driver!!.becomeAvailable()
        status = Status.CANCELLED
    }

    fun accept() {
        if (status != Status.REQUESTED) {
            throw RideInvalidState("Ride cannot be accepted in status $status")
        }
        if (driver == null) {
            throw RideInvalidState("Ride cannot be accepted without a driver")
        }
        driver!!.becomeBusy()
        status = Status.IN_PROGRESS
    }

    fun complete(price: BigDecimal) {
        if (status != Status.IN_PROGRESS) {
            throw RideInvalidState("Ride cannot be finished in status $status")
        }
        if (driver == null) {
            throw RideInvalidState("Ride cannot be finished without a driver")
        }
        driver!!.becomeAvailable()
        this.price = price
        status = Status.COMPLETED
    }

    fun refuse() {
        if (status == Status.COMPLETED) {
            throw RideInvalidState("Ride cannot be refused because it is already completed")
        }
        if (driver == null) {
            throw RideInvalidState("Ride cannot be refused without a driver")
        }
        driver!!.becomeAvailable()
        status = Status.REFUSED
    }
}