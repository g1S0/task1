FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY gradle /app/gradle
COPY gradlew /app/
COPY build.gradle /app/

COPY src /app/src/

COPY config/checkstyle /app/config/checkstyle

RUN chmod +x gradlew

RUN ./gradlew clean build -x test -PtestPackage --no-daemon

EXPOSE 8080

CMD ["java", "-jar", "build/libs/task1-1.0-SNAPSHOT.jar"]