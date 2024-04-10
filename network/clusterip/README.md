An nginx pod exposed within the cluster by using a ClusterIP service.

The pod contains an nginx container. Although the pod is already visible within the cluster, a restart of the pod would modify the IP address, having to update the objects referencing this pod.

The service is assigned with a unique IP. Communication to this service will be load-balanced to any of the pods that are members of the Service.

## Setup
Deploy the pod:
```
kubectl apply -f deployment.yml
```

Deploy the service:
```
kubectl apply -f service.yml
```

## Verify
Check the IPs of the two replicas created:
```
% kubectl get pods -l run=my-nginx -o wide
NAME                        READY   STATUS    RESTARTS   AGE   IP           NODE             NOMINATED NODE   READINESS GATES
my-nginx-5b56ccd65f-84j2v   1/1     Running   0          12s   10.1.0.164   docker-desktop   <none>           <none>
my-nginx-5b56ccd65f-zpsv8   1/1     Running   0          12s   10.1.0.165   docker-desktop   <none>           <none>
```

Now, let's check that both pods are members of the Service:
```
% kubectl describe svc my-nginx                 
Name:              my-nginx
Namespace:         default
Labels:            run=my-nginx
Annotations:       <none>
Selector:          run=my-nginx
Type:              ClusterIP
IP Family Policy:  SingleStack
IP Families:       IPv4
IP:                10.104.150.180
IPs:               10.104.150.180
Port:              <unset>  80/TCP
TargetPort:        80/TCP
Endpoints:         10.1.0.164:80,10.1.0.165:80
Session Affinity:  None
Events:            <none>
```

In endpoints, you can see the IPs of the two pods.


## Execution
To access the application you need to call it from within the cluster.

Let's create a temporary pod and connect to it:

```
kubectl run pod-with-curl --image=radial/busyboxplus:curl -i --tty --rm
```

From within the pod, you can call the nginx application in the pod by using the IP and the port of the service. You can see this information when you described the service.

```
[ root@pod-with-curl:/ ]$ curl 10.104.150.180:80
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
```

Kubernetes automatically assigns DNS names to services. Hence, you can make the same call using the service name:

```
[ root@pod-with-curl:/ ]$ curl my-nginx:80
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
```