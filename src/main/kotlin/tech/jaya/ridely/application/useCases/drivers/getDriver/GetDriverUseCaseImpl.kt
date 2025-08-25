package tech.jaya.ridely.application.useCases.drivers.getDriver

import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.DriverResponse
import tech.jaya.ridely.domain.dtos.toResponse
import tech.jaya.ridely.domain.exceptions.DriverNotFound
import tech.jaya.ridely.infrastructure.repositories.DriverRepo

@Service
class GetDriverUseCaseImpl(private val driverRepo: DriverRepo) : GetDriverUseCase {
    override fun execute(id: Long): DriverResponse {
        return driverRepo
            .findById(id)
            .orElseThrow { DriverNotFound("Drive not found.") }.toResponse()
    }
}