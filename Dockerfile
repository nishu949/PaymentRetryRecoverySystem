FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=render", "-Xmx256m", "-jar", "target/*.jar"]
