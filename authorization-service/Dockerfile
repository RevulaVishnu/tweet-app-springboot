FROM openjdk:11
EXPOSE 8081
VOLUME /tweet-app
ADD "target/auth-service.jar" "auth-service.jar"
ENTRYPOINT [ "java", "-jar", "/auth-service.jar" ]