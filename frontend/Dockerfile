# syntax=docker/dockerfile:1
FROM mirror.gcr.io/library/node:20-alpine
WORKDIR /usr/src/app

ENV NODE_ENV=production \
    PORT=4173 \
    npm_config_fund=false \
    npm_config_audit=false \
    npm_config_progress=false \
    npm_config_update_notifier=false

# 1) Cacheable layer for runtime deps
COPY package.json package-lock.json ./

# 2) Copy the app
ADD build.tgz ./
ADD node_modules.tgz ./

EXPOSE 4173
CMD ["node", "build"]