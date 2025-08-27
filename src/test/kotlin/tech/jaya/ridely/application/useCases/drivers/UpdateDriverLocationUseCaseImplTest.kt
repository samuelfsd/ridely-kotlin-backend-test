package tech.jaya.ridely.application.useCases.drivers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.geo.Point
import org.springframework.data.redis.core.GeoOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation
import tech.jaya.ridely.application.useCases.drivers.updateDriverLocation.UpdateDriverLocationUseCase
import tech.jaya.ridely.application.useCases.drivers.updateDriverLocation.UpdateDriverLocationUseCaseImpl
import tech.jaya.ridely.infrastructure.config.RedisKeys
import kotlin.test.assertEquals

@SpringBootTest(classes = [UpdateDriverLocationUseCaseImpl::class])
class UpdateDriverLocationUseCaseImplTest {

    @Autowired
    private lateinit var updateDriverLocationUseCase: UpdateDriverLocationUseCase

    @MockkBean
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Test
    fun `should update driver location in Redis successfully`() {
        val driverId = 1L
        val latitude = -7.2123
        val longitude = -35.8856

        val geoOpsMock = mockk<GeoOperations<String, String>>()
        every { redisTemplate.opsForGeo() } returns geoOpsMock

        val locationSlot = slot<GeoLocation<String>>()

        every { geoOpsMock.add(eq(RedisKeys.DRIVERS_GEO_KEY), any<GeoLocation<String>>()) } returns 1L

        updateDriverLocationUseCase.execute(driverId, latitude, longitude)

        verify(exactly = 1) {
            geoOpsMock.add(RedisKeys.DRIVERS_GEO_KEY, capture(locationSlot))
        }

        val capturedLocation = locationSlot.captured
        assertEquals(driverId.toString(), capturedLocation.name)
        assertEquals(longitude, (capturedLocation.point as Point).x)
        assertEquals(latitude, (capturedLocation.point as Point).y)
    }
}