package org.example.routeanalyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RouteAnalyzerApplication

fun main(args: Array<String>) {
    runApplication<RouteAnalyzerApplication>(*args)
}
