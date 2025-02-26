#!/bin/bash

# Konfigurationsdatei generieren
if [[ ! -f /opt/app/svws/svwsconfig.json ]]; then
    echo "Konfigurationsdatei nicht vorhanden. Erstelle Konfigurationsdatei..."
	envsubst < /etc/app/svws/conf/svwsconfig-template.json > /opt/app/svws/svwsconfig.json
else
    echo "Konfigurationsdatei bereits vorhanden."
fi


# Testdatenbank importieren
if [[ -d $INIT_SCRIPTS_DIR ]]; then
    echo "INIT_SCRIPTS_DIR: $INIT_SCRIPTS_DIR"
	for f in "$INIT_SCRIPTS_DIR"/*.sh; do
	  echo "Starte Shell script: $f"
	  /bin/bash "$f"
	done
fi

# prüfen ob MariaDB läuft und ggf. warten
for seconds in {1..60};
do 
	STARTED=$(mysqladmin ping -h $$MARIADB_HOST -u $$MARIADB_USER --password=$$MARIADB_PASSWORD | grep "mysqld is alive")
	if [ "$STARTED" != "" ]; then
		echo "MariaDB is running"
		break
	else
		sleep 1
	fi
done

# SVWS-Server starten
echo "Starte SVWS-Server ..."
java -cp "svws-server-app-*.jar:./*:./lib/*" de.svws_nrw.server.jetty.Main
