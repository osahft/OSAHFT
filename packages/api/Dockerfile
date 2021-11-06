#
# Package API
#
FROM openjdk:11-jre-slim
ADD ./target/api.jar /home/api/target/api.jar
ENTRYPOINT ["java","-jar","api.jar", "--spring.config.location=classpath:/application.properties,/etc/osahft/api.properties"]
