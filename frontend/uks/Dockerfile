FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:stable-alpine
COPY default.conf /etc/nginx/conf.d
COPY --from=build /app/dist/uks/ /usr/share/nginx/html
# EXPOSE 80