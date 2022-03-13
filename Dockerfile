FROM openjdk:11.0-jdk-slim

ENV API_SERVER_PORT=8080
ENV MANAGEMENT_SERVER_PORT=8081
ENV JAVA_OPTS=""
EXPOSE 8080 8081
USER 1001
COPY target/PhoneBook-0.0.1-SNAPSHOT.jar PhoneBook-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "PhoneBook-0.0.1-SNAPSHOT.jar"]