# Etapa 1: Construcción
FROM gradle:8.5.0-jdk21-alpine AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon

# Etapa 2: Imagen final liviana
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

# Copia el .jar desde la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Opciones recomendadas para entornos cloud
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
