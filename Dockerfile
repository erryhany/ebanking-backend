# Étape 1 : compilation avec Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# Étape 2 : image d'exécution légère
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/ebanking-backend-*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
