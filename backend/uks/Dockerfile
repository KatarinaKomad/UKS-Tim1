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

# Install git and openssh
RUN apk add --no-cache git openssh

# Copy private key into /root/.ssh - maybe not necessary
COPY src/gitolite/gitolite /root/.ssh/id_rsa
RUN chmod 600 /root/.ssh/id_rsa

WORKDIR /app
COPY --from=build /app/target/*.jar ./uks.jar

# Copy the gitolite folder into /app
COPY src/gitolite /app/gitolite

# Copy the entrypoint script from resources to /usr/local/bin
COPY src/main/resources/entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

# Make all scripts inside the gitolite folder executable
RUN find /app/gitolite -type f -iname "*.sh" -exec chmod +x {} \;

# Use the custom entrypoint script
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

CMD ["java", "-jar", "uks.jar"]
