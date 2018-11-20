# request-validator

This repository is an coding evaluation task for a company I've applied.

Details for the task is omitted for now.

# database

Project needs a mysql database. A sample DDL SQL is provided here:
## create database
```sql
CREATE USER 'textual'@'localhost'
  IDENTIFIED BY 'textual';

CREATE DATABASE textual;

GRANT ALL PRIVILEGES ON textual.* TO 'textual'@'localhost';
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
      
   
# references