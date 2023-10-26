FROM openjdk:20-ea-11
VOLUME /tmp
COPY build/libs/backend-0.0.1.jar daon-backend.jar
ENTRYPOINT ["java", "-jar", "daon-backend.jar"]
