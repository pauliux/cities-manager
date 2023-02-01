# Base image
FROM node
# Copy package json and install dependencies
COPY package*.json .
RUN npm install
# Copy our app
COPY . .
# Expose port to access server
ENV PORT 3000
EXPOSE 3000
# Command to run our app
CMD [ "npm", "start" ]