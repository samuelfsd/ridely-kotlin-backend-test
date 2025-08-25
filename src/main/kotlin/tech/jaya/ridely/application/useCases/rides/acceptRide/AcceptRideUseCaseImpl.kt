package tech.jaya.ridely.application.useCases.rides.acceptRide

import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.AcceptResponse
import tech.jaya.ridely.domain.dtos.ActionRideRequest
import tech.jaya.ridely.domain.exceptions.RideNotFoundException
import tech.jaya.ridely.infrastructure.repositories.RideRepo

@Service
class AcceptRideUseCaseImpl(private val rideRepo: RideRepo) : AcceptRideUseCase {
    override fun execute(req: ActionRideRequest): AcceptResponse {
        val ride = rideRepo.findById(req.id).orElseThrow { RideNotFoundException("No ride found.") }
        ride.accept()

        return AcceptResponse.fromRide(rideRepo.save(ride))
    }
}