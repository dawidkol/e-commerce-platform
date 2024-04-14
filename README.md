# e-commerce-platform
This application is a comprehensive e-commerce platform built using various technologies.
It provides a range of features for both users and administrators,
enabling seamless online shopping experiences and efficient management of the platform.

## Technologies used:
* **Spring Boot:** Framework for building Java applications.
* **Spring Data JPA:** Data access and persistence.
* **Flyway:** Database migration tool.
* **PostgreSQL:** Relational database management system.
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

## API Documentation
### **Swagger**:
```http request
GET http://localhost:8080/swagger-ui/index.html
```

## Viewer endpoints:
### Get products:
```http request
GET /products?page=0&size=12&property=price&dir=DESC
```
```
Optional parameters:
- page(int): Default value : 0
- size(int): Default value : 10
- property(string): Default value : name
- dir(String): Available values : ASC, DESC
```

***

### **Get product:**

```http request
GET /products/{id}
```
***

### **Get product reviews:** 

```http request
GET /products/{id}/reviews
```
***

### **Searching products:**

```http request
GET /products/search?name=apple&category=electronics
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
GET /products/promotions?page=0&size=12
```
```
Optional parameters:
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Get brand:
```http request
GET /brands/{id}
```
---

### Get brands:
```http request
GET /brands?page=0&size=5
```
```
Optional parameters:
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Get all categories:
```http request
GET /category
```
---

### Get products by category:
```http request
GET /category/{name}?page=0&size=5
```
```
Required parameters:
- name(String):

Optional parameters:
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Get all supported currencies:
```http request
GET /currency
```
---

### Get all shipping methods:
```http request
GET /shipping
```
---

### Send contact message:
```http request
POST /contact
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
GET /stats/top3
```
---

## User endpoints:

### User registration:

```http request
POST /users
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
POST /users/token
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
POST /auth
```
Body raw (json):
```json lines
{
  "email": "john.doe@gmail.com",
  "password": "password"
}
```
---

### Update user:
```http request
PATCH /users
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
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
POST /carts
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
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
PATCH /carts
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
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
DELETE /carts
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
```
---

### Create shipping address:
```http request
POST /address
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
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
PUT /address/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
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
POST /orders
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
```
Body raw (json):
```json lines
{
  "shippingId": 1,
  "addressId": 1,
  "promoCode": "testPromoCode"
}
```
```
Optional property:
- "promoCode"
```
---

### Get user's order:
```http request
GET /orders/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
```
---

### Get user's orders:
```http request
GET /orders
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
```
---

### Calculate user's order value with different currency:
```http request
GET /orders/{id}/value?code=EUR
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
```
---

### Create payment:
```http request
POST /payments/{orderId}?code=EUR
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
```
```
Optional parameters:
- code(String): Default value : PLN
Available currency values:
- PLN
- EUR
- USD
- GBP
```
---

### Create review:
```http request
POST /reviews
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDYwMzgsInN1YiI6InNlYmFzdGlhbi5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.mTZypirPpZ6InSFgx-IDN3EK-splq07pjnm_BfJ1XQA
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
### Save new category:
```http request
POST /category
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
POST /brand
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
POST /shipping
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
PATCH /shipping/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
Body raw (json):
```json lines
{
  "newPrice": 10.99
}
```
---

### Delete shipping method price:
```http request
DELETE /shipping/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```

### Update order status:
```http request
PATCH /orders/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
POST /products
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
PATCH /products/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
GET /items?page=1&size=7
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
```
Optional parameters:
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Save item:
```http request
POST /items
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
DELETE /items/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
---

### Update item:
```http request
PATCH /items/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
POST /promos
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
GET /promos
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
```
Optional parameters:
- page(int): Default value : 0
- size(int): Default value : 10
```
---

### Get promo:
```http request
GET /promos/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
---

### Update promo:
```http request
PATCH /promos/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
DELETE /users/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
---

### Register new employee:
```http request
POST /users/employee
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
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
GET /reviews/{id}}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
---

### Save new product's image:
```http request
POST /images/{productId}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
Body form-data:
``` json lines
"image":
```
---

### Delete image:
```http request
DELETE /images/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
---

### Send reply to message:
```http request
POST /contact/response
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
Body raw (json):
```json lines
{
  "contactId": "1",
  "message": "This is example response.",
}
```
---

### Get statistics form period:
```http request
GET http://localhost:8080/stats/avg?start=2024-01-01&end=2024-02-02
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIwMjg0MDU3ODEsInN1YiI6ImphbnVzei5rb3dhbHNraUB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.b0ylAcStwPGn9NzJHtWxbR5ugwZnhavWlBfUzdGMz9A
```
```
Optional property:
- "start"
- "end"
If you do not provide properties, statistics for the last month will be generated.
```
---

## Test coverage: 
![Screenshot 2024-04-13 at 23 22 01](https://github.com/dawidkol/e-commerce-platform/assets/15035709/1d0e31c0-b408-45d8-ae97-4cb6519a9b60)


