FROM gradle:9.3.1-jdk25-alpine AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:25.0.2_10-jre

RUN apt-get update && apt-get install -y ffmpeg && rm -rf /var/lib/apt/lists/*

EXPOSE 8080

RUN mkdir /src

COPY --from=build /app/build/libs/*.jar /src/app.jar

ENTRYPOINT ["java", "-jar", "/src/app.jar"]