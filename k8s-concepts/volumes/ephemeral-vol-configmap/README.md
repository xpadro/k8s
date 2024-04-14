A pod with an ephemeral read-only volume. The data in the volume is injected from a ConfigMap.


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

In the container, the volume is mounted in `pod-dir` directory:

```
/ # cd pod-dir/
/pod-dir # ls
entry_a  entry_b

/pod-dir # cat entry_a
a_content

/pod-dir # cat entry_b
b_content
```
