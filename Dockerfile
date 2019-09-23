FROM openjdk:12
LABEL maintainer="j40f893@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/ticket-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} ticket.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/ticket.jar"]