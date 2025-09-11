# syntax=docker/dockerfile:1.4
FROM mirror.gcr.io/library/node:20

WORKDIR /usr/src/app

# 1) Install deps (cached on lockfile changes)
COPY package.json package-lock.json ./
COPY .npmrc .npmrc
ENV NODE_OPTIONS="--max-old-space-size=2048" \
    npm_config_fund=false \
    npm_config_audit=false \
    npm_config_progress=false

RUN --mount=type=cache,target=/root/.npm npm ci

# 2) Copy the rest and build
COPY . .
RUN npm run build

EXPOSE 4173
CMD ["npm","run","preview","--","--host","0.0.0.0","--port","4173"]