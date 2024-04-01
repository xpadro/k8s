Pod using environment variables configured by a ConfigMap

The configmap defines three variables (param1, param2 and param3), but as defined in pod.yml, only param3 is exposed as environment variable in the Pod


## Deployment

Deploy the pod and the configmap:

```
kubectl apply -f .
```

## Execution
Get into the Pod:

```
kubectl exec -it test-config sh
```

You can see PARAM_3 when listing the environment variables:

```
env
```

Or see the specific environment variable

```
echo $PARAM_3
```