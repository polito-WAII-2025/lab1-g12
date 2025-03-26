package org.example.routeanalyzer

import org.example.routeanalyzer.models.JSONOutput
import org.example.routeanalyzer.models.MaxDistanceFromStart
import org.example.routeanalyzer.models.MostFrequentedArea
import org.example.routeanalyzer.models.OutsideGeofence
import org.example.routeanalyzer.services.FileService
import org.example.routeanalyzer.services.RouteAnalyzerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

const val maxDist = 2000.0

@SpringBootApplication
class RouteAnalyzerApplication


fun main(args: Array<String>) {
    runApplication<RouteAnalyzerApplication>(*args)

    // Initializing file service and analyzer service
    val fileService = FileService()
    val routeAnalyzerService = RouteAnalyzerService()

    // Reading waypoints file and custom parameters
    val waypoints = fileService.readCsv("/waypoints.csv")
    val customParameter = fileService.readYaml("/custom-parameters.yaml")

    // Computing data
    val maxDistanceFromStart: MaxDistanceFromStart = routeAnalyzerService.calculateMaxDistance(customParameter ,waypoints)
    val mostFrequentedArea: MostFrequentedArea = routeAnalyzerService.mostFrequentedArea(customParameter, waypoints)
    val waypointsOutsideGeofence: OutsideGeofence = routeAnalyzerService.waypointsOutsideGeofence(customParameter, waypoints)

    // Putting everything together
    val finalOutput = JSONOutput(
        maxDistanceFromStart,
        mostFrequentedArea,
        waypointsOutsideGeofence
    )

    //Writing to Output
    fileService.output("./output.json", finalOutput)
}
