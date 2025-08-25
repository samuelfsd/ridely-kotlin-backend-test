package tech.jaya.ridely.application.useCases.rides.acceptRide

import tech.jaya.ridely.domain.dtos.AcceptResponse
import tech.jaya.ridely.domain.dtos.ActionRideRequest

interface AcceptRideUseCase {
    fun execute(req: ActionRideRequest): AcceptResponse
}