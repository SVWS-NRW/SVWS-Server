#!/bin/bash

CURRENT_SCRIPT=$(readlink -f "$0")
CURRENT_DIR=$(dirname "$CURRENT_SCRIPT")

# Testdatenbanken von github als zip-File herunterladen
echo "Lade Testdatenbanken herunter ..."
wget https://github.com/SVWS-NRW/SVWS-TestMDBs/archive/refs/heads/main.zip -O $CURRENT_DIR/databases.zip

# Access-Datenbanken aus den heruntergeladenen zip-File extrahieren
echo "Extrahiere Access-Datenbanken ..."
unzip $CURRENT_DIR/databases.zip -d $CURRENT_DIR/databases "*.mdb"
rm -rf $CURRENT_DIR/databases.zip

# Import (MigrateDB) der Access-Datenbank(en) durchführen ...
MDBFILE="$CURRENT_DIR/databases/SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.mdb"
echo "Importiere Datenbank: ${MDBFILE} ..."
java -cp "svws-server-app-*.jar:./*:./lib/*" de.nrw.schule.svws.db.utils.app.MigrateDB -j -d -r -1 -sd "MDB" \
   -sl "${MDBFILE}" -sp "${TESTDB_PASSWORD}" \
   -td "MARIA_DB" \
   -tl ${MYSQL_HOST} \
   -ts ${MYSQL_DATABASE} \
   -tu ${MYSQL_USER} \
   -tp ${MYSQL_PASSWORD} \
   -tr ${MYSQL_ROOT_PASSWORD}