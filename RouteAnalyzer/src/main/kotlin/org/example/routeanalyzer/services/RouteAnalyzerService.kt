package org.example.routeanalyzer.services

import org.example.routeanalyzer.models.Waypoint
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.atan2
import kotlin.math.sin
import kotlin.math.cos

class RouteAnalyzerService {

    fun isPointInCircle(centerLat: String, centerLng: String, radius: Double, lat: String, lng: String): Boolean {

        val centerLatDouble = centerLat.toDoubleOrNull() ?: return false
        val centerLngDouble = centerLng.toDoubleOrNull() ?: return false
        val latDouble = lat.toDoubleOrNull() ?: return false
        val lngDouble = lng.toDoubleOrNull() ?: return false

        val geometryFactory = GeometryFactory()
        val center = geometryFactory.createPoint(Coordinate(centerLngDouble, centerLatDouble)) // JTS usa (lng, lat)
        val point = geometryFactory.createPoint(Coordinate(lngDouble, latDouble))

        val circle = center.buffer(radius / 111320.0)

        return circle.contains(point)
    }

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

    fun calculateMaxDistance(waypoints: List<Waypoint>, earthRadiusKm: Double): Pair<Waypoint, Double>? {
        if (waypoints.isEmpty()) return null

        val startPoint = waypoints.first()
        var maxDistance = 0.0
        var farthestWaypoint = startPoint

        for (waypoint in waypoints) {
            val distance = haversineDistance(
                startPoint.lat.toDouble(), startPoint.lng.toDouble(),
                waypoint.lat.toDouble(), waypoint.lng.toDouble(),
                earthRadiusKm
            )
            if (distance > maxDistance) {
                maxDistance = distance
                farthestWaypoint = waypoint
            }
        }

        return Pair(farthestWaypoint, maxDistance)
    }

}