FROM openjdk:11

COPY . /server
WORKDIR /server

CMD ["./gradlew", "bootRun", "-Dspring.profiles.active=dev"]