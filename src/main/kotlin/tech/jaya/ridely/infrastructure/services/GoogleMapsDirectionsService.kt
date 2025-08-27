package tech.jaya.ridely.infrastructure.services

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import tech.jaya.ridely.application.useCases.directions.getRouteInfo.GetRouteInfoUseCase
import tech.jaya.ridely.application.useCases.directions.getRouteInfo.RouteInfo
import tech.jaya.ridely.domain.dtos.GoogleMapsResponse
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class GoogleMapsDirectionsService(
    restTemplateBuilder: RestTemplateBuilder,
    @Value("\${google.maps.api.key}") private val apiKey: String
) : GetRouteInfoUseCase {

    private val restTemplate: RestTemplate = restTemplateBuilder.build()
    private val logger: Logger = LoggerFactory.getLogger(GoogleMapsDirectionsService::class.java)

    @CircuitBreaker(name = "googleMaps", fallbackMethod = "fallbackRouteInfo")
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
                distanceInKm = distanceInMeters.toBigDecimal().divide(
                    BigDecimal(1000), 2, RoundingMode.HALF_UP
                ),
                durationInMinutes = durationInSeconds.toBigDecimal().divide(
                    BigDecimal(60), 2, RoundingMode.HALF_UP
                )
            )

        } catch (e: Exception) {
            logger.error("error in request to google $e")
            return null
        }
    }

    @Suppress("unused")
    private fun fallbackRouteInfo(
        originLat: Double,
        originLng: Double,
        destLat: Double,
        destLng: Double,
        t: Throwable
    ): RouteInfo? {
        logger.error(
            "fallback active. circuit breaker open or fail call api. route: $originLat,$originLng - $destLat,$destLng. | ${t.message}"
        )
        return null
    }

}