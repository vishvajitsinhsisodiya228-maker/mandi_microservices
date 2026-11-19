# Hari Bhari Mandi – mandi_microservices

| Folder | Port | What it is |
|--------|------|-----------|
| `mandi-monolith/` | 8080 | Original single app (no database) |
| `discovery-server/` | 8761 | Eureka server |
| `produce-service/` | 8081 | Produce CRUD, custom stock health check, Eureka client |
| `order-service/` | 8082 | Orders, Feign clients, Eureka client |
| `recommendation-service/` | 8000 | Python FastAPI suggestions |
| `monitoring/` | 9090 / 3000 | `prometheus.yml` and Grafana dashboard JSON |

See `ARCHITECTURE.md` for the planned split diagram.

## Start order
1. `discovery-server`: `mvn spring-boot:run` (dashboard at http://localhost:8761)
2. `produce-service`: `mvn spring-boot:run`
3. `order-service`: `mvn spring-boot:run`
4. `recommendation-service`: `pip install -r requirements.txt` then `uvicorn main:app --port 8000`

Registration in Eureka can take about 30 seconds. To run a second produce instance:
`mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8084`

## Postman tests

### produce-service (8081)
| Test | Request | Expected |
|------|---------|----------|
| Known id | `GET /api/produce/1` | 200 |
| Unknown id | `GET /api/produce/99` | 404 |
| Create | `POST /api/produce` `{"name":"Carrot","pricePerKg":40,"stockKg":200}` | 201 |
| Invalid create | `POST /api/produce` `{"name":"","pricePerKg":0,"stockKg":10}` | 400 |
| Update | `PUT /api/produce/7` | 200 |
| Delete | `DELETE /api/produce/7` | 204 |
| Health | `GET /actuator/health` | UP, `totalStockKg` 1830 |
| Stock DOWN | Use `PUT` to bring the total below 1000 (e.g. 900) | health DOWN |

### order-service (8082)
| Test | Request | Expected |
|------|---------|----------|
| Order OK | `POST /api/orders` `{"produceId":2,"quantityKg":40}` | 201, total 880.0 |
| Unknown produce | `POST /api/orders` `{"produceId":99,"quantityKg":40}` | 404 |
| No produce-service | stop produce-service, repeat the order | 503 |
| Instances | `GET /api/orders/instances/produce` | list with host, port, uri |
| Suggestions | `GET /api/orders/1/suggestions` | FastAPI list; empty list if FastAPI is stopped |
| Unknown order | `GET /api/orders/99/suggestions` | 404 |
| Health | `GET /actuator/health` | UP |

### recommendation-service (8000)
`GET /recommendations/Tomato` returns onion, green chilli, coriander.
`GET /recommendations/dragonfruit` returns potato, onion, tomato.

## Monitoring (no Docker)
1. Download the plain Prometheus release for your OS, copy `monitoring/prometheus.yml` next to it and run `./prometheus --config.file=prometheus.yml`. Check Status > Targets at http://localhost:9090.
2. Install Grafana OSS with the normal installer (port 3000), add a Prometheus data source at `http://localhost:9090`.
3. Import `monitoring/mandi_dashboard.json` (Dashboards > New > Import), or build the same panel by hand with `sum by (job) (rate(http_server_requests_seconds_count[1m]))`.
4. Send 20 requests, then export the dashboard JSON again over `mandi_dashboard.json` if you want your own export.
