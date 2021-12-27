Pod using a volume mounted with the content of a ConfigMap. Each key in the configMap will result in a separate file in the volume

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
Get into the Pod and check the config files:

```
kubectl exec -it test-config sh
cd config
ls
```

you will see two created files:
```
config.txt
param3
```

Let's check the content:
```
> cat config.txt
param1=value1
param2= value2

> cat param3
value3
```