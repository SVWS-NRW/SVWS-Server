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

# SVWS-Server starten
echo "Starte SVWS-Server ..."
java -cp "svws-server-app-*.jar:./*:./lib/*" de.svws_nrw.server.jetty.Main
