package org.example.routeanalyzer

import org.example.routeanalyzer.models.*
import org.example.routeanalyzer.services.FileService
import org.example.routeanalyzer.services.RouteAnalyzerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

const val maxDist = 2000.0

@SpringBootApplication
class RouteAnalyzerApplication


fun main(args: Array<String>) {

    val csvFilePath = if (args.isNotEmpty()) args[0] else "/waypoints.csv"

    runApplication<RouteAnalyzerApplication>(*args)

    // Initializing file service and analyzer service
    val fileService = FileService()
    val routeAnalyzerService = RouteAnalyzerService()

    // Reading waypoints file and custom parameters
    val waypoints = fileService.readCsv(csvFilePath)
    val customParameter = fileService.readYaml("C:\\Users\\USER\\Documents\\Codice\\lab1-g12\\RouteAnalyzer\\custom-parameters.yaml")

    // Computing data
    val maxDistanceFromStart: MaxDistanceFromStart = routeAnalyzerService.calculateMaxDistance(customParameter ,waypoints)
    val mostFrequentedArea: MostFrequentedArea = routeAnalyzerService.mostFrequentedArea(customParameter, waypoints)
    val waypointsOutsideGeofence: OutsideGeofence = routeAnalyzerService.waypointsOutsideGeofence(customParameter, waypoints)

    val totalDistance = routeAnalyzerService.totalDistance(customParameter, waypoints)

    // Putting everything together
    val finalOutput = JSONOutput(
        maxDistanceFromStart,
        mostFrequentedArea,
        waypointsOutsideGeofence
    )

    val advancedOutput = JSONOutputAdvanced(totalDistance)

    //Writing to Output
    fileService.output("./output.json", finalOutput)
    fileService.outputAdvanced("./output_advanced.json", advancedOutput)
}
