package tech.jaya.ridely.infrastructure.repositories

import org.springframework.data.jpa.repository.JpaRepository
import tech.jaya.ridely.domain.Passenger

interface PassengerRepo : JpaRepository<Passenger, Long> {
    fun findByEmail(email: String): Passenger?
}