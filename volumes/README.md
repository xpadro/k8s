An application (crud-app) which accesses a MongoDB deployment with a volume mounted for containing the data

## Setup
```
kubectl apply -f .
```

This will create the following objects:

```
NAME                           READY   STATUS    RESTARTS   AGE
pod/crud-app                   1/1     Running   0          53s
pod/mongodb-7fcffccf85-jjc4w   1/1     Running   0          53s

NAME                 TYPE        CLUSTER-IP    EXTERNAL-IP   PORT(S)     AGE
service/mongodb      ClusterIP   10.100.9.16   <none>        27017/TCP   53s

NAME                      READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/mongodb   1/1     1            1           53s

NAME                                 DESIRED   CURRENT   READY   AGE
replicaset.apps/mongodb-7fcffccf85   1         1         1       53s
```


## Execution
To test the application:

```
kubectl port-forward hello 8080:8080
```

In another terminal, make requests to the app:

- Persist data:

```
curl -X POST localhost:8080/countries -H 'Content-Type: application/json' -d '{"name":"England"}'
```

- Retrieve data:
```
curl localhost:8080/countries
```