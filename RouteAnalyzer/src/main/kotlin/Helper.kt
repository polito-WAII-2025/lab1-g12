import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
}

fun parseCsv(filePath: String): List<Waypoint> {
    return File(filePath).readLines()
        .map { line ->
            val parts = line.split(";").map { it.trim() }
            Waypoint(parts[0].toDouble(), parts[1].toDouble(), parts[2].toDouble())
        }
}

fun haversine(
    latitude1: Double,
    longitude1: Double,
    latitude2: Double,
    longitude2: Double
): Double {
    val r = 6371e3
    val phi1 = Math.toRadians(latitude1)
    val phi2 = Math.toRadians(latitude2)
    val deltaPhi = Math.toRadians(latitude2 - latitude1)
    val deltaLambda = Math.toRadians(longitude2 - longitude1)

    val a = sin(deltaPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(deltaLambda / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return r * c
}

fun output(
    filePath: String,
    result: GeofenceResult,
){
    val outputPath = filePath.replace("waypoints.csv", "output.json")
    File(outputPath).writeText(json.encodeToString(result))
}