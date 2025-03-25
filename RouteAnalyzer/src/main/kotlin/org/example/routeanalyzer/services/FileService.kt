package org.example.routeanalyzer.services

import org.example.routeanalyzer.models.CustomParameter
import org.example.routeanalyzer.models.Waypoint
import org.yaml.snakeyaml.Yaml
import java.io.FileNotFoundException
import java.io.InputStream

class FileService {

    fun readCsv(resourcePath: String): List<Waypoint> {
        val inputStream: InputStream = object {}.javaClass.getResourceAsStream(resourcePath)
            ?: throw FileNotFoundException("File non trovato: $resourcePath")

        val reader = inputStream.bufferedReader()
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (timestamp, lat, lng) = it.split(';', ignoreCase = false, limit = 3)
                Waypoint(timestamp, lat, lng)
            }.toList()
    }

    fun readYaml(resourcePath: String = "/custom-parameters.yaml"): CustomParameter {
        val yaml = Yaml()
        val inputStream = object {}.javaClass.getResourceAsStream(resourcePath)
            ?: throw FileNotFoundException("File not found: $resourcePath")

        return yaml.loadAs(inputStream, CustomParameter::class.java)
    }

}