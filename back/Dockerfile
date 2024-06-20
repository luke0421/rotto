FROM openjdk:17-jdk
WORKDIR /app
COPY build/libs/rotto-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=production", ">>", "/home/ubuntu/applicationBE.log", "2>&1"]