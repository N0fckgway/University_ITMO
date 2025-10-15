FROM gradle:8.10-jdk17 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle jar --no-daemon

FROM eclipse-temurin:17-jre

EXPOSE 1919
RUN mkdir /server
WORKDIR /server
COPY --from=build /home/gradle/src/server/build/libs/*.jar /server/server.jar
COPY libs/fastcgi-lib.jar /server/fastcgi-lib.jar
ENTRYPOINT ["java", "-DFCGI_PORT=1919", "-cp", "/server/server.jar:/server/fastcgi-lib.jar", "dev.n0fckgway.Main"]
