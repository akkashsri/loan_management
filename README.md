# Loan Management System

This is a Spring Boot project that provides a complete system for managing loan applications. It allows users to register and apply for new loans. An admin can then log in to a dashboard to review all applications, view their history, and update their status.


## Key Features

  * **User Registration and Login**: Secure endpoints for users to create an account and log in.
  * **Loan Application Submission**: A simple form for applicants to submit their loan requests.
  * **Admin Dashboard**: A central dashboard for administrators to view and manage all loan applications.
  * **Loan Status Management**: Admins can view loan details, see the complete approval history, and update the loan status through different stages.
  * **Secure API**: The application is secured using Spring Security to protect all endpoints.


## Tech Stack Used

  * **Backend**: Java 17, Spring Boot 3
  * **Database**: MySQL with Spring Data JPA
  * **Frontend**: Basic HTML, CSS, and JavaScript
  * **Build Tool**: Maven


## Setup Instructions

Here are the steps to get the project running on your local machine.

### Prerequisites

Make sure you have these installed:

  * JDK 17 or later
  * Apache Maven
  * MySQL Server

### 1\. Clone the Repository

First, clone the project to your local machine.

```bash
git clone <your-repository-url>
cd loan_management
```

### 2\. Database Configuration

You need to create a database and update the connection details.

  * **Create the database in MySQL**:

    ```sql
    CREATE DATABASE loan_management_db;
    ```

  * **Update properties file**:
    Open the `src/main/resources/application.properties` file. Change the database username and password to match your MySQL setup.

    ```properties
    # src/main/resources/application.properties

    spring.datasource.url=jdbc:mysql://localhost:3306/loan_management_db
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    ```

    **Please note**: The property `spring.jpa.hibernate.ddl-auto` is set to `create`. This will automatically create the database tables when you first run the application. For a real project, it is better to change this to `update` or `validate`.

### 3\. Run the Application

You can now run the application using the Maven wrapper.

  * **On Windows**:

    ```cmd
    .\mvnw spring-boot:run
    ```

  * **On macOS/Linux**:

    ```bash
    ./mvnw spring-boot:run
    ```

The server will start on port **8087**.

### 4\. Access the Application

Once it's running, you can open the application in your browser.

  * **Register Page**: `http://localhost:8087/register.html`
  * **Login Page**: `http://localhost:8087/login.html`


## Running the Tests

To run the unit tests for the project, use the following command from the root directory:

```bash
./mvnw test
```
