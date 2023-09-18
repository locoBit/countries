# Countries REST API

This app contains end endpoint to get countries information from [restcountries](https://restcountries.com/v3.1/all) API.

I have added one parameter in the internal request to filter the response and keep the result simple as possible:
- fields=name,population,flag

Which returns only the name, population and flag of each country.

## Running the app

The application is configured to run in the port 8090.

```bash
./mvnw install
./mvnw spring-boot:run
```

If you what to run the app in debug mode:

```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"
```

Then you need to attach your debugger to the port 8000.

## Local Environment

We are using java version 20.0.2

```
openjdk version "20.0.2" 2023-07-18
OpenJDK Runtime Environment Corretto-20.0.2.9.1 (build 20.0.2+9-FR)
OpenJDK 64-Bit Server VM Corretto-20.0.2.9.1 (build 20.0.2+9-FR, mixed mode, sharing)
```

If you want to manage java versions locally I suggest to install [sdkman](https://sdkman.io/).

## Example of usage

See the API usage and examples [here](https://documenter.getpostman.com/view/8585549/2s9YC8uqUg#429cd4a9-47db-4d1f-a248-72d1812eb145)

## Code Quality

See the Sonar Cloud results [here](https://sonarcloud.io/project/overview?id=locoBit_countries)
