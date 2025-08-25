package tech.jaya.ridely.application.useCases.passengers.deletePassenger

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.jaya.ridely.infrastructure.repositories.PassengerRepo

@Service
class DeletePassengerUseCaseImpl(private val passengerRepo: PassengerRepo): DeletePassengerUseCase {

    @Transactional
    override fun execute(id: Long) {
        passengerRepo.deleteById(id);
    }
}