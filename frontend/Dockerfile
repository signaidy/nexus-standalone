# syntax=docker/dockerfile:1
FROM mirror.gcr.io/library/node:20-alpine
WORKDIR /usr/src/app

ENV NODE_ENV=production \
    NPM_CONFIG_FUND=false \
    NPM_CONFIG_AUDIT=false \
    NPM_CONFIG_PROGRESS=false \
    NPM_CONFIG_FETCH_RETRIES=5 \
    NPM_CONFIG_FETCH_RETRY_MINTIMEOUT=20000 \
    NPM_CONFIG_FETCH_RETRY_MAXTIMEOUT=120000 \
    NPM_CONFIG_NETWORK_TIMEOUT=600000 \
    NPM_CONFIG_CACHE=/root/.npm

# Install deps from lockfile
COPY package.json package-lock.json ./
RUN npm ci --no-audit --no-fund --prefer-offline --progress=false

# App sources
COPY src/ ./src/
COPY static/ ./static/
COPY svelte.config.js .
COPY vite.config.ts .
COPY tsconfig.json .
COPY tailwind.config.* postcss.config.* ./

RUN npm run build

# svelte-kit preview for test envs
EXPOSE 4173
CMD ["npm","run","preview","--","--host","0.0.0.0","--port","4173"]
