FROM maven:3.9.12-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
COPY server/pom.xml server/pom.xml
COPY gateway/pom.xml gateway/pom.xml
RUN mvn -B dependency:go-offline
COPY . .
RUN mvn -B clean package -DskipTests

FROM amazoncorretto:21 AS server
COPY --from=builder /build/server/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

FROM amazoncorretto:21 AS gateway
COPY --from=builder /build/gateway/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]