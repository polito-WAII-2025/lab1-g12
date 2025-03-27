[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/vlo9idtn)
# lab1-wa2025

# General specifications

- waypoints.csv is already in the project and the program will automatically recognize it, if not specified otherwise. If specified, the input files will be available from a volume "input" in docker:
  

    val csvFilePath = if (args.isNotEmpty()) args[0] else "/app/waypoints.csv


- The output.json is saved in a mounted volume in Docker called "output". The output.json and output_advanced.json will be there

# How to run
## Building
  `./gradlew clean build`

## Use default paths
`docker run -v output:/app route-analyzer`

## Specify custom paths
`docker run -v output:/app/ -v "<path-to-your-input-folder>:/input" route-analyzer /input/waypoints.csv /input/custom-parameters.yml`