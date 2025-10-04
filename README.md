# 🌦️ Weather Viewer

Веб-приложение для просмотра текущей погоды. Пользователь может зарегистрироваться и добавить в коллекцию одну или несколько локаций (городов, сёл, других пунктов), после чего главная страница приложения начинает отображать список локаций с их текущей погодой.

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring MVC 6.x**
- **Thymeleaf 3.1.x**
- **Hibernate ORM (JPA) 7.x**
- **PostgreSQL 15.x**
- **Flyway 11.11.x**
- **Maven 3.9.9**
- **Tomcat 10**
- **OpenWeather API**

---

## 🖥️ Принцип работы

### 🔑 Аутентификация
- **Sign up** → Регистрация нового пользователя
- **Sign in** → Вход под пользователем с созданием сессии + Cookie с временем жизни
- **Logout** → Удаление сессии и Cookie

### 🏠 HomePage (`/`)
- Homepage для пользователей показывает карточки сохраненных локаций
- Поиск локаций `/search`
- Удаление локаций по нажатию на ❌

### 🔎 Search-Results (`/search-results`)
- Список найденных локаций
- Возможность добавить локацию в сохраненные `add`

### 🌦️ Weather Data
- Интеграция с OpenWeather API
- Данные в цельсиях **°C**

---

## 🧰 Как запустить

### 1️⃣ **Подготовка**
- **Java 17** installed
- **PostgreSQL**
- **Tomcat 10**
- **Clone Repository** — https://github.com/LlqWst/weather-viewer.git
- **OpenWeather API Key** ([OpenAPI](https://openweathermap.org/api))

### 2️⃣ **Конфигурация БД и API**
Создайте схему в PostgreSQL:
```sql
CREATE SCHEMA weather_viewer;
```
- Добавить переменные окружения.

**Linux / macOS (bash/zsh):**
```bash
export DB_USER='YOUR_DB_USERNAME'
export DB_PASS='YOUR_DB_PASSWORD'
export APP_ID='YOUR_OPENWEATHER_API_KEY'
```

**Windows (Command Prompt):**
```shell
set DB_USER=YOUR_DB_USERNAME
set DB_PASS=YOUR_DB_PASSWORD
set APP_ID=YOUR_OPENWEATHER_API_KEY
```

### 3️⃣ **Build Проекта**
```bash
mvn clean package
```

### 4️⃣ **Деплой через Tomcat**
- Cкопируйте WAR из `target/weather-tracker.war` в папку Tomcat `webapps`
- запустите Tomcat:

Для Linux/MAC:
```bash
$CATALINA_HOME/bin/startup.sh
```
Для Windows:
```shell
startup.bat
```
- App available at [http://localhost:8080/weather-viewer/](http://localhost:8080/weather-viewer/)

---

## 📚 Дополнительная информация
Проект был завершен в рамках [Java Backend Learning Roadmap](https://zhukovsd.github.io/java-backend-learning-course/projects/weather-viewer/)