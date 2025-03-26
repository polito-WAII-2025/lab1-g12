package org.example.routeanalyzer.models

import kotlinx.serialization.Serializable

/**
 * Represents a geographical location recorded at a specific point in time.
 *
 * @property timestamp The timestamp of the waypoint. The unit of this value
 * (e.g., seconds since the epoch, milliseconds) should be consistent throughout
 * the application's data.
 * @property latitude The latitude of the waypoint in degrees.
 * @property longitude The longitude of the waypoint in degrees.
 */
@Serializable
data class Waypoint(
    val timestamp: Double,
    val latitude: Double,
    val longitude: Double,
)
