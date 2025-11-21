# cache deps.
FROM maven:3.9.10-eclipse-temurin-17 as DEPS
WORKDIR /opt/app
COPY ../library/pom.xml library/pom.xml
COPY ../pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.9.0:go-offline

# build
FROM maven:3.9.10-eclipse-temurin-17 as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app/ /opt/app
COPY ../library/src /opt/app/library/src
# use -o (--offline) if you need to exclude artifacts.
# if you have excluded artifacts, then remove -o flag
RUN mvn -B -e -o clean install -DskipTests=true

# copy app  jar to final image
FROM azul/zulu-openjdk-debian:17-latest
WORKDIR /opt/app
COPY --from=builder /opt/app/library/target/*.jar application.jar
EXPOSE 8686
CMD [ "java", "-jar", "/opt/app/application.jar" ]