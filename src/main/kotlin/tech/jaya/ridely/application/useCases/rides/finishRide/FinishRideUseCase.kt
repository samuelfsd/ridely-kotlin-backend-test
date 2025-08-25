package tech.jaya.ridely.application.useCases.rides.finishRide

import tech.jaya.ridely.domain.dtos.FinishResponse
import tech.jaya.ridely.domain.dtos.FinishRideRequest

interface FinishRideUseCase {
    fun execute(req: FinishRideRequest): FinishResponse
}