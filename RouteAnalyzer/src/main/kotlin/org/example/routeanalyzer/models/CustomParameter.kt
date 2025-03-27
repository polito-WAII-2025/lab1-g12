package org.example.routeanalyzer.models

/**
 * Configuration parameters for various geographical calculations and geofencing.
 *
 * @property earthRadiusKm The radius of the Earth in kilometers used for distance calculations.
 * Defaults to 6371.0 km.
 * @property geofenceCenterLatitude The latitude of the geofence center in degrees.
 * Defaults to 45.0 degrees.
 * @property geofenceCenterLongitude The longitude of the geofence center in degrees.
 * Defaults to 41.0 degrees.
 * @property geofenceRadiusKm The radius of the geofence in kilometers.
 * Defaults to 0.4 km.
 * @property mostFrequentedAreaRadiusKm (Optional) The radius in kilometers to use when
 * identifying the most frequented area. If `null`, a default value will be used
 * in the relevant calculation.
 */
data class CustomParameter(
    var earthRadiusKm: Double = 6371.0,
    var geofenceCenterLatitude: Double = 45.0,
    var geofenceCenterLongitude: Double = 41.0,
    var geofenceRadiusKm: Double = 0.4,
    var mostFrequentedAreaRadiusKm: Double? = null
)
