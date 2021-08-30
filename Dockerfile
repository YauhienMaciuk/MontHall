FROM ubuntu:20.04

RUN apt-get update -y
RUN apt-get install -y software-properties-common
RUN apt-get install -y openjdk-11-jdk

WORKDIR /usr/app

COPY ./target/montyhall-0.0.1-SNAPSHOT.jar ./

CMD java -jar montyhall-0.0.1-SNAPSHOT.jar