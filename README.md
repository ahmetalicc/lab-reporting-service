# Laboratory Reporting Application

## Overview

This project is a laboratory reporting application developed using Spring Boot, JPA, and Maven. It includes features for managing reports and laborants, user authentication and authorization with JWT, and role-based access control. The application is designed to be run locally or in a Docker environment.

## Features

- **Report Management**: Create, update, view, and delete reports.
- **Laborant Management**: Add, update, list, and delete laborants.
- **User Management**: Add, update, list and delete users.
- **Authentication and Authorization**: Secure endpoints with JWT and role-based access control.
- **Roles**: Admin, Laborant, and Standard User with different permissions.
- **Unit Tests**: Comprehensive unit tests for service methods.
- **Search and Sorting**: Search reports by patient or laborant details and sort by report date.

## Used Technologies

- **JDK 17**: Java Development Kit
- **Spring Boot**: Application framework
- **JPA (Java Persistence API)**: Data persistence
- **Maven**: Dependency management
- **Log4j2**: Logging framework
- **Lombok**: Boilerplate code reduction
- **MySQL**: Database
- **JWT**: Authentication and authorization
- **JUnit**: Unit testing
- **Docker**: Containerization

## Setup and Installation

### Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher
- **MySQL** (for database)
- **Docker** and **Docker Compose** (for Docker setup)

### Running Locally

**Clone the repository:**
   ```bash
   git clone https://github.com/ahmetalicc/ReportingApp.git
   cd ReportingApp
```

**Set Up Database and Environment Variables:**

  - Create a PostgreSQL database with a name of your choice.
  - Create a .env file in the project and add the necessary environment variables. Refer to the .env.example file for the required variables. Make sure to update the database credentials and name according to your setup.

**Build the Application:**

In the root directory of the project, run:

`mvn clean install`

**Run the Application:**

`mvn spring-boot:run`

**Access the Application:**

The application will be running at http://localhost:8080.

### Running with Docker

**Set Up Database and Environment Variables:**

  - Create a PostgreSQL database with a name of your choice.
  - Create a .env file in the project and add the necessary environment variables. Refer to the .env.example file for the required variables. Make sure to update the database credentials and name according to your setup.

**Build Jar File and Docker Image of the Project:**

In the root directory of the project, run:

`mvn clean install`

```bash
docker build -t "image-name":latest .
```

**Build and Run with Docker Compose:**

`docker-compose up -d`

**Stop the Running Application:**

`docker-compose down`

## API Endpoints

### Authentication

- **Login**: `POST localhost:8080/auth/login`
  - **Request Body**:
    ```json
    {
      "username": "string",
      "password": "string"
    }
    ```
  - **Response**:
    ```json
    {
      "token": "string"
    }
    ```
