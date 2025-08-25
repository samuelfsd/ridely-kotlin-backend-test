package tech.jaya.ridely.application.useCases.rides.processRide

import tech.jaya.ridely.domain.dtos.RequestDriver

interface ProcessRideRequestUseCase {
    fun execute(request: RequestDriver)
}