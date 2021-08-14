FROM java:8

ARG JAR_FILE=./build/libs/*.jar

ADD ${JAR_FILE} /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]