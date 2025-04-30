# Stage 1: Build the application using Maven Wrapper
FROM eclipse-temurin:17-jdk-jammy as builder

# Set the working directory for the build stage
WORKDIR /build

# Copy the Maven wrapper files first
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make the wrapper executable
RUN chmod +x mvnw

# Download dependencies using the wrapper
RUN ./mvnw dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the application using the wrapper (skip tests)
# This will create the jar in /build/target/
RUN ./mvnw package -Dmaven.test.skip=true

# Stage 2: Create the final runtime image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory for the runtime stage
WORKDIR /app

# Copy the built JAR file from the builder stage
# Make sure the JAR filename here matches what your build produces in Stage 1
COPY --from=builder /build/target/TwistThrottle-0.0.1-SNAPSHOT.jar application.jar

# Expose the port the main application runs on (default 8080)
EXPOSE 8080

# Specify the command to run on container start
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
