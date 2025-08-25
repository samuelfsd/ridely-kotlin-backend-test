package tech.jaya.ridely.application.useCases.drivers.getDriver

import tech.jaya.ridely.domain.dtos.DriverResponse

interface GetDriverUseCase {
    fun execute(id: Long): DriverResponse
}