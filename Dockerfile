FROM maven:3-jdk-11
ADD . /build
WORKDIR /build
RUN mvn clean install

FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=/build/target/wow-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
