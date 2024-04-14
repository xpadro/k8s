A pod with an assigned service account with access to namespaces resource in API server by using role and rolebinding.

## Deployment
```
kubectl apply -f namespace.yml
kubectl apply -f service-account.yml
kubectl apply -f role.yml
kubectl apply -f role-binding.yml
kubectl apply -f pod.yml
```


## Execution
Connect to the pod:

```
kubectl exec -it test -n test-ns -- sh
```

Let's check if the pod has access to the API server 'namespace' resource. First we grab the token

```
TOKEN=$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)
```

Next, install curl

```
apk update && apk add curl curl-dev bash

```

And access the API server (I got the IP from the kubernetes service since with my minikube somehow is not resolving DNS for kubernetes.default):

```
curl -sSk -H "Authorization: Bearer $TOKEN" https://10.96.0.1:443/api/v1/namespaces/test-ns

{
  "kind": "Namespace",
  "apiVersion": "v1",
  "metadata": {
    "name": "test-ns",
    "selfLink": "/api/v1/namespaces/test-ns",
    "uid": "5c53c192-f43f-11ee-8b7d-080027284afe",
    "resourceVersion": "6222",
    "creationTimestamp": "2024-04-06T17:59:06Z",
    "labels": {
      "name": "test"
    },
    "annotations": {
      "kubectl.kubernetes.io/last-applied-configuration": "{\"apiVersion\":\"v1\",\"kind\":\"Namespace\",\"metadata\":{\"annotations\":{},\"labels\":{\"name\":\"test\"},\"name\":\"test-ns\"}}\n"
    }
  },
  "spec": {
    "finalizers": [
      "kubernetes"
    ]
  },
  "status": {
    "phase": "Active"
  }
}
```





