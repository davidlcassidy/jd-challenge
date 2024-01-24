# John Deere Challenge Microservice

- [Dependencies](#dependencies)
- [Setting Up the Project](#setting-up-the-project)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Update Application Properties with Local Database Credentials](#2-update-application-properties-with-local-database-credentials)
- [Running the Microservice Locally](#running-the-microservice-locally)
  - [Using IntelliJ](#using-intellij)
  - [Using Docker](#using-docker)
- [Architectural Documentation](#architectural-documentation)
  - [Current Architecture Diagram](#current-architecture-diagram)
  - [Future Improvements](#future-improvements)
- [Swagger API Documentation](#swagger-api-documentation)
- [Postman Queries](#postman-queries)

## Dependencies

- [Java](https://www.java.com/en/download/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Docker](https://www.docker.com/)

## Setting Up the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/davidlcassidy/jd-challenge
   cd jd-challenge
   ```

2. Update the Application Properties file with Local Database Credentials:
Open the `application.properties` file and update the properties with your local database credentials

## Running the Microservice Locally

### Using IntelliJ

1.  Open the project in IntelliJ IDEA.
    
2.  Run the main application class JDChallenge.
    

### Using Docker

1.  Build and run the Docker image:
    
   ```bash
   docker-compose up --build
   ```

## Architectural Documentation

### Current Architecture Diagram

I tried to limit my dev effort to eight hours as specified in the requirements document and unfortunately I ran out of time before I could implement everything I wanted to. The biggest item I missed was the integration of a queue, so I created two POST requests instead to allow the microservice to be fully tested. 

![Architecture Diagram](images/current_architecture_diagram.jpg)

### Future Improvements

Along with the queue, I was also not able build a deployment pipeline, but I documented what I was planning with blue arrows in the image below.

![Future Improvements](images/future_improvements.jpg)

## Swagger API Documentation

Access the Swagger API documentation at http://localhost:8080/swagger-ui.html when the microservice is running locally.

## Postman Queries

Import the provided Postman collection for testing the endpoints.

 

