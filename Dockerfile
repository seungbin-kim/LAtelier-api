FROM openjdk:11

COPY . /server
WORKDIR /server

CMD ["chmod", "+x", "gradlew"]
CMD ["./gradlew", "bootRun", "-Dspring.profiles.active=dev"]