# syntax=docker/dockerfile:1.5
FROM mirror.gcr.io/library/node:20-alpine
WORKDIR /usr/src/app

# deps layer
COPY package.json package-lock.json ./

ENV NODE_OPTIONS="--max-old-space-size=2048" \
    npm_config_fund=false \
    npm_config_audit=false \
    npm_config_progress=false
RUN npm ci

# app sources
COPY src/ ./src/
COPY static/ ./static/
COPY svelte.config.js .
COPY vite.config.ts .
COPY tsconfig.json .
# add any other files your build actually needs

RUN npm run build
EXPOSE 4173
CMD ["npm","run","preview","--","--host","0.0.0.0","--port","4173"]