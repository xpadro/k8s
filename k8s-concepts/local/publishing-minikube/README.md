
# Local Publishing (Minikube)
This example shows how to run an application in a local k8s cluster on Minikube without publishing the image to a public Docker registry.


## Build the image
Build the application:

```
cd app
./mvnw clean package
```

In order to build the image, use `minikube build` command instead of `docker build`:


```
minikube image build -t xpadro/local-app -f ./Dockerfile .
```

Check the image is in the image list inside minikube:

```
minikube image ls --format table

|----------------------------------------------------|---------|---------------|--------|
|                       Image                        |   Tag   |   Image ID    |  Size  |
|----------------------------------------------------|---------|---------------|--------|
| docker.io/xpadro/local-app                         | latest  | f93c5d75853c7 | 665MB  |
| registry.k8s.io/etcd                               | 3.5.9-0 | 73deb9a3f7025 | 294MB  |
```



## Deploy the application

Deploy the application. Make sure to set imagePullPolicy to `Ǹever`. The image specification is in the `deployment.yaml` file in app/build:


```
spec:
      containers:
      - name: local-app
        image: xpadro/local-app
        imagePullPolicy: Never
```


Execute the following command:

```
kubectl apply -f build/deployment.yaml
```

If you check the pods logs, you should see that the application started successfully



