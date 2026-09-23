
# Food Delivery System

A simple Food Delivery Management System developed using **Java Spring Boot and MySQL**.

## Technologies Used

* Java
* Spring Boot
* Spring Data JPA
* Spring Security
* MySQL
* REST API
* Postman


## Features

### Admin

* Add Restaurant
* Add Food
* View Restaurants
* View Foods
* View Orders
* View Delivery Partners
* Assign Delivery Partner

### Customer

* Register and Login
* View Restaurants
* View Foods
* View Foods by Restaurant
* Place Food Order
* View Orders
* Check Order Status

### Delivery Partner

* View Assigned Orders
* Update Order Status
* Update Availability

## Authentication

The project uses **Spring Security** for authentication and role-based authorization.

### Roles

```text
ADMIN
CUSTOMER
DELIVERY_PARTNER
```

## API Endpoints

### Authentication

```text
POST   /auth/register
POST   /auth/register-dto
POST   /auth/login
GET    /auth/user/{id}
```

### Admin

```text
POST   /admin/addresturant
POST   /admin/addresturant-dto
POST   /admin/addfood
POST   /admin/addfood-dto
PUT    /admin/assign/{orderId}/{partnerId}
GET    /admin/restaurants
GET    /admin/foods
GET    /admin/orders
GET    /admin/delivery-partners
```

### Customer

```text
GET    /customer/showfoods
POST   /customer/order/{customerId}
POST   /customer/order-dto/{customerId}
GET    /customer/restaurants
GET    /customer/foods/restaurant/{restaurantId}
GET    /customer/orders/{customerId}
GET    /customer/order/status/{orderId}
```

### Delivery Partner

```text
GET    /delivery/orders/{partnerId}
PUT    /delivery/status/{orderId}
PUT    /delivery/status/{orderId}/update
PUT    /delivery/availability/{partnerId}
```

## Database

MySQL is used to store application data such as:

* Users
* Restaurants
* Food Items
* Orders
* Delivery Partners

## DTO

DTO stands for **Data Transfer Object**.

It is used to transfer only the required data between the client and the application.

Example:

```text
Postman
   ↓
Controller
   ↓
DTO
   ↓
Service
   ↓
Repository
   ↓
MySQL
```

## Security

Different APIs are accessible based on user roles.

```text
/auth/**       → Public
/admin/**      → ADMIN
/customer/**   → CUSTOMER / ADMIN
/delivery/**   → DELIVERY_PARTNER / ADMIN
```

Protected APIs can be tested in Postman using **Basic Authentication**.

## How to Run

### 1. Create Database

Create a MySQL database:

```sql
CREATE DATABASE fooddelevery;
```


### 3. Run the Application

Run the Spring Boot application.

Application URL:

```text
http://localhost:8080
```

### 4. Test APIs

Use **Postman** to test the REST APIs.

## Final Output

The complete application flow is:

```text
Customer
   ↓
Register / Login
   ↓
View Restaurants
   ↓
View Foods
   ↓
Place Order
   ↓
Admin
   ↓
Assign Delivery Partner
   ↓
Delivery Partner
   ↓
Update Order Status
   ↓
Customer
   ↓
Check Order Status
```

### Order Status Flow

```text
PLACED
   ↓
ASSIGNED
   ↓
OUT_FOR_DELIVERY
   ↓
DELIVERED
```

The system manages **users, restaurants, food items, orders, delivery partners, authentication, and order status** through REST APIs.

## Author

**Keerthika**

Java Full Stack Developer
