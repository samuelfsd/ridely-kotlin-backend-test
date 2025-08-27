package tech.jaya.ridely.domain

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DriverTest {

    private lateinit var driver: Driver

    @BeforeEach
    fun setUp() {
        driver = Driver(
            id = 1L,
            name = "Zé Gotinah",
            available = true,
            activationDate = LocalDateTime.now().minusHours(1),
            carLicensePlate = "ABC-1234",
            carModel = "Fusca",
            carColor = "Azul",
            latitude = 0.0,
            longitude = 0.0
        )
    }

    @Test
    fun `it should become busy when calling becomeBusy`() {
        val originalActivationDate = driver.activationDate
        assertTrue(driver.available)

        driver.becomeBusy()

        assertFalse(driver.available)
        assertTrue(driver.activationDate.isAfter(originalActivationDate))
    }

    @Test
    fun `it should become available when calling becomeAvailable`() {
        driver.available = false
        assertFalse(driver.available)

        driver.becomeAvailable()

        assertTrue(driver.available)
    }

    @Test
    fun `it should update the location when calling updateLocation`() {
        val newLatitude = -7.2199
        val newLongitude = -35.8811
        assertEquals(0.0, driver.latitude)
        assertEquals(0.0, driver.longitude)

        driver.updateLocation(newLatitude, newLongitude)

        assertEquals(newLatitude, driver.latitude)
        assertEquals(newLongitude, driver.longitude)
    }
}