A CRUD application with a MongoDB database exposed outside of the cluster by using a load balancer


## Setup
Deploy the applicaation and associated services:

```
kubectl apply -f .
```


## Verify
Once deployed, we can see that the load balancer external IP is still pending:

```
kubectl get svc
NAME         TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
kubernetes   ClusterIP      10.96.0.1        <none>        443/TCP          15h
mongodb      ClusterIP      10.105.209.222   <none>        27017/TCP        53s
test-lb      LoadBalancer   10.108.166.184   <pending>     8080:32000/TCP   53s
```

We need to create a route to the services deployed which have LoadBalancer type. If we are using minikube, we can create a tunnel:

```
minikube tunnel
```

If we now check the service again, it has the IP assigned:

```
kubectl get svc
NAME         TYPE           CLUSTER-IP       EXTERNAL-IP      PORT(S)          AGE
kubernetes   ClusterIP      10.96.0.1        <none>           443/TCP          15h
mongodb      ClusterIP      10.105.209.222   <none>           27017/TCP        2m4s
test-lb      LoadBalancer   10.108.166.184   10.108.166.184   8080:32000/TCP   2m4s
```

We can now call the application using the external IP. In another terminal, save some data to the app's database:

```
curl -X POST 10.108.166.184:8080/countries -H 'Content-Type: application/json' -d '{"name":"England"}'
```

And make a get request to retrieve data:

```
curl 10.108.166.184:8080/countries

[{"name":"England"}]
```