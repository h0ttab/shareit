# ShareIt - Item Sharing Platform API 📦

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-green.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)

**ShareIt** is a REST API service that allows users to share things they own with others. Need a drill for one day? Don't buy it, just rent it from your neighbors! 

This project is built using a **microservice architecture pattern (API Gateway + Backend Server)** to ensure request validation is isolated from the core business logic.

## 🛠 Tech Stack & Tools
* **Core:** Java 21, Spring Boot 3.3 (Web, Data JPA, Validation)
* **Database:** PostgreSQL (production), H2 (testing)
* **Communication:** Spring RestClient
* **Mapping:** MapStruct, Lombok
* **Infrastructure & CI:** Docker, Docker Compose, GitHub Actions
* **Quality Assurance:** JUnit 5, Mockito, JaCoCo, Checkstyle, SpotBugs
* **API Documentation:** Swagger / OpenAPI 3

## 🏗 Architecture
The application is divided into two separate modules communicating via REST:
1. **Gateway:** Handles incoming HTTP requests, performs strict input validation, and routes valid requests to the server.
2. **Server:** Contains business logic, database transactions, and data mappings. 

*Note: For demonstration purposes and to simplify testing scenarios without a frontend, authentication is temporarily simulated via the `X-Sharer-User-Id` HTTP header rather than full JWT/Spring Security implementation.*

## 🚀 How to Run (Docker Compose)

1. Clone the repository:
   ```bash
   git clone https://github.com/h0ttab/shareit.git
   cd shareit
   ```
2. Create an `.env` file in the root directory and set your database credentials:
   ```env
   DB_USER=shareit_user
   DB_PASSWORD=secret_password
   ```
3. Run the application using Docker Compose:
   ```bash
   docker-compose up --build -d
   ```
4. The API Gateway will be available at `http://localhost:8080`.

## 📖 API Documentation (Swagger)
Once the application is running, you can explore and test all endpoints via the Swagger UI:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

## 📊 Key Features Implemented
* **CRUD operations** for Users and Items.
* **Booking System:** Requesting, approving, and rejecting item bookings with strict date-overlap validation.
* **Search Engine:** Text-based search for available items.
* **Item Requests:** Users can post requests for items they need, and others can fulfill them.
* **Performance optimization:** Solved N+1 query problems in database fetching using `IN` clauses, Maps, and JPQL fetch joins.