
# Local Publishing (Docker Desktop)
This example shows how to run an application in a local k8s cluster on Docker Desktop without publishing the image to a public Docker registry.

## Build the image
Build the application:

```
cd app
./mvnw clean package
```

Build the image:

```
docker build -t local-app -f ./Dockerfile .
```

## Run distribution service

To avoid having to publish the application image to external registry, we can use a local distribution service.

Run the distribution service locally:

```
docker run -d -p 5000:5000 --name registry registry:2.7
```

Now, tag the image and push it to the local registry:

```
docker tag local-app localhost:5000/local-app
docker push localhost:5000/local-app
```


## Deploy the application
Open /etc/hosts to see the host IP address for Docker Desktop. You should see something similar to this:

```
# Added by Docker Desktop
127.0.0.1 kubernetes.docker.internal
```

Now, note down this IP and update the `deployment.yaml` file in app/build:

```
spec.template.spec.image: 127.0.0.1:5000/local-app
```


Finally, deploy the application. Execute the following command:

```
kubectl apply -f build/deployment.yaml
```

If you check the pods logs, you should see that the application started successfully



