package tech.jaya.ridely.application.useCases.rides.estimateRide

import tech.jaya.ridely.domain.dtos.EstimateRideRequest
import tech.jaya.ridely.domain.dtos.EstimateRideResponse

interface EstimateRideUseCase {
    fun execute(request: EstimateRideRequest): EstimateRideResponse
}