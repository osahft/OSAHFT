# OSAHFT REST API

## Build & Run

### Requirements

- JDK 11 or higher
- Maven 3.5.0 or higher

### Build

- JAR: `mvn clean package`
- Docker Image: `docker build . -t $IMAGE_TAG`

### Run

- JAR: `java -jar api.jar`
- Docker Image: `docker run $IMAGE_TAG`

## How to generate assertions for unit tests

1. open pom.xml
2. set the parameter `skip` of the `configuration` section of the `assertj-assertions-generator-maven-plugin` to `false`
3. run `mvn assertj:generate-assertions`
4. copy the newly generated assertions
   from `target/generated-test-sources/assertj-assertions/com/osahft/api/internal/assertion`
   to `src/test/java/com/osahft/api/internal/assertion` (Make sure you only copy the missing ones)
5. set the parameter `skip` of the `configuration` section of the `assertj-assertions-generator-maven-plugin`
   to `true` (restore original state)
6. open `src/test/java/com/osahft/api/internal/assertion/Assertions.java` and add the `assertThat` methods representing
   the asserts you just generated / copied
7. run `mvn clean install -DskipTests=true` to make sure that nothing went wrong

## Additional notes

- if you get an error similar to `No qualifying bean of type 'org.springframework.boot.info.BuildProperties' available`
  you should delegate IDE build/run actions to maven (Intellij: Settings > Build, Execution, Deployment > Build Tools >
  Maven > Runner > check `Delegate IDE build/run actions to Maven`)