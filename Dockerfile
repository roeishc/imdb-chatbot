FROM openjdk:8
COPY target/imdb-chatbot*.jar /usr/src/imdb-chatbot.jar
COPY src/main/resources/application.properties /opt/conf/application.properties
CMD ["java", "-jar", "/usr/src/imdb-chatbot.jar", "--spring.config.location=file:/opt/conf/application.properties"]