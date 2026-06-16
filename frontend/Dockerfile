FROM node:18-alpine as builder

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build
FROM node:18-alpine
RUN npm install -g http-server
WORKDIR /app
COPY --from=builder /app/dist/loan-request-frontend ./dist/loan-request-frontend
EXPOSE 81
CMD ["http-server", "dist/loan-request-frontend", "-p", "80", "-c-1"]
