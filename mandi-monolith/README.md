# Hari Bhari Mandi – mandi-monolith

A Spring Boot monolith (Java 17, port 8080) for a fictional wholesale vegetable mandi.
No database or JPA – all data is kept in memory.

## Run
```
mvn spring-boot:run
```

## Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| GET  | `/api/produce` | List the 6 vegetables |
| POST | `/api/orders`  | Place an order, reduces stock, returns total in ₹ |
| GET  | `/api/orders`  | List stored orders |

## Postman tests (produceId 2 = Potato, stock 500 kg, ₹22/kg)

**1. 40 kg – success (HTTP 201)**
`POST http://localhost:8080/api/orders`
```json
{"produceId": 2, "quantityKg": 40}
```
Response:
```json
{"orderId":1,"produceId":2,"produceName":"Potato","quantityKg":40,"pricePerKg":22.0,"totalInRupees":880.0}
```

**2. 5000 kg – too much stock (HTTP 400)**
```json
{"produceId": 2, "quantityKg": 5000}
```
Response: HTTP 400 Bad Request with message
`Only 460 kg of Potato in stock, but 5000 kg requested`
