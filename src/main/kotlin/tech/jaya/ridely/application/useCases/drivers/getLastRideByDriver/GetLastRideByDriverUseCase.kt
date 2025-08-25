package tech.jaya.ridely.application.useCases.drivers.getLastRideByDriver

import tech.jaya.ridely.domain.dtos.AcceptResponse

interface GetLastRideByDriverUseCase {
    fun execute(id: Long): AcceptResponse
}