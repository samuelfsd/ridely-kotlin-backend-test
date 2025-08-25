package tech.jaya.ridely.infrastructure.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import tech.jaya.ridely.application.useCases.directions.getRouteInfo.GetRouteInfoUseCase
import tech.jaya.ridely.application.useCases.directions.getRouteInfo.RouteInfo
import tech.jaya.ridely.domain.dtos.GoogleMapsResponse
import java.math.BigDecimal
import org.slf4j.Logger

@Service
class GoogleMapsDirectionsService(
    private val logger: Logger,
    private val restTemplate: RestTemplate,
    @Value("\${google.maps.api.key}") private val apiKey: String
) : GetRouteInfoUseCase {

    override fun execute(
        originLat: Double,
        originLng: Double,
        destinationLat: Double,
        destinationLng: Double
    ): RouteInfo? {

        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=$originLat,$originLng&destination=$destinationLat,$destinationLng&key=$apiKey"

        try {
            val response = restTemplate.getForObject(url, GoogleMapsResponse::class.java)
            val leg = response?.routes?.firstOrNull()?.legs?.firstOrNull() ?: return null

            val distanceInMeters = leg.distance.value
            val durationInSeconds = leg.duration.value

            return RouteInfo(
                distanceInKm = (distanceInMeters.toBigDecimal() / BigDecimal(1000)),
                durationInMinutes = (durationInSeconds.toBigDecimal() / BigDecimal(60))
            )

        } catch (e: Exception) {
            logger.error("error in request to google $e")
            return null
        }
    }

}