# ---------- Stage 1: Build Angular App ----------
FROM node:18 AS builder

WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy the entire project
COPY . .

# Build Angular app for production (pass args correctly)
RUN npm run build -- --configuration production

# ---------- Stage 2: Serve with Nginx ----------
FROM nginx:alpine

# Remove default Nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Configure Nginx for Angular SPA on port 4200
RUN echo 'server { \
    listen 4200; \
    server_name localhost; \
    root /usr/share/nginx/html; \
    index index.html; \
    location / { try_files $uri $uri/ /index.html; } \
}' > /etc/nginx/conf.d/default.conf

# Copy built Angular files from builder stage
COPY --from=builder /app/dist/demo /usr/share/nginx/html

# Expose port 4200
EXPOSE 4200

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
