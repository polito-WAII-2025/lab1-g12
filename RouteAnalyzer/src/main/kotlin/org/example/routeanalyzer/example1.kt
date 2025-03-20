package org.example.routeanalyzer

package org.example

import java.io.File
import java.io.InputStream
import org.locationtech.jts.geom.*
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import kotlin.math.round


data class CustomerParameters (
    val mostFrequentedAreaRadiusKm: Double?
)

fun readYaml(path: String): Double? {
    val yaml = Yaml()
    val inputStream = File(path).inputStream()
    val data = yaml.load<Map<String, Any>>(inputStream)
    return(data["mostFrequentedAreaRadiusKm"] as? Double)
}

data class Waypoint(
    val timestamp: String,
    val lat: String,
    val lng: String
)

fun readCsv(inputStream: InputStream): List<Waypoint> {
    val reader = inputStream.bufferedReader()
    return reader.lineSequence()
        .filter { it.isNotBlank() }
        .map {
            val (timestamp, lat, lng) = it.split(';', ignoreCase = false, limit = 3)
            Waypoint(timestamp, lat, lng)
        }.toList()
}

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

val radius: Double? = readYaml("src/main/resources/custom-parameters.yaml")
val maxdist = 2000.0

fun main() {
    val path = "src/main/resources/waypoints.csv"
    val inputStream: InputStream = File(path).inputStream()
    val waypoints = readCsv(inputStream)
    lateinit var centralWaypoint: Waypoint
    var entriesCount = 0
    for(i in waypoints){
        val centerLat = i.lat
        val centerLng = i.lng
        val radius = radius ?: (round((maxdist/10) * 10) / 10)
        var counter = 0
        for(j in waypoints){
            if(isPointInCircle(centerLat,centerLng,radius,j.lat,j.lng)){
                counter++
            }
        }
        if (counter > entriesCount){
            entriesCount = counter
            centralWaypoint = i
        }
    }
    println(centralWaypoint)
    println(entriesCount)
}