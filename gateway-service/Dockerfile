FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache curl
VOLUME /tmp
ARG JAR_FILE=target/gateway-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
