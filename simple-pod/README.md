A single pod containing a container with a web application which exposes a 'hello-world' endpoint on port 8080.

## Setup
```
kubectl apply -f .
```


## Execution
To test the application:

```
kubectl port-forward hello 8080:8080
```

In another terminal, make requests to the app:

```
curl localhost:8080/hello
```