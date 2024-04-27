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

```
mvnw clean install
```

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

### Expose the application
We will expose the application via Ingress.

First, we need to enable Ingress support to minikube by installing an addon:

```
minikube addons enable ingress
```

Verify it is working:

```
kubectl get pods -n ingress-nginx
```

You should see something like this:

```
NAME                                        READY   STATUS      RESTARTS   AGE
ingress-nginx-admission-create-wjckw        0/1     Completed   0          68s
ingress-nginx-admission-patch-79lgc         0/1     Completed   0          68s
ingress-nginx-controller-7c6974c4d8-bxrpw   1/1     Running     0          68s
```

Deploy the ingress, which defines the demo application Service we deployed before as its backend:

```
kubectl apply -f ./deployment/ingress.yaml
```

Check it by executing `kubectl get ingress`:

```
NAME           CLASS   HOSTS                  ADDRESS        PORTS   AGE
demo-ingress   nginx   k8s.springboot.kafka   192.168.49.2   80      39s
```

Now, we need to update `/etc/hosts` file in order to send requests to the Ingress host (k8s.springboot.kafka). The IP is retrieved from the Ingress (`kubectl get ingress`)

Open the file with `vi` for example, and add this line:

```
192.168.49.2 k8s.springboot.kafka
```

Finally, we need to create a route to the services deployed which have LoadBalancer type. If we are using minikube, we can create a tunnel:

```
minikube tunnel
```

Everything should be properly setup.


## Validation

### Pod exposes application
Check that the pod exposes the application endpoint. Get the pod name with `kubectl get pods` and get the Pod IP from the following command:

```
kubectl get pods spring-boot-kafka-55cd8885cb-4ll8d -o yaml
```

The field we are looking for is:

```
podIP: 10.244.0.34
```

Now run a pod with curl to execute commands from within the cluster:

```
kubectl run pod-with-curl --image=radial/busyboxplus:curl -i --tty --rm
```

In the pod's shell run a curl against the Pod's IP:

```
curl 10.244.0.34:8080/demo
> Request received
```

The application exposes the endpoint through the port 8080, as stated in the deployment (and also in application.yaml):

```
ports:
        - containerPort: 8080
```

The application is reachable.


### Service discovered
Check if the service is reachable and has the Pod as one of its backends.

Get the service IP:

```
kubectl get svc
NAME                        TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S) 
spring-boot-kafka-service   LoadBalancer   10.102.68.23     <pending>     9001:30378/TCP
```

Connect again with the dummy pod command and try out a request targetting the service:

```
curl spring-boot-kafka-service:9001/demo
> Request received
```

The service exposes the port 9001, which targets the port 8080 in the backend as stated in the app-deployment.yaml configuration:

```
ports:
  - protocol: TCP
    port: 9001
    targetPort: 8080
```

The service works as expected.


## Execution
Open the browser and make a request to `http://k8s.springboot.kafka/demo`.

If you open the logs on the application's Pod, you should see the following:

```
2024-04-27T14:14:37.627Z  INFO 7 --- [nio-8080-exec-4] com.xpadro.kafka.AppController           : Sending message
2024-04-27T14:14:37.635Z  INFO 7 --- [ntainer#0-0-C-1] com.xpadro.kafka.Consumer                : Message received: test message
```


## Clean up
Stop using Minikube host for Docker:

```
eval $(minikube docker-env -u)
```

Remove the entry you added to `/etc/hosts`

Remove the deployment:

```
kubectl delete -f ./deployment/app-deployment.yaml
kubectl delete -f ./deployment/ingress.yaml

```