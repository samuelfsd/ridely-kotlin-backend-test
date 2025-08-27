package tech.jaya.ridely.application.useCases.drivers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import tech.jaya.ridely.application.useCases.drivers.getLastRideByDriver.GetLastRideByDriverUseCase
import tech.jaya.ridely.application.useCases.drivers.getLastRideByDriver.GetLastRideByDriverUseCaseImpl
import tech.jaya.ridely.domain.Driver
import tech.jaya.ridely.domain.Ride
import tech.jaya.ridely.domain.enums.Status
import tech.jaya.ridely.domain.exceptions.RideNotFoundException
import tech.jaya.ridely.infrastructure.repositories.RideRepo
import java.util.Optional
import kotlin.test.assertEquals

@SpringBootTest(classes = [GetLastRideByDriverUseCaseImpl::class])
class GetLastRideByDriverUseCaseImplTest {

    @Autowired
    private lateinit var getLastRideByDriverUseCase: GetLastRideByDriverUseCase

    @MockkBean
    private lateinit var rideRepo: RideRepo

    @Test
    fun `should return the last ride for a driver when it exists`() {
        val driverId = 1L
        val mockDriver = Driver(
            id = driverId,
            name = "Motorista",
            carLicensePlate = "ABC-1234",
            carModel = "Gol",
            carColor = "Bolinha"
        )

        val mockRide = Ride(
            id = 100L,
            passengerName = "Test Passenger",
            passengerEmail = "test@email.com",
            pickUpAddress = "Origin",
            dropOffAddress = "Destination",
            status = Status.IN_PROGRESS,
            driver = mockDriver
        )

        every { rideRepo.findLastRideByDriveId(driverId) } returns Optional.of(mockRide)

        val result = getLastRideByDriverUseCase.execute(driverId)

        assertEquals(mockRide.id, result.id)
        assertEquals(mockRide.passengerName, result.passenger.name)
        assertEquals(Status.IN_PROGRESS, result.status)
    }

    @Test
    fun `should throw RideNotFoundException if no ride exists for the driver`() {
        val driverId = 2L
        every { rideRepo.findLastRideByDriveId(driverId) } returns Optional.empty()

        val exception = assertThrows<RideNotFoundException> {
            getLastRideByDriverUseCase.execute(driverId)
        }

        assertEquals("Dont have any ride", exception.message)
    }
}