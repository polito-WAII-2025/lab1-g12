package org.example.routeanalyzer.models

data class CustomParameter(
    var earthRadiusKm: Double = 6371.0,
    var geofenceCenterLatitude: Double = 45.0,
    var geofenceCenterLongitude: Double = 41.0,
    var geofenceRadiusKm: Double = 0.4,
    var mostFrequentedAreaRadiusKm: Double? = null
)
