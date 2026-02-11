FROM mysql:8.0

# Copy your backup script into the container
COPY deployment/docker/backup/backup.sh .
RUN chmod +x backup.sh

# Set environment variables from Docker Compose at runtime
ENV MARIADB_USER=root
ENV MARIADB_PASSWORD=root

# Run the backup script
ENTRYPOINT ["/bin/sh", "backup.sh"]
