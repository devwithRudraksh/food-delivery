# ----------- Stage 1: Build -------------------
FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ----------- Stage 2: Runtime -------------------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
