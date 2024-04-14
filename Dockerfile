FROM openjdk:17-oracle
COPY --from=build /target/*.jar application.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","unialbums-backend-app.jar"]
