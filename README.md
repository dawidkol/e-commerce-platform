# e-commerce-platform
This application is a comprehensive e-commerce platform built using various technologies.
It provides a range of features for both users and administrators,
enabling seamless online shopping experiences and efficient management of the platform.

## Setup and Run
This guide will walk you through setting up and running the e-commerce-platform using Docker Compose.
Make sure you have Docker and Docker Compose installed on your machine before proceeding

## Prerequisites
Ensure you have the following installed on your system:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Step 1: Clone the Repository
```shell
git clone https://github.com/dawidkol/e-commerce-platform.git
cd e-commerce-platform
```

### Step: 2. Set Up Environment Variables
Create a `.env` file in the root directory of the project and add the following environment variables:
```env
# PostgreSQL Database Configuration
POSTGRES_USER=your_postgres_user
POSTGRES_PASSWORD=your_postgres_password

# JWS Shared Key (for security)
JWS_SHARED_KEY=your_jws_shared_key

# Stripe API Keys
STRIPE_API_KEY=your_stripe_api_key
STRIPE_WEBHOOK_KEY=your_stripe_webhook_key

# Email Configuration (for sending emails)
EMAIL=your_email@gmail.com
EMAIL_PASSWORD=your_email_password
```
Replace the placeholders (your_postgres_user, your_postgres_password, etc.) with your actual values.

### Step 3: Build and Run the Application
Run the following command to start the application and database using Docker Compose:
```shell
docker compose -f docker-compose.yml up -d
```
This will:
- Build the Docker images for the e-commerce platform and PostgreSQL database.
- Start the containers and link them together.
- Expose the e-commerce-platform on port 8080.

### Step 4: Access the Application
Once the containers are up and running, you can access the e-commerce platform at:
``` http request
http://localhost:8080
```
### Step 5: Stopping the Application
To stop the application and remove the containers, run:
```shell
docker compose -f docker-compose.yml down
```

If you want to remove the volumes (including the database data), use:
```shell
docker compose -f docker-compose.yml down -v
```
### Step 6: (Optional) Running Tests
To run unit and integration tests, use the following command:
```shell
mvn test -Dspring.profiles.active=test
```
---
## Technologies used:
* **Spring Boot:** Framework for building Java applications.
* **Spring Data JPA:** Data access and persistence.
* **Flyway:** Database migration tool.
* **PostgresSQL:** Relational database management system.
* **Lombok:** Library for reducing boilerplate code in Java classes.
* **Spring Test:** Testing framework for Spring components.
* **Spring Security:** Framework for authentication and authorization.
* **Springdoc OpenAPI (Swagger):** Library for documenting and testing API endpoints.
* **JSON Patch:** Library for applying partial updates to JSON documents.
* **Stripe Java:** Library for integrating with the Stripe payment gateway.
* **Jackson Datatype JSR310:** Library for handling date and time serialization/deserialization.
* **Testcontainers:** Framework for running tests with external dependencies.
* **Spring Boot Mail:** Library for sending emails from Spring Boot applications.
---

## Key Features:
### For Users:
* **Registration:** Users can create accounts to access the platform.
* **Token Authentication:** Secure authentication mechanism using tokens for user access.
* **Fetching currencies from external server:** Integration with an external server to fetch
        up-to-date currency data.
* **Stripe:** Seamless integration with the Stripe payment gateway for processing transactions.
* **Payment in different currencies:** Capability to accept payments in various currencies, enhancing
        flexibility for international customers.
* **Email notifications:** Automatic email notifications to users for important events such as
        registration confirmation, contact confirmation.

### For Administrators:
* **Management of Categories, Brands, and Shipping Methods:** Admins can create, update, and delete categories, brands, and shipping methods.
* **Promotion Management:** Admins can create and manage promotional codes with customizable discounts, validity periods, and usage limits.
* **Product Management:** Admins can add, update, and delete products, including features like product descriptions, pricing, and promotional prices.
* **Order Management:** Admins can update order statuses, view and manage orders.
* **User Management:** Admins can delete user accounts, register new employees, and manage user-related operations like resetting activation links and sending replies to user messages.
---

## Entity relationship diagram:

![Screenshot 2024-04-21 at 18 01 55](https://github.com/dawidkol/e-commerce-platform/assets/15035709/7c5a8f54-2d7c-4f4d-ad71-c1180c112316)

---

## API Documentation
### **Swagger**:
```http request
GET http://localhost:8080/api/v1/swagger-ui/index.html
```

## Viewer endpoints:
### Get products:
```http request
GET http://localhost:8080/api/v1/products?page=0&size=12&property=price&dir=DESC
```
```
Optional parameters(pagination and filtering):
- page(int): Default value : 0
- size(int): Default value : 10
- property(string): Default value : name
- dir(String): Available values : ASC, DESC
```

***

### **Get product:**

```http request
GET http://localhost:8080/api/v1/products/{id}
```
***

### **Get product reviews:** 

```http request
GET http://localhost:8080/api/v1/products/{id}/reviews
```
***

### **Searching products:**

```http request
GET http://localhost:8080/api/v1/products/search?name=apple&category=electronics
```
```
Required parameters:
- name(String)

Optional parameters:
- category(String)
```
---

### Get promotion products: 
```http request
GET http://localhost:8080/api/v1/products/promotions?page=0&size=12
```
```
Optional parameters(pagination):
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Get brand:
```http request
GET http://localhost:8080/api/v1/brands/{id}
```
---

### Get brands:
```http request
GET http://localhost:8080/api/v1/brands?page=0&size=5
```
```
Optional parameters(pagination):
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Get all categories:
```http request
GET http://localhost:8080/api/v1/category
```
---

### Get products by category:
```http request
GET http://localhost:8080/api/v1/category/{name}?page=0&size=5
```
```
Required parameters:
- name(String):

Optional parameters(pagination):
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Get all supported currencies:
```http request
GET http://localhost:8080/api/v1/currency
```
---

### Get all shipping methods:
```http request
GET http://localhost:8080/api/v1/shipping
```
---

### Send contact message:
```http request
POST http://localhost:8080/api/v1/contact
```
Body raw (json):
```json lines
{
  "sender": "john.doe@test.com",
  "subject": "example subject",
  "message": "example message"
}
```
---

### Get top 3 sold product:
```http request
GET http://localhost:8080/api/v1/stats/top3
```
---

## User endpoints:

### User registration:

```http request
POST http://localhost:8080/api/v1/users
```
Body raw (json):
```json lines
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password"
}
```
After executing the above request, an email with an account activation link will be sent. In the email message, you will find a description along with a link to activate your account. The activation link is valid for 15 minutes. **If you do not confirm your account within 15 minutes, you will need to regenerate the link.**

---
### Activation link regeneration:

```http request
POST http://localhost:8080/api/v1/users/token
```
Body raw (json):
```json lines
{
  "email": "john.doe@gmail.com",
  "password": "password"
}
```
---

### Bearer token generation:

```http request
POST http://localhost:8080/api/v1/auth
```
Body raw (json):
```json lines
{
  "email": "john.doe@gmail.com",
  "password": "password"
}
```
---

### If you prefer not to register, you can generate a Bearer token using the data below:
```json lines
{
  "email": "sebastian.kowalski@test.pl",
  "password": "password"
}
```

### Update user:
```http request
PATCH http://localhost:8080/api/v1/users
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
Body raw (json):
```json lines
{
  "firstName": "New name",
  "lastName": "New last name",
  "email": "new.email@example.com",
  "password": "newpassword"
}
```
---

### Add product to user's cart:
```http request
POST http://localhost:8080/api/v1/carts
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
Body raw (json):
```json lines
{
  "productId": 1,
  "quantity": 1
}
```
---

### Update product quantity in user's cart:
```http request
PATCH http://localhost:8080/api/v1/carts
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
Body raw (json):
```json lines
{
  "productId": 1,
  "quantity": 3
}
```
---

### Delete all products from user's cart:
```http request
DELETE http://localhost:8080/api/v1/carts
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
---

### Create shipping address:
```http request
POST http://localhost:8080/api/v1/address
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
Body raw (json):
```json lines
{
  "postalCode": "22-400",
  "street": "Ulicowa",
  "buildingNumber": "12",
  "apartmentNumber": "20",
  "phoneNumber": "666999666"
}
```
---

### Update shipping address:
```http request
PUT http://localhost:8080/api/v1/address/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
Body raw (json):
```json lines
{
  "postalCode": "22-400",
  "street": "Nowa ulica",
  "buildingNumber": "20",
  "apartmentNumber": "30",
  "phoneNumber": "666999666"
}
```
---

### Create order:
```http request
POST http://localhost:8080/api/v1/orders
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
Body raw (json):
```json lines
{
  "shippingId": 1,
  "addressId": 1,
  "promoCode": "testPromoCode"
}
```
---

### Cancel order:
```http request
PATCH http://localhost:8080/api/v1/orders/{id}/delete
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
---

### Get user's order:
```http request
GET http://localhost:8080/api/v1/orders/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
---

### Get user's orders:
```http request
GET http://localhost:8080/api/v1/orders
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
---

### Calculate user's order value with different currency:
```http request
GET http://localhost:8080/api/v1/orders/{id}/value?code=EUR
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
---

### Create payment:
```http request
POST http://localhost:8080/api/v1/payments/{orderId}?code=EUR
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
```
Optional parameters(pagination):
- code(String): Default value : PLN
Available currency values(code):
- PLN
- EUR
- USD
- GBP
```
---

### Create review:
```http request
POST http://localhost:8080/api/v1/reviews
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWJhc3RpYW4ua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODczMDE3MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.urEAuriMLLZNbLvvzUvDJlhCGWw0fRopaneit4VW_2Y
```
Body raw (json):
```json lines
{
  "productId": 1,
  "rating": 5,
  "comment": "This is test comment"
}
```
---

## Admin endpoints:

### Admin example data for a Bearer token generation:
```json lines
{
  "email": "janusz.kowalski@test.pl",
  "password": "password"
}
```

### Save new category:
```http request
POST http://localhost:8080/api/v1/category
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "name": "New unique test category name"
}
```
---

### Save new brand:
```http request
POST http://localhost:8080/api/v1/brand
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "name": "New unique test brand name"
}
```
---

### Save new shipping method:
```http request
POST http://localhost:8080/api/v1/shipping
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "name": "New shipping method",
  "shippingCost": 10.99
}
```
---

### Update shipping method price:
```http request
PATCH http://localhost:8080/api/v1/shipping/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "newPrice": 10.99
}
```
---

### Delete shipping method:
```http request
DELETE http://localhost:8080/api/v1/shipping/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```

### Update order status:
```http request
PATCH http://localhost:8080/api/v1/orders/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "status": "ORDER_HANDED_FOR_PROCESSING"
}
```
```
Available status values: 
- NEW
- PAID
- ORDER_HANDED_FOR_PROCESSING
- PROCESSING_ORDER
- SENT
- DELIVERED
- RECEIVER
```

### Save new product:
```http request
POST http://localhost:8080/api/v1/products
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "name": "New product",
  "description": "Best new product in the market.",
  "price": 99.9,
  "categoryId": 1,
  "brandId": 1,
  "promotionPrice": 89.99
}
```
```
Optional property: 
- "promotionPrice":
```
---

### Update  product:
```http request
PATCH http://localhost:8080/api/v1/products/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "name": "New product",
  "description": "Best new product in the market.",
  "price": 99.9,
  "categoryId": 1,
  "brandId": 1,
  "promotionPrice": 89.99
}
```
```
Optional property: 
- "name"
- "description"
- "price"
- "categoryId"
- "brandId"
- "promotionPrice"
```
---

### Get items:
```http request
GET http://localhost:8080/api/v1/items?page=1&size=7
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
```
Optional parameters(pagination):
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Save item:
```http request
POST http://localhost:8080/api/v1/items
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "productId": 20,
  "quantity": 20,
  "available": true
}
```
---

### Delete item:
```http request
DELETE http://localhost:8080/api/v1/items/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
---

### Update item:
```http request
PATCH http://localhost:8080/api/v1/items/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "productId": 20,
  "quantity": 20,
  "available": true
}
```
```
Optional property: 
- "productId"
- "quantity"
- "available"
```
---

### Save promo:
```http request
POST http://localhost:8080/api/v1/promos
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "code": "promo123",
  "discountPercent": 5,
  "activeStart": "2024-04-13T16:54:23.512Z",
  "activeEnd": "2024-04-14T16:54:23.512Z",
  "active": true,
  "maxUsageCount": 10
}
```
---

### Get all promo codes:
```http request
GET http://localhost:8080/api/v1/promos
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
```
Optional parameters(pagination):
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Get promo code:
```http request
GET http://localhost:8080/api/v1/promos/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
---

### Update promo code:
```http request
PATCH http://localhost:8080/api/v1/promos/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "code": "update promo123",
  "discountPercent": 10,
  "activeStart": "2024-04-13T16:54:23.512Z",
  "activeEnd": "2024-04-20T16:54:23.512Z",
  "active": true,
  "maxUsageCount": 100
}
```
```
Optional property:
- "code"
- "discountPercent"
- "activeStart"
- "activeEnd"
- "active"
- "maxUsageCount"
```
---

### Delete user's account:
```http request
DELETE http://localhost:8080/api/v1/users/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
---

### Register new employee:
```http request
POST http://localhost:8080/api/v1/users/employee
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json): 
```json lines
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "hardPassword"
}
```
---

### Get review:
```http request
GET http://localhost:8080/api/v1/reviews/{id}}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
---

### Save new product's image:
```http request
POST http://localhost:8080/api/v1/images/{productId}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body form-data:
``` json lines
"image":
```
---

### Delete image:
```http request
DELETE http://localhost:8080/api/v1/images/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
---

### Send an email reply to the contact message:
```http request
POST http://localhost:8080/api/v1/contact/response
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
Body raw (json):
```json lines
{
  "contactId": "1",
  "message": "This is example response.",
}
```
---

### Get sales statistics for the period:
```http request
GET http://localhost:8080/api/v1/stats/avg?start=2024-01-01&end=2024-02-02
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
```
Optional parameters:
- "start"
- "end"
If you do not provide properties, statistics for the last month will be generated.
```
---

### Get all payments to refund:
```http request
GET http://localhost:8080/api/v1/payments/refunds
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
---

### Refund payment:
```http request
POST http://localhost:8080/api/v1/payments/refunds/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW51c3oua293YWxza2lAdGVzdC5wbCIsImV4cCI6MjAyODcyOTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.EFYP41cXOCD2J3fhQnPOHrn1RMTbWoNY_vDZREiLNnk
```
---

## Test coverage: 
![Screenshot 2024-04-13 at 23 22 01](https://github.com/dawidkol/e-commerce-platform/assets/15035709/1d0e31c0-b408-45d8-ae97-4cb6519a9b60)

---
## CI/CD Pipeline
- **GitHub Actions** is configured for automated builds and deployments.
- Uses **Docker Hub** for containerization.
- Runs unit tests and static analysis before pushing to production.
---
## Author:
### dawid.kolano@igmail.com

---




