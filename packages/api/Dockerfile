#
# Build API
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/api/src
COPY pom.xml /home/api
RUN mvn -f /home/api/pom.xml clean package

#
# Package API
#
FROM openjdk:11-jre-slim
COPY --from=build /home/api/target/api.jar api.jar
ENTRYPOINT ["java","-jar","api.jar", "--spring.config.location=classpath:/application.properties,/etc/osahft/api.properties"]
