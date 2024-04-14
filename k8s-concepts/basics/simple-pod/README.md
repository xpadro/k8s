A single pod containing a container with a web application which exposes a 'hello-world' endpoint on port 8080.

## Deployment
```
kubectl apply -f .
```


## Execution
To test the application, forward pod's port 8080 to localhost:8081

```
kubectl port-forward hello 8081:8080
```

In another terminal, make requests to the app:

```
curl localhost:8081/hello
```