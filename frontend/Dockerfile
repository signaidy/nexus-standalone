FROM node:20
WORKDIR /usr/src/app

# No build-time API vars — we read at runtime via $env/dynamic/public
COPY . .
RUN --mount=type=cache,target=/root/.npm npm ci
RUN npm run build

EXPOSE 4173
CMD ["npm", "run", "preview", "--", "--host", "0.0.0.0", "--port", "4173"]