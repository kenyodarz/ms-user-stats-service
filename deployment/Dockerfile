# Etapa 1: Build
FROM gradle:8.5.0-jdk21-alpine AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon --no-configuration-cache

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY --from=build /app/app-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
