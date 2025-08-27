package tech.jaya.ridely.infrastructure.services

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import tech.jaya.ridely.application.useCases.directions.getRouteInfo.RouteInfo
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RestClientTest(GoogleMapsDirectionsService::class)
class GoogleMapsDirectionsServiceTest {

    @Autowired
    private lateinit var service: GoogleMapsDirectionsService

    @Autowired
    private lateinit var server: MockRestServiceServer

    private val apiKey = "AIzaSyBhwQ8NV9v6SZQu7KhvJt7sr19YFzmrKHk"

    @BeforeEach
    fun setup() {
        val field = service.javaClass.getDeclaredField("apiKey")
        field.isAccessible = true
        field.set(service, apiKey)
    }

    @Test
    fun `execute should return correct RouteInfo on successful API call`() {
        val originLat = -7.21
        val originLng = -35.88
        val destLat = -7.22
        val destLng = -35.89

        val expectedUrl =
            "https://maps.googleapis.com/maps/api/directions/json?origin=$originLat,$originLng&destination=$destLat,$destLng&key=$apiKey"

        val mockJsonResponse = """
        {
            "routes": [{
                "legs": [{
                    "distance": {"value": 15000},
                    "duration": {"value": 1800}
                }]
            }]
        }
        """

        server.expect(requestTo(expectedUrl))
            .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON))

        val result: RouteInfo? = service.execute(originLat, originLng, destLat, destLng)

        assertNotNull(result)
        assertEquals(BigDecimal("15.00"), result.distanceInKm)
        assertEquals(BigDecimal("30.00"), result.durationInMinutes)
        server.verify()
    }
}