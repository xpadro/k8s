A pod with injected data from a ConfigMap. The data is accessed from within the pod as a read-only volume.


## Setup
Deploy the pod:

```
kubectl apply -f .
```

This will create the following objects:

```
NAME       READY   STATUS    RESTARTS   AGE
pod/test   1/1     Running   0          47s
```

## Execution
Connect to the pod:

```
kubectl exec -it test -- sh
```

In the container, the volume is mounted in `pod-dir`:

```
/ # cd pod-dir/
/pod-dir # ls
entry_a  entry_b

/pod-dir # cat entry_a
a_content

/pod-dir # cat entry_b
b_content
```
