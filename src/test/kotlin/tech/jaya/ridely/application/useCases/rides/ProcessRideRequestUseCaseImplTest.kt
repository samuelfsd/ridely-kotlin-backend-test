package tech.jaya.ridely.application.useCases.rides

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.geo.*
import org.springframework.data.redis.connection.RedisGeoCommands
import org.springframework.data.redis.core.GeoOperations
import org.springframework.data.redis.core.RedisTemplate
import tech.jaya.ridely.application.useCases.rides.processRide.ProcessRideRequestUseCaseImpl
import tech.jaya.ridely.domain.Driver
import tech.jaya.ridely.domain.Passenger
import tech.jaya.ridely.domain.Ride
import tech.jaya.ridely.domain.dtos.LocationDto
import tech.jaya.ridely.domain.dtos.PassengerRequest
import tech.jaya.ridely.domain.dtos.RequestDriver
import tech.jaya.ridely.infrastructure.config.RedisKeys
import tech.jaya.ridely.infrastructure.repositories.DriverRepo
import tech.jaya.ridely.infrastructure.repositories.PassengerRepo
import tech.jaya.ridely.infrastructure.repositories.RideRepo
import kotlin.test.assertEquals

@SpringBootTest(classes = [ProcessRideRequestUseCaseImpl::class])
class ProcessRideRequestUseCaseImplTest {

    @Autowired
    private lateinit var processRideUseCase: ProcessRideRequestUseCaseImpl

    @MockkBean
    private lateinit var driverRepo: DriverRepo

    @MockkBean
    private lateinit var rideRepo: RideRepo

    @MockkBean
    private lateinit var passengerRepo: PassengerRepo

    @MockkBean
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Test
    fun `execute should find an available driver and create a ride successfully`() {
        val request = RequestDriver(
            passenger = PassengerRequest(name = "Teste", email = "teste@gmail.com"),
            pickUp = LocationDto(address = "Start", latitude = -7.21, longitude = -35.88),
            dropOff = LocationDto(address = "End", latitude = -7.22, longitude = -35.89)
        )
        val driverId = 1L
        val mockDriver = Driver(id = driverId, name = "Motorista", available = true)
        val mockPassenger = Passenger(id = 10L, email = request.passenger.email, inTraveling = false)

        val geoOpsMock = mockk<GeoOperations<String, String>>()

        val mockDistance = Distance(1.0, Metrics.KILOMETERS)
        val geoResult = GeoResult(RedisGeoCommands.GeoLocation(driverId.toString(), Point(0.0, 0.0)), mockDistance)
        val geoResults = GeoResults(listOf(geoResult))

        every { redisTemplate.opsForGeo() } returns geoOpsMock
        every { geoOpsMock.radius(any(), any<Circle>(), any()) } returns geoResults
        every { geoOpsMock.remove(any(), any<String>()) } returns 1L

        every { driverRepo.findByIdInAndAvailableTrue(listOf(driverId)) } returns listOf(mockDriver)
        every { passengerRepo.findByEmail(request.passenger.email) } returns mockPassenger
        every { rideRepo.save(any()) } answers { firstArg() }

        processRideUseCase.execute(request)

        val rideSlot = slot<Ride>()
        verify(exactly = 1) { rideRepo.save(capture(rideSlot)) }
        verify(exactly = 1) { geoOpsMock.remove(RedisKeys.DRIVERS_GEO_KEY, driverId.toString()) }

        assertEquals(mockDriver.id, rideSlot.captured.driver?.id)
        assertEquals(mockPassenger.email, rideSlot.captured.passengerEmail)
    }

}