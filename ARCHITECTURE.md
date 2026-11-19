# Hari Bhari Mandi – Planned Microservices Architecture

The `mandi-monolith` (port 8080) will be split into two services:

- **produce-service** – owns the vegetable catalogue and stock. It is drawn twice (ports 8081 and 8083) to show it can be scaled on its own, because it gets the most read traffic.
- **order-service** (port 8082) – owns orders. It calls produce-service to look up a vegetable before placing an order.

## Diagram

```
                         +---------------------------------+
                         |            CLIENT               |
                         |     (Postman / Mobile App)      |
                         +----------------+----------------+
                                          |
                +-------------------------+-------------------------+
                |                         |                         |
                v                         v                         v
   +---------------------------+  +-----------------------+  +---------------------------+
   |  produce-service  (#1)    |  |   order-service       |  |  produce-service  (#2)    |
   |       PORT 8081           |  |     PORT 8082         |  |       PORT 8083           |
   |---------------------------|  |-----------------------|  |---------------------------|
   | GET  /api/produce         |  | POST /api/orders      |  | GET  /api/produce         |
   | GET  /api/produce/{id}    |  | GET  /api/orders      |  | GET  /api/produce/{id}    |
   | PUT  /api/produce/{id}/   |  |                       |  | PUT  /api/produce/{id}/   |
   |      reduce-stock         |  |                       |  |      reduce-stock         |
   +---------------------------+  +-----------+-----------+  +---------------------------+
                ^                             |                            ^
                |                             |                            |
                |    GET /api/produce/{id}    |    GET /api/produce/{id}   |
                +-----------------------------+----------------------------+
                      (order-service calls produce-service;
                       any one of the two copies can answer)
```

### Simplified view of the single arrow

```
+---------------+     GET /api/produce/{id}     +------------------+
| order-service | ----------------------------> | produce-service  |
|   (8082)      |                               | (8081 and 8083)  |
+---------------+                               +------------------+
```

## Endpoints each service keeps

### produce-service (ports 8081 and 8083 – same code, two instances)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/produce` | List all vegetables |
| GET | `/api/produce/{id}` | Get one vegetable (price, stock) |
| PUT | `/api/produce/{id}/reduce-stock` | Reduce stockKg after an order |

### order-service (port 8082)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/orders` | Place an order; returns the total in ₹ |
| GET | `/api/orders` | List all stored orders |

## Why this split?
- Reading the vegetable list is far more frequent than placing orders, so **produce-service** can be scaled alone (8081 + 8083) without also starting extra copies of order-service.
- Each service has its own code, port and (in-memory) data, so one can be deployed or fixed without touching the other.
- order-service never reads produce data directly; it only calls `GET /api/produce/{id}` on produce-service.

> Note: with two produce-service copies, each keeps its own in-memory stock. A shared store (database) or a single owner of stock would be needed later to keep them in sync.
