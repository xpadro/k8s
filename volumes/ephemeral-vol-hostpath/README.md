A pod with a mounted volume mapped to a folder in the host filesystem (hostPath).


## Setup
For minikube setup, we create a test.txt file so we access it later on from within the pod:

```
minikube ssh
```

In my case, the current directory is `/home/docker`, which is the path defined in `pod.yml`. We create the file here:

```
echo "hello" > test.txt
```

Exit the minikube filesystem and deploy the pod:

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
/ # ls
bin      etc      lib      media    pod-dir  root     sbin     tmp      var
dev      home     linuxrc  mnt      proc     run      sys      usr
/ # cd pod-dir/
/pod-dir # ls
test.txt
/pod-dir # cat test.txt
hello
```
