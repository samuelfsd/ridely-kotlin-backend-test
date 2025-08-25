package tech.jaya.ridely.application.useCases.rides.processRide

import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.redis.connection.RedisGeoCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.RequestDriver
import tech.jaya.ridely.domain.exceptions.DriverUnavailable
import tech.jaya.ridely.domain.exceptions.PassengerAlreadyInRideException
import tech.jaya.ridely.infrastructure.config.RedisKeys.DRIVERS_GEO_KEY
import tech.jaya.ridely.infrastructure.repositories.DriverRepo
import tech.jaya.ridely.infrastructure.repositories.PassengerRepo
import tech.jaya.ridely.infrastructure.repositories.RideRepo

@Service
class ProcessRideRequestUseCaseImpl(
    private val driverRepo: DriverRepo,
    private val rideRepo: RideRepo,
    private val passengerRepo: PassengerRepo,
    private val redisTemplate: RedisTemplate<String, String>,
    private val logger: Logger,
) : ProcessRideRequestUseCase {

    @Transactional
    override fun execute(request: RequestDriver) {
        logger.info("processing ride request for: ${request.passenger.email}")

        try {
            val passengerPoint = org.springframework.data.geo.Point(request.pickUp.longitude, request.pickUp.latitude)

            val radius = Distance(10.0, Metrics.KILOMETERS)
            val searchCircle = Circle(passengerPoint, radius)

            val args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .sortAscending()
                .limit(30) // limita a busca aos 30 mais próx

            val geoResults = redisTemplate.opsForGeo().radius(DRIVERS_GEO_KEY, searchCircle, args)
            val nearbyDriverIds = geoResults?.content?.mapNotNull { it.content.name.toLongOrNull() }

            if (nearbyDriverIds.isNullOrEmpty()) {
                throw DriverUnavailable("there are no drivers in your area at the moment")
            }

            val availableNearbyDrivers = driverRepo.findByIdInAndAvailableTrue(nearbyDriverIds)
            val driver =
                availableNearbyDrivers.firstOrNull() ?: throw DriverUnavailable("Motoristas próximos estão ocupados.")

            val passenger = passengerRepo.findByEmail(request.passenger.email)
            if (passenger?.inTraveling == true) {
                throw PassengerAlreadyInRideException("Passageiro ${passenger.email} já está em uma corrida.")
            }

            val ride = request.toRide(driver)
            ride.request(driver)
            rideRepo.save(ride)

            redisTemplate.opsForGeo().remove(DRIVERS_GEO_KEY, driver.id.toString())
            logger.info("driver removed from the GEO index until the end of the race.")
            logger.info("ride created with success ${request.passenger.email} with driver ${driver.name}")
        } catch (e: Exception) {
            logger.error("fail in solicitation ${request.passenger.email}: ${e.message}")
        }
    }
}