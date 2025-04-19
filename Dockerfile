FROM amazoncorretto:21-alpine-jdk
COPY build/libs/*.jar java-blog.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080