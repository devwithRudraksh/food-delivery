# 🍽️ Food Delivery Backend System

A full-stack, production-ready **Food Delivery Backend** built using **Java Spring Boot**, **Kafka**, **MySQL**, **Redis**, and **Docker** — inspired by real-world systems like Zomato and Swiggy.

🔗 **Live Demo (Coming Soon)** | 🧪 **Tested and Modular** | 📦 **Microservice-Ready Architecture**

---

## 🚀 Features

- 🔐 **JWT Authentication** (Register/Login)
- 🧾 **Menu & Restaurant Management**
- 🛒 **Redis-based Cart System**
- 💳 **Kafka-Driven Payment Flow**
- 📦 **Order Status Tracking** (Payment & Delivery Status)
- 🏃‍♂️ **Scalable Design** with caching and messaging queues
- 📈 **Dockerized for Production** + MySQL Container
- 🔍 **Logging + Exception Handling** + Global Error Handler

---

## 🧠 System Architecture

![Architecture](docs/architecture-diagram.png) <!-- Optional: add this diagram later -->

> Clean separation between layers:
- **Controller** → handles API requests
- **Service** → business logic
- **Repository** → data access
- **DTOs** → clean request/response contracts

✅ Follows **SOLID**, **DTO Pattern**, and **Best Practices**

---

## 🧪 Tech Stack

| Layer            | Technology                   |
|------------------|------------------------------|
| Language         | Java 17                      |
| Framework        | Spring Boot 3                |
| Database         | MySQL (Dockerized)           |
| Cache            | Redis                        |
| Messaging Queue  | Kafka                        |
| Auth             | JWT (Stateless Auth)         |
| DevOps           | Docker + Docker Compose      |
| API Testing      | Postman                      |
| Documentation    | Swagger (Coming Soon)        |

---

## 📁 Module Breakdown

| Module            | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| `auth`           | Signup/Login with JWT, role-based auth                                      |
| `restaurant`     | Add/get restaurants and their menus                                         |
| `cart` (Redis)   | Add/Remove menu items before order, per user                                |
| `order`          | Places order, generates orderId, processes Kafka event                      |
| `payment`        | Kafka consumer deducts wallet amount and confirms payment                   |
| `delivery`       | Kafka-based delivery status update (Manual & Automated Support)             |
| `user`           | Address management, order history                                           |

---

## ⚙️ Getting Started (Locally)

```bash
# 1. Clone the repo
git clone https://github.com/yourusername/food-delivery-backend.git
cd food-delivery-backend

# 2. Start MySQL and Redis in Docker
docker-compose up -d

# 3. Run the Spring Boot App
./mvnw spring-boot:run

# App will be running on:
# http://localhost:8080
