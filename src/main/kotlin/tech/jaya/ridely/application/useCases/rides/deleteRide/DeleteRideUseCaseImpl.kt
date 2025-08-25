package tech.jaya.ridely.application.useCases.rides.deleteRide

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.jaya.ridely.infrastructure.repositories.RideRepo

@Service
class DeleteRideUseCaseImpl(private val rideRepo: RideRepo) : DeleteRideUseCase {
    @Transactional
    override fun execute(id: Long) {
        return rideRepo.deleteById(id);
    }
}