Example showing how to mount a persistent volume on a pod

## Setup
```
kubectl apply -f .
```

This will create the following objects:

```
kubectl get pv
NAME      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM                   STORAGECLASS   REASON   AGE
host-pv   2Gi        RWO            Retain           Bound    default/host-pv-claim   manual                  30s


kubectl get pvc
NAME            STATUS   VOLUME    CAPACITY   ACCESS MODES   STORAGECLASS   AGE
host-pv-claim   Bound    host-pv   2Gi        RWO            manual         100s


kubectl get pods
NAME   READY   STATUS    RESTARTS   AGE
test   1/1     Running   0          119s
```


## Execution
Connect to the pod:

```
kubectl exec -it test -- sh
```

In the container, the volume is mounted in `pod-dir` directory.