#!/bin/bash
set -e
set +H  # disable ! history expansion

while true; do
  DATE=$(date +%Y-%m-%d_%H-%M-%S)
  echo "Backing up all databases..."

  mysqldump -h mariadb -u"$MARIADB_USER" -p"$MARIADB_PASSWORD" \
            --all-databases --single-transaction --routines --triggers --events \
            --skip-lock-tables --column-statistics=0 \
            > /backups/all_databases_$DATE.sql

  DBS=$(mysql -h mariadb -u"$MARIADB_USER" -p"$MARIADB_PASSWORD" -sNe "SHOW DATABASES;" | grep -Ev '^(mysql|information_schema|performance_schema|sys)$')

  while IFS= read -r db; do
    OUT=$(echo "$db" | tr ' ' '_')
    echo "Backing up '$db' individually..."
    mysqldump -h mariadb -u"$MARIADB_USER" -p"$MARIADB_PASSWORD" \
              --single-transaction --routines --triggers --events \
              --databases "$db" --skip-lock-tables --column-statistics=0 \
              > /backups/${OUT}_$DATE.sql
    sleep 2
  done <<< "$DBS"

  echo "Cleaning old backups..."
  ls -1tr /backups/*.sql | head -n -10 | xargs -r rm -f

  echo "Next backup in 1 week..."
  sleep 604800
done
