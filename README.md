
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

> Namespaces we use: `dev`, `uat`, `main`, and **`db`** (Oracle).

### Pause (scale everything to 0 + remove LBs)

**PowerShell**

```powershell
$namespaces = @('dev','uat','main','db')

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