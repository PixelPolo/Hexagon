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
# --- RUNTIME STAGE ---
# ---------------------
# Runtime stage: Temurin 21 JRE on Ubuntu 24.04 LTS
FROM eclipse-temurin:21-jre-noble AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]