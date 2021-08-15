FROM java:8

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=./build/libs/*.jar

ADD ${JAR_FILE} /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]