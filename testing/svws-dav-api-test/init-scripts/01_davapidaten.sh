#!/bin/bash

echo "Setze Admin und Lehrerpasswort"
mariadb -h svws-dav-api-test-db -u ${MariaDB_USER} -p${MariaDB_PASSWORD} < ${INIT_SCRIPTS_DIR}/davapidaten.sql