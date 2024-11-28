FROM maven:3.6.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/target/TaskManager-0.0.1-SNAPSHOT.jar app.jar


ENV POSTGRES_HOST=localhost
ENV POSTGRES_PORT=5432
ENV POSTGRES_DB=TaskManager
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=Aezakmi1111

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
