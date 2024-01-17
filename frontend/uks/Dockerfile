FROM node:20
RUN mkdir /app
COPY /frontend/uks /app
RUN cd /app && npm install && npm run build
EXPOSE 4200

CMD ["npm", "start"]