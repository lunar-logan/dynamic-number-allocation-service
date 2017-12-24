Phone Number Allocation Service
===

This is a simple `dropwizard` based web service that allocates a new phone number to clients.

Requirements
---
1. [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) version ``9`` or higher 
2. [Gradle](https://gradle.org/) build tool
3. [Redis](https://redis.io/)

Running the code
---
```$xslt
$ git clone https://github.com/lunar-logan/dynamic-number-allocation-service.git
$ cd dynamic-number-allocation-service
$ gradle shadowJar 
$ java -jar build/libs/dynamic-number-allocation-service.jar server /path/to/config/file.yml

```

License: [The Unlicense](http://unlicense.org/)