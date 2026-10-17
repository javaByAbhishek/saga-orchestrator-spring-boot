# Saga Orchestration with Spring Boot

A Java 17 and Spring Boot 3.2.5 demonstration of an order-processing saga coordinated through Apache Kafka. The project is a Maven multi-module build with four runnable services and a shared `core` module.

## Architecture

- **Orders service** accepts orders, coordinates the saga, and exposes order history.
- **Products service** stores products and reserves inventory in response to saga commands.
- **Payments service** processes payment commands and calls the credit-card processor service.
- **Credit-card processor service** is a demonstration endpoint that accepts processing requests; it does not integrate with a real payment provider.
- **Core** contains shared DTOs, commands, events, exceptions, and order status types.

The saga communicates through Kafka command and event topics. The order flow reserves a product, processes payment, and then approves the order; failures are represented by events and handled by the saga. Each business service uses an embedded H2 database by default, so data is local to each service process and is not durable across restarts.

## Prerequisites

- JDK 17
- Maven 3.8 or later
- Docker with the Docker Compose plugin

## Setup and run

From the repository root, start the Kafka brokers defined in `docker-compose.yml`:

```shell
docker compose up -d
```

Build and install all modules, including the shared `core` artifact:

```shell
mvn clean install
```

In separate terminals, run the four services from the repository root:

```shell
mvn -pl orders-service spring-boot:run
mvn -pl products-service spring-boot:run
mvn -pl payments-service spring-boot:run
mvn -pl credit-card-processor-service spring-boot:run
```

Stop the Kafka brokers when finished:

```shell
docker compose down
```

## Local configuration

The defaults are in each service's `src/main/resources/application.properties` file:

| Service | HTTP port | Kafka brokers |
| --- | ---: | --- |
| Orders | 8080 | `localhost:9091,localhost:9092,localhost:9093` |
| Products | 8081 | `localhost:9091,localhost:9092,localhost:9093` |
| Payments | 8082 | `localhost:9091,localhost:9092,localhost:9093` |
| Credit-card processor | 8084 | Not used |

Payments calls the processor at `http://localhost:8084`. The compose file publishes Kafka listeners on ports 9091, 9092, and 9093. If you change these values, keep the service settings and compose listeners in sync. The services use local demo settings; configure secrets through an external secret manager or environment-specific configuration for any non-local deployment. Do not commit credentials or private keys.

## HTTP API

- `POST /orders` — submit an order; returns `202 Accepted` while the saga runs.
- `GET /orders/{orderId}/history` — retrieve the recorded status history for an order.
- `GET /products` — list products.
- `POST /products` — create a product.
- `POST /ccp/process` — demo credit-card processing endpoint.

The services do not include an API gateway; call each service using its own port.

## Repository layout

```text
core/                         Shared domain types and Kafka messages
orders-service/               Order API and saga orchestration
products-service/             Product and inventory service
payments-service/             Payment service
credit-card-processor-service Demo payment processor
```
