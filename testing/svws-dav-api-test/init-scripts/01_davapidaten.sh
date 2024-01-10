#!/bin/bash

echo "Setze Admin und Lehrerpasswort"
mariadb -h $MariaDB_HOST -u ${MariaDB_USER} -p${MariaDB_PASSWORD} < ${INIT_SCRIPTS_DIR}/davapidaten.sql
