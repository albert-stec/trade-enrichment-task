FROM openjdk:17-jdk-alpine
MAINTAINER astec
COPY target/trade-enrichment-service-0.0.1-SNAPSHOT.jar enricher-1.0.0.jar
COPY product.csv product.csv
ENTRYPOINT ["java","-jar","/enricher-1.0.0.jar"]