@'
# Nexus (Spectra)

Monorepo with:
- backend (Spring Boot)
- frontend (Svelte/Vite)
- k8s/ (base + overlays for dev/uat/main)
- Oracle runs in `db` namespace (StatefulSet)
- Container images live in Google Artifact Registry

## Deploy
kubectl apply -k k8s/overlays/dev   # or uat/main
'@ | Out-File -Encoding utf8 README.md