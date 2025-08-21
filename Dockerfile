FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/Shvidkiy-Homework_int3-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

LABEL org.opencontainers.image.title="Shvidkiy-Homework_int3" \
    org.opencontainers.image.authors="Viktor Shvidkiy"