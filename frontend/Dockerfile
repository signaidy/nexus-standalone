# syntax=docker/dockerfile:1
FROM gcr.io/distroless/nodejs20-debian12:nonroot
WORKDIR /app

ENV NODE_ENV=production \
    PORT=4173

# keep metadata for Node resolution
COPY package.json package-lock.json ./

# drop in prebuilt app + prepruned deps (from the Drone build step)
ADD build.tgz ./
ADD node_modules.tgz ./

EXPOSE 4173
USER nonroot
CMD ["/nodejs/bin/node", "build"]