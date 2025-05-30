FROM eclipse-temurin:18-jdk
VOLUME /tmp
WORKDIR /app
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]