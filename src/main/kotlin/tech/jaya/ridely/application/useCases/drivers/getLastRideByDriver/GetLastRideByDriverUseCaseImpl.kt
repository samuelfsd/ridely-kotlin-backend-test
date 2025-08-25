package tech.jaya.ridely.application.useCases.drivers.getLastRideByDriver

import org.springframework.stereotype.Service
import tech.jaya.ridely.domain.dtos.AcceptResponse
import tech.jaya.ridely.domain.exceptions.RideNotFoundException
import tech.jaya.ridely.infrastructure.repositories.RideRepo

@Service
class GetLastRideByDriverUseCaseImpl(private val rideRepo: RideRepo) : GetLastRideByDriverUseCase {
    override fun execute(id: Long): AcceptResponse {
        val ride = rideRepo
            .findLastRideByDriveId(id)
            .orElseThrow({ RideNotFoundException("Dont have any ride") })

        return AcceptResponse.Companion.fromRide(ride);
    }
}