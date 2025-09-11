# syntax=docker/dockerfile:1.4

FROM node:20
WORKDIR /usr/src/app

# No build-time API vars — we read at runtime via $env/dynamic/public
COPY . .

RUN --mount=type=cache,target=/root/.npm \
    npm ci --no-audit --no-fund --loglevel=verbose \
 || (echo '--- NPM ERROR LOGS ---' \
     && (ls -1 /root/.npm/_logs || true) \
     && (cat /root/.npm/_logs/* || true) \
     && exit 1)

RUN --mount=type=cache,target=/root/.npm npm ci
RUN npm run build

EXPOSE 4173
CMD ["npm", "run", "preview", "--", "--host", "0.0.0.0", "--port", "4173"]