FROM node:18.20-alpine AS builder

WORKDIR /app

COPY package*.json ./

RUN npm ci --no-audit --no-fund && \
    npm cache clean --force

COPY . .

ARG NODE_ENV=production
ARG VITE_API_BASE_URL=/api
ARG VITE_API_TIMEOUT=10000

ENV NODE_ENV=${NODE_ENV}
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}
ENV VITE_API_TIMEOUT=${VITE_API_TIMEOUT}

RUN npm run build && \
    npm cache clean --force && \
    rm -rf node_modules ~/.npm

FROM nginx:alpine

RUN apk add --no-cache curl dumb-init && \
    rm -rf /var/cache/apk/* /tmp/* /var/tmp/*

COPY --from=builder /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost/ || exit 1

ENTRYPOINT ["/usr/bin/dumb-init", "--"]
CMD ["nginx", "-g", "daemon off;"]