
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY pom.xml ./

RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests


ARG JAR_FILE=target/cart-service-1.0.0.jar

COPY ${JAR_FILE} application.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
