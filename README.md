# TaskManager

TaskManager — это RESTful приложение для управления задачами, реализованное с использованием Spring Boot, PostgreSQL и
Docker Compose.

## Установка и запуск

1. Убедитесь, что у вас установлен [Docker](https://www.docker.com/)
   и [Docker Compose](https://docs.docker.com/compose/).
2. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/TolubeevM/TaskManager.git
   cd TaskManager
3. Запустите проект с помощью Docker Compose:

   ```bash
      docker-compose up --build
Приложение будет доступно по адресу:  

API: http://localhost:8080

## Сервисы
База данных: PostgreSQL (порт 5433)  
Сервер приложения: Spring Boot (порт 8080)

## Документация API
После запуска проекта документация API доступна по адресу:  
http://localhost:8080/swagger-ui/index.html
## Стек технологий
Backend: Spring Boot (REST, Spring Data JPA)  
База данных: PostgreSQL  
Контейнеризация: Docker, Docker Compose