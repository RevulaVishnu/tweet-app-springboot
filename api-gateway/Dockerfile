FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8084
ADD "target/api-gateway.jar" "api-gateway.jar"
ENTRYPOINT [ "java", "-jar", "/api-gateway.jar" ]