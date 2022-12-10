FROM openjdk:18.0-jdk
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]