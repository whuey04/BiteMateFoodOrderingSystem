# BiteMate - Food Ordering System (Backend)

**BiteMate** is a **Food Ordering System Backend project** developed with **Java** and **Spring Boot**. 

It provides two sets of APIs:

- **Admin APIs**: Allow restaurant staff to manage categories, dishes, set meals, orders, employees, and display business statistics.
- **User APIs**: Enable customers to browse menus, manage shopping carts, place orders, make payments, and track their orders.



---

## Table of Contents

1. [Features](#features)  
2. [Tech Stack](#tech-stack)  
3. [Highlights](#highlights)  
4. [Project Setup & Run](#project-setup--run)  

---

## Features

### 1. Admin APIs

| Module              | Description                                                  |
| ------------------- | ------------------------------------------------------------ |
| Login/Logout        | Staff must login before accessing the system management.     |
| Employee Management | Manage employee information in the backend, including search, add, edit and disable functions. |
| Category Management | Manage and maintain dish categories or set meal categories, including search, add, update, delete, enable, and disable functions. |
| Dish Management     | Manage and maintain dish information under each category, including search, add, update, delete, enable, and disable functions. |
| Set Meal Management | Manage and maintain set meal information in the restaurant, including search, add, update, delete, enable, and disable functions. |
| Order Management    | Maintain order information placed by users on the mobile app, including search, cancel, dispatch and complete functions. |
| Data Statistics     | Perform various data statistics for the restaurant, such as revenue, user count, and orders. |



### 2. User APIs

| Module        | Description                                                  |
| ------------- | ------------------------------------------------------------ |
| Login/Logout  | Users need to log in via email authorization before using the app to place orders. |
| Menu          | The ordering page displays dish categories/set meal categories, and loads the corresponding dishes based on the selected category for users to browse and choose. |
| Shopping Cart | Selected dishes are added to the user’s shopping cart, which includes functions such as viewing the cart, adding items, removing items, and clearing the cart. |
| Order Payment | After selecting dishes/set meals, users can proceed to checkout and make payment for the items in the cart. |
| Personal Info | The personal center page displays the user’s basic information, allows management of delivery addresses, and provides access to historical order data. |



---



## Tech Stack

- **Language**: Java 17

- **Framework**: Spring Boot 2.7.3

- **Data Access**: MyBatis

- **Database**: MySQL

- **Cache**: Redis (caches frequently accessed dish data)

- **Object Storage**: MinIO 7.1.0 (stores dish images)

- **Security**: JWT Authentication

- **API Docs**: Swagger

- **Build Tools**: Maven, Lombok

  

---



## Highlights

- Follows **layered architecture (Controller-Service-DAO)** for clean structure.
- **JWT + interceptors** for authentication and role-based authorization.
- **Redis caching** for frequently accessed dish data to reduce database load.
- **Redis caching** reduces database load on frequently queried data.
- **MinIO integration** for image upload and access management.
- **Swagger UI** for interactive API documentation and testing.



---



## Project Setup & Run

### 1. Prerequisites

Before running the project, make sure you have:

- **Java 17**
- **Maven 3.6+**
- **MySQL 8.x**
- **Redis** (self-installed or Docker)
- **MinIO** (self-installed or Docker)


### 2. Clone the Repository

```
git clone https://github.com/whuey04/BiteMateFoodOrderingSystem.git
cd BiteMateFoodOrderingSystem
```


### 3. Configure Database

1. Import the provided SQL file (`bitemate.sql`) from the `sql/` folder

2. Update `src/main/resources/application-dev.yml`:

```
# Example
bm:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: localhost
    port: 3306
    database: bitemate
    username: root
    password: your_password
```


### 4. Configure Redis

Update `application-dev.yml` with your Redis info:

```
# Example
bm:
  redis:
    host: localhost
    port: 6379
    database: 10
```

> You can use Docker or a local installation for Redis.


### 5. Configure MinIO

1. Set up MinIO and create a bucket (e.g., `bitemate`).
2. Update `application-dev.yml`:

```
# Example
bm:
  minio:
    accessKey: minioadmin
    secretKey: minioadmin
    bucket: bitemate
    endpoint: http://localhost:9000
    readPath: http://localhost:9000/bitemate
```

> You can use Docker or a local installation for MinIO.


### 6. Configure Email (For User APIs)

```
# Example
bm:
  mail:
    host: smtp.yourmail.com
    username: your_email@example.com
    password: your_password
    port: 587
```


### 7. Build & Run the Application

```
mvn clean install
mvn spring-boot:run
```

The application will start at: http://localhost:8080


### 8. Default Accounts

- **Admin**:
  - **Username**: `admin`
  - **Password**: `123456`
  - Preloaded in `sql/bitemate.sql`, can be used immediately to access Admin APIs.

- **User**:
  - No default accounts.
  - The first time user login via email, a new account will be automatically created in the database.


### 9. API Testing with Swagger

All APIs can be tested interactively using **Swagger UI**:

- Open your browser and go to: http://localhost:8080/doc.html
- You can view all available endpoints, their request/response structures, and try them out directly.
- Both **Admin** and **User** APIs are documented and ready for testing.

