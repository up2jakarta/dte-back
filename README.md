# Up2Jakarta :: DTE | Decision Tree Engine

[![Maven Central](https://img.shields.io/maven-central/v/io.github.up2jakarta/up2dte-exe?style=for-the-badge&color=green)](https://central.sonatype.com/artifact/io.github.up2jakarta/up2dte-exe)

## Requirements

1. JDK v8
2. Maven V3

# Dependencies

``` xml
    <dependency>
        <groupId>io.github.up2jakarta</groupId>
        <artifactId>up2dte-exe</artifactId>
        <version>1.0.0</version>
    </dependency>
```

## Maven Build

```
https://github.com/up2jakarta/dte-back.git
cd dte-back/
mvn clean install
```

## License

Up2Jakarta/Divers is Open Source software released under the [MIT license](./LICENSE.txt).

### DB Migration
* Run `cd app-dbm` to work with the DBM application.
* Run `mvn clean package spring-boot:run -DskipTests=true` to update the DB.

### Web API
* Run `cd app-web` to work with the API application.
* Run `mvn clean package spring-boot:run -DskipTests=true` to run API Server.
* Navigate to `http://localhost:8080/` to check API version