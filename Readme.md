# Directa24 Back-End Developer Challenge
## CHALLENGE IMPLEMENTATION
The implementation is based on Spring Boot 3 (version 3.3.4):
The endpoint is exposed once the com.directa24.main.challenge.DirectorApplication class is run.
- [Sample Request](http://localhost:8080/api/directors?threshold=4)

Besides the main feature endpoints, other ones are available:
- [Health check](http://localhost:8080/actuator/health)
- [Swagger Documentation](http://localhost:8080/webjars/swagger-ui/index.html)
- [Application Metrics](http://localhost:8080/actuator/metrics)


### There are some pending improvements:
#### 1. Persistence:
To avoid persistence dependencies, all the persistence is in memory (using ConcurrentHashMap to store).
A better approach is add Docker (docker-compose) to handle persistence is a proper way (Using Postgresql, for instance)
#### 2. Data Pagination:
In case the director name list is too long, it could cause performance issues. A better approach is to paginate the response.
#### 3. Caching:
Current implementation is per application instance; it is not scalable. Distributed caching is a better approach
#### 4. Integration tests.
Once Docker is added, TestContainer is an excellent tool to create a prod-like environment for testing purposes.
#### 5. Add more tools for improve code quality (Jacoco Plugin was added to validate test coverage)
###### Some alternatives could be:
- SonarQube plugin to ensure code quality
- ArchUnit to ensure that the domain code is isolated from technical details.