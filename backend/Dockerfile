# syntax=docker/dockerfile:1

# Comments are provided throughout this file to help you get started.
# If you need more help, visit the Dockerfile reference guide at
# https://docs.docker.com/go/dockerfile-reference/

# Want to help us make this template better? Share your feedback here: https://forms.gle/ybq9Krt8jtBL3iCk7

################################################################################

# Create a stage for resolving and downloading dependencies.
FROM gradle:8.7.0-jdk21-jammy

WORKDIR /usr/src/app

COPY . .

EXPOSE 42069

CMD gradle bootRun

# Expose default Spring Boot port
EXPOSE 8080

# Profiles/DB via environment variables; see .env.* files
