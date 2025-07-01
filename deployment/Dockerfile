# Etapa 1: Construcción del JAR
FROM gradle:8.5.0-jdk21-alpine AS build

# Crear directorio de trabajo
WORKDIR /app

# Copiar el proyecto completo
COPY --chown=gradle:gradle . .

# Compilar usando bootJar
RUN gradle bootJar --no-daemon --no-configuration-cache

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:21-jdk-alpine

# Directorio de ejecución
WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
