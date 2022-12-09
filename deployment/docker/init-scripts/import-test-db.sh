#!/bin/bash

CURRENT_SCRIPT=$(readlink -f "$0")
CURRENT_DIR=$(dirname "$CURRENT_SCRIPT")

echo "Starte Testdaten-Import ..."
MDBFILE=$(ls "$CURRENT_DIR"/*.mdb| head -1)
java -cp "svws-server-app-*.jar:./*:./lib/*" de.nrw.schule.svws.db.utils.app.MigrateDB -j -d -r -1 -sd "MDB" \
   -sl "${MDBFILE}" -sp "${TESTDB_PASSWORD}" \
   -td "MARIA_DB" \
   -tl ${MYSQL_HOST} \
   -ts ${MYSQL_DATABASE} \
   -tu ${MYSQL_USER} \
   -tp ${MYSQL_PASSWORD} \
   -tr ${MYSQL_ROOT_PASSWORD}