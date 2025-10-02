
# Nexus (Spectra)

Monorepo with:
- `backend/` (Spring Boot)
- `frontend/` (Svelte/Vite)
- `k8s/` (base + overlays for `dev` / `uat` / `main`)
- Oracle DB runs in **`db`** namespace (StatefulSet)
- Container images live in Google Artifact Registry

---

## Prereqs

- `gcloud` CLI authenticated to **project `spectra-kube`**
- `kubectl` pointed at the GKE Autopilot cluster (**`spectra-autopilot`**)
- PowerShell or Bash terminal

```bash
# (helpful defaults)
gcloud config set project spectra-kube
gcloud config set compute/region us-central1
gcloud config set compute/zone us-central1-f
````

---

## Deploy (normal)

```bash
# pick your env
kubectl apply -k k8s/overlays/dev
kubectl apply -k k8s/overlays/uat
kubectl apply -k k8s/overlays/main
```

---

## SonarQube (VM) — start/stop

We host SonarQube on a small Compute Engine VM: **`sonar-vm`** (Debian, Docker Compose).
It uses a **static IP** (name: `sonar-ip`) so the URL won’t change.

### Start the VM (brings Sonar up)

```bash
gcloud compute instances start sonar-vm --zone us-central1-f
# Sonar + Postgres auto-start via Docker restart policy
# Open: http://<STATIC_IP>:9000   (admin/admin on first run)
```

## 🔁 Zero-to-Redeploy (Images via Kustomize) — PowerShell

> Use Kustomize to pin images in the overlay so future `kubectl apply -k ...` never reverts you.

### Prereqs
- `gcloud`, `kubectl`
- **`kustomize` CLI installed**  
  - Chocolatey: `choco install kustomize -y`  
  - Scoop: `scoop install kustomize`
- `kubectl` context pointing to the cluster (namespace e.g. `dev`)

### 0) Common variables

```powershell
# --- GCP / K8s ---
$PROJECT   = "spectra-kube"
$REGION    = "us-central1"
$REPO      = "nexus"
$NS        = "dev"
$STAMP     = (Get-Date).ToUniversalTime().ToString("yyyyMMdd-HHmmss")

# --- Images (new tags minted each time) ---
$BACK_IMG  = "$REGION-docker.pkg.dev/$PROJECT/$REPO/nexus-backend:manual-$STAMP"
$FRONT_IMG = "$REGION-docker.pkg.dev/$PROJECT/$REPO/nexus-frontend:manual-$STAMP"

# --- (Optional) Ingress/LB URL (after Ingress is ready) ---
$IP = "http://<LOAD_BALANCER_IP>"   # e.g. http://34.49.133.125
````

### 1) Build & push images (Cloud Build)

**Backend (Dockerfile under `backend/`):**

```powershell
gcloud builds submit backend --tag $BACK_IMG --timeout=3600s
```

**Frontend (uses `frontend/cloudbuild.yml`, which creates build artifacts and image):**

```powershell
gcloud builds submit `
  --config=frontend/cloudbuild.yml `
  --substitutions=_IMAGE=$FRONT_IMG `
  --timeout=3600s `
  .
```

### 2) Pin images in the overlay (Kustomize = source of truth)

```powershell
Push-Location k8s/overlays/$NS

kustomize edit set image `
  us-central1-docker.pkg.dev/$PROJECT/$REPO/nexus-backend=$BACK_IMG

kustomize edit set image `
  us-central1-docker.pkg.dev/$PROJECT/$REPO/nexus-frontend=$FRONT_IMG

# (Optional) preview the rendered manifests
kubectl kustomize . | Select-String -Pattern 'image:'

Pop-Location
```

> **Why:** Avoids drift. If you only run `kubectl set image ...`, the next `kubectl apply -k ...`
> will snap back to whatever `kustomization.yml` says. Updating via `kustomize edit set image`
> keeps manifests authoritative.

### 3) Apply

```powershell
kubectl apply -k k8s/overlays/$NS
kubectl -n $NS rollout status deploy/backend  --timeout=180s
kubectl -n $NS rollout status deploy/frontend --timeout=180s
```

### 4) Verify the running images (Windows-friendly JSONPath)

```powershell
kubectl -n $NS get deploy backend  -o jsonpath='{.spec.template.spec.containers[?(@.name=="backend")].image}';  echo
kubectl -n $NS get deploy frontend -o jsonpath='{.spec.template.spec.containers[?(@.name=="frontend")].image}'; echo
```

### 5) Quick smoke checks

**In-cluster → backend Service:**

```powershell
kubectl -n $NS run chk --rm -it --image=curlimages/curl:8.7.1 --restart=Never -- `
  sh -lc "curl -sS -o /dev/null -w '%{http_code}\n' http://backend.$NS.svc.cluster.local:8080/nexus/healthz"
```

**Through the LB (after Ingress is healthy):**

```powershell
kubectl -n $NS run chk2 --rm -it --image=curlimages/curl:8.7.1 --restart=Never -- `
  sh -lc "curl -sS -o /dev/null -w '%{http_code}\n' $IP/nexus/healthz"
```

**Frontend env sanity:**

```powershell
kubectl -n $NS get deploy/frontend `
  -o jsonpath="{range .spec.template.spec.containers[?(@.name=='frontend')].env[*]}{.name}={.value}{'\n'}{end}"
# Expect: PUBLIC_BACKEND_URL=/nexus
```

---

### Notes & Tips

* Keep `PUBLIC_BACKEND_URL=/nexus` in the frontend Deployment (overlay sets it).
* Health path used by GCLB: `/nexus/healthz`.
* If you must hot-swap a pod image with `kubectl set image`, **also** run the
  `kustomize edit set image` step so future applies don’t revert you.
  
### 2) Frontend (SvelteKit)

**Build & push (uses `frontend/cloudbuild.yml`, which creates `build.tgz` etc.)**

```powershell
gcloud builds submit `
  --config=frontend/cloudbuild.yml `
  --substitutions=_IMAGE=$FRONT_IMG `
  --timeout=3600s `
  .
```

**Roll out to K8s**

```powershell
kubectl -n $NS set image deploy/frontend frontend=$FRONT_IMG
kubectl -n $NS rollout status deploy/frontend --timeout=180s
```

**Verify env for API base**

```powershell
kubectl -n $NS get deploy/frontend `
  -o jsonpath="{range .spec.template.spec.containers[?(@.name=='frontend')].env[*]}{.name}={.value}{'\n'}{end}"
# Expect: PUBLIC_BACKEND_URL=/nexus
```

**Smoke tests (from your browser / LB)**

* `GET $IP/nexus/aboutus/1` → **200 + JSON**
* Login form should POST to `POST $IP/nexus/auth/login` and set cookies.

---

### 3) Oracle (XE) database

> If you maintain a custom Oracle image, build it; otherwise skip to “Roll out”.

**(Optional) Build & push**

```powershell
# If you have a local Dockerfile under oracle/
gcloud builds submit oracle --tag $ORA_IMG --timeout=3600s
```

**Roll out to K8s**
(choose the matching workload kind/name you use — `statefulset/oracle` is common)

```powershell
# If it's a StatefulSet
kubectl -n $NS set image statefulset/oracle oracle=$ORA_IMG
kubectl -n $NS rollout status statefulset/oracle --timeout=300s

# Or if it's a Deployment
# kubectl -n $NS set image deploy/oracle oracle=$ORA_IMG
# kubectl -n $NS rollout status deploy/oracle --timeout=300s
```

**Connectivity check from backend Pod**

```powershell
kubectl -n $NS exec deploy/backend -- sh -lc \
  "getent hosts oracle.$NS.svc.cluster.local || true; echo $SPRING_DATASOURCE_URL"
```

> Ensure `nexus-backend-config` has the right `DB_URL` (Oracle JDBC) and the Secret has `DB_USER/DB_PASS`.

---

### 4) Drone (CI) — server & runner

> If you deploy Drone in-cluster, this updates the images. Replace with upstream tags if you don’t build custom images:
>
> * Server: `drone/drone:2`
> * Kube runner: `drone/drone-runner-kube:1`

**(Optional) Build & push**

```powershell
# Only if you have local Dockerfiles for Drone:
# gcloud builds submit drone/server  --tag $DRONE_IMG   --timeout=3600s
# gcloud builds submit drone/runner  --tag $RUNNER_IMG  --timeout=3600s
```

**Roll out to K8s**

```powershell
# Server
# (use your actual deployment name; `drone` is typical)
kubectl -n $NS set image deploy/drone drone=$DRONE_IMG
kubectl -n $NS rollout status deploy/drone --timeout=180s

# Runner (Kubernetes runner)
kubectl -n $NS set image deploy/drone-runner drone-runner=$RUNNER_IMG
kubectl -n $NS rollout status deploy/drone-runner --timeout=180s
```

**Secrets to have in place**

* `DRONE_RPC_SECRET`, `DRONE_SERVER_HOST`, `DRONE_SERVER_PROTO`, VCS client/secret (e.g., GitHub), etc., typically provided via `Secret` + `envFrom` in the Deployments.

---

### 5) Troubleshooting quickies

```powershell
# Describe Ingress and watch address assignment
kubectl -n $NS describe ingress nexus
kubectl -n $NS get ingress nexus -w

# NEG status (for GCLB)
kubectl -n $NS get svc backend  -o jsonpath='{.metadata.annotations.cloud\.google\.com/neg-status}'; echo
kubectl -n $NS get svc frontend -o jsonpath='{.metadata.annotations.cloud\.google\.com/neg-status}'; echo

# Check Pods
kubectl -n $NS get pods -o wide
kubectl -n $NS logs deploy/backend   --tail=200
kubectl -n $NS logs deploy/frontend  --tail=200
```

**Common signals**

* `502` from `$IP/nexus/*` but `200` in-cluster → wait for health checks/NEGs or verify `BackendConfig` paths (`/nexus/healthz`).
* `403` on `/nexus/auth/*` → verify Spring Security matchers include `/auth/**` and controllers (or `server.servlet.context-path`) line up with `/nexus`.

### Stop the VM (keeps data and IP)

```bash
gcloud compute instances stop sonar-vm --zone us-central1-f
```

> Data is persisted in Docker named volumes on the VM’s disk:
> `pg`, `sonar_data`, `sonar_extensions`, `sonar_logs`.

### (Optional) manage containers on the VM

```bash
# SSH into the VM
gcloud compute ssh sonar-vm --zone us-central1-f

# Check status
docker compose ps

# Start/Stop stack manually (usually not needed)
docker compose up -d
docker compose down
```

> If you **really** want zero IP cost while stopped, you can release the static IP:
> `gcloud compute addresses delete sonar-ip --region us-central1`
> (You’ll get a new IP next time.)

---

## Pause / Resume cluster workloads (“dockers”) in GKE

Autopilot can’t be “stopped”, but you can drop runtime cost to near-zero by:

* scaling all **Deployments**/**StatefulSets** to **0**
* **suspending CronJobs**
* removing or converting **LoadBalancer** Services (to avoid LB charges)
* keeping PVCs (storage charges remain)

> Namespaces we use: `dev`, `uat`, `main`, `ci` and **`db`** (Oracle).

### Pause (scale everything to 0 + remove LBs)

**PowerShell**

```powershell
$namespaces = @('dev','uat','main','db', 'ci')

# 1) Scale Deployments/StatefulSets to 0
foreach ($ns in $namespaces) {
  kubectl -n $ns scale deploy --all --replicas=0 2>$null
  kubectl -n $ns scale statefulset --all --replicas=0 2>$null
  kubectl -n $ns patch cronjob --all --type merge -p '{\"spec\":{\"suspend\":true}}' 2>$null
}

# 2) Convert any LoadBalancer Services to ClusterIP (avoids LB billing)
$lbs = kubectl get svc -A --field-selector spec.type=LoadBalancer -o json | ConvertFrom-Json
foreach ($s in $lbs.items) {
  $ns = $s.metadata.namespace; $name = $s.metadata.name
  kubectl -n $ns patch svc $name -p '{\"spec\":{\"type\":\"ClusterIP\"}}'
}

# 3) Verify
kubectl get pods -A
kubectl get svc -A --field-selector spec.type=LoadBalancer
```

**Bash (alternative)**

```bash
namespaces=(dev uat main db)

for ns in "${namespaces[@]}"; do
  kubectl -n "$ns" scale deploy --all --replicas=0 || true
  kubectl -n "$ns" scale statefulset --all --replicas=0 || true
  kubectl -n "$ns" patch cronjob --all --type merge -p '{"spec":{"suspend":true}}' || true
done

for ns in "${namespaces[@]}"; do
  for svc in $(kubectl -n "$ns" get svc --field-selector spec.type=LoadBalancer -o name); do
    kubectl -n "$ns" patch "$svc" -p '{"spec":{"type":"ClusterIP"}}'
  done
done

kubectl get pods -A
kubectl get svc -A --field-selector spec.type=LoadBalancer
```

### Resume (bring workloads back)

**Option 1 — Re-apply manifests (recommended)**

```bash
kubectl apply -k k8s/overlays/dev
kubectl apply -k k8s/overlays/uat
kubectl apply -k k8s/overlays/main
```

This recreates Services (including LoadBalancers), Deployments, probes, etc.
Oracle in `db`:

```bash
kubectl -n db scale statefulset --all --replicas=1
kubectl -n db patch cronjob --all --type merge -p '{"spec":{"suspend":false}}' 2>/dev/null || true
```

**Option 2 — Manual scale-up (if you scaled down by hand)**

```bash
# Example: scale everything back to 1
for ns in dev uat main db; do
  kubectl -n "$ns" scale deploy --all --replicas=1 || true
  kubectl -n "$ns" scale statefulset --all --replicas=1 || true
  kubectl -n "$ns" patch cronjob --all --type merge -p '{"spec":{"suspend":false}}' || true
done

# Recreate LBs if you converted them
# (either re-apply your Service manifests or patch back to LoadBalancer)
kubectl -n dev  patch svc frontend-svc -p '{"spec":{"type":"LoadBalancer"}}'   2>/dev/null || true
kubectl -n dev  patch svc backend-svc  -p '{"spec":{"type":"LoadBalancer"}}'   2>/dev/null || true
# ...repeat per namespace as needed
```

```powershell
function Restore-Replicas($ns) {
  $deps = (Get-Content "$ns-deploys.json" | ConvertFrom-Json).items
  foreach ($d in $deps) {
    $name = $d.metadata.name
    $rep  = [int]$d.spec.replicas
    if ($rep -gt 0) { kubectl -n $ns scale deploy $name --replicas $rep }
  }

  $sts = (Get-Content "$ns-sts.json" | ConvertFrom-Json).items
  foreach ($s in $sts) {
    $name = $s.metadata.name
    $rep  = [int]$s.spec.replicas
    if ($rep -gt 0) { kubectl -n $ns scale statefulset $name --replicas $rep }
  }

  # Re-enable CronJobs
  kubectl -n $ns patch cronjob --all --type merge -p '{\"spec\":{\"suspend\":false}}' 2>$null
}

Restore-Replicas dev
Restore-Replicas uat
Restore-Replicas main
```

### Quick checks

```bash
kubectl get pods -A
kubectl get svc -A --field-selector spec.type=LoadBalancer
kubectl -n db get pods   # Oracle
```

---

## CI / Quality Gate (backend only)

* GitHub Actions workflow: `.github/workflows/ci-sonar-backend.yml`
* Runs tests + JaCoCo + Sonar analysis
* Waits for **quality gate**; the job **fails** if the gate fails
* We send a Gmail email on pass/fail for PRs

**Secrets required (GitHub → Settings → Secrets & variables → Actions):**

* `SONAR_HOST_URL` — `http://<STATIC_IP>:9000` (or your HTTPS URL)
* `SONAR_TOKEN` — token from Sonar (My Account → Security → Generate Token)
* `GMAIL_USERNAME`, `GMAIL_APP_PASSWORD`, `ALERT_EMAIL_TO`

**Branch protection**

* For `dev`, `uat`, `main`: Require status check
  `CI - Backend SonarQube / SonarQube Analysis (backend)`
  and restrict who can push so only **you** can merge.

---