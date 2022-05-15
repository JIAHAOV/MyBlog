FROM java:8

COPY *.jar /app.jar

EXPOSE 8090

ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]