LABEL authors="Hai"

# First stage, build the custom JRE
FROM eclipse-temurin:17-jdk-alpine AS jre-builder

# Install binutils, required by jlink
RUN apk update && apk add binutils

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /optimized-jdk-17

# Second stage, use the custom JRE and build the app image
FROM alpine:latest

# Set environment variables
ENV JAVA_HOME=/opt/jdk/jdk-17
ENV PATH="${JAVA_HOME}/bin:${PATH}"
# Copy JRE from the base image
COPY --from=jre-builder /optimized-jdk-17 $JAVA_HOME

# Add app user
ARG APPLICATION_USER=spring

# Create a user to run the application, don't run as root
RUN addgroup -S $APPLICATION_USER && \
    adduser -S $APPLICATION_USER -G $APPLICATION_USER

# Create the application directory
RUN mkdir /app && chown -R $APPLICATION_USER /app

# Copy the application JAR file
COPY --chown=$APPLICATION_USER:$APPLICATION_USER target/*.jar /app/app.jar

# Set working directory
WORKDIR /app

# Switch to the app user
USER $APPLICATION_USER

# Expose the application's port
EXPOSE 8080
# Dockerfile
# Use environment variables for database connection
ENV SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3307/dd_db"
ENV SPRING_DATASOURCE_USERNAME="admin"
ENV SPRING_DATASOURCE_PASSWORD="admin1234"

# ENTRYPOINT with environment variables
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --spring.datasource.url=$SPRING_DATASOURCE_URL --spring.datasource.username=$SPRING_DATASOURCE_USERNAME --spring.datasource.password=$SPRING_DATASOURCE_PASSWORD"]