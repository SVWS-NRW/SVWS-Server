#!/bin/bash

CURRENT_SCRIPT=$(readlink -f "$0")
CURRENT_DIR=$(dirname "$CURRENT_SCRIPT")

# Testdatenbanken von github als zip-File herunterladen
echo "Lade Testdatenbanken herunter ..."
wget https://github.com/SVWS-NRW/SVWS-TestMDBs/archive/refs/heads/main.zip -O $CURRENT_DIR/databases.zip

# Access-Datenbanken aus den heruntergeladenen zip-File extrahieren
echo "Extrahiere Access-Datenbanken ..."
unzip $CURRENT_DIR/databases.zip -d $CURRENT_DIR/databases "*.mdb" "*.sqlite"
rm -rf $CURRENT_DIR/databases.zip

# Import (MigrateDB) der Access-Datenbank(en) durchführen ...
SQLITEFILE="$CURRENT_DIR/databases/SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.sqlite"
echo "Importiere Datenbank: ${SQLITEFILE} ..."
java -cp "svws-server-app-*.jar:./*:./lib/*" de.svws_nrw.db.utils.app.ImportDB -j \
   -f ${SQLITEFILE}\
   -td "MARIA_DB" \
   -tl ${MariaDB_HOST} \
   -ts ${MariaDB_DATABASE} \
   -tu ${MariaDB_USER} \
   -tp ${MariaDB_PASSWORD} \
   -tr ${MariaDB_ROOT_PASSWORD}

