package org.example.routeanalyzer.models

import kotlinx.serialization.Serializable

/**
 * Represents the waypoint that is farthest from the starting point and the distance to it.
 *
 * @property waypoint The [Waypoint] object representing the location farthest from the start.
 * @property distanceKm The Haversine distance in kilometers between the starting waypoint
 * and the [waypoint] property.
 */
@Serializable
data class MaxDistanceFromStart(
    val waypoint: Waypoint,
    val distanceKm: Double
)