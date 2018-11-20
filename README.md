# request-validator

This repository is an coding evaluation task for a company I've applied.

Details for the task is omitted for now.

# database

Project needs a mysql database. A sample DDL SQL is provided here:
## create database
```sql
CREATE USER 'validator'@'localhost'
  IDENTIFIED BY 'validator';

CREATE DATABASE validator;

GRANT ALL PRIVILEGES ON validator.* TO 'validator'@'localhost';
```

## create tables and feed sample data
```sql
CREATE TABLE `customer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `active` tinyint(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
);
```
```sql
INSERT INTO `customer` VALUES (1,'Big News Media Corp',1),(2,'Online Mega Store',1),(3,'Nachoroo Delivery',0),(4,'Euro Telecom Group',1);
```
```sql
CREATE TABLE `ip_blacklist` (
  `ip` int(11) unsigned NOT NULL,
  PRIMARY KEY (`ip`)
);
```
```sql
INSERT INTO `ip_blacklist` VALUES (0),(2130706433),(4294967295);
```
```sql
CREATE TABLE `ua_blacklist` (
  `ua` varchar(255) NOT NULL,
  PRIMARY KEY (`ua`)
);
```
```sql
INSERT INTO `ua_blacklist` VALUES ('A6-Indexer'),('Googlebot-News'),('Googlebot');
```
```sql
CREATE TABLE `hourly_stats` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) unsigned NOT NULL,
  `time` timestamp NOT NULL,
  `request_count` bigint(20) unsigned NOT NULL DEFAULT '0',
  `invalid_count` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_customer_time` (`customer_id`,`time`),
  KEY `customer_idx` (`customer_id`),
  CONSTRAINT `hourly_stats_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);
```

# build
RequestValidator is a spring-boot project.

To package and run as a executable jar, first compile and package the project wih maven `mvn package` command.

Tests will be executed and executable jar will be generated in the `target` folder.

# run

To start the project as a server run:

```
java -jar target/request-validator-1.0-SNAPSHOT.jar
```

Project is serving on HTTP port 8090.

# send request & get reply

All HTTP request to be validated should be in the given JSON format:

```json
{
  "customerID":1,
  "tagID":2,
  "userID":"aaaaaaaa-bbbb-cccc-1111-222222222222",
  "remoteIP":"123.234.56.78",
  "timestamp":1500000000
}
```
All requests should be send with `HTTP POST` to `/processor` with `application/json` content type. Example:

```http request
POST http://localhost:8090/processor
Content-Type: application/json

{
  "customerID":1,
  "tagID":2,
  "userID":"aaaaaaaa-bbbb-cccc-1111-222222222222",
  "remoteIP":"123.234.56.78",
  "timestamp":1500000000
}
```

## responses for valid and invalid requests
All examples here is implemented as `junit4 test` in the `test` folder in the project. All valid requests are reponded with `OK (200)`
and all invalids are with `BAD REQUEST (400)` HTTP status codes. 
### valid request
#### request
```json
{
  "customerID":1,
  "tagID":2,
  "userID":"aaaaaaaa-bbbb-cccc-1111-222222222222",
  "remoteIP":"123.234.56.78",
  "timestamp":1500000000
}
```
#### response
```
OK
```

### malformed JSON
#### request
```
{
```
#### response
```
Malformed JSON
```

### missing field
#### request
```json
{
  "tagID":2,
  "userID":"aaaaaaaa-bbbb-cccc-1111-222222222222",
  "remoteIP":"123.234.56.78",
  "timestamp":1500000000
}
```
#### response
```
customerID cannot be null
```

### customer not found
#### request
```json
{
  "customerID":5,
  "tagID":2,
  "userID":"aaaaaaaa-bbbb-cccc-1111-222222222222",
  "remoteIP":"123.234.56.78",
  "timestamp":1500000000
}
```
#### response
```
Customer ID 5 not found
``` 

### customer disabled
#### request
```json
{
  "customerID":3,
  "tagID":2,
  "userID":"aaaaaaaa-bbbb-cccc-1111-222222222222",
  "remoteIP":"123.234.56.78",
  "timestamp":1500000000
}
```
#### response
```
Customer with ID 3 is disabled
``` 

### remote ip blacklisted
#### request
```json
{
  "customerID":3,
  "tagID":2,
  "userID":"aaaaaaaa-bbbb-cccc-1111-222222222222",
  "remoteIP":"127.0.0.1",
  "timestamp":1500000000
}
```
#### response
```
Remote IP 127.0.0.1 [2130706433] is blacklisted
``` 

### user-agent blacklisted
#### request
```http request
POST http://localhost:8090/processor
Content-Type: application/json
User-Agent: A6-Indexer

{
  "customerID":1,
  "tagID":2,
  "userID":"aaaaaaaa-bbbb-cccc-1111-222222222222",
  "remoteIP":"123.234.56.78",
  "timestamp":1500000000
}
```
#### response
```
User-Agent A6-Indexer is blacklisted
``` 

# stats service
## request
```http request
POST http://localhost:8090/stats
Content-Type: application/json

{
  "customerID": 1,
  "timestamp": 1542574800000
}
```

## response
```json
{
  "hourlyStats": {
    "0": {
      "requestCount": 24,
      "invalidCount": 0
    },
    "1": {
      "requestCount": 23,
      "invalidCount": 0
    },
    "2": {
      "requestCount": 22,
      "invalidCount": 0
    },
    "3": {
      "requestCount": 21,
      "invalidCount": 0
    },
    "4": {
      "requestCount": 20,
      "invalidCount": 0
    },
    "5": {
      "requestCount": 19,
      "invalidCount": 0
    },
    "6": {
      "requestCount": 18,
      "invalidCount": 0
    },
    "7": {
      "requestCount": 17,
      "invalidCount": 0
    },
    "8": {
      "requestCount": 16,
      "invalidCount": 0
    },
    "9": {
      "requestCount": 15,
      "invalidCount": 0
    },
    "10": {
      "requestCount": 14,
      "invalidCount": 0
    },
    "11": {
      "requestCount": 13,
      "invalidCount": 0
    },
    "12": {
      "requestCount": 12,
      "invalidCount": 0
    },
    "13": {
      "requestCount": 11,
      "invalidCount": 0
    },
    "14": {
      "requestCount": 10,
      "invalidCount": 0
    },
    "15": {
      "requestCount": 9,
      "invalidCount": 0
    },
    "16": {
      "requestCount": 8,
      "invalidCount": 0
    },
    "17": {
      "requestCount": 7,
      "invalidCount": 0
    },
    "18": {
      "requestCount": 6,
      "invalidCount": 0
    },
    "19": {
      "requestCount": 5,
      "invalidCount": 0
    },
    "20": {
      "requestCount": 4,
      "invalidCount": 0
    },
    "21": {
      "requestCount": 3,
      "invalidCount": 0
    },
    "22": {
      "requestCount": 2,
      "invalidCount": 0
    },
    "23": {
      "requestCount": 1,
      "invalidCount": 0
    }
  },
  "totalRequestCount": 300
}
```
# testing
Project provides `junit4` test in the `test` folder. Tests are executed on a in-memory test database. Schema creation and sample data feeding is handled automatically.  
A sample case for every possible scenario is tried to be provided.
To run the tests:
```
mvn test
```

# further considerations
Since the project is for evaluational purposes, performance optimisation is not considered:

* For example all requests are stored in mysql database, however in a real production environment either a in-memory database or cache server should be chosen as data store.  
* The HTTP layer is handled via `spring-boot`. In real world, a custom HTTP interceptor may been developed and benchmarked with `spring-boot`, then choose the one that has most performance.

# references
## MyRequestWrapper
To extract the request body in the `CollectorRequestInterceptor.preHandle` method, `request.getInputStream()` method needs to be called. 
However, `request.getInputStream()` method cannot be called more than once [(*)](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/ServletRequest.html#getInputStream--).
A workaround is provided [here](https://howtodoinjava.com/servlets/httpservletrequestwrapper-example-read-request-body/). 

## IpUtils
Long and IP address string conversion is done with the code provided [here](https://www.mkyong.com/java/java-convert-ip-address-to-decimal-number/) 
