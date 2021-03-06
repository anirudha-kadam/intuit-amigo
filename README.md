# intuit-amigo

### Description
A Twitter like application APIs

### Storage
- Distributed Redis Cache
	- Currently running in a standalone mode but clustering as well as sharding possible for scaling
	- In memory storage as well as persistance enabled
	- RDB and AOF modes enabled for fault recovery to consistent state
	- Redisson java client and in memory grid
	- Extensive use of Redis data structures viz List, Sorted Set, Hash, Atomic Integer to solve various use cases
	
### Frameworks/Libraries Used
	- Spring Boot
	- Spring Web
	- Spring Security (Basic Auth Login)
	- Spring Session Data Redis (User sessions are stored in redis makes application instance purely stateless)
	- Apache CXF (JAX-RS Implementation)
	- Spring Cloud Config Server (Externalize properties to github and change properties without having to rebuild/deploy)
	- Swagger (Document in code)
	- Lombok (No boilerplate please)
	
### Unit Tests
	- JUnit
	- Mockito
	- Powermock
	- Jacoco for test coverage

### Swagger URL
https://localhost:8080/intuit-amigo/api/api-docs?url=/intuit-amigo/api/swagger.json#/

### How to build
mvn clean package

### How to run
mvn spring-boot:run (embedded tomcat)

Or

Exlpode war in a standalone tomcat
