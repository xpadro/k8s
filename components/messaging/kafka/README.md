# Kafka Messaging


# Setup

### Deploy Kafka
Deploy Zookeeper and Kafka:

```
kubectl apply -f zookeeper-deploy.yaml
kubectl apply -f kafka-deploy.yaml
```

### Create a topic
Connect to the Kafka Pod:

```
kubectl exec -it kafka-deployment-6dfd6b99f5-bnlpf -- /bin/bash
```

And create a topic:

```
kafka-topics --bootstrap-server localhost:9092 --create --topic my-topic --replication-factor 1 --partitions 3
```

Check if the topic is created:

```
kafka-topics --bootstrap-server localhost:9092 --list
```

### Deploy the application
First, let's build the application:

´´´
mvnw clean install
´´´

In order to deploy the application in the local minikube cluster, we need to generate its image. However, instead of publishing the image into a registry like DockerHub, we can directly push the image into the local cluster.

In order to do that, we first need to point our terminal to use the Docker daemon inside minikube instead of the one running in our host:

```
eval $(minikube docker-env)
```

Also, make sure that the application deploymeny yaml file has 'Never' as its imagePullPolicy.

Now, any Docker command executed on our terminal will be sent to the daemon inside the cluster. Let's generate the application image:

```
docker build -t xpadro/spring-boot-kafka:latest .
```

Next, deploy the application to the cluster:

```
kubectl apply -f ./deployment/app-deployment.yaml
```


## Execution
