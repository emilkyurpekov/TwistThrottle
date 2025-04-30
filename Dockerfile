
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app


COPY pom.xml ./

RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests


ARG JAR_FILE=target/TwistThrottle-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
