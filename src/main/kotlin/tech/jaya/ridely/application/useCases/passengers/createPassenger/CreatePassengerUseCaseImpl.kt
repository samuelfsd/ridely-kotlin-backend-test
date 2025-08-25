package tech.jaya.ridely.application.useCases.passengers.createPassenger

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.PassengerCreation
import tech.jaya.ridely.domain.dtos.PassengerCreationResponse
import tech.jaya.ridely.domain.dtos.toResponse
import tech.jaya.ridely.infrastructure.repositories.PassengerRepo

@Service
class CreatePassengerUseCaseImpl(private val passengerRepo: PassengerRepo) : CreatePassengerUseCase {
    @Transactional
    override fun execute(passengerCreation: PassengerCreation): PassengerCreationResponse {
        val savedPassenger = passengerRepo.save(passengerCreation.toPassenger())

        return savedPassenger.toResponse()
    }
}