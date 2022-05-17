FROM java

RUN mkdir /app

COPY ./build/libs/persecution-0.0.1-all.jar /persecution-app/persecution-0.0.1-all.jar
WORKDIR /persecution-app

CMD ["java", "-jar", "persecution-0.0.1-all.jar"]

EXPOSE 8080