FROM openjdk:21-slim-bookworm
WORKDIR /app
EXPOSE 8080
COPY target/e-commerce-platform-0.0.1-SNAPSHOT.jar /app/e-commerce-platform.jar
ENTRYPOINT ["java","-jar","e-commerce-platform.jar"]