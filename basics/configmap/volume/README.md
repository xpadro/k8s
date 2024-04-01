Pod using a volume mounted with the content of a ConfigMap. Each key in the configMap will result in a separate file in the volume

## Deployment
Deploy the pod and the configmap:

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

you will see three created files:
```
param1
param2
param3
```

Let's check the content for param3:
```
> cat param3
value3
```