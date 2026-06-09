# 🎬 Movieflix - Case Study & CI/CD Pipeline

Welcome to the **Movieflix** backend application, a robust Spring Boot project representing the continuation of advanced use-cases modeling, security validation, exception handling, and automated DevOps integration.

---

## 🚀 Technology Stack & Badges

<div align="left">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.3-brightgreen?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Docker-Multi--Platform-cyan?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/SonarCloud-Quality%20Gate-success?style=for-the-badge&logo=sonarcloud&logoColor=white" alt="SonarCloud" />
</div>

---

## 📌 Implementation Achievements

* **Security Config**: Fully customized Spring Security OAuth2.0 authentication workflow. Resolved role-based access validation anomalies using annotations.
* **UML Domain Mapping**: Implemented Movie-Genre relations, returning full objects mapping DTOs, and validating endpoints.
* **Autonomous CD (Oracle Cloud VM)**: Full three-stage pipeline (Test & Scan ➡️ Docker Buildx ➡️ Deploy) to remote OCI VM with dynamic SHA tagging.
* **JVM Performance**: Replaced old dependencies cache targets to run seamlessly on macOS and Linux ARM64/AMD64 architectures.

---

## 🗺️ Domain Model (UML)

```
       +------------+                  +------------+
       |   Genre    | 1            0..*|   Movie    |
       +------------+------------------+------------+
       | - id       |                  | - id       |
       | - name     |                  | - title    |
       +------------+                  | - subTitle |
                                       | - year     |
                                       | - imgUrl   |
                                       | - synopsis |
                                       +------------+
```

---

## 🛠️ Local Development & Quick Start

Follow these steps to set up the application on your local machine.

### 1. Runtime Environment (SDKMAN!)
This project configures **Java 17 (17.0.10-tem)** using a local `.sdkmanrc` file. 

* Ensure you have [SDKMAN!](https://sdkman.io/) installed.
* Run to switch to the correct JRE version:
  ```bash
  sdk env
  ```
* *Tip*: Enable automatic switching by changing `sdkman_auto_env=true` in `~/.sdkman/etc/config`.

### 2. Private Environment Configuration (`.env.dev`)
The project reads private configs from `.env.dev`, which is gitignored to protect secrets. 

1. Copy the template example file:
   ```bash
   cp .env.example .env.dev
   ```
2. Open `.env.dev` and fill in your database, client secrets, and JWT secret tokens.

---

## 🐳 Running with Docker

### Development Stack (Local Build)
Builds the code locally and spins up PostgreSQL alongside the application:
```bash
docker compose --env-file .env.dev up --build -d
```

### Homolog VM Stack (Pre-Built Images)
To run the production/homolog setup without compiling source code locally (resolves remote image tags directly from Docker Hub):
```bash
docker compose -f docker-compose-homolog.yml --env-file .env.dev up -d
```

---

## 🎯 Testing & Verification

| Command | Purpose |
| :--- | :--- |
| `APP_PROFILE=test ./mvnw clean test` | Runs Unit & Integration Test Suites |
| `APP_PROFILE=test ./mvnw clean verify` | Runs all tests and generates **JaCoCo** code coverage |
| `docker image prune -a -f` | Cleans all unused docker containers and images (frees disk space) |

*The JaCoCo coverage report will be available locally at:* `target/site/jacoco/index.html`

---

## 📖 API Documentation & H2 Console

When running locally under the `test` profile:
* **Swagger Interactive UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **OpenAPI Specs (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
* **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:testdb`, User: `sa`, No Password).