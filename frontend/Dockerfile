# frontend/Dockerfile
# syntax=docker/dockerfile:1

FROM gcr.io/distroless/nodejs20-debian12:nonroot
WORKDIR /app

ENV NODE_ENV=production \
    PORT=4173

# only runtime bits — produced by the CI "frontend-build" step
COPY package*.json ./
COPY build ./build
COPY node_modules ./node_modules

EXPOSE 4173
USER nonroot
# distroless: ENTRYPOINT ["node"]; this runs "node build"
CMD ["build"]