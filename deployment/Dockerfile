# Stage 1: Build
FROM amazoncorretto:21 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew

# Ejecutar build con output detallado
RUN ./gradlew clean bootJar -x test -x validateStructure --info

# Mostrar todo lo que se generó
#RUN echo "=== ARCHIVOS JAR GENERADOS ===" && find /app -name "*.jar" -type f -exec ls -la {} \;
#RUN echo "=== CONTENIDO DE build/libs/ ===" && ls -la /app/build/libs/ || echo "Directorio build/libs no existe"

# Stage 2: Runtime
FROM amazoncorretto:21
VOLUME /tmp

# Copiar todos los JARs posibles y luego seleccionar el correcto
COPY --from=builder /app/ /tmp/build/

# Buscar y copiar el JAR ejecutable (Spring Boot JAR)
RUN find /tmp/build -name "*.jar" -type f -exec sh -c 'jar -tf "{}" | grep -q "BOOT-INF" && cp "{}" /app.jar && echo "Copiado: {}"' \; || echo "No se encontró JAR ejecutable"

# Verificar que tenemos el JAR correcto
RUN ls -la /app.jar && jar -tf /app.jar | head -10

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]