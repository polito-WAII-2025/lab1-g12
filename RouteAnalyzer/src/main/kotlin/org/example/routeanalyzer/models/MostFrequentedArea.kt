package org.example.routeanalyzer.models

import kotlinx.serialization.Serializable

/**
 * Represents the most frequented geographical area identified within the waypoints.
 *
 * @property centralWaypoint The [Waypoint] object representing the center of the most
 * frequented area. This waypoint was determined to be the center of a circle
 * (with radius [areaRadiusKm]) containing the highest number of other waypoints.
 * @property areaRadiusKm The radius in kilometers of the circle defining the most
 * frequented area. This radius is typically configured in the application's
 * [CustomParameter] settings.
 * @property entriesCount The number of waypoints found within the circle defined by
 * [centralWaypoint] and [areaRadiusKm].
 */
@Serializable
data class MostFrequentedArea(
    val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val entriesCount: Int,
)
