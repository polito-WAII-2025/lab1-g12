import kotlinx.serialization.Serializable

@Serializable
data class OutsideWaypointsInfo(
    val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val count: Int,
    val waypoints: List<Waypoint>
)