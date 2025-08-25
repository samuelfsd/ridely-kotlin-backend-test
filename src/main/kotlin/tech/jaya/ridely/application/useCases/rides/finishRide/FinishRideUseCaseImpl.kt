package tech.jaya.ridely.application.useCases.rides.finishRide

import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.FinishResponse
import tech.jaya.ridely.domain.dtos.FinishRideRequest
import tech.jaya.ridely.domain.exceptions.RideNotFoundException
import tech.jaya.ridely.infrastructure.repositories.RideRepo

@Service
class FinishRideUseCaseImpl(private val rideRepo: RideRepo) : FinishRideUseCase {
    override fun execute(req: FinishRideRequest): FinishResponse {
        val (id, price) = req

        val ride = rideRepo.findById(id).orElseThrow { RideNotFoundException("No ride found.") }
        ride.complete(price)

        return FinishResponse.fromRide(rideRepo.save(ride))
    }
}