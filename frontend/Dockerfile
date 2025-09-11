# syntax=docker/dockerfile:1
FROM mirror.gcr.io/library/node:20-alpine
WORKDIR /usr/src/app

# Run as production server on port 4173 to match your k8s probes
ENV NODE_ENV=production \
    PORT=4173

# Copy prebuilt server & client output and pruned production deps
# (These are created by the new Drone "frontend-build" step)
COPY build ./build
COPY package.json package-lock.json ./
COPY node_modules ./node_modules

EXPOSE 4173
# adapter-node runs with: node build
CMD ["node", "build"]
