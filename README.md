# Petstore API Autotests

Пример проекта автотестов для публичного сервиса Swagger Petstore  
https://petstore.swagger.io



---

## Tech Stack
- Java 17
- Gradle
- JUnit 5
- REST Assured
- Hamcrest

---

## How to Run Tests

### Windows (PowerShell)
```powershell
.\gradlew.bat clean test

### macOS / Linux
./gradlew clean test


После выполнения команды результат прогона тестов выводится в консоль.  
HTML-отчёт доступен по пути:
build/reports/tests/test/index.html

---
```md
## Project Structure
```
src/test/java
└── ru/ragim/petstore
├── config
│ └── TestConfig.java # Базовая конфигурация (base URI)
└── tests
├── PetCrudTests.java # CRUD тесты для /pet
├── PetSearchTests.java # GET с query-параметрами
└── StoreOrderTests.java # Тесты для /store/order
```

---

## Covered API Endpoints

### Pet
- POST /pet
- GET /pet/{id}
- PUT /pet
- DELETE /pet/{id}
- GET /pet/findByStatus

### Store
- POST /store/order
- GET /store/order/{id}
- DELETE /store/order/{id}

---

## Test Design Notes
- Для каждого теста используются уникальные идентификаторы, чтобы тесты не
  влияли друг на друга.
- Проверки сделаны устойчивыми к нестабильным ответам публичного Petstore.
- Используется логирование ответов при падении тестов
  (`log().ifValidationFails()`).
- Структура проекта позволяет легко добавлять новые тесты и API-эндпоинты.

---


