package tech.jaya.ridely.application.useCases.drivers.createDriver

import tech.jaya.ridely.domain.dtos.DriverCreation
import tech.jaya.ridely.domain.dtos.DriverResponse

interface CreateDriverUseCase {
    fun execute(driverCreate: DriverCreation): DriverResponse
}