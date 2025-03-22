import kotlinx.serialization.Serializable

@Serializable
data class OutsideWaypointsInfo(
    val centralWaypoint: Waypoint,
    val outsideWaypointsCount: Int,
    val waypoints: List<Waypoint>
)