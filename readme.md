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
```sh
$ git clone https://github.com/lunar-logan/dynamic-number-allocation-service.git
$ cd dynamic-number-allocation-service
$ gradle shadowJar 
$ java -jar build/libs/dynamic-number-allocation-service.jar server /path/to/config/file.yml

```
If you don't want to provide any configuration file, then make sure that your `redis` instance is up and running at `localhost` on default port.

Examples
---

**Sample request**
```sh
curl -X PUT \
  'http://localhost:8080/phone-number/allocate?preferred=1111111124' \
  -H 'accept: */*' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json'
  
```

**Sample response**
```bash
111-111-1136
```
License: [The Unlicense](http://unlicense.org/)