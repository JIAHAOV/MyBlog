FROM maven:jdk8 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
# COPY src ./src
COPY src/main/resources/application-prod.yaml /app

# Build a release artifact.
# RUN mvn package -DskipTests


EXPOSE 8090

# Run the web service on container startup.
CMD ["java","-jar","/app/target/reproduce-0.0.1-SNAPSHOT.jar","--spring.config.location=/app/application-prod.yaml"]