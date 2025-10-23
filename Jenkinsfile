pipeline {
    agent {
        kubernetes {
            label 'kaniko-kubectl'
            // do NOT set "defaultContainer" to something that needs 'cat'
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
                    image: gcr.io/kaniko-project/executor:latest
                    tty: true
                    resources:
                    requests:
                        cpu: "250m"
                        memory: "512Mi"
                    limits:
                        cpu: "500m"
                        memory: "1Gi"
                - name: kubectl
                    image: bitnami/kubectl:latest
                    command: ["sleep"]
                    args: ["99d"]
                    tty: true
                    resources:
                    requests:
                        cpu: "50m"
                        memory: "128Mi"
                    limits:
                        cpu: "250m"
                        memory: "256Mi"
                """
        }
    }

  environment {
    PROJECT = 'spectra-kube'
    REGION  = 'us-central1'
    REPO    = 'nexus'
    STAMP   = "${new Date().format('yyyyMMdd-HHmmss', TimeZone.getTimeZone('UTC'))}"

    // Map branch -> namespace
    // Multibranch sets BRANCH_NAME automatically (e.g., dev/uat/main or PR-xxx)
    NS = "${BRANCH_NAME == 'main' ? 'main' : (BRANCH_NAME == 'uat' ? 'uat' : 'dev')}"

    BACK_IMG  = "${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/nexus-backend:manual-${STAMP}"
    FRONT_IMG = "${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/nexus-frontend:manual-${STAMP}"
  }

  triggers {
    // Use polling since Jenkins has no public webhook (free)
    // Disable if you later enable GitHub webhooks
    pollSCM('H/2 * * * *')
  }

  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build backend (Kaniko)') {
      when { anyOf { branch 'dev'; branch 'uat'; branch 'main' } }
      steps {
        container('kaniko') {
          sh '''
            /kaniko/executor \
              --context=${WORKSPACE}/backend \
              --dockerfile=${WORKSPACE}/backend/Dockerfile \
              --destination=${BACK_IMG} \
              --cache=true \
              --use-new-run
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
              --context=${WORKSPACE}/frontend \
              --dockerfile=${WORKSPACE}/frontend/Dockerfile \
              --destination=${FRONT_IMG} \
              --cache=true \
              --use-new-run
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

            # Fetch kustomize (portable install)
            curl -sL https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh | bash

            ./kustomize edit set image \
              ${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/nexus-backend=${BACK_IMG}
            ./kustomize edit set image \
              ${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/nexus-frontend=${FRONT_IMG}

            echo "Rendered images:"
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
            kubectl -n ${NS} rollout status deploy/backend  --timeout=180s
            kubectl -n ${NS} rollout status deploy/frontend --timeout=180s
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