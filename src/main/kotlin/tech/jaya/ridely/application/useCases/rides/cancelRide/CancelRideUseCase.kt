package tech.jaya.ridely.application.useCases.rides.cancelRide

import tech.jaya.ridely.domain.dtos.ActionRideRequest
import tech.jaya.ridely.domain.dtos.CancelResponse

interface CancelRideUseCase {
    fun execute(req: ActionRideRequest): CancelResponse
}