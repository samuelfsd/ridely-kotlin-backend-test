package tech.jaya.ridely.application.useCases.rides.refuseRide

import tech.jaya.ridely.domain.dtos.ActionRideRequest
import tech.jaya.ridely.domain.dtos.RefuseResponse

interface RefuseRideUseCase {
    fun execute(req: ActionRideRequest): RefuseResponse
}