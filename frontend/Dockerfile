# Use LTS version of Node.js on Alpine Linux
FROM node:lts-alpine3.19 AS builder

# Set working directory
WORKDIR /app

# Install Angular CLI globally
RUN npm install -g @angular/cli

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Set default port
ENV FE_DOCKER_PORT 4200

# Expose the port defined in the environment variable
EXPOSE $FE_DOCKER_PORT

# Start the Angular development server
CMD ng serve --host="0.0.0.0" --port=${FE_DOCKER_PORT} --disable-host-check
