# 1. Base Image
FROM openjdk:17-jdk-slim

# 2. Working directory inside container
WORKDIR /app

# 3. Copy JAR file into container
COPY target/blog-0.0.1-SNAPSHOT.jar app.jar

# 4. Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
