
---
## 🎯 Goal
When a customer requests a reservation (date, time, party size), Order Service asks Table Service if a table is available.
If available, a pending reservation is created and must be confirmed within a time limit; otherwise it’s auto-canceled.

<imag src="img/main-services">

## 🧩 Services & Responsibilities

### 1) 🧾 Order Service (REST, **Layered**)
**Architecture:** Controller → Service → Repository (JPA)  
**Purpose:** Accept reservation requests, manage lifecycle (pending → confirmed/canceled), call Table Service for availability.

**Core endpoints (JSON, REST):**
- `POST /api/reservations` — create pending reservation  
- `POST /api/reservations/{id}/confirm`  
- `GET /api/reservations/{id}`  


#### Edge Cases:
---
# 🚦 API Validation & Edge Cases

This document outlines the business rules and validation constraints for the Restaurant Reservation API.

---

### 1. Table Capacity Constraint
*   **Rule:** Maximum allowed party size per table is **6**.
*   **Validation:** `partySize <= 6`

**Example Request:**
```json
{
  "date": "2026-05-09", 
  "time": "20:30", 
  "partySize": 7,
  "customerId": "c-125"
}
```
**Expected Response:**
> `400 Bad Request`: "Maximum allowed party size per table is 6."

---

### 2. Lead Time Constraint
*   **Rule:** Reservations must be made at least **30 minutes** in the future from the current system time.
*   **Validation:** `requestedTime >= currentTime + 30m`

**Example Request:**
```json
{
  "date": "2026-05-09",
  "time": "03:30", 
  "partySize": 4,
  "customerId": "c-125"
}
```
**Expected Response:**
> `400 Bad Request`: "Reservations must be at least 30 minutes in the future."

---

### 3. Operational Hours Constraint
*   **Rule:** Bookings are only accepted between **11:00** and **22:00**.
*   **Validation:** `time >= 11:00` AND `time <= 22:00`

**Example Request:**
```json
{
  "date": "2026-05-16", 
  "time": "10:30", 
  "partySize": 4, 
  "customerId": "c-125"
}
```
**Expected Response:**
> `400 Bad Request`: "Booking Rejected: Outside operational hours. [Requested: 10:30], [Allowed: 11:00 - 22:00]"

---

### 4. Data Format & Type Validation
*   **Rule:** Time must follow the strict ISO 24-hour format (`HH:mm`).
*   **Common Error:** Missing leading zeros (e.g., `8:30` instead of `08:30`).

**Example Request:**
```json
{
  "date": "2026-05-09",
  "time": "8:30", 
  "partySize": 4,
  "customerId": "c-125"
}
```
**Expected Response:**
> `400 Bad Request`: "JSON parse error: Cannot deserialize value of type `java.time.LocalTime` from String '8:30': Text '8:30' could not be parsed at index 0"

---
###
```
### Another edge cases
---

  6:00 ───────── 7:30   Cleanup   7:45

  [ Customer A ]         [15m]    [Next Guest]



  Existing:  6:00 -------- 7:30

          New:   7:00 -------- 8:00

                 OVERLAP
```

---

### 🛠️ Developer Implementation Note
All validations are handled via a global `@RestControllerAdvice` to ensure consistent error messaging across the microservice. For scheduling and automated cleanup of expired `PENDING` reservations, refer to the **ShedLock** configuration.




### Usage Spring Library:
# 🛠️ Project Dependencies & Infrastructure

This project uses a modern microservices stack built on **Spring Boot**. Below is a breakdown of the key libraries and their roles in the system.

## 🚀 Core Dependencies (Production)


| Dependency | Purpose |
| :--- | :--- |
| **ShedLock Spring** | Ensures `@Scheduled` tasks run **at most once** across multiple server instances by using a distributed lock. |
| **ShedLock JDBC Provider** | Manages the coordination of locks within the PostgreSQL `shedlock` table. |
| **Spring Data JPA** | Simplifies data persistence and management for `RestaurantTableEntity` using the Repository pattern. |
| **PostgreSQL Driver** | The database driver that allows the application to connect to the PostgreSQL instance. |
| **Spring Web** | The foundation for building REST APIs, handling JSON serialization, and HTTP routing. |
| **Spring HATEOAS** | Enhances API responses with hypermedia links, making the API self-discoverable. |
| **Spring Boot Docker Compose** | Automatically manages local infrastructure by starting your `docker-compose.yml` services when the app runs. |

---

## 🧪 Testing & Quality Suite


| Dependency | Purpose |
| :--- | :--- |
| **Testcontainers** | Launches a **real** PostgreSQL instance in Docker during integration tests for total environment parity. |
| **WireMock** | Simulates external API responses to test how the Order Service handles external failures. |
| **RestAssured** | Provides a fluent, readable DSL for testing REST endpoints with `given/when/then` syntax. |
| **ArchUnit** | Enforces architectural constraints to ensure clean code boundaries and package structure. |
| **Spring Boot Starter Test** | The standard testing toolkit providing JUnit 5, Mockito, and AssertJ. |

---

## 🏗️ Infrastructure Requirements

*   **Database:** PostgreSQL (with a `shedlock` table for task coordination).
*   **Containerization:** Docker & Docker Compose for local development.
*   **Time Management:** All timestamps are synchronized to **UTC** to avoid timezone mismatches between the App and DB.



























spring.cloud.stream.bindings.<bindingName>.<property>=<value>. The <bindingName>

spring.cloud.stream.bindings.<binding name>.destination=myExchange

spring.cloud.stream.bindings.<binding name>.group=myQueue


spring.cloud.stream.bindings.processCreationRequest-in-0.destination=table--exchange

Spring Cloud Stream uses functional programming. It automatically looks for a Java bean in your code with this exact name

```
@Bean
public Consumer<String> processCreationRequest() {
    return message -> {
        System.out.println("Received message: " + message);
    };
}


 Enables Load Balancing (Competing Consumers)
If you scale up your application and run multiple instances of your microservice:
They will all share this exact same queue.RabbitMQ will round-robin the incoming messages between the instances.Result:
 Each message is processed exactly once by only one instance, preventing duplicate work.


```

The -in- segment tells Spring that this is an inbound binding.
This means your application is acting as a consumer (or listener).
It will actively listen for incoming data rather than sending data out (-out-).Step 3: Map the Index (-0)
The -0 represents the index of the input parameter.Standard Java functions only have one input, so it defaults to 0.


The .destination property tells the binder the exact name of the source data pool in your message broker.
When your Spring Boot application starts up, it communicates with your message broker and says: "Please hook up my processCreationRequest function so it receives everything sent to the destination named table--exchange.

The config property table--exchange strongly indicates that you are using RabbitMQ as your message broker.

RabbitMQ uses a core component called an "Exchange" to receive messages from producers and route them to specific queues.

