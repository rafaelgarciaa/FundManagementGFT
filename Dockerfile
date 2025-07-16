# Primera etapa: Construir la aplicación JAR
# Usamos una imagen base de Maven que incluye Java 17 para la compilación
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

# Establece el directorio de trabajo dentro del contenedor para la fase de construcción
WORKDIR /app

# Copia el archivo pom.xml y descarga las dependencias.
# Esto ayuda a que las reconstrucciones sean más rápidas si el pom.xml no cambia.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente de tu aplicación Spring Boot
COPY src ./src

# Empaqueta tu aplicación Spring Boot en un JAR ejecutable
# El nombre del JAR será fondosGFT-0.0.1-SNAPSHOT.jar (basado en tu pom.xml)
RUN mvn package -DskipTests -B

# Segunda etapa: Construir la imagen final ligera para la ejecución
# Usamos una imagen base más ligera con solo el Java Runtime Environment (JRE) para Java 17
FROM eclipse-temurin:17-jre-alpine

# Establece el directorio de trabajo en /app dentro del contenedor final
WORKDIR /app

# Copia el JAR ejecutable desde la etapa de construcción anterior a esta imagen final
# Asegúrate de que este nombre de archivo coincida con el JAR generado por Maven
COPY --from=build /app/target/fondosGFT-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que tu aplicación Spring Boot se ejecutará (por defecto 8080)
EXPOSE 8080

# Define el comando que se ejecutará cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]