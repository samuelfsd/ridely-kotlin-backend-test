package tech.jaya.ridely.domain.exceptions

class DriverUnavailable(message: String) : Exception(message)

class DriverNotFound(message: String) : Exception(message)

class RideNotFoundException(message: String) : Exception(message)

class RideInvalidState(message: String) : Exception(message)

class PassengerNotFound(message: String): Exception(message)

class PassengerAlreadyInRideException(message: String): Exception(message)