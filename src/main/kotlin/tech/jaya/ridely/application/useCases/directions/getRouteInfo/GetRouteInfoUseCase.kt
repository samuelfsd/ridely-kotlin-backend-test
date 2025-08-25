package tech.jaya.ridely.application.useCases.directions.getRouteInfo

import java.math.BigDecimal

data class RouteInfo(
    val distanceInKm: BigDecimal,
    val durationInMinutes: BigDecimal
)

interface GetRouteInfoUseCase {
    fun execute(originLat: Double, originLng: Double, destinationLat: Double, destinationLng: Double): RouteInfo?
}