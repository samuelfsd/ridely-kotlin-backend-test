package tech.jaya.ridely.application.useCases.drivers.updateDriverLocation

interface UpdateDriverLocationUseCase {
    fun execute(driverId: Long, latitude: Double, longitude: Double)
}