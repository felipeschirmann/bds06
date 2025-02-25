FROM eclipse-temurin:21.0.5_11-jdk-alpine AS builder
WORKDIR /opt/app
COPY --chmod=755 .mvn/ .mvn
COPY --chmod=755 ./mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:21.0.5_11-jre-alpine AS final
RUN apk update && apk upgrade && apk --no-cache add curl
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]
HEALTHCHECK --start-interval=30s --interval=10s --timeout=3s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1