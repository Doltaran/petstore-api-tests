# Petstore API Autotests

Проект автотестов для публичного сервиса Swagger Petstore  
https://petstore.swagger.io

---

## Tech Stack
- **Java 17**
- **Gradle** - система сборки
- **JUnit 5** - фреймворк для тестирования
- **REST Assured** - библиотека для тестирования REST API
- **Hamcrest** - библиотека для assertions
- **Gatling** - инструмент для нагрузочного тестирования (в папке `tools/gatling`)

---

## How to Run Tests

### Windows (PowerShell)
>.\gradlew.bat clean test


### macOS / Linux
```bash
./gradlew clean test
```


После выполнения команды результат прогона тестов выводится в консоль.  
HTML-отчёт доступен по пути:
```
build/reports/tests/test/index.html
```

### Запуск нагрузочных тестов (Gatling)

Нагрузочные тесты находятся в папке `tools/gatling` и используют Maven.

**Windows:**
```bash
cd tools/gatling
.\mvnw.cmd gatling:test
```

**macOS / Linux:**
```bash
cd tools/gatling
./mvnw gatling:test
```

После выполнения отчёт будет доступен в `tools/gatling/target/gatling/`

---

## Project Structure

```
src/test/java/ru/ragim/petstore/
├── client/                    # API клиенты для работы с эндпоинтами
│   ├── BaseApiClient.java     # Базовый класс для всех клиентов
│   ├── PetClient.java         # Клиент для работы с /pet
│   ├── StoreClient.java       # Клиент для работы с /store
│   └── UserClient.java        # Клиент для работы с /user
│
├── config/                    # Конфигурация тестов
│   └── AbstractIntegrationTest.java  # Базовый класс для всех тестов
│                                    # Автоматически создаёт пользователя,
│                                    # выполняет логин перед каждым тестом
│                                    # и разлогин после теста
│
├── data/                      # Фабрика тестовых данных
│   └── TestDataFactory.java   # Генерация уникальных тестовых данных
│
├── steps/                     # Шаги для работы с API (Page Object паттерн)
│   ├── PetSteps.java          # Шаги для работы с питомцами
│   ├── StoreSteps.java        # Шаги для работы с заказами
│   └── UserSteps.java         # Шаги для работы с пользователями
│
└── tests/                     # Тестовые классы
    ├── pet/
    │   └── PetTests.java      # Все тесты для питомцев (25 тестов)
    ├── user/
    │   └── UserTests.java      # Все тесты для пользователей (11 тестов)
    ├── store/
    │   └── StoreTests.java    # Все тесты для заказов (11 тестов)
    └── integration/
        └── PetStoreIntegrationTests.java  # Интеграционные тесты
```

### Нагрузочные тесты (Gatling)

```
tools/gatling/src/test/java/ru.ragim.petstore/
├── PetLoadTest.java      # Нагрузочный тест для /pet/findByStatus (1 RPS)
├── UserLoadTest.java     # Нагрузочный тест для /user/login (1 RPS)
└── StoreLoadTest.java    # Нагрузочный тест для /store/inventory (1 RPS)
```

---

## Test Statistics

- **PetTests**: 25 тестов
  - CRUD операции
  - Поиск по статусам
  - Негативные тесты
  - Тесты на контроль доступа
  - Тесты с различными статусами (available, pending, sold)

- **UserTests**: 11 тестов
  - CRUD операции
  - Авторизация (login/logout)
  - Тесты на контроль доступа

- **StoreTests**: 11 тестов
  - Создание и управление заказами
  - Работа с инвентарём
  - Интеграционные тесты (Pet + Order)
  - Тесты на контроль доступа

- **PetStoreIntegrationTests**: Интеграционные сценарии

**Всего: ~48 тестов**

---



## Test Design

### Архитектура тестов

1. **AbstractIntegrationTest** - базовый класс для всех тестов
  - Автоматически создаёт уникального пользователя перед каждым тестом
  - Выполняет логин перед тестом (`@BeforeEach`)
  - Выполняет разлогин после теста (`@AfterEach`)
  - Это обеспечивает изоляцию тестов и правильную работу с авторизацией

2. **Page Object Pattern** - используется в `steps/`
  - Инкапсуляция логики работы с API
  - Переиспользование кода
  - Упрощение поддержки тестов

3. **Test Data Factory** - генерация уникальных тестовых данных
  - Каждый тест использует уникальные ID и username
  - Тесты не влияют друг на друга
  - Использование временных меток для генерации уникальных значений



### Нагрузочное тестирование

Проект включает 3 нагрузочных теста на Gatling:
- Каждый тест проверяет один метод API
- Нагрузка: 1 RPS (1 запрос в секунду) в течение 60 секунд
- Отдельные тесты для Pet, User и Store API

---





## Test Reports

После запуска тестов доступны следующие отчёты:

1. **Gradle Test Report**: `build/reports/tests/test/index.html`
  - Детальная информация о каждом тесте
  - Статистика прохождения/падения
  - Логи выполнения

2. **Gatling Reports**: `tools/gatling/target/gatling/[simulation-name]/index.html`
  - Графики производительности
  - Статистика ответов
  - Детальная информация о каждом запросе

---



