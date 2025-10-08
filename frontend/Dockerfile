# syntax=docker/dockerfile:1.6

FROM node:20-bullseye AS build
WORKDIR /app

# keep npm fast + quiet
ENV npm_config_fund=false \
    npm_config_audit=false \
    npm_config_progress=false \
    npm_config_fetch_retries=8 \
    npm_config_fetch_retry_mintimeout=30000 \
    npm_config_fetch_retry_maxtimeout=180000 \
    npm_config_network_timeout=600000 \
    # ↓ cap Node heap to ~1.5 GiB so it doesn't OOM your builder
    NODE_OPTIONS=--max-old-space-size=1536

COPY package*.json ./
RUN npm ci --no-audit --no-fund --loglevel=warn

# copy sources and build
COPY . .
RUN npm run build -- --force \
 && npm prune --omit=dev --omit=optional \
 && npm dedupe --omit=dev || true

FROM gcr.io/distroless/nodejs20-debian12:nonroot
WORKDIR /app
ENV NODE_ENV=production PORT=4173

COPY --from=build /app/package*.json ./
COPY --from=build /app/build ./build
COPY --from=build /app/node_modules ./node_modules

EXPOSE 4173
USER nonroot
CMD ["build"]