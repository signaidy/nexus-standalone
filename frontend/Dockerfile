# syntax=docker/dockerfile:1.4

# 1) Install deps in their own layer using only the lockfiles for best cache reuse
FROM node:20 AS deps
WORKDIR /usr/src/app

# If you use a private registry token via .npmrc, copy it too
COPY package.json package-lock.json ./
COPY .npmrc .npmrc

# Keep npm/Node memory in check and turn off extras
ENV NODE_OPTIONS="--max-old-space-size=2048" \
    npm_config_fund=false \
    npm_config_audit=false \
    npm_config_progress=false

RUN --mount=type=cache,target=/root/.npm npm ci

# 2) Build
FROM node:20 AS build
WORKDIR /usr/src/app
COPY --from=deps /usr/src/app/node_modules ./node_modules
COPY . .
RUN npm run build

# 3) Runtime: serve static files only
FROM node:20-alpine AS runner
WORKDIR /usr/src/app
RUN npm i -g serve
COPY --from=build /usr/src/app/dist ./dist
EXPOSE 4173
CMD ["serve", "-s", "dist", "-l", "4173"]