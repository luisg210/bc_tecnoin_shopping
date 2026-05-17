FROM openjdk:18-jdk-alpine AS builder

WORKDIR /app/msvc-departamento

COPY ./pom.xml /app
COPY ./msvc-departamento/.mvn ./.mvn
COPY ./msvc-departamento/mvnw .
COPY ./msvc-departamento/pom.xml .

RUN sed -i -e 's/\r$//' ./mvnw
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-departamento/src ./src

RUN ./mvnw clean package -DskipTests


FROM openjdk:18-jdk-alpine

WORKDIR /app

RUN echo "Antes"
COPY --from=builder /app/msvc-departamento/target/msvc-departamento-0.0.1-SNAPSHOT.jar .
RUN echo "Lo pase!!"

#referencia del jar
ENTRYPOINT ["java","-jar", "msvc-departamento-0.0.1-SNAPSHOT.jar"]