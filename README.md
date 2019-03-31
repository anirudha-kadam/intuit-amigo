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
	- Spring Security
	- Spring Session
	- Apache CXF
	- Spring Cloud Config Server
	- Swagger
	- Lombok
	
### Unit Tests
	- Mockito
	- Junit
	- Powermock

### Swagger URL
https://localhost:8080/intuit-amigo/api/api-docs?url=/intuit-amigo/api/swagger.json#/

### How to build
mvn clean package

### How to run
mvn spring-boot:run (embedded tomcat)

Or

Exlpode war in a standalone tomcat
