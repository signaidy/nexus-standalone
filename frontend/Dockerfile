# syntax=docker/dockerfile:1.6

FROM node:20-bullseye AS build
WORKDIR /app

# Speed & reliability knobs
ENV npm_config_fund=false \
    npm_config_audit=false \
    npm_config_progress=false \
    npm_config_fetch_retries=8 \
    npm_config_fetch_retry_mintimeout=30000 \
    npm_config_fetch_retry_maxtimeout=180000 \
    npm_config_network_timeout=600000

# Install deps first for better layer cache
COPY package*.json ./
RUN npm ci

# Copy sources and build
COPY . .
RUN npm run build \
 && npm prune --omit=dev --omit=optional \
 && npm dedupe --omit=dev || true

# ---- runtime image
FROM gcr.io/distroless/nodejs20-debian12:nonroot
WORKDIR /app

ENV NODE_ENV=production \
    PORT=4173

# copy exactly what we need
COPY --from=build /app/package*.json ./
COPY --from=build /app/build ./build
COPY --from=build /app/node_modules ./node_modules

EXPOSE 4173
USER nonroot
# distroless has ENTRYPOINT ["node"], so this runs: node build
CMD ["build"]