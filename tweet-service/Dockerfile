FROM openjdk:11
EXPOSE 8082
VOLUME /tweet-app
ADD "target/tweet-service.jar" "tweet-service.jar"
ENTRYPOINT [ "java", "-jar", "/tweet-service.jar" ]