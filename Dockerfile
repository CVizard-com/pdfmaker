
FROM maven:3-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
copy resources ./resources

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/pdfmaker.jar .
RUN mkdir -p resources
EXPOSE 8084:8084
CMD ["java", "-jar", "pdfmaker.jar"]