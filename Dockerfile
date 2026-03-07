# -------------------
# --- BUILD STAGE ---
# -------------------
# Build stage: Maven with Temurin 21 JDK
FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

# ---------------------
# --- EXTRACT STAGE ---
# ---------------------
# Extract stage: Use the Spring Boot layered JAR to optimize the final image
FROM maven:3-eclipse-temurin-21 AS builder
WORKDIR /builder
COPY --from=build /app/target/*.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# ---------------------
# --- RUNTIME STAGE ---
# ---------------------
# Runtime stage: Temurin 21 JRE with the extracted layers for optimal caching and performance
FROM eclipse-temurin:21-jre-noble AS runtime
WORKDIR /application
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]