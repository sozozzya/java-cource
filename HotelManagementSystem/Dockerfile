FROM maven:3.9.9-eclipse-temurin-23-alpine

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

EXPOSE 9090

CMD ["mvn", "-pl", "presentation", "jetty:run"]