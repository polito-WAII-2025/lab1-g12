package org.example.routeanalyzer

import org.example.routeanalyzer.models.*
import org.example.routeanalyzer.services.FileService
import org.example.routeanalyzer.services.RouteAnalyzerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.FileNotFoundException

@SpringBootApplication
class RouteAnalyzerApplication


fun main(args: Array<String>) {

    val defaultCsvFilePath = "/app/waypoints.csv"
    val defaultYamlFilePath = "/app/custom-parameters.yml"

    val csvFilePath = args.getOrNull(0) ?: defaultCsvFilePath
    val yamlFilePath = args.getOrNull(1) ?: defaultYamlFilePath

    println("Using Waypoints File: $csvFilePath")
    println("Using Custom Parameters File: $yamlFilePath")

    runApplication<RouteAnalyzerApplication>(*args)

    val fileService = FileService()
    val routeAnalyzerService = RouteAnalyzerService()

    val waypoints = try {
        fileService.readCsv(csvFilePath)
    } catch (e: FileNotFoundException) {
        println("Error reading CSV file: ${e.message}. Using default path if applicable.")
        if (csvFilePath != defaultCsvFilePath) {
            fileService.readCsv(defaultCsvFilePath)
        } else {
            throw e
        }
    }

    val customParameter = try {
        fileService.readYaml(yamlFilePath)
    } catch (e: FileNotFoundException) {
        println("Error reading YAML file: ${e.message}. Using default path if applicable.")
        if (yamlFilePath != defaultYamlFilePath) {
            fileService.readYaml(defaultYamlFilePath)
        } else {
            throw e
        }
    }

    val maxDistanceFromStart: MaxDistanceFromStart = routeAnalyzerService.calculateMaxDistance(customParameter ,waypoints)
    val mostFrequentedArea: MostFrequentedArea = routeAnalyzerService.mostFrequentedArea(customParameter, waypoints)
    val waypointsOutsideGeofence: OutsideGeofence = routeAnalyzerService.waypointsOutsideGeofence(customParameter, waypoints)

    val totalDistance = routeAnalyzerService.totalDistance(customParameter, waypoints)

    val finalOutput = JSONOutput(
        maxDistanceFromStart,
        mostFrequentedArea,
        waypointsOutsideGeofence
    )
    val advancedOutput = JSONOutputAdvanced(totalDistance)

    fileService.output("/app/output.json", finalOutput)
    fileService.outputAdvanced("/app/output_advanced.json", advancedOutput)
}
