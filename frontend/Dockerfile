# syntax=docker/dockerfile:1
FROM gcr.io/distroless/nodejs20-debian12:nonroot
WORKDIR /app
ENV NODE_ENV=production PORT=4173

COPY package*.json ./
COPY build ./build
COPY node_modules ./node_modules

EXPOSE 4173
USER nonroot
CMD ["build"]
