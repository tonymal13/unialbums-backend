#FROM openjdk:21-oracle
#COPY target/unialbums-backend-0.0.1-SNAPSHOT.jar unialbums-backend-app.jar
#EXPOSE 8081
#ENTRYPOINT ["java","-jar","unialbums-backend-app.jar"]

FROM maven:3.8.3-openjdk-17 AS build
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package

FROM openjdk:21-oracle
COPY --from=build /target/*.jar unialbums-backend-app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "unialbums-backend-app.jar"]