package tech.jaya.ridely.application.useCases.rides

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import tech.jaya.ridely.application.useCases.rides.finishRide.FinishRideUseCaseImpl
import tech.jaya.ridely.domain.Ride
import tech.jaya.ridely.domain.dtos.FinishRideRequest
import tech.jaya.ridely.domain.enums.Status
import tech.jaya.ridely.infrastructure.repositories.RideRepo
import java.math.BigDecimal
import java.util.Optional
import kotlin.test.assertEquals

@SpringBootTest(classes = [FinishRideUseCaseImpl::class])
class FinishRideUseCaseImplTest {

    @Autowired
    private lateinit var finishRideUseCase: FinishRideUseCaseImpl

    @MockkBean
    private lateinit var rideRepo: RideRepo

    @Test
    fun `execute should complete ride and return finish response`() {
        val rideId = 1L
        val price = BigDecimal("25.50")
        val request = FinishRideRequest(rideId, price)

        val mockRide = mockk<Ride>(relaxed = true)
        every { mockRide.id } returns rideId
        every { mockRide.status } returns Status.COMPLETED
        every { mockRide.price } returns price

        every { rideRepo.findById(rideId) } returns Optional.of(mockRide)
        every { rideRepo.save(any()) } returns mockRide
        justRun { mockRide.complete(price) }

        val response = finishRideUseCase.execute(request)

        verify(exactly = 1) { mockRide.complete(price) }
        verify(exactly = 1) { rideRepo.save(mockRide) }

        assertEquals(rideId, response.id)
        assertEquals(Status.COMPLETED.name, response.status.name)
        assertEquals(price, response.price)
    }
}