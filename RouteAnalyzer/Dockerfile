# Use a lightweight OpenJDK image
FROM openjdk:21-jdk-slim

# Working directory inside the container
WORKDIR /app

# Application JAR
COPY build/libs/RouteAnalyzer-0.0.1-SNAPSHOT.jar /app/RouteAnalyzer.jar

# Custom parameters file
COPY custom-parameters.yml /app/custom-parameters.yml

# Waypoints file
COPY waypoints.csv /app/waypoints.csv

# Output directory
RUN mkdir -p /app/output

# Volume for persisting output files
VOLUME ["/app/output"]

# Run
CMD ["java", "-jar", "RouteAnalyzer.jar"]
