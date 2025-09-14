# Gainlog Fitness Application️

## Overview
A **microservices-based application** built with **Spring Boot, API Gateway, JWT Authentication, Kafka, gRPC, Docker, and CI/CD**.  
The goal of the project is to demonstrate **backend development skills** while following modern **cloud-native and distributed system principles**.

This project simulates a real-world fitness tracking platform where users can:
- **Register & authenticate** via JWT
- **Create and track workouts**
- **Receive notifications** (via event-driven Kafka communication)

---

## Features
- ✅ **Microservices architecture** with Spring Boot
- ✅ **API Gateway** with centralized routing
- ✅ **JWT Authentication & Role-based Authorization**
- ✅ **gRPC** for inter-service communication
- ✅ **Kafka** for async notifications
- ✅ **Docker** for containerized deployment
- ✅ **PostgreSQL + Flyway** migrations
- ✅ **Global Exception Handling** with standardized responses
- ✅ **CI/CD pipeline** via GitHub Actions
-  **Planned**: Workout performance tracking, statistics & reporting, monitoring (Prometheus/Grafana)

---

## Architecture
- **API Gateway** – entry point for all clients, handles routing & authentication
- **Auth Service** – handles user login and registration, communicates with User Service via gRPC
- **User Service** – user profile & management
- **Workout Service** – workout creation and logging
- **Notification Service** – consumes Kafka events (e.g., user registration → send notification)
- **Analytics Service** – workout historical performance tracking (planned)
- **Proto Common** – proto files for gRPC and Kafka communication
- **Core** – module for commonly used files like exceptions or security

---

## Tech Stack
**Backend:** Java 21, Spring Boot (Web, Security, Data JPA, Cloud Gateway, gRPC, Kafka)  
**Database:** PostgreSQL + Flyway  
**Messaging:** Apache Kafka  
**Infrastructure:** Docker, Docker Compose  
**CI/CD:** GitHub Actions  
**Auth:** JWT (Access token, roles)

---
