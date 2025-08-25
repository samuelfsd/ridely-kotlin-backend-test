package tech.jaya.ridely.application.useCases.rides.queueRide

import tech.jaya.ridely.domain.dtos.RequestDriver
import tech.jaya.ridely.domain.dtos.RideRequestStatusResponse

interface QueueRideRequestUseCase {
    fun execute(req: RequestDriver): RideRequestStatusResponse
}