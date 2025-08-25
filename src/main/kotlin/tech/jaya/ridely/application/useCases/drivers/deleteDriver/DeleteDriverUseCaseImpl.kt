package tech.jaya.ridely.application.useCases.drivers.deleteDriver

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.jaya.ridely.infrastructure.repositories.DriverRepo

@Service
class DeleteDriverUseCaseImpl(private val driverRepo: DriverRepo): DeleteDriverUseCase {

    @Transactional
    override fun execute(id: Long) {
        return driverRepo.deleteById(id)
    }
}