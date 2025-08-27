package tech.jaya.ridely.application.useCases.rides

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.RedisTemplate
import tech.jaya.ridely.application.useCases.rides.queueRide.QueueRideRequestUseCaseImpl
import tech.jaya.ridely.domain.dtos.LocationDto
import tech.jaya.ridely.domain.dtos.PassengerRequest
import tech.jaya.ridely.domain.dtos.RequestDriver
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(classes = [QueueRideRequestUseCaseImpl::class])
class QueueRideRequestUseCaseImplTest {

    @Autowired
    private lateinit var queueRideUseCase: QueueRideRequestUseCaseImpl

    @MockkBean
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Test
    fun `execute should add ride request to redis queue and return processing status`() {
        val request = RequestDriver(
            passenger = PassengerRequest(name = "Teste José", email = "jose@gmail.com"),
            pickUp = LocationDto(address = "Start", latitude = 1.0, longitude = 1.0),
            dropOff = LocationDto(address = "End", latitude = 2.0, longitude = 2.0)
        )

        val listOperationsMock = mockk<ListOperations<String, Any>>()
        every { redisTemplate.opsForList() } returns listOperationsMock
        every { listOperationsMock.leftPush(any(), any()) } returns 1L

        val requestSlot = slot<RequestDriver>()

        val response = queueRideUseCase.execute(request)

        assertEquals("PROCESSING", response.status)
        assertNotNull(response.requestId)

        verify(exactly = 1) { listOperationsMock.leftPush("ride-requests", capture(requestSlot)) }

        assertEquals(request.passenger.email, requestSlot.captured.passenger.email)
        assertNotNull(requestSlot.captured.requestId)
    }
}