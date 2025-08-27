package tech.jaya.ridely.application.useCases.rides

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import tech.jaya.ridely.application.useCases.directions.getRouteInfo.RouteInfo
import tech.jaya.ridely.application.useCases.rides.estimateRide.EstimateRideUseCaseImpl
import tech.jaya.ridely.domain.Driver
import tech.jaya.ridely.domain.dtos.EstimateRideRequest
import tech.jaya.ridely.domain.dtos.LocationDto
import tech.jaya.ridely.infrastructure.repositories.DriverRepo
import tech.jaya.ridely.infrastructure.services.GoogleMapsDirectionsService
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.test.assertEquals

@SpringBootTest(classes = [EstimateRideUseCaseImpl::class])
class EstimateRideUseCaseImplTest {

    @Autowired
    private lateinit var estimateRideUseCase: EstimateRideUseCaseImpl

    @MockkBean
    private lateinit var directionsService: GoogleMapsDirectionsService

    @MockkBean
    private lateinit var driverRepo: DriverRepo

    @Test
    fun `execute should return ride estimate with price and nearby drivers`() {
        val pickUpLocation = LocationDto(address = "Rua 1, 123", latitude = -7.21, longitude = -35.88)
        val dropOffLocation = LocationDto(address = "Rua 2, 456", latitude = -7.22, longitude = -35.89)
        val request = EstimateRideRequest(pickUp = pickUpLocation, dropOff = dropOffLocation)

        val mockRouteInfo = RouteInfo(
            distanceInKm = BigDecimal("10.00"),
            durationInMinutes = BigDecimal("20.00")
        )
        every {
            directionsService.execute(
                request.pickUp.latitude, request.pickUp.longitude,
                request.dropOff.latitude, request.dropOff.longitude
            )
        } returns mockRouteInfo

        val mockDrivers = listOf(
            Driver(id = 1, name = "Driver A", latitude = -7.211, longitude = -35.881, available = true),
            Driver(id = 2, name = "Driver B", latitude = -7.215, longitude = -35.885, available = true)
        )
        every { driverRepo.findAllAvailable() } returns mockDrivers

        val response = estimateRideUseCase.execute(request)

        val expectedPrice = (BigDecimal("10.00") * BigDecimal("3.00") + BigDecimal("20.00") * BigDecimal("2.00"))
        val finalPrice = (expectedPrice + (expectedPrice * BigDecimal("0.01"))).setScale(2, RoundingMode.HALF_UP)

        assertEquals(finalPrice, response.estimatedPrice)
        assertEquals(mockRouteInfo.distanceInKm, response.distanceKm)
        assertEquals(mockRouteInfo.durationInMinutes, response.estimatedTimeMinutes)
        assertEquals(2, response.nearbyDrivers.size)
        assertEquals("Driver A", response.nearbyDrivers.first().name)
    }
}