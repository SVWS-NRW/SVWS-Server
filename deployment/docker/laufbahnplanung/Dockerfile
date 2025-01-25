# Stage 1: Build
FROM eclipse-temurin:21 AS build

RUN apt update && apt-get -y upgrade && apt-get -y install gettext zip && apt autoclean

RUN mkdir -p /opt/app/svws/client && mkdir /opt/app/svws/conf

COPY app /opt/app/svws

RUN unzip -d /opt/app/svws/laufbahnplanung /opt/app/svws/SVWS-Laufbahnplanung*.zip && rm -rf /opt/app/svws/SVWS-Laufbahnplanung*.zip

# Stage 2: Runtime
FROM nginx:latest

# Copy the built files from the previous stage
COPY --from=build /opt/app/svws/laufbahnplanung /usr/share/nginx/html

# Remove the default nginx configuration file
RUN rm /etc/nginx/nginx.conf

# Copy your custom nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
