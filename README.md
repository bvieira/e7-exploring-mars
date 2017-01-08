e7-exploring-mars
======================

# Technologies
List of technologies that I chose to work:
* java - required for coding test
* solr - required for coding test
* logback - async logging library
* resteasy - lightweight framework for REST APIs
* guice - lightweight dependency injection framework
* gson - simple serialization/deserialization json library
* assertj, junit, mockito - unit test with mocks and asserts
* jetty - HTTP (Web) server and Java Servlet container
* docker/docker-compose - container, helps to guarantees the environment creation and isolates the development

# Notes
* 

# Build
```sh
$ docker run -it --rm -v "$PWD":/usr/src/project -w /usr/src/project maven mvn clean install
```

# Run
```sh
$ docker-compose up --build -d
```

# Stop
```sh
$ docker-compose stop
```

# Logs
```sh
$ docker-compose logs -f
```

