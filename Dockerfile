# Use the official Maven image to build the application
FROM maven:3.8.7-eclipse-temurin-19 AS build
WORKDIR /app
COPY . .

# Build the application
RUN mvn clean install

# Create the final image with only the JAR
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/jd-challenge-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
