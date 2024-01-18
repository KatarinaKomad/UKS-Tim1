# Build Stage
FROM openjdk:17-alpine AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN dos2unix ./mvnw
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

# CMD ["./mvnw", "spring-boot:run"]
RUN ./mvnw package -DskipTests


# Final Stage
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar ./uks.jar
CMD ["java", "-jar", "uks.jar"]
