FROM java:8
COPY target/bo-0.0.1-SNAPSHOT.jar apibackoffice.jar
EXPOSE 8081
CMD ["java", "-jar", "apibackoffice.jar"]