FROM openjdk:17-oracle
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} store_me.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=Asia/Seoul", "/store_me.jar"]