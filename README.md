# ShareIt

ShareIt is a backend service for a peer-to-peer item sharing platform. It allows users to list items they own for rent, book items from other users, and leave reviews after the booking is completed.

This is a personal educational project built to practice microservice-oriented architecture, ORM (Hibernate), and strict request validation in Spring Boot.

## Features
* **Item Management:** Users can add, update, and search for available items.
* **Booking System:** Users can request to book an item for specific dates. Owners can approve or reject these requests. The system prevents date overlapping.
* **Item Requests:** If a user needs an item that is not currently listed, they can create an "Item Request". Other users can fulfill these requests.
* **Comments:** Only users who have successfully booked and used an item can leave a comment on it.

## Architecture & Tech Stack
The application is split into two loosely coupled services communicating via REST:
1. **Gateway:** Handles incoming HTTP requests and performs strict DTO validation. 
2. **Server:** Contains the core business logic, database transactions, and data mapping.

* **Language:** Java 21
* **Framework:** Spring Boot 3.3 (Web, Data JPA, Validation)
* **Database:** PostgreSQL (production), H2 (testing)
* **Tools:** MapStruct, Docker, RestClient
* **Quality Assurance:** JUnit 5, Mockito, JaCoCo, Checkstyle, SpotBugs

## How to Run

You can run the entire infrastructure (Database, Server, and Gateway) using Docker Compose.

1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_GITHUB_NAME/shareit.git
   cd shareit
   ```
2. Create an `.env` file in the root directory:
   ```env
   DB_USER=shareit_user
   DB_PASSWORD=secret_password
   ```
3. Start the application:
   ```bash
   docker-compose up --build -d
   ```

## API Documentation
The API Gateway exposes a Swagger UI for easy endpoint testing.
*(Note: Authentication is simulated via the `X-Sharer-User-Id` header for simplicity in this learning project).*

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**