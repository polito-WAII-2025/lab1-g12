package org.example.routeanalyzer.services

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.example.routeanalyzer.models.CustomParameter
import org.example.routeanalyzer.models.JSONOutput
import org.example.routeanalyzer.models.JSONOutputAdvanced
import org.example.routeanalyzer.models.Waypoint
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class FileService {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    /**
     * Reads waypoint data from a CSV file located within the application's resources.
     *
     * The CSV file is expected to have three semicolon-separated columns:
     * - **timestamp**: A Double representing the timestamp of the waypoint.
     * - **latitude**: A Double representing the latitude of the waypoint.
     * - **longitude**: A Double representing the longitude of the waypoint.
     *
     * Empty or blank lines in the CSV file are ignored.
     *
     * @param filePath The path to the CSV file (e.g., "/data/waypoints.csv").
     * @return A List of [Waypoint] objects parsed from the CSV file.
     * @throws FileNotFoundException If the file specified by [filePath] cannot be found in the resources.
     * @throws NumberFormatException If any of the timestamp, latitude, or longitude values cannot be parsed as a Double.
     */
    fun readCsv(filePath: String): List<Waypoint> {
        val inputStream: InputStream = try {
            FileInputStream(File(filePath))
        } catch (e: FileNotFoundException) {
            throw FileNotFoundException("File not found at path: $filePath")
        }

        val reader = inputStream.bufferedReader()
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (timestamp, latitude, longitude) = it.split(';', ignoreCase = false, limit = 3)
                Waypoint(timestamp.toDouble(), latitude.toDouble(), longitude.toDouble())
            }.toList()
    }

    /**
     * Reads custom parameter data from a YAML file located within the application's resources.
     *
     * The YAML file is expected to contain data that can be mapped to the [CustomParameter] class.
     *
     * @param filePath The path to the YAML file
     * Defaults to "/app/custom-parameters.yaml" if no path is provided.
     * @return A [CustomParameter] object populated with the data from the YAML file.
     * @throws FileNotFoundException If the file specified by [filePath] cannot be found in the resources.
     * of the YAML file does not match the expected structure for
     * the [CustomParameter] class.
     */
    fun readYaml(filePath: String = "/app/custom-parameters.yaml"): CustomParameter {
        val yaml = Yaml()
        val inputStream = File(filePath).inputStream()

        return yaml.loadAs(inputStream, CustomParameter::class.java)
    }

    /**
     * Writes the provided [JSONOutput] object to a JSON file at the specified path.
     *
     * The [JSONOutput] object is serialized into a JSON string using the default
     * [Json] encoder. If the file at [filePath] does not exist, it will be created.
     * If it does exist, its contents will be overwritten.
     *
     * @param filePath The path to the file where the JSON output should be written.
     * @param result The [JSONOutput] object to be serialized and written to the file.
     */
    fun output(
        filePath: String,
        result: JSONOutput,
    ){
        File(filePath).writeText(json.encodeToString(result))
    }

    fun outputAdvanced(
        filePath: String,
        result: JSONOutputAdvanced,
    ){
        File(filePath).writeText(json.encodeToString(result))
    }
}