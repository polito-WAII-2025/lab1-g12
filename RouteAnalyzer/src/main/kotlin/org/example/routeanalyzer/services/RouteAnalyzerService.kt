package org.example.routeanalyzer.services

import org.example.routeanalyzer.maxDist
import org.example.routeanalyzer.models.CustomParameter
import org.example.routeanalyzer.models.MaxDistanceFromStart
import org.example.routeanalyzer.models.MostFrequentedArea
import org.example.routeanalyzer.models.OutsideGeofence
import org.example.routeanalyzer.models.Waypoint
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.atan2
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.round

class RouteAnalyzerService {

    /**
     * Checks if a given point (latitude and longitude) lies within a circle defined by
     * its center coordinates and radius.
     *
     * This function utilizes the JTS Topology Suite for geometric calculations.
     * Note that JTS uses (longitude, latitude) order for coordinates.
     * The radius is assumed to be in meters and is converted to degrees for buffering
     * the center point. A rough conversion factor of 1 degree ≈ 111320 meters at the equator
     * is used for this approximation.
     *
     * @param centerLatitude The latitude of the circle's center.
     * @param centerLongitude The longitude of the circle's center.
     * @param radius The radius of the circle in meters.
     * @param latitude The latitude of the point to check.
     * @param longitude The longitude of the point to check.
     * @return `true` if the point is within or on the boundary of the circle, `false` otherwise.
     */
    fun isPointInCircle(
        centerLatitude: Double,
        centerLongitude: Double,
        radius: Double,
        latitude: Double,
        longitude: Double
    ): Boolean {
        val geometryFactory = GeometryFactory()
        val center = geometryFactory.createPoint(Coordinate(centerLongitude, centerLatitude)) // JTS usa (lng, lat)
        val point = geometryFactory.createPoint(Coordinate(longitude, latitude))

        val circle = center.buffer(radius / 111320.0)

        return circle.contains(point)
    }

    /**
     * Calculates the Haversine distance between two points on the Earth's surface.
     *
     * The Haversine formula is used to determine the great-circle distance between
     * two points given their latitudes and longitudes. It assumes a spherical Earth.
     *
     * @param lat1 The latitude of the first point in degrees.
     * @param lon1 The longitude of the first point in degrees.
     * @param lat2 The latitude of the second point in degrees.
     * @param lon2 The longitude of the second point in degrees.
     * @param earthRadiusKm The radius of the Earth in kilometers to use for the calculation.
     * Standard value is approximately 6371.
     * @return The Haversine distance between the two points in kilometers.
     */
    private fun haversineDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double,
        earthRadiusKm: Double
    ): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusKm * c
    }

    /**
     * Calculates the maximum Haversine distance of any waypoint in the provided list
     * from the first waypoint, using parameters from [CustomParameter].
     *
     * It also identifies a 'central' waypoint, defined as the center of a circle
     * with a radius specified in [CustomParameter] (or a fraction of the maximum
     * distance if not specified), that contains the most other waypoints.
     *
     * @param customParameters An instance of [CustomParameter] containing configuration
     * such as the Earth's radius and the radius for identifying the most frequented area.
     * @param waypoints A list of [Waypoint] objects representing the recorded locations.
     * @return A [MaxDistanceFromStart] object containing the waypoint farthest from the
     * start and the calculated maximum distance.
     *
     * Note: The radius for identifying the most frequented area, if not explicitly
     * defined in [customParameters], is calculated as
     * one-tenth of the maximum distance (rounded to the nearest tenth).
     */
    fun calculateMaxDistance(
        customParameters: CustomParameter,
        waypoints: List<Waypoint>
    ): MaxDistanceFromStart {
        lateinit var centralWaypoint: Waypoint
        var entriesCount = 0
        for(i in waypoints){
            val centerLat = i.latitude
            val centerLng = i.longitude
            val radius = customParameters.mostFrequentedAreaRadiusKm ?: (round((maxDist/10) * 10) / 10)
            var counter = 0
            for(j in waypoints){
                if(isPointInCircle(centerLat,centerLng,radius,j.latitude,j.longitude)){
                    counter++
                }
            }
            if (counter > entriesCount){
                entriesCount = counter
                centralWaypoint = i
            }
        }

        val startPoint = waypoints.first()
        var maxDistance = 0.0
        var farthestWaypoint = startPoint

        for (waypoint in waypoints) {
            val distance = haversineDistance(
                startPoint.latitude, startPoint.longitude,
                waypoint.latitude, waypoint.longitude,
                customParameters.earthRadiusKm
            )
            if (distance > maxDistance) {
                maxDistance = distance
                farthestWaypoint = waypoint
            }
        }

        return MaxDistanceFromStart(farthestWaypoint, maxDistance)
    }

    /**
     * Identifies the most frequented geographical area based on the provided waypoints.
     *
     * This function iterates through each waypoint, considering it as the center of a
     * circle with a radius defined in [CustomParameter] (or a default based on a maximum distance).
     * It then counts how many other waypoints fall within this circle. The waypoint that
     * results in the highest count is considered the center of the most frequented area.
     *
     * @param customParameters An instance of [CustomParameter] containing configuration
     * such as the radius for identifying the most frequented area and the geofence radius.
     * @param waypoints A list of [Waypoint] objects representing the recorded locations.
     * @return A [MostFrequentedArea] object containing the central [Waypoint] of the
     * most frequented area, the defined area radius (from [CustomParameter.geofenceRadiusKm]),
     * and the number of waypoints found within that area.
     *
     * Note: The radius for identifying the most frequented area, if not explicitly
     * defined in [customParameters], defaults to one-tenth
     * of a fixed maximum distance (2000.0 km in this implementation, rounded to the
     * nearest tenth). Consider reviewing and adjusting this default if needed.
     */
    fun mostFrequentedArea(
        customParameters: CustomParameter,
        waypoints: List<Waypoint>
    ): MostFrequentedArea {
        var centralWaypoint: Waypoint = waypoints[0]
        var entriesCount = 0
        val maxdist = calculateMaxDistance(customParameters, waypoints).distanceKm
        val radius = customParameters.mostFrequentedAreaRadiusKm?.times(1000) ?: (maxdist*100)

        for (i in waypoints) {
            val centerLat = i.latitude
            val centerLng = i.longitude
            var counter = 0

            for (j in waypoints) {
                if (isPointInCircle(centerLat, centerLng, radius, j.latitude, j.longitude)) {
                    counter++
                }
            }

            if (counter > entriesCount) {
                entriesCount = counter
                centralWaypoint = i
            }
        }
        val areaRadiusKm = customParameters.mostFrequentedAreaRadiusKm ?: ((round(maxdist * 10) / 10)/10)


        return MostFrequentedArea(centralWaypoint, areaRadiusKm, entriesCount)
    }

    /**
     * Identifies and counts the number of waypoints that lie outside a defined geofence.
     *
     * The geofence is defined by a center latitude, a center longitude, and a radius in kilometers,
     * all sourced from the provided [CustomParameter] object. The Haversine distance is used
     * to determine if a waypoint is outside the geofence boundary.
     *
     * @param customParameters An instance of [CustomParameter] containing the geofence
     * center coordinates ([CustomParameter.geofenceCenterLatitude], [CustomParameter.geofenceCenterLongitude]),
     * the geofence radius ([CustomParameter.geofenceRadiusKm]), and the Earth's radius ([CustomParameter.earthRadiusKm]).
     * @param waypoints A list of [Waypoint] objects to be checked against the geofence.
     * @return An [OutsideGeofence] object containing:
     * - A central [Waypoint] representing the center of the geofence.
     * - The radius of the geofence in kilometers.
     * - The total count of waypoints found to be outside the geofence.
     * - A list of the [Waypoint] objects that are located outside the geofence.
     */
    fun waypointsOutsideGeofence(
        customParameters: CustomParameter,
        waypoints: List<Waypoint>,
    ): OutsideGeofence {
        val earthRadiusKm = customParameters.earthRadiusKm
        val centerLatitude = customParameters.geofenceCenterLatitude
        val centerLongitude = customParameters.geofenceCenterLongitude
        val radiusKm = customParameters.geofenceRadiusKm

        val outsideWaypoints = waypoints.filter {
            haversineDistance(it.latitude, it.longitude, centerLatitude, centerLongitude, earthRadiusKm) > radiusKm
        }

        return OutsideGeofence(
            centralWaypoint = Waypoint(0.0, centerLatitude, centerLongitude),
            areaRadiusKm = radiusKm,
            count = outsideWaypoints.size,
            waypoints = outsideWaypoints
        )
    }

    fun totalDistance(customParameters: CustomParameter, waypoints: List<Waypoint>): Double {
        if (waypoints.size < 2) return 0.0

        var totalDist = 0.0
        for (i in 0 until waypoints.size - 1) {
            val lat1 = waypoints[i].latitude
            val lon1 = waypoints[i].longitude
            val lat2 = waypoints[i+1].latitude
            val lon2 = waypoints[i+1].longitude

            totalDist += haversineDistance(lat1, lon1, lat2, lon2, customParameters.earthRadiusKm)
        }
        return totalDist
    }
}