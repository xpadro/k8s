# RabbitMQ Messaging
Two Spring boot services (producer and consumer) communicating through a RabbitMQ message broker.

## Setup
Set up a RabbitMQ Broker server:

```
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
```

## Execution
Run both Spring Boot applications.

- The producer has configured a scheduled sender, which will send messages to the exchange every 5 seconds
- The consumer uses a listener to receive the messages and log them to the console