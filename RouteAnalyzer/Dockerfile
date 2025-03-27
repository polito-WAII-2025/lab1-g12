# Usa un'immagine base con Java per eseguire il codice Kotlin
FROM openjdk:21-jdk-slim

# Imposta la directory di lavoro all'interno del contenitore
WORKDIR /app

# Copia il codice sorgente dell'applicazione (per esempio, i file .jar o le cartelle build) nel contenitore
COPY build/libs/RouteAnalyzer-1.0-SNAPSHOT.jar /app/RouteAnalyzer.jar

# Copia il file custom-parameters.yml nel contenitore (questo sarà montato durante l'esecuzione)
COPY custom-parameters.yml /app/custom-parameters.yml

# Imposta il comando per eseguire l'applicazione Kotlin
CMD ["java", "-jar", "RouteAnalyzer.jar"]

