FROM openjdk:17-oracle
COPY target/unialbums-backend-0.0.1-SNAPSHOT.jar unialbums-backend-app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","unialbums-backend-app.jar"]
