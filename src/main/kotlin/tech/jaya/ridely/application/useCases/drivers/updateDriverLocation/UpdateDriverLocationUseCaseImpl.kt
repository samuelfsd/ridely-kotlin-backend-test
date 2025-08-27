package tech.jaya.ridely.application.useCases.drivers.updateDriverLocation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.RedisGeoCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import tech.jaya.ridely.infrastructure.config.RedisKeys.DRIVERS_GEO_KEY

@Service
class UpdateDriverLocationUseCaseImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : UpdateDriverLocationUseCase {

    private val logger: Logger = LoggerFactory.getLogger(UpdateDriverLocationUseCaseImpl::class.java)

    override fun execute(driverId: Long, latitude: Double, longitude: Double) {
        val member = driverId.toString()

        redisTemplate.opsForGeo().add(
            DRIVERS_GEO_KEY,
            RedisGeoCommands.GeoLocation(member, org.springframework.data.geo.Point(longitude, latitude))
        )

        logger.info("location of driver $driverId updated.")
    }
}