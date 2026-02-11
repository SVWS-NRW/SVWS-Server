# ==== Stage 1: Build complete server distribution ====
FROM gradle:8.5-jdk21 AS build-stage
WORKDIR /app

# Copy entire repo
COPY . .

# Prepare all artifacts used in the runtime image (skip tests to keep build fast)
RUN gradle :deployment:docker:copyDockerClientArtifacts -x test --no-daemon


# ==== Stage 2: Build Frontend ====
FROM node:20 AS frontend-builder
WORKDIR /app

COPY package*.json ./
COPY tsconfig.base.json ./
COPY eslint.config.js ./

COPY svws-webclient/client ./svws-webclient/client
COPY svws-webclient/admin ./svws-webclient/admin
COPY svws-webclient/core ./svws-webclient/core
COPY svws-webclient/ui ./svws-webclient/ui
COPY svws-webclient/enmserver ./svws-webclient/enmserver

ARG GIT_COMMIT=fallback
RUN echo "export const githash = '${GIT_COMMIT}';" > svws-webclient/client/githash.ts \
 && echo "export const githash = '${GIT_COMMIT}';" > svws-webclient/admin/githash.ts

RUN npm ci --include=dev
RUN npm install -D vite@7.2.2 rollup@^4 esbuild typescript vue-tsc \
    @vitejs/plugin-vue unplugin-vue-components unplugin-vue-markdown \
    @tailwindcss/vite tailwindcss @tailwindcss/typography remixicon

RUN npm run -w @svws-nrw/svws-client build
RUN npm run -w @svws-nrw/svws-admin-client build


# ==== Stage 3: Final Runtime Image ====
FROM eclipse-temurin:21
WORKDIR /opt/app/svws

# Install dependencies for startup.sh
RUN apt-get update && apt-get install -y gettext unzip wget && rm -rf /var/lib/apt/lists/*

# Create folders
RUN mkdir -p client adminclient conf init logs

# Copy server jars and dependency libs prepared by Gradle
COPY --from=build-stage /app/deployment/docker/build/svws/app/svws-*.jar ./
COPY --from=build-stage /app/deployment/docker/build/svws/app/lib ./lib

# Copy frontend outputs from dedicated build stage
COPY --from=frontend-builder /app/svws-webclient/client/build/output ./client
COPY --from=frontend-builder /app/svws-webclient/admin/build/output ./adminclient

# Copy configs and startup script
COPY deployment/docker/svws/svwsconfig-template.json .
COPY deployment/docker/svws/svwsconfig-template-nodb.json .
COPY deployment/docker/svws/startup.sh .
RUN chmod +x startup.sh

# Ensure dynamic JAR detection in startup
WORKDIR /opt/app/svws

ENTRYPOINT ["bash", "startup.sh"]
