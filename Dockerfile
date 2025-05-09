FROM maven:3.9.6-eclipse-temurin-22-alpine AS build
#Vi ved ikke helt hvorfor vi har fundet det her build tool version
#Apline er et linux system

WORKDIR /app
#Det her fortæller imagen at work folderen er inden i /application i image

COPY pom.xml ./
#Det her kopier vores dependencies ind i vores maven build

RUN  mvn dependency \
#den her køre alle dependency så vi har de rigtige libaries inde i folderne

COPY src ./scr
#kopier vores src code over i vores docker image.

RUN mvn clean package -DskipTest
#Køre et clean builder af vores program.

FROM openjdk:22-jdk-slim AS runtime
#Den her henter fra image vores builder

WORKDIR /app
#Builder application og ligger den i app

COPY --from=build /app/target/*.jar app.jar
#Når vi har bygget ligger programmet i vores target folder, der henter vi bare den jar fil vi har

EXPOSE 8080
#Nu tilkende giver vi lige en port til vores program

ENTRYPOINT ["java","-jar","app.jar"]
# Når kontaineren køre op, køre der de her java ting. Ved ikke om det her er nødvendigt. Wild card



