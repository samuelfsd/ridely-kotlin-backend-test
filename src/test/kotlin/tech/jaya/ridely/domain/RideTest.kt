package tech.jaya.ridely.domain

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tech.jaya.ridely.domain.enums.Status
import tech.jaya.ridely.domain.exceptions.RideInvalidState
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RideTest {

    private lateinit var driver: Driver
    private lateinit var ride: Ride

    @BeforeEach
    fun setUp() {
        driver = Driver(id = 1L, name = "Test Driver", available = true)
        ride = Ride(
            id = 100L,
            passengerName = "Zeca urubu",
            passengerEmail = "zeca@gmail.com"
        )
    }

    @Nested
    inner class RequestTests {
        @Test
        fun `should request a ride successfully`() {
            ride.request(driver)

            assertEquals(Status.REQUESTED, ride.status)
            assertEquals(driver, ride.driver)
            assertFalse(driver.available, "driver should become busy after a request")
        }

        @Test
        fun `should throw exception when requesting an already completed ride`() {
            ride.status = Status.COMPLETED

            assertThrows<RideInvalidState> {
                ride.request(driver)
            }
        }
    }

    @Nested
    inner class AcceptTests {
        @Test
        fun `should accept a requested ride successfully`() {
            ride.request(driver)
            assertEquals(Status.REQUESTED, ride.status)

            ride.accept()

            assertEquals(Status.IN_PROGRESS, ride.status)
            assertFalse(driver.available, "driver should remain busy when ride is accepted")
        }

        @Test
        fun `should throw exception when accepting a ride that is not in REQUESTED state`() {
            ride.status = Status.IN_PROGRESS

            assertThrows<RideInvalidState> {
                ride.accept()
            }
        }
    }

    @Nested
    inner class CompleteTests {
        @Test
        fun `should complete an in-progress ride successfully`() {
            ride.request(driver)
            ride.accept()
            assertEquals(Status.IN_PROGRESS, ride.status)
            assertFalse(driver.available)

            val price = BigDecimal("25.50")
            ride.complete(price)

            assertEquals(Status.COMPLETED, ride.status)
            assertEquals(price, ride.price)
            assertTrue(driver.available, "driv should becom available after ride is completed")
        }

        @Test
        fun `should throw exception when completing a ride that is not in progress`() {
            ride.status = Status.REQUESTED

            assertThrows<RideInvalidState> {
                ride.complete(BigDecimal.TEN)
            }
        }
    }

    @Nested
    inner class CancelTests {
        @Test
        fun `should cancel a ride successfully`() {
            ride.request(driver)
            assertFalse(driver.available)

            ride.cancel()

            assertEquals(Status.CANCELLED, ride.status)
            assertTrue(driver.available, "driver should become available after ride is cancelled")
        }

        @Test
        fun `should throw exception when cancelling an already completed ride`() {
            ride.status = Status.COMPLETED

            assertThrows<RideInvalidState> {
                ride.cancel()
            }
        }
    }

    @Nested
    inner class RefuseTests {
        @Test
        fun `should refuse a ride successfully`() {
            ride.request(driver)
            assertFalse(driver.available)

            ride.refuse()

            assertEquals(Status.REFUSED, ride.status)
            assertTrue(driver.available, "drive should become available after ride is refused")
        }

        @Test
        fun `should throw exception when refusing an already completed ride`() {
            ride.status = Status.COMPLETED

            assertThrows<RideInvalidState> {
                ride.refuse()
            }
        }
    }
}