Explains how the control-plane API is called within a pod

## Deployment
```
kubectl apply -f .
```


## Execution
Connect to the pod:

```
kubectl exec -it test -n test-ns -- sh


If you don't specify a service account, the default is assigned to the pod. A token to call the API is automatically mounted to any pod assigned to the service account. The token can be found at the following path:

```
cd /var/run/secrets/kubernetes.io/serviceaccount

/var/run/secrets/kubernetes.io/serviceaccount # ls
ca.crt     namespace  token

/var/run/secrets/kubernetes.io/serviceaccount # cat token
eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJ0ZXN0LW5zIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImRlZmF1bHQtdG9rZW4tcGRydmsiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVmYXVsdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjllYjkwZGEzLWY0MDAtMTFlZS04YzkxLTA4MDAyN2JlMzI3YSIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDp0ZXN0LW5zOmR...
```

Let's check if the pod has access to the API server using this token.

First we grab the token

```
TOKEN=$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)
```

Next, install curl

```
apk update && apk add curl curl-dev bash

```

And access the API server:

```
curl -sSk -H "Authorization: Bearer $TOKEN" https://10.96.0.1:443/api/v1
```

However, if you try to get a specific resource, you won't have permissions, since the default service account does not have enough permissions:

```
curl -sSk -H "Authorization: Bearer $TOKEN" https://10.96.0.1:443/api/v1/namespaces/test-ns

{
  "kind": "Status",
  "apiVersion": "v1",
  "metadata": {
    
  },
  "status": "Failure",
  "message": "namespaces \"test-ns\" is forbidden: User \"system:serviceaccount:test-ns:default\" cannot get resource \"namespaces\" in API group \"\" in the namespace \"test-ns\"",
  "reason": "Forbidden",
  "details": {
    "name": "test-ns",
    "kind": "namespaces"
  },
  "code": 403
}
```