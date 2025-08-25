package tech.jaya.ridely.application.useCases.drivers.createDriver

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.DriverCreation
import tech.jaya.ridely.domain.dtos.DriverResponse
import tech.jaya.ridely.domain.dtos.toResponse
import tech.jaya.ridely.infrastructure.repositories.DriverRepo

@Service
class CreateDriverUseCaseImpl(private val driverRepo: DriverRepo) : CreateDriverUseCase {
    @Transactional
    override fun execute(driverCreate: DriverCreation): DriverResponse {
        return driverRepo.save(driverCreate.toDriver()).toResponse()
    }
}