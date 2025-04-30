# Stage 1: Build the application using Maven
FROM eclipse-temurin:17-jdk-jammy as builder

# Set the working directory for the build stage
WORKDIR /build

# Copy the pom.xml file first
COPY pom.xml ./

# Download dependencies
# Install Maven (needed in this builder stage)
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the application (skip tests)
# This will create the jar in /build/target/
RUN mvn package -DskipTests

# Stage 2: Create the final runtime image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory for the runtime stage
WORKDIR /app

# Copy the built JAR file from the builder stage
# Make sure the JAR filename here matches what your build produces in Stage 1
COPY --from=builder /build/target/cart-service-1.0.0.jar application.jar

# Expose the port the cart-service runs on
EXPOSE 8081

# Specify the command to run on container start
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
