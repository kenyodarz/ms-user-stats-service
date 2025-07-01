# Etapa 1: Build
FROM gradle:8.5.0-jdk21-alpine AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
ENV DISABLE_VALIDATION=true
RUN gradle bootJar --no-daemon --no-configuration-cache

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/ms-user-stats-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

