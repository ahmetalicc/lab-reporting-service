FROM openjdk:17-alpine
WORKDIR /ReportingApp
COPY target/ReportingApp-0.0.1-SNAPSHOT.jar reporting-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reporting-app.jar"]