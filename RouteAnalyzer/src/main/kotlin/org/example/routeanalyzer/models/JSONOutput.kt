package org.example.routeanalyzer.models

import kotlinx.serialization.Serializable

/**
 * Represents the overall JSON output of the route analysis.
 *
 * This data class aggregates the results of different analysis steps performed on a set of waypoints.
 *
 * @property maxDistanceFromStart An object of type [MaxDistanceFromStart] containing information
 * about the waypoint farthest from the starting point and the calculated maximum distance.
 * @property mostFrequentedArea An object of type [MostFrequentedArea] detailing the area
 * where waypoints are most concentrated, including its central waypoint, radius, and the
 * number of waypoints within it.
 * @property waypointsOutsideGeofence An object of type [OutsideGeofence] providing information
 * about waypoints that fall outside the defined geofence, including the geofence center,
 * radius, the count of outside waypoints, and a list of those waypoints.
 */
@Serializable
data class JSONOutput(
    val maxDistanceFromStart: MaxDistanceFromStart,
    val mostFrequentedArea: MostFrequentedArea,
    val waypointsOutsideGeofence: OutsideGeofence,
)
