# Usamos Node LTS
FROM node:20

WORKDIR /usr/src/app

# Variable para el build (expuesta al cliente)
ARG VITE_API_BASE_URL
ENV VITE_API_URL=${VITE_API_BASE_URL}

# Copiamos el código
COPY . .

# Instalar deps y construir
RUN --mount=type=cache,target=/root/.npm npm ci
RUN npm run build

# Exponer puerto de preview (Vite usa 4173 por defecto)
EXPOSE 4173

# Ejecutar preview en 0.0.0.0 para accesos externos
CMD ["npm", "run", "preview", "--", "--host", "0.0.0.0", "--port", "4173"]