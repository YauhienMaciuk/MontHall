FROM openjdk:17-jdk-slim

WORKDIR /usr/app

COPY ./target/montyhall-0.0.1-SNAPSHOT.jar ./

CMD java -jar montyhall-0.0.1-SNAPSHOT.jar