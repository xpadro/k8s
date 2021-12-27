Pod using environment variables configured from a ConfigMap

## Setup
Create a configMap:

```
kubectl create configmap my-config \
--from-file=config.txt \
--from-literal=param3=value3
```

Deploy the pod:

```
kubectl apply -f .
```

## Execution
Get into the Pod and print the environment variable:

```
kubectl exec -it test-config sh
echo $PARAM_3
```