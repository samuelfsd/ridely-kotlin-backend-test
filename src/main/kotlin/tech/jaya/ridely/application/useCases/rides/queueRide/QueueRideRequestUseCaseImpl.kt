package tech.jaya.ridely.application.useCases.rides.queueRide

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.RequestDriver
import tech.jaya.ridely.domain.dtos.RideRequestStatusResponse
import java.util.*

@Service
class QueueRideRequestUseCaseImpl(
    private val redisTemplate: RedisTemplate<String, Any>,
) : QueueRideRequestUseCase {

    private val logger: Logger = LoggerFactory.getLogger(QueueRideRequestUseCaseImpl::class.java)

    override fun execute(req: RequestDriver): RideRequestStatusResponse {

        val requestId = UUID.randomUUID().toString()

        val requestWithId = req.copy(requestId = requestId)

        redisTemplate.opsForList().leftPush("ride-requests", requestWithId)

        logger.info("request $requestId - ${req.passenger.email} add in queue.")

        return RideRequestStatusResponse(
            requestId = requestId,
            status = "PROCESSING",
            message = "Your request is being processed. We are finding a driver for you."
        )
    }
}

