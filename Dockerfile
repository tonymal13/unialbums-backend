FROM openjdk:17-oracle
COPY target/*.jar unialbums-backend-app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","unialbums-backend-app.jar"]