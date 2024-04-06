An application (crud-app) accessingwhich accesses a MongoDB deployment with a volume mounted for containing the data. The application is deployed using a deployment and specifying 1 replica.

The volume is of emptyDir type, which means that it will exist only as long as the Pod exists.

## Setup
```
kubectl apply -f .
```

This will create the following objects:

```
NAME                                           READY   STATUS    RESTARTS   AGE
pod/crud-app-deploy-598478d95c-lnlwb           1/1     Running   0          50s
pod/mongodb-emptydir-deploy-5f9cbdffd5-sx2s5   1/1     Running   0          50s

NAME                 TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)     AGE
service/kubernetes   ClusterIP   10.96.0.1        <none>        443/TCP     6d3h
service/mongodb      ClusterIP   10.109.166.127   <none>        27017/TCP   50s

NAME                                      READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/crud-app-deploy           1/1     1            1           50s
deployment.apps/mongodb-emptydir-deploy   1/1     1            1           50s

NAME                                                 DESIRED   CURRENT   READY   AGE
replicaset.apps/crud-app-deploy-598478d95c           1         1         1       50s
replicaset.apps/mongodb-emptydir-deploy-5f9cbdffd5   1         1         1       50s
```

## Execution
To test the application (using the crud-app pod name):

```
kubectl port-forward crud-app-deploy-598478d95c-lnlwb 8080:8080
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

As soon as the mongodb pod is destroyed, data will be lost.