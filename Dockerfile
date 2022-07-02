FROM openjdk:11
VOLUME /tmp
ADD ./target/movement-service-1.0.0.jar movement-service-1.0.0.jar
ENTRYPOINT ["java","-jar","/movement-service-1.0.0.jar"]