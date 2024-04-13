# Vault Injector
Deployment of Vault server and Vault injector sidecar which injects a secret to an application's pod.


## Installation
Add Hashicorp helm repository:

```
helm repo add hashicorp https://helm.releases.hashicorp.com
helm repo update
```

Install Vault server and Vault injector:

```
helm install vault hashicorp/vault
```

## Unseal Vault
When vault is installed, it is in a sealed state. It knows how to access the physical storage, but doesn't know how to decrypt it. Unsealing is the process of getting a plain text root key used to decrypt the decryption key (which is stored in the storage and encrypted using the root key) and decrypt the data.

First, we create the keys to start using vault. Those keys are stored in a local file 'keys.json:

```
kubectl exec vault-0 -- vault operator init -key-shares=1 -key-threshold=1 -format=json > keys.json
```

Next, we get the unseal key and also the root key:

```
VAULT_UNSEAL_KEY=$(cat keys.json | jq -r ".unseal_keys_b64[]")
echo $VAULT_UNSEAL_KEY

VAULT_ROOT_KEY=$(cat keys.json | jq -r ".root_token")
echo $VAULT_ROOT_KEY
```

Finally, we unseal Vault:

```
kubectl exec vault-0 -- vault operator unseal $VAULT_UNSEAL_KEY
```


## Create a secret
To create a secret, we need to connect to the Vault pod in order to be able to use the vault cli.

However, we must first login to Vault using the root key. Otherwise, the actions we want to perform like enabling the kv secrets engine will return a '403 permission denied':

```
kubectl exec vault-0 -- vault login $VAULT_ROOT_KEY
```

Now that we are logged, we can connect to the pod to use the vault cli:

```
kubectl exec -it vault-0 -- /bin/sh
```

The kv secrets engine is used by Vault to store secrets within the physical storage. Enable it for the specific 'demo-vault' path:

```
vault secrets enable -version=2 -path="demo-vault" kv
```

Create a secret within the enabled path:

```
vault kv put demo-vault/demo-secrets/demo-user mykey=myvalue
```

Retrieve the secret to check if it was properly persisted:

```
vault kv get demo-vault/demo-secrets/demo-user
```

```
============= Secret Path =============
demo-vault/data/demo-secrets/demo-user

======= Metadata =======
Key                Value
---                -----
created_time       2024-04-13T07:17:52.358823412Z
custom_metadata    <nil>
deletion_time      n/a
destroyed          false
version            1

==== Data ====
Key      Value
---      -----
mykey    myvalue
```

Exit the pod's shell.


## Configure kubernetes authentication
The kubernetes auth method is used to authenticate with Vault using a token from a kubernetes service account. This mechanism will be used to introduce the Vault token into a Pod.

Enter the vault's pod shell:

```
kubectl exec -it vault-0 -- /bin/sh
```

Enable the kubernetes authentication method:

```
vault auth enable kubernetes
```

During authentication, Vault needs to verify that the service account token that it receives is valid. To do so, it needs to call a verification endpoint in the kubernetes API server.

Configure the internal network address of the kubernetes host:

```
vault write auth/kubernetes/config \
      kubernetes_host="https://$KUBERNETES_PORT_443_TCP_ADDR:443"
```

To enable a client to read a secret from the path where we stored the secret (demo-vault/demo-secrets/demo-user), we need to grant it the read capability. We do that with a policy. The policy is named 'internal-app':

```
vault policy write internal-app - <<EOF
path "demo-vault/data/demo-secrets/demo-user" {
   capabilities = ["read"]
}
EOF

```

Be aware of the 'data' within the path. 

Create a kubernetes role that connects a kubernetes service account 'internal-app' with the vault policy 'internal-app' that we just created:

```
vault write auth/kubernetes/role/internal-app \
      bound_service_account_names=internal-app \
      bound_service_account_namespaces=default \
      policies=internal-app \
      ttl=24h
```

Once a client authenticates with vault and receives a token, it will be valid for 24 hours.

Exit the pod's shell.

Create the service account mentioned in the created role:

```
kubectl create sa internal-app
```

## Deploy the application

The following command will deploy the application:

```
kubectl apply -f deployment.yaml
```

This deployment contains 3 annotations:

```
vault.hashicorp.com/agent-inject: 'true'
vault.hashicorp.com/role: 'internal-app'
vault.hashicorp.com/agent-inject-secret-database-config.txt: 'demo-vault/data/demo-secrets/demo-user'
```

- agent-inject: Enables the Vault Agent injector service
- role: Specifies the role we created
- agent-inject-secret...: The path of the secret we want to inject

Again, verify that 'data' is included in the path. Otherwise, the pod will fail with a CrashLoopBackOff error.

Wait until the pod is ready:

```
NAME                                   READY   STATUS    RESTARTS
demo-app-7bf7dc5958-9r4f8              2/2     Running   0
vault-0                                1/1     Running   1
vault-agent-injector-dbfc5cd77-lbh5f   1/1     Running   1
```

Display the secret written into the container:

```
kubectl exec \
      $(kubectl get pod -l app=demo-app -o jsonpath="{.items[0].metadata.name}") \
      --container demo-app -- cat /vault/secrets/database-config.txt
```

Or you can log into the demo-app container and read the file:

```
cat /vault/secrets/database-config.txt
```

The result is:

```
data: map[mykey:myvalue]
metadata: map[created_time:2024-04-13T07:17:52.358823412Z custom_metadata:<nil> deletion_time: destroyed:false version:1]
```

## Format the secret output
If you want it to be usable for the application, you can apply a template to the secret. For example, to build the connection string for a database. In that case, change the last annotation in the deployment file:

```
vault.hashicorp.com/agent-inject-template-database-config.txt: |
            {{- with secret "demo-vault/data/demo-secrets/demo-user" -}}
            postgresql://{{ .Data.data.username }}:{{ .Data.data.password }}@postgres:5432/testdb
            {{- end -}}
```

This would output the secret as:

```
postgresql://my-user-value:my-password-value@postgres:5432/testdb
```

