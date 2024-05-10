# misc-info

Image names with tags
=======================
adoptopenjdk/openjdk8:jdk8u412-b08-alpine


adoptopenjdk/openjdk8:jdk8u412-b08-alpine-slim


adoptopenjdk/openjdk8:alpine-nightly-slim

adoptopenjdk/openjdk11:x86_64-debianslim-jdk-11.0.23_9-slim

adoptopenjdk/openjdk11:jdk-11.0.24_1-ea-beta-ubuntu-nightly-slim

https://catalog.redhat.com/software/containers/search?product_listings_names=Red%20Hat%20build%20of%20OpenJDK&p=1




C:\Users\vidha>docker scout quickview registry.access.redhat.com/ubi8/openjdk-11:1.19-4
    i New version 1.8.0 available (installed version is 1.6.3) at https://github.com/docker/scout-cli
    v Image stored for indexing
    v Indexed 305 packages

  Target     │  registry.access.redhat.com/ubi8/openjdk-11:1.19-4  │    0C    22H    79M    32L
    digest   │  de232d52a601                                       │
  Base image │  redhat/ubi8-minimal:latest                         │

What's Next?
  View vulnerabilities → docker scout cves registry.access.redhat.com/ubi8/openjdk-11:1.19-4
  Include policy results in your quickview by supplying an organization → docker scout quickview registry.access.redhat.com/ubi8/openjdk-11:1.19-4 --org <organization>


C:\Users\vidha>docker scout quickview adoptopenjdk/openjdk11:jdk-11.0.24_1-ea-beta-ubuntu-nightly-slim
    i New version 1.8.0 available (installed version is 1.6.3) at https://github.com/docker/scout-cli
    v SBOM of image already cached, 191 packages indexed

  Target             │  adoptopenjdk/openjdk11:jdk-11.0.24_1-ea-beta-ubuntu-nightly-slim  │    0C     0H     5M    15L
    digest           │  8119655eacda                                                      │
  Base image         │  ubuntu:20.04                                                      │    0C     0H     2M    10L
  Updated base image │  ubuntu:23.10                                                      │    0C     0H     2M     5L
                     │                                                                    │                         -5

What's Next?
  View vulnerabilities → docker scout cves adoptopenjdk/openjdk11:jdk-11.0.24_1-ea-beta-ubuntu-nightly-slim
  View base image update recommendations → docker scout recommendations adoptopenjdk/openjdk11:jdk-11.0.24_1-ea-beta-ubuntu-nightly-slim
  Include policy results in your quickview by supplying an organization → docker scout quickview adoptopenjdk/openjdk11:jdk-11.0.24_1-ea-beta-ubuntu-nightly-slim --org <organization>



====================
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-wiremock-project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.github.tomakehurst</groupId>
            <artifactId>wiremock-standalone</artifactId>
            <version>2.27.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.0.0</version>
                <executions>
                    <execution>
                        <id>wiremock</id>
                        <phase>pre-integration-test</phase>
                        <goals>
                            <goal>java</goal>
                        </goals>
                        <configuration>
                            <classpathScope>test</classpathScope>
                            <mainClass>com.github.tomakehurst.wiremock.standalone.WireMockServerRunner</mainClass>
                            <arguments>
                                <argument>--port</argument>
                                <argument>8080</argument>
                                <argument>--root-dir</argument>
                                <argument>${project.basedir}/src/main/resources</argument>
                            </arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>

# Use an appropriate base image with Maven and Java installed
FROM maven:3.8.4-openjdk-11-slim AS build

# Set working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .

# Download dependencies
RUN mvn -B dependency:resolve dependency:resolve-plugins

# Copy the entire project
COPY . .

# Build the project
RUN mvn -B clean install

# Use a lightweight base image for the final image
FROM adoptopenjdk:11-jre-hotspot AS final

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/my-wiremock-project-1.0-SNAPSHOT.jar .

# Expose port 8080 (the port WireMock runs on)
EXPOSE 8080

# Command to run WireMock
CMD ["java", "-jar", "my-wiremock-project-1.0-SNAPSHOT.jar"]

=========================



https://wiremock.org/docs/standalone/docker/


https://github.com/wiremock/wiremock-docker/blob/3.5.4-1/Dockerfile


docker pull adoptopenjdk/openjdk11:jdk-11.0.23_9-slim

docker pull adoptopenjdk/openjdk11:jdk-11.0.23_9

docker pull adoptopenjdk/openjdk11:jdk-11.0.24_1-ea-beta-alpine-nightly-slim
