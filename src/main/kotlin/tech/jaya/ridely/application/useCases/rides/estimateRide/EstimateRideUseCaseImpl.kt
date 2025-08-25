package tech.jaya.ridely.application.useCases.rides.estimateRide

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tech.jaya.ridely.application.useCases.directions.getRouteInfo.RouteInfo
import tech.jaya.ridely.application.useCases.utils.Haversine
import tech.jaya.ridely.domain.dtos.EstimateRideRequest
import tech.jaya.ridely.domain.dtos.EstimateRideResponse
import tech.jaya.ridely.domain.dtos.NearbyDriverDto
import tech.jaya.ridely.infrastructure.repositories.DriverRepo
import tech.jaya.ridely.infrastructure.services.GoogleMapsDirectionsService
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class EstimateRideUseCaseImpl(
    private val directionsService: GoogleMapsDirectionsService,
    private val driverRepo: DriverRepo
) : EstimateRideUseCase {

    @Cacheable(
        value = ["rideEstimates"],
        key = "#request.pickUp.toString() + '-' + #request.dropOff.toString()"
    )
    override fun execute(request: EstimateRideRequest): EstimateRideResponse {
        val routeInfo = directionsService.execute(
            request.pickUp.latitude, request.pickUp.longitude,
            request.dropOff.latitude, request.dropOff.longitude
        ) ?: throw RuntimeException("Could not calculate route")

        val price = calculatePrice(routeInfo)

        val availableDrivers = driverRepo.findAllAvailable()

        val nearbyDrivers = availableDrivers
            .map { driver ->
                val distance = Haversine.distance(
                    request.pickUp.latitude, request.pickUp.longitude,
                    driver.latitude, driver.longitude
                )
                Pair(driver, distance)
            }
            .sortedBy { it.second } // ordena pela distancia menor
            .take(3)
            .map { NearbyDriverDto.from(it.first, it.second) }

        return EstimateRideResponse(
            estimatedPrice = price,
            distanceKm = routeInfo.distanceInKm,
            estimatedTimeMinutes = routeInfo.durationInMinutes,
            nearbyDrivers = nearbyDrivers
        )
    }

    //  utilitário que calcula o preço conforme nas regras do desafio
    private fun calculatePrice(routeInfo: RouteInfo): BigDecimal {
        val pricePerKm = BigDecimal("3.00")
        val pricePerMinute = BigDecimal("2.00")
        val appFee = BigDecimal("0.01") // 1% da taxa pro ridely

        val ridePrice = (routeInfo.distanceInKm * pricePerKm) + (routeInfo.durationInMinutes * pricePerMinute)
        val finalPrice = ridePrice + (ridePrice * appFee)

        return finalPrice.setScale(2, RoundingMode.HALF_UP)
    }

}