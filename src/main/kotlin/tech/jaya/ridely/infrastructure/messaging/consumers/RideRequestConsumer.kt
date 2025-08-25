package tech.jaya.ridely.infrastructure.messaging.consumers

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tech.jaya.ridely.application.useCases.rides.processRide.ProcessRideRequestUseCase
import tech.jaya.ridely.domain.dtos.RequestDriver

@Component
class RideRequestConsumer(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val processRideRequestUseCase: ProcessRideRequestUseCase
) {

    @Scheduled(fixedDelay = 2000)
    fun processRideRequest() {
        val request = redisTemplate.opsForList().rightPop("ride-requests") as? RequestDriver

        request?.let {
            processRideRequestUseCase.execute(it)
        }
    }
}