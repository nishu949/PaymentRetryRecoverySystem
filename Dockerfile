FROM maven:3.9.4-eclipse-temurin-17-alpine

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=render", "-Xmx256m", "-jar", "target/*.jar"]