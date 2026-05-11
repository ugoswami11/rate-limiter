# Distributed Rate Limiter Service

A production-style distributed rate limiter built using Java, Spring Boot, Redis, Lua scripting, Docker, and Docker Compose.

This project demonstrates backend engineering concepts beyond standard CRUD applications, including distributed systems design, atomic Redis operations, burst traffic handling, and containerized deployment.

---

# Project Overview

## Problem Statement

In modern backend systems, APIs must protect themselves from:

* abuse
* traffic spikes
* bot attacks
* accidental overload
* unfair resource consumption

A Rate Limiter ensures that users or clients can only make a fixed number of requests within a defined time window.

This project implements a **Token Bucket Algorithm** using Redis and Lua scripting for atomic distributed rate limiting.

---

# Tech Stack

## Backend

* Java 21+ (compatible with higher JDK versions)
* Spring Boot
* Maven
* Redis
* Lua Script

## Testing

* JUnit 5
* Mockito
* MockMvc
* Integration Testing

## DevOps

* Docker
* Docker Compose

---

# Architecture Diagram

```text
                +----------------------+
                |      Client/App      |
                +----------+-----------+
                           |
                           |
                           v
                +----------------------+
                | Spring Boot REST API |
                |  RateLimiterController |
                +----------+-----------+
                           |
                           v
                +----------------------+
                | RateLimiterService   |
                +----------+-----------+
                           |
                           v
                +----------------------+
                | Strategy Pattern     |
                | RedisRateLimiterStrategy |
                +----------+-----------+
                           |
                           v
                +----------------------+
                | Redis + Lua Script   |
                | Atomic Token Bucket  |
                +----------------------+
```

---

# Request Flow

```text
Request → Controller → Service → Redis Strategy → Lua Script → Redis → Response
```

## Flow Explanation

1. Client sends API request
2. Controller receives request
3. Service delegates to strategy layer
4. Redis strategy executes Lua script
5. Lua performs atomic token bucket operations
6. Redis stores updated token state
7. Response returned with:

   * allowed = true/false
   * remainingTokens

---

# Token Bucket Algorithm

## Configuration

```text
Capacity = 5 tokens
Refill Rate = 1 token/second
```

## Example

```text
Initial bucket = 5

Request 1 → allowed → 4 left
Request 2 → allowed → 3 left
Request 3 → allowed → 2 left

Wait 3 seconds

Bucket refills by 3 tokens
```

This supports both:

* burst traffic
* controlled sustained traffic

---

# Why Redis?

Redis is used because:

* extremely fast (in-memory)
* supports distributed systems
* shared state across multiple application instances
* supports Lua scripting for atomic execution
* production-grade for rate limiting use cases

Using MySQL for this would be slower and less suitable.

---

# Why Lua Script?

Without Lua:

```text
GET → calculate → SET
```

This causes race conditions under concurrency.

With Lua:

```text
All operations execute atomically inside Redis
```

This ensures correctness and consistency.

---

# Project Structure

```text
src/main/java
 ├── controller
 ├── dto
 ├── exception
 ├── model
 ├── service
 ├── strategy
 ├── config
 └── util

src/main/resources
 └── scripts
      └── rate-limiter.lua
```

---

# Setup Instructions

## Step 1 — Clone Repository

```bash
git clone <repository-url>
cd rate-limiter-backend
```

---

## Step 2 — Build Application

```bash
mvn clean package
```

This generates:

```text
target/*.jar
```

---

## Step 3 — Start Using Docker Compose

```bash
docker compose up --build
```

This starts:

* Spring Boot application
* Redis server

---

## Step 4 — Verify Application

Check logs:

```bash
docker compose logs -f app
```

Application should run on:

```text
http://localhost:8080
```

---

# API Examples

## Check Rate Limit

### Endpoint

```http
POST /api/rate-limit/check
```

### Request Body

```json
{
  "userId": "test_user"
}
```

### Success Response

```json
{
  "allowed": true,
  "remainingTokens": 4
}
```

### Blocked Response

```json
{
  "allowed": false,
  "remainingTokens": 0
}
```

---

# Common Docker Commands

## Build Image

```bash
docker build -t rate-limiter-app .
```

## Start Services

```bash
docker compose up
```

## Start with Rebuild

```bash
docker compose up --build
```

## Stop Services

```bash
docker compose down
```

## View Logs

```bash
docker compose logs -f
```

## View Running Containers

```bash
docker ps
```

## Enter Container

```bash
docker exec -it <container_id> sh
```

---

# Test Strategy

This project uses multiple layers of testing.

---

## 1. Unit Testing

Tools:

* JUnit 5
* Mockito

Coverage:

* service logic
* strategy validation
* edge cases
* exception scenarios

Example:

```text
First 5 requests allowed
Next requests blocked
```

---

## 2. Controller Testing

Tools:

* MockMvc

Coverage:

* request validation
* response structure
* HTTP status codes
* exception handling

Example:

```text
Blank userId → 400 BAD REQUEST
```

---

## 3. Integration Testing

Coverage:

* Spring Boot + Redis integration
* end-to-end API verification

Example:

```text
Burst requests + refill validation
```

---

## 4. Manual Testing

Tools:

* Postman

Coverage:

* burst traffic testing
* refill timing validation
* Redis behavior validation

---

# Real-World Production Considerations

This project can scale further with:

* API key authentication
* plan-based rate limits
* admin dashboard APIs
* usage analytics
* Prometheus + Grafana monitoring
* CI/CD pipelines
* Kubernetes deployment
* multi-region Redis setup
* fallback strategies during Redis failure

---

# Future Improvements

## Planned Enhancements

### API Key Based Rate Limiting

Replace manual userId with secure API key validation.

---

### Tier-Based Plans

```text
Free → 10 req/min
Pro → 100 req/min
Enterprise → custom
```

---

### Admin Dashboard APIs

Examples:

```text
GET /admin/usage/{userId}
GET /admin/blocked-users
GET /admin/stats
```

---

### Monitoring

Using:

* Spring Actuator
* Prometheus
* Grafana

---

### CI/CD Pipeline

Using:

* GitHub Actions
* Docker Build Pipeline
* Automated Test Execution

---

### Cloud Deployment

Possible platforms:

* AWS
* Render
* Railway
* DigitalOcean

---

# Author

Built as a production-style backend engineering project focused on real-world distributed system design.
