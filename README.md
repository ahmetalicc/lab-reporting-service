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

## **Important Points**
- **Using Spring Boot Framework**
- **N-tier Architecture**
- **Object Oriented Programming**
- **Compatible with SOLID principles**
- **Token based authentication and authorization**
- **Logging**
- **Unit Tests**
- **Dockerization**

## Setup and Installation

### Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher
- **MySQL** (for database)
- **Docker** and **Docker Compose** (for Docker setup)

### Running Locally

**Clone the repository:**
   ```bash
   git clone https://github.com/ahmetalicc/lab-reporting-service.git
   cd ReportingApp
```

**Set Up Database and Environment Variables:**

  - Create a MySQL database with a name of your choice.
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

  - Create a MySQL database with a name of your choice.
  - Create a .env file in the project and add the necessary environment variables. Refer to the .env.example file for the required variables. Make sure to update the database credentials and name according to your setup.

**Build Jar File and Docker Image of the Project:**

In the root directory of the project, run:

`mvn clean install`

```bash
docker build -t "image-name":latest .
```

**Build and Run with Docker Compose:**

`docker-compose up -d`

**Access the Application:**

The application will be running at http://localhost:8080.

**Stop the Running Application:**

`docker-compose down`

## API Endpoints

### Authentication

#### Login

**Endpoint:** `POST /auth/login`

**Roles Required: None (Public access)**

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```
**Response**:
```json
{
"token": "string"
}
```

### Laborants
#### Create Laborant
**Endpoint:** `POST /laborant/createLaborant`

**Roles Required: ADMIN**

**Request Body:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "hospitalId": "string",
  "user": {
    "username": "string",
    "password": "string",
    "roleIds": "List<Long>"
  }
}
```
**Response:**
```json
{
  "success": "true"
  "message": "Laborant created successfully.",
  "data": {
    "firstName": "string",
    "lastName": "string",
    "hospitalId": "string"
  }
}
```

#### Get All Laborants

**Endpoint:** `GET /laborant/getAllLaborants`

**Roles Required: Any role (ADMIN, LABORANT, STANDARD_USER)**

**Request Parameters:**

- `page` (default: `0`): The page number to retrieve.
- `size` (default: `3`): The number of records per page.
- `sortBy` (default: `id`): The field to sort by.
- `sortOrder` (default: `asc`): The order of sorting (`asc` for ascending, `desc` for descending).
- `filter` (optional): A filter to apply to the laborants list.

**Response:**
```json
{
  "success": "true"
  "message": "Data has been listed successfully.",
  "data": [
    {
      "firstName": "string",
      "lastName": "string",
      "hospitalId": "string"
    }
  ]
}
```

#### Update Laborant
**Endpoint:** `PUT /laborant/updateLaborant/{id}`

**Roles Required: ADMIN**

**Request Body:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "hospitalId": "string"
}
```
**Response:**
```json
{
  "success": "true"
  "message": "Laborant updated successfully.",
  "data": {
    "firstName": "string",
    "lastName": "string",
    "hospitalId": "string"
  }
}
```

#### Delete Laborant
**Endpoint:** `DELETE /laborant/deleteLaborant/{id}`

**Roles Required: ADMIN**

**Response:** 
```json
{
  "success": "true"
  "message": "Laborant is deleted successfully."
}
```

### Reports

#### Get All Reports
**Endpoint:** `GET /report/getAllReports`

**Roles Required: Any role (ADMIN, LABORANT, STANDARD_USER)**

**Request Parameters:**
- `page` (default: `0`)
- `size` (default: `3`)
- `sortBy` (default: `id`)
- `sortOrder` (default: `asc`)
- `patientName` (optional)
- `patientSurname` (optional)
- `patientIdentityNumber` (optional)
- `laborantName` (optional)
- `laborantSurname` (optional)

**Response:**
```json
{
  "success": "true"
  "message": "Data has been listed successfully.",
  "data": [
    {
      "fileNumber": "string",
      "patientName": "string",
      "patientSurname": "string",
      "patientIdentityNumber": "string",
      "diagnosisTitle": "string",
      "diagnosisDetails": "string",
      "reportDate": "LocalDate",
      "reportImage": "string",
      "laborantId": "Long"
    }
  ]
}
```

#### Generate Report
**Endpoint:** `POST /report/generateReport`

**Roles Required: LABORANT**

**Request Body:**
```json
   {
      "fileNumber": "string",
      "patientName": "string",
      "patientSurname": "string",
      "patientIdentityNumber": "string",
      "diagnosisTitle": "string",
      "diagnosisDetails": "string",
      "reportDate": "LocalDate",
      "reportImage": "string",
      "laborantId": "Long"
    }
```
**Response:**
```json
{
  "success": "true"
  "message": "Report generated successfully.",
  "data": [
    {
      "fileNumber": "string",
      "patientName": "string",
      "patientSurname": "string",
      "patientIdentityNumber": "string",
      "diagnosisTitle": "string",
      "diagnosisDetails": "string",
      "reportDate": "LocalDate",
      "reportImage": "string",
      "laborantId": "Long"
    }
  ]
}
```

#### Get One Report
**Endpoint:** `GET /report/getOneReport/{id}`

**Roles Required: Any role (ADMIN, LABORANT, STANDARD_USER)**

**Response:**
```json
{
  "success": "true"
  "message": "Data has been listed successfully.",
  "data": {
    {
      "fileNumber": "string",
      "patientName": "string",
      "patientSurname": "string",
      "patientIdentityNumber": "string",
      "diagnosisTitle": "string",
      "diagnosisDetails": "string",
      "reportDate": "LocalDate",
      "reportImage": "string",
      "laborantId": "Long"
    }
  }
}
```

#### Update Report
**Endpoint:** `PUT /report/updateReport/{id}`

**Roles Required: LABORANT**

**Request Body:**
```json
   {
      "fileNumber": "string",
      "patientName": "string",
      "patientSurname": "string",
      "patientIdentityNumber": "string",
      "diagnosisTitle": "string",
      "diagnosisDetails": "string",
      "reportDate": "LocalDate",
      "reportImage": "string",
      "laborantId": "Long"
    }
```
**Response:**
```json
{
  "success": "true"
  "message": "Report updated successfully.",
  "data": [
    {
      "fileNumber": "string",
      "patientName": "string",
      "patientSurname": "string",
      "patientIdentityNumber": "string",
      "diagnosisTitle": "string",
      "diagnosisDetails": "string",
      "reportDate": "LocalDate",
      "reportImage": "string",
      "laborantId": "Long"
    }
  ]
}
```

#### Delete Report
**Endpoint:** `DELETE /report/deleteReport/{id}`

**Roles Required: LABORANT**

**Response:**
```json
{
  "success": "true"
  "message": "Report deleted successfully."
}
```

### Users

#### Get All Users
**Endpoint:** `GET /user/getAllUsers`

**Roles Required: ADMIN**

**Request Parameters:**
- `page` (default: `0`)
- `size` (default: `3`)
- `sortBy` (default: `id`)
- `sortOrder` (default: `asc`)
- `filter` (optional)

**Response:**
```json
{
  "success": "true"
  "message": "Data has been listed successfully.",
  "data": [
    {
      "firstName": "string",
      "lastName": "string",
      "username": "string",
      "roles": [
        "string"
      ]
    }
  ]
}
```

#### Save User
**Endpoint:** `POST /user/save`

**Roles Required: ADMIN**

**Request Body:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "username": "string",
  "password": "string",
  "roleIds": [
    "List<Long>"
  ]
}
```

**Response:**
```json
{
  "success": "true"
  "message": "User is saved to the database successfully.",
  "data": {
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "roles": [
      "string"
    ]
  }
}
```

####  Update User
**Endpoint:** `PUT /user/updateUser/{id}`

**Roles Required: ADMIN**

**Request Body:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "username": "string"
}
```

**Response:**
```json
{
  "success": "true"
  "message": "User updated successfully.",
  "data": {
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "roles": [
      "string"
    ]
  }
}
```

#### Delete User
**Endpoint:** `DELETE /user/deleteUser/{id}`

**Roles Required: ADMIN**

**Response:**
```json
{
  "success": "true"
  "message": "User is deleted successfully."
}
```

## Future Roadmap

- **Implement a frontend.**
- **Enhance search and filtering capabilities for reports.**
- **Add more comprehensive integration tests.**
- **Implement additional security measures** such as rate limiting and account lockout after multiple failed login attempts.
- **Expand logging capabilities** to include detailed logging for various services and components.
- **Consider adopting a microservices architecture** to improve scalability and maintainability.
- **Enhance documentation** with more detailed Javadoc and other relevant documentation for better clarity and maintainability.

## **Contact**

This project is developed by Ahmet Alıç. Contact for the missing parts, different ideas, questions, and suggestions. 
(ahmetalicswe@gmail.com)











