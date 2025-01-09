FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# Copy essential build files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Build dependencies layer separately
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Copy source code and env file
COPY src src
COPY .env .env

# Build the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# Create app directory and set permissions
RUN mkdir -p /app && \
    addgroup -S spring && \
    adduser -S spring -G spring && \
    chown -R spring:spring /app

# Copy application artifacts
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build /workspace/app/.env /app/.env

# Set working directory and user
WORKDIR /app
USER spring

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1


ENTRYPOINT ["java","-cp","app:app/lib/*","com.finapp.backend.BackendApplication"]