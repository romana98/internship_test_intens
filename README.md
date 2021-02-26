# Internship task
    
### Technologies:
    1) Spring boot application for backend - runs on port: http://localhost:8080
    2) Angular application for frontend - runs port: http://localhost:4200
    3) PostgreSQL database

### Instructions for project startup:

PostgreSQL
- Have an installed PostgreSQL database with set username/password

Spring boot
- Before starting Spring boot application it is needed to change database username/password in: **internship\src\main\resources\application.properties**
<br /><br /> and execute maven command: 
 > mvn clean compile install

Angular
- Before starting Angular application it is needed to position on: **internship-frontend** and execute npm command: 
 > npm install

### Additional information:

Swagger
- Spring boot application also has Swagger implementation. 
- Swagger UI is accessible at: http://localhost:8080/swagger-ui.html#/

Tests
- Implemented unit and integration tests for service layer and rest APIs
- Available SuiteAll which calls all implemented tests. Path to SuiteAll: **internship\src\test\java\com\project\internship\SuiteAll**
