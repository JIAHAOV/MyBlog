FROM maven:java8 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

EXPOSE 8090

# Run the web service on container startup.
CMD ["java","-jar","/app/target/reproduce-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]