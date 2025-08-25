package tech.jaya.ridely.application.useCases.rides.cancelRide

import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.ActionRideRequest
import tech.jaya.ridely.domain.dtos.CancelResponse
import tech.jaya.ridely.domain.exceptions.RideNotFoundException
import tech.jaya.ridely.infrastructure.repositories.RideRepo

@Service
class CancelRideUseCaseImpl(private val rideRepo: RideRepo) : CancelRideUseCase {
    override fun execute(req: ActionRideRequest): CancelResponse {
        val ride = rideRepo.findById(req.id).orElseThrow { RideNotFoundException("No ride found.") }
        ride.cancel()

        return CancelResponse.fromRide(rideRepo.save(ride))
    }
}