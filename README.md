# intuit-amigo

### Description
A Twitter like application APIs

### Storage
- Distribute Redis Cache
	- Currently running in a standalone mode but clustering as well as sharding possible for scaling
	- In memory storage as well as persistance enabled
	- RDB and AOF modes enabled for fault recovery to consistent state
	- Redisson java client and in memory grid
	
### Frameworks/Libraries Used
	- Spring Boot
	- Spring Web
	- Spring Security (Basic Auth Login)
	- Spring Session Data Redis (Sessions are stored in redis)
	- Apache CXF (JAX-RS Implementation)
	- Spring Cloud Config Server (Externalize properties to github and change properties with ZDD)
	- Swagger (Document in code)
	- Lombok (No boilerplate please)
	
### Unit Tests
	- JUnit
	- Mockito
	- Powermock

### Swagger URL
https://localhost:8080/intuit-amigo/api/api-docs?url=/intuit-amigo/api/swagger.json#/

### How to build
mvn clean package

### How to run
mvn spring-boot:run (embedded tomcat)

Or

Exlpode war in a standalone tomcat
