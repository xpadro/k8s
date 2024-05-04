# Postgres Deployment

## Setup
The Postgres deployment uses a local storage volume, indicating a local path:

```
local:
    path: /home/postgres
```

I'm running the k8s cluster on minikube, so the pod will have visibility over minikube filesystem, not the host's filesystem. For this reason, you have to create the path in minikube:

```
minikube ssh -- sudo mkdir /home/postgres
```

## Deployment
Execute the following command to deploy Postgres:

```
kubectl apply -f postgres.yaml
```

If the Postgres pod gets stuck in 'ContainerCreating' status, use the following command to see where might the issue be:

```
kubectl get events --all-namespaces  --sort-by='.metadata.creationTimestamp'
```

# Validation
You can now connect to the database. Get the Id of the pod and execute the following commands:

```
kubectl exec -it postgres-deployment-7956dfcf4-sqpc4 bash
> psql -U postgres
```

