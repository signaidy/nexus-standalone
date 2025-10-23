pipeline {
  agent {
    kubernetes {
      label 'kaniko-kubectl'
      yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    run: jenkins-kaniko
spec:
  serviceAccountName: jenkins
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:v1.23.2-debug
    command: ["sleep"]
    args: ["99d"]
    tty: true
    resources:
      requests:
        cpu: "1000m"
        memory: "2Gi"
        ephemeral-storage: "10Gi"
      limits:
        cpu: "2000m"
        memory: "4Gi"
        ephemeral-storage: "20Gi"
  - name: node
    image: node:20-bullseye
    command: ["sleep"]
    args: ["99d"]
    tty: true
    env:
    - name: npm_config_fund
      value: "false"
    - name: npm_config_audit
      value: "false"
    - name: npm_config_progress
      value: "false"
    - name: npm_config_fetch_retries
      value: "8"
    - name: npm_config_fetch_retry_mintimeout
      value: "30000"
    - name: npm_config_fetch_retry_maxtimeout
      value: "180000"
    - name: npm_config_network_timeout
      value: "600000"
    - name: NODE_OPTIONS
      value: "--max-old-space-size=1536"
    resources:
      requests:
        cpu: "1000m"
        memory: "2Gi"
        ephemeral-storage: "8Gi"
      limits:
        cpu: "2000m"
        memory: "4Gi"
        ephemeral-storage: "16Gi"
  - name: kubectl
    image: bitnami/kubectl:1.29.7
    command: ["sleep"]
    args: ["99d"]
    tty: true
    resources:
      requests:
        cpu: "500m"
        memory: "512Mi"
        ephemeral-storage: "2Gi"
      limits:
        cpu: "2000m"
        memory: "1Gi"
        ephemeral-storage: "4Gi"
"""
    }
  }

  environment {
    PROJECT = 'spectra-kube'
    REGION  = 'us-central1'
    REPO    = 'nexus'
    STAMP   = "${new Date().format('yyyyMMdd-HHmmss', TimeZone.getTimeZone('UTC'))}"
    NS      = "${(BRANCH_NAME?:'dev') == 'main' ? 'main' : ((BRANCH_NAME?:'dev') == 'uat' ? 'uat' : 'dev')}"
    BACK_IMG  = "${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/nexus-backend:${GIT_COMMIT}"
    FRONT_IMG = "${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/nexus-frontend:${GIT_COMMIT}"
  }

  triggers { pollSCM('H/2 * * * *') }
  options { timestamps() }

  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Build backend (Kaniko)') {
      when { anyOf { branch 'dev'; branch 'uat'; branch 'main' } }
      steps {
        container('kaniko') {
          sh '''
            /kaniko/executor \
              --verbosity=info \
              --context=dir://${WORKSPACE}/backend \
              --dockerfile=${WORKSPACE}/backend/Dockerfile \
              --destination=${BACK_IMG} \
              --cache=true \
              --cache-ttl=168h \
              --snapshot-mode=time \
              --single-snapshot \
              --compressed-caching=false \
              --use-new-run \
              --build-arg=COMMIT_SHA=${GIT_COMMIT}
          '''
        }
      }
    }

    stage('Frontend build (Node)') {
      when { anyOf { branch 'dev'; branch 'uat'; branch 'main' } }
      steps {
        container('node') {
          sh '''
            set -eux
            cd frontend
            npm ci --no-audit --no-fund --loglevel=warn
            npm run build
            npm prune --omit=dev --omit=optional || true
            npm dedupe --omit=dev || true
            ls -lh build || true
            du -sh build node_modules || true
          '''
        }
      }
    }

    stage('Build frontend (Kaniko)') {
      when { anyOf { branch 'dev'; branch 'uat'; branch 'main' } }
      steps {
        container('kaniko') {
          sh '''
            /kaniko/executor \
              --verbosity=debug \
              --context=dir://${WORKSPACE}/frontend \
              --dockerfile=${WORKSPACE}/frontend/Dockerfile \
              --destination=${FRONT_IMG} \
              --cache=true \
              --cache-ttl=168h \
              --snapshot-mode=time \
              --single-snapshot \
              --compressed-caching=false \
              --use-new-run \
              --build-arg=COMMIT_SHA=${GIT_COMMIT}
          '''
        }
      }
    }

    stage('Pin images in Kustomize') {
      when { anyOf { branch 'dev'; branch 'uat'; branch 'main' } }
      steps {
        container('kubectl') {
          sh '''
            set -eux
            cd k8s/overlays/${NS}
            curl -sL https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh | bash
            ./kustomize edit set image ${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/nexus-backend=${BACK_IMG}
            ./kustomize edit set image ${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/nexus-frontend=${FRONT_IMG}
            ./kustomize build . | grep 'image:' || true
          '''
        }
      }
    }

    stage('Deploy to K8s') {
      when { anyOf { branch 'dev'; branch 'uat'; branch 'main' } }
      steps {
        container('kubectl') {
          sh '''
            set -eux
            cd k8s/overlays/${NS}
            ./kustomize build . | kubectl apply -f -
            kubectl -n ${NS} rollout status deploy/frontend --timeout=600s
            kubectl -n ${NS} rollout status deploy/backend  --timeout=600s
          '''
        }
      }
    }

    stage('Smoke (in-cluster)') {
      when { anyOf { branch 'dev'; branch 'uat'; branch 'main' } }
      steps {
        container('kubectl') {
          sh '''
            set -eux
            kubectl -n ${NS} run chk --rm -it --image=curlimages/curl:8.7.1 --restart=Never -- \
              sh -lc "curl -sS -o /dev/null -w '%{http_code}\\n' http://backend.${NS}.svc.cluster.local:8080/nexus/healthz"
          '''
        }
      }
    }
  }

  post {
    always {
      echo "Built & deployed -> NS=${env.NS}"
      echo "Images: BACK=${env.BACK_IMG} | FRONT=${env.FRONT_IMG}"
    }
  }
}