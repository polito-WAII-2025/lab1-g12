import kotlinx.serialization.Serializable

@Serializable
data class GeofenceResult(
    val waypointsOutsideGeofence: OutsideWaypointsInfo
)