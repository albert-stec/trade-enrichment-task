FROM openjdk:17-jdk-alpine
MAINTAINER astec
COPY target/*.jar enricher-1.0.0.jar
ENTRYPOINT ["java","-jar","/enricher-1.0.0.jar"]