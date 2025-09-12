# syntax=docker/dockerfile:1
FROM gcr.io/distroless/nodejs20-debian12:nonroot
WORKDIR /app

ENV NODE_ENV=production \
    PORT=4173

COPY package.json package-lock.json ./
ADD build.tgz ./
ADD node_modules.tgz ./

EXPOSE 4173
USER nonroot
# distroless already sets ENTRYPOINT=["node"], so this runs: node build
CMD ["build"]