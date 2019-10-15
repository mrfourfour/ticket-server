FROM openjdk:12
LABEL maintainer="j40f893@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/ticket-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} ticket.jar
ARG AWS_ACCESS_KEY=$AWS_ACCESS_KEY
ARG AWS_SECRET_KEY=$AWS_SECRET_KEY
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY}
ENV AWS_SECRET_KEY=${AWS_SECRET_KEY}

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/ticket.jar"]