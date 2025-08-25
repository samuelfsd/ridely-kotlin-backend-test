package tech.jaya.ridely.application.useCases.passengers.getPassenger

import tech.jaya.ridely.domain.dtos.PassengerCreationResponse

interface GetPassengerUseCase {
    fun execute(id: Long): PassengerCreationResponse
}