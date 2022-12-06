#!/bin/bash

# Konfigurationsdatei generieren
envsubst < /etc/app/svws/conf/svwsconfig-template.json > /opt/app/svws/svwsconfig.json

# Testdatenbank importieren
if [ "$INIT_TESTDB_ON_STARTUP" = "true" ]; then
  echo "Testdaten-Import: AKTIVERT"
  MDBFILE=$(ls /opt/app/svws/data/*.mdb| head -1)
  
  echo "Starte Testdaten-Import ..."
  java -cp "svws-server-app-*.jar:./*:./lib/*" de.nrw.schule.svws.db.utils.app.MigrateDB -j -d -r -1 -sd "MDB" \
       -sl "${MDBFILE}" -sp "${TESTDB_PASSWORD}" \
       -td "MARIA_DB" \
       -tl ${MYSQL_HOST} \
       -ts ${MYSQL_DATABASE} \
       -tu ${MYSQL_USER} \
       -tp ${MYSQL_PASSWORD} \
       -tr ${MYSQL_ROOT_PASSWORD}
else
  echo "Testdaten-Import: DEAKTIVERT"
fi

# SVWS-Server starten
echo "Starte SVWS-Server ..."
java -cp "svws-server-app-*.jar:./*:./lib/*" de.nrw.schule.svws.server.jetty.Main