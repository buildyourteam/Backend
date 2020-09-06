FROM openjdk:11-jre-slim
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} eskiiimo.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-DSLACK_URL{$SLACK_URL}","-DDB_URL{$DB_URL}","-DDB_USER{$DB_USER}","-DDB_PASSWORD{$DB_PASSWORD}","/eskiiimo.jar"]