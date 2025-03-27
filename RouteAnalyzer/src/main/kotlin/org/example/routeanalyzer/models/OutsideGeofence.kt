package org.example.routeanalyzer.models

import kotlinx.serialization.Serializable

/**
 * Represents information about waypoints that lie outside a defined geofence.
 *
 * @property centralWaypoint A [Waypoint] object representing the center of the geofence.
 * The coordinates for this point are typically defined in the application's
 * [CustomParameter] settings.
 * @property areaRadiusKm The radius in kilometers of the geofence. This value is
 * also usually configured in the [CustomParameter] settings.
 * @property count The total number of waypoints from the analyzed set that were found
 * to be located outside the boundaries of the geofence.
 * @property waypoints A list of [Waypoint] objects that are located outside the defined
 * geofence.
 */
@Serializable
data class OutsideGeofence(
    val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val count: Int,
    val waypoints: List<Waypoint>
)