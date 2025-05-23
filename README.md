# Product Service

[![CI Workflow](https://github.com/PhumlaniDev/infinity-tech-product-service/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/PhumlaniDev/infinity-tech-product-service/actions/workflows/ci-cd.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=bugs)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=PhumlaniDev_infinity-tech-product-service&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=PhumlaniDev_infinity-tech-product-service)

## Overview

The `auth-service` is a Spring Boot-based microservice responsible for user authentication and authorization. It supports OAuth2, JWT, and integrates with Spring Security for secure access control.

## Features

- User authentication and authorization.
- OAuth2 and JWT token-based security.
- Integration with SonarCloud for code quality analysis.
- Dockerized for easy deployment.
- CI/CD pipeline with GitHub Actions.

## Prerequisites

- Java 21
- Maven
- Docker
- GitHub account with necessary secrets configured for CI/CD.

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/PhumlaniDev/infinity-tech-auth-service.git
cd infinity-tech-auth-service
```

### Build the Project

```bash
mvn clean install -DskipTests
```

### Run the Application

```bash
mvn spring-boot:run
```

### Run with Docker
```bash
docker build -t auth-service .
docker run -p 8080:8080 auth-service
```

## CI/CD Pipeline
The project uses GitHub Actions for CI/CD. The pipeline includes:


- Checkstyle: Ensures code adheres to style guidelines.
- Build: Compiles and packages the application.
- SAST: Static Application Security Testing.
- SCA: Software Composition Analysis.
- Notifications: Sends status updates to Discord.

### Contributing
Contributions are welcome! Please fork the repository and submit a pull request.



### License
This project is licensed under the MIT License. See the LICENSE file for details.