FROM eclipse-temurin:23-jdk-alpine

COPY build/libs/*.jar app.jar

EXPOSE 3030

ENTRYPOINT ["java", "-jar", "app.jar"]