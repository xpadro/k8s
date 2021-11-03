An application (crud-app) which accesses a MongoDB deployment with a volume mounted for containing the data. The application is deployed using a deployment and specifying 2 replicas.

## Setup
```
kubectl apply -f .
```

This will create the following objects:

```
NAME                                   READY   STATUS    RESTARTS   AGE
pod/crud-app-deploy-598478d95c-8xqvx   1/1     Running   0          61s
pod/crud-app-deploy-598478d95c-kv975   1/1     Running   0          61s
pod/mongodb-7fcffccf85-wpqng           1/1     Running   0          61s

NAME                 TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)     AGE
service/kubernetes   ClusterIP   10.96.0.1        <none>        443/TCP     2d8h
service/mongodb      ClusterIP   10.102.132.161   <none>        27017/TCP   61s

NAME                              READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/crud-app-deploy   2/2     2            2           61s
deployment.apps/mongodb           1/1     1            1           61s

NAME                                         DESIRED   CURRENT   READY   AGE
replicaset.apps/crud-app-deploy-598478d95c   2         2         2       61s
replicaset.apps/mongodb-7fcffccf85           1         1         1       61s
```


## Execution
To test the application (using one of the crud-app pod names):

```
kubectl port-forward crud-app-deploy-598478d95c-8xqvx 8080:8080
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