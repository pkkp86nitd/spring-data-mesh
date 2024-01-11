FROM openjdk:17
COPY target/spring-data-mesh-1.0-SNAPSHOT.jar spring-data-mesh.jar
EXPOSE 8080


ENV JAVA_OPTS="--add-exports=java.base/sun.nio.ch=ALL-UNNAMED"


CMD ["sh", "-c", "java $JAVA_OPTS -jar spring-data-mesh.jar"]
