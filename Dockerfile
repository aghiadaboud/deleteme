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
RUN chmod +x /app/wait-for-it.sh
EXPOSE 8080





