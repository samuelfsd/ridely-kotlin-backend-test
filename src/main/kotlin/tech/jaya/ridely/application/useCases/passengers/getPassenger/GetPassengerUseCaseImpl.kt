package tech.jaya.ridely.application.useCases.passengers.getPassenger

import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.PassengerCreationResponse
import tech.jaya.ridely.domain.dtos.toResponse
import tech.jaya.ridely.domain.exceptions.PassengerNotFound
import tech.jaya.ridely.infrastructure.repositories.PassengerRepo

@Service
class GetPassengerUseCaseImpl(private val passengerRepo: PassengerRepo) : GetPassengerUseCase {
    override fun execute(id: Long): PassengerCreationResponse {
        return passengerRepo
            .findById(id)
            .orElseThrow { PassengerNotFound("Drive not found.") }.toResponse()
    }
}