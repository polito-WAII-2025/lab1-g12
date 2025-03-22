fun waypointsOutsideGeofence(
    filePath: String,
    centerLatitude: Double,
    centerLongitude: Double,
    radius: Double
) {
    val waypoints = parseCsv(filePath)
    val outsideWaypoints = waypoints.filter {
        haversine(it.latitude, it.longitude, centerLatitude, centerLongitude) > radius
    }

    val result = GeofenceResult(
        OutsideWaypointsInfo(
            centralWaypoint = Waypoint(0.0, centerLatitude, centerLongitude),
            outsideWaypointsCount = outsideWaypoints.size,
            waypoints = outsideWaypoints
        )
    )

    output(filePath, result)
}