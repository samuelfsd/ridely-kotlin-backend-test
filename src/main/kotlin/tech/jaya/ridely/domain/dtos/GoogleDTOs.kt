package tech.jaya.ridely.domain.dtos

data class GoogleMapsResponse(val routes: List<Route>)

data class Route(val legs: List<Leg>)

data class Leg(val distance: Value, val duration: Value)

data class Value(val value: Int)