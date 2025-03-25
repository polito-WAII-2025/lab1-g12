package org.example.routeanalyzer

import org.example.routeanalyzer.models.Waypoint
import org.example.routeanalyzer.services.FileService
import org.example.routeanalyzer.services.RouteAnalyzerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.math.round

const val maxdist = 2000.0

@SpringBootApplication
class RouteAnalyzerApplication



fun main(args: Array<String>) {
    runApplication<RouteAnalyzerApplication>(*args)

    val fileService = FileService()
    val routeAnalyzerService = RouteAnalyzerService()

    val customParameter = fileService.readYaml("/custom-parameters.yaml")


    val waypoints = fileService.readCsv("/waypoints.csv")
    lateinit var centralWaypoint: Waypoint
    var entriesCount = 0
    for(i in waypoints){
        val centerLat = i.lat
        val centerLng = i.lng
        val radius = customParameter.mostFrequentedAreaRadiusKm ?: (round((maxdist/10) * 10) / 10)
        var counter = 0
        for(j in waypoints){
            if(routeAnalyzerService.isPointInCircle(centerLat,centerLng,radius,j.lat,j.lng)){
                counter++
            }
        }
        if (counter > entriesCount){
            entriesCount = counter
            centralWaypoint = i
        }
    }

    val result = routeAnalyzerService.calculateMaxDistance(waypoints, customParameter.earthRadiusKm)

    result?.let { (waypoint, distance) ->
        println("Max Distance from Start: $distance km at waypoint: $waypoint")
    } ?: println("No waypoints available.")

    println(centralWaypoint)
    println(entriesCount)
}
