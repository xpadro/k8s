# Kafka Messaging


## Setup
Deploy Zookeeper and Kafka:

```
kubectl apply -f zookeeper-deploy.yaml
kubectl apply -f kafka-deploy.yaml
```

## Create a topic
Connect to the Kafka Pod:

```
kubectl exec -it kafka-deployment-6dfd6b99f5-bnlpf -- /bin/bash
```

And create a topic:

```
kafka-topics --bootstrap-server localhost:9092 --create --topic my-topic --replication-factor 1 --partitions 3
```


## Execution
