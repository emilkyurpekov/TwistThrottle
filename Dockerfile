# Stage 1: Build the application using Maven Wrapper
FROM eclipse-temurin:17-jdk-jammy as builder

# Set the working directory for the build stage
WORKDIR /build

# Copy the Maven wrapper files first
# Ensure .mvn folder, mvnw, and mvnw.cmd are in your repo root
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make the wrapper executable
RUN chmod +x mvnw

# Download dependencies using the wrapper
# This command might take a while the first time
RUN ./mvnw dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the application using the wrapper (skip tests)
# This should create the war file in /build/target/
RUN ./mvnw package -Dmaven.test.skip=true

# Stage 2: Create the final runtime image
# Using a JRE image is smaller and more secure for running the app
FROM eclipse-temurin:17-jre-jammy

# Set the working directory for the runtime stage
WORKDIR /app

# Copy the built WAR file from the builder stage
# Ensure this filename matches the output of the 'mvnw package' command
COPY --from=builder /build/target/TwistThrottle-0.0.1-SNAPSHOT.war application.war

# Expose the port the main application runs on (default 8080)
# This should match the server.port in your application.properties if you changed it
EXPOSE 8080

# Specify the command to run on container start
# Use java -jar to run the executable WAR file
ENTRYPOINT ["java", "-jar", "/app/application.war"]
