package tech.jaya.ridely.application.useCases.passengers.createPassenger

import tech.jaya.ridely.domain.dtos.PassengerCreation
import tech.jaya.ridely.domain.dtos.PassengerCreationResponse

interface CreatePassengerUseCase {
    fun execute(passengerCreation: PassengerCreation): PassengerCreationResponse
}