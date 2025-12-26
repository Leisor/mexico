# Stage 1: Build the Java application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the fat jar
RUN mvn clean package -DskipTests

# Stage 2: Setup the runtime environment with GoTTY
FROM eclipse-temurin:21-jre
WORKDIR /app

# Install GoTTY (Linux amd64 version)
ADD https://github.com/yudai/gotty/releases/download/v1.0.1/gotty_linux_amd64.tar.gz /tmp/
RUN tar -xvf /tmp/gotty_linux_amd64.tar.gz -C /usr/local/bin/ && \
    chmod +x /usr/local/bin/gotty

# Copy the built jar from the previous stage
COPY --from=build /app/target/mexico-1.0-SNAPSHOT.jar ./mexico.jar

# Expose the port
EXPOSE 8080

# Run GoTTY wrapping the java command
# -w allows writing (interaction)
# --title sets the browser tab title
CMD ["gotty", "-w", "--title-format", "Mexico Dice Game", "java", "-jar", "mexico.jar"]
