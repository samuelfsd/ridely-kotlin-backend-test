package tech.jaya.ridely.application.useCases.rides.refuseRide

import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.ActionRideRequest
import tech.jaya.ridely.domain.dtos.RefuseResponse
import tech.jaya.ridely.domain.exceptions.RideNotFoundException
import tech.jaya.ridely.infrastructure.repositories.RideRepo

@Service
class RefuseRideUseCaseImpl(private val rideRepo: RideRepo): RefuseRideUseCase {
    override fun execute(req: ActionRideRequest): RefuseResponse {
        val ride = rideRepo.findById(req.id).orElseThrow { RideNotFoundException("No ride found with id ${req.id}") }
        ride.refuse()

        return RefuseResponse.fromRide(rideRepo.save(ride))
    }
}
