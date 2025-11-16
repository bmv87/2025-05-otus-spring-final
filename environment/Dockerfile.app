FROM openjdk:21-jdk
EXPOSE 8686

ADD ./library/target/*.jar otus-spring-final.jar
ENTRYPOINT ["java", "-jar", "/otus-spring-final.jar"]