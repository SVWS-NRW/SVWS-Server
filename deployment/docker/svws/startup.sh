#!/bin/bash

# Wechsle in das Verzeichnis mit der Konfiguration von wo aus der Server gestartet wird
cd conf

# Konfigurationsdatei generieren, wenn nicht vorhanden
if [[ ! -s /opt/app/svws/conf/svwsconfig.json ]];  then
    echo "Konfigurationsdatei '/opt/app/svws/conf/svwsconfig.json' nicht vorhanden. Erstelle Konfigurationsdatei..."
	if [ -z "${MARIADB_DATABASE}" ] || [ -z "${MARIADB_USER}" ] || [ -z "${MARIADB_PASSWORD}" ]; then
		echo "Erstelle Konfigurationsdatei ohne Datenbankschema. Verwenden Sie den Admin-Client."
		envsubst < /opt/app/svws/svwsconfig-template-nodb.json > /opt/app/svws/conf/svwsconfig.json
	else
		echo "Erstelle Konfigurationsdatei mit dem angegebenen Datenbankschema."
		envsubst < /opt/app/svws/svwsconfig-template.json > /opt/app/svws/conf/svwsconfig.json
	fi
else
    echo "Konfigurationsdatei vorhanden."
fi

# Keystore erzeugen, wenn nicht vorhanden
if [[ ! -s ${SVWS_TLS_KEYSTORE_PATH}/keystore ]]; then
	echo "Keystore '${SVWS_TLS_KEYSTORE_PATH}/keystore' nicht vorhanden. Erstelle Keystore..."
	echo "dname CN=$SVWS_TLS_CERT_CN, OU=$SVWS_TLS_CERT_OU, O=$SVWS_TLS_CERT_O, L=$SVWS_TLS_CERT_L, S=$SVWS_TLS_CERT_S, C=$SVWS_TLS_CERT_C"
	keytool -genkey -noprompt -alias ${SVWS_TLS_KEY_ALIAS} -dname "CN=$SVWS_TLS_CERT_CN, OU=$SVWS_TLS_CERT_OU, O=$SVWS_TLS_CERT_O, L=$SVWS_TLS_CERT_L, S=$SVWS_TLS_CERT_S, C=$SVWS_TLS_CERT_C" -keystore ${SVWS_TLS_KEYSTORE_PATH}/keystore -storepass ${SVWS_TLS_KEYSTORE_PASSWORD} -keypass ${SVWS_TLS_KEYSTORE_PASSWORD} -keyalg RSA
else
	echo "Keystore vorhanden."
fi

# Testdatenbank importieren
if [ "$IMPORT_TEST_DATA" = "true" ]; then
    echo "Importiere Testdatenbank"
	CURRENT_SCRIPT=$(readlink -f "$0")
	CURRENT_DIR=$(dirname "$CURRENT_SCRIPT")

	# Testdatenbanken von github als zip-File herunterladen
	echo "Lade Testdatenbanken herunter ..."
	wget https://github.com/SVWS-NRW/SVWS-TestMDBs/archive/refs/heads/main.zip -O $CURRENT_DIR/databases.zip -o /dev/null

	# SQLITE-Datenbanken aus den heruntergeladenen zip-File extrahieren
	echo "Extrahiere SQLITE-Datenbanken ..."
	unzip -q $CURRENT_DIR/databases.zip -d $CURRENT_DIR/databases "*.sqlite"
	rm -rf $CURRENT_DIR/databases.zip

	# Import (MigrateDB) der SQLite-Datenbank durchführen ...
	SQLITEFILE="$CURRENT_DIR/databases/SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.sqlite"
	echo "Importiere Datenbank: ${SQLITEFILE} ..."
	java -cp "../svws-server-app.jar:../*:../lib/*" de.svws_nrw.db.utils.app.ImportDB -j -r -1 -td "MARIA_DB" \
  		-f ${SQLITEFILE} \
  		-tl ${MARIADB_HOST} \
  		-ts ${MARIADB_DATABASE} \
  		-tu ${MARIADB_USER} \
  		-tp ${MARIADB_PASSWORD} \
  		-tr ${MARIADB_ROOT_PASSWORD}

	# Aufräumen
	rm -rf $CURRENT_DIR/databases
fi

# SVWS-Server starten
echo "Starte SVWS-Server ..."
JAR_FILE=$(ls /opt/app/svws/*.jar 2>/dev/null | head -n 1)

if [[ -z "$JAR_FILE" ]]; then
    echo "No JAR file found!"
    exit 1
fi

java -cp "$JAR_FILE:../*:../lib/*" de.svws_nrw.server.jetty.Main
