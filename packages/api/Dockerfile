#
# Package API
#
FROM openjdk:11-jre-slim
ADD ./target/api.jar api.jar
ENTRYPOINT ["java","-jar","api.jar", "--spring.config.location=classpath:/application.properties,/etc/osahft/api.properties"]
