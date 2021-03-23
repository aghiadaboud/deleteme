FROM gradle:jdk11 AS BUILD
WORKDIR /terminplanerBuild
COPY . /terminplanerBuild
RUN gradle bootJar

#FROM adoptopenjdk/openjdk11:alpine-jre
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY --from=BUILD /terminplanerBuild/build/libs/*.jar \
                    /app/app.jar
COPY wait-for-it.sh /app/wait-for-it.sh
COPY src/main/resources/key.der /app/src/main/resources/key.der
COPY src/main/resources/organisatoren /app/src/main/resources/organisatoren
COPY src/main/resources/tutoren /app/src/main/resources/tutoren
RUN chmod +x /app/wait-for-it.sh
EXPOSE 8080





