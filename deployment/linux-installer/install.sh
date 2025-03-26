#!/bin/bash

if [ -f "/etc/debian_version" ] || [ -f "/etc/debian_release" ]; then
	IS_DEBIAN_LIKE=true;
fi

if ! $IS_DEBIAN_LIKE; then
  echo "Dieses Skript wird nicht auf einem Debian-basierten System ausgeführt. Beende Ausführung..."
  exit 1
fi

if [[ $EUID -ne 0 ]]; then
   echo "Dieses Skript muss als Root ausgeführt werden"
   exit 1
fi

# Überprüfe, ob der Service läuft
if systemctl is-active --quiet svws.service; then
	if [[ "$1" == "--update" ]]; then
		  echo "SVWS ist bereits gestartet. Bende Service ..."
		  systemctl stop svws.service
	else
		   echo "SVWS ist bereits installiert und gestartet! Zum updaten bitte --update verwenden"
           exit 1
	fi
fi

script_dir="$PWD"

# Variablen für Passwortlänge und erlaubte Zeichen
LENGTH=12
CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

# Passwort generieren
password2=$(head /dev/urandom | tr -dc $CHARS | fold -w $LENGTH | head -n 1)
password1=$(head /dev/urandom | tr -dc $CHARS | fold -w $LENGTH | head -n 1)

export CREATE_MARIADB=J
export CREATE_KEYSTORE=J
export CREATE_TESTDATA=J
export MARIADB_ROOT_PASSWORD=${password1}
export MARIADB_HOST=localhost

export APP_PATH=/opt/app/svws
export CONF_PATH=/etc/app/svws/conf

export APP_PORT=8443

export SVWS_TLS_KEYSTORE_PATH=$CONF_PATH/keystore
export SVWS_TLS_KEYSTORE_PASSWORD=test123
export SVWS_TLS_KEY_ALIAS=




if [[ "$1" == "--update" ]]; then

	if [ -f .env ]; then
     export $(grep -v '^#' .env | xargs)
    fi

    if [ ! -f "LINUX_INSTALLER_FILE_NAME" ]; then
        DOWNLOAD_PFAD=BASE_DOWNLOAD_URL/LINUX_INSTALLER_FILE_NAME
    fi

    script_dir="$PWD"

    # SVWS laden und auspacken
    echo "Lade SVWS ..."

    # Wenn DOWNLOAD_PFAD gesetzt ist, lade Datei herunter
    if [ ! -z "$DOWNLOAD_PFAD" ]; then
      echo "Lade Datei herunter von $DOWNLOAD_PFAD..."
      wget -N $DOWNLOAD_PFAD
      echo "Herunterladen abgeschlossen."
    fi

    # SVWS Dienst stoppen
    echo "stoppe SVWS ..."
    systemctl stop svws

    # SVWS Dateien löschen
    echo "lösche SVWS ..."
    rm -r $APP_PATH/app
    rm -r $APP_PATH/client
    rm -r $APP_PATH/adminclient

    # Entpacke die SVWS-Installationsdatei
    tar xzf ./LINUX_INSTALLER_FILE_NAME

    # Erstelle Verzeichnisse
    mkdir -p $APP_PATH
    mkdir $APP_PATH/client

    # Kopiere App
    cp -r ./svws/app $APP_PATH

    # Entpacke den Client in das Client-Verzeichnis
    unzip -d $APP_PATH/client $APP_PATH/app/SVWS-Client*.zip

    # Lösche die entpackte Client-Datei
    rm -rf $APP_PATH/app/SVWS-Client*.zip

    # Entpacke den Admin-Client in das Admin-Client-Verzeichnis
    unzip -d $APP_PATH/adminclient $APP_PATH/app/SVWS-Admin-Client*.zip

    # Lösche die entpackte Admin-Client-Datei
    rm -rf $APP_PATH/app/SVWS-Admin-Client*.zip

    # Erstelle einen symbolischen Link zur Konfigurationsdatei
    ln -sf $CONF_PATH/svwsconfig.json $APP_PATH/svwsconfig.json

    cd $script_dir

    # Lösche das Verzeichnis 'svws' im Home-Verzeichnis
    echo "Lösche das Verzeichnis 'svws' im Home-Verzeichnis..."
    rm -r ./svws

    # Lösche das Verzeichnis 'init-scripts' im Home-Verzeichnis
    echo "Lösche das Verzeichnis 'init-scripts' im Home-Verzeichnis..."
    rm -r ./init-scripts

    # Aktualisieren der Systemd-Konfigurationen und Starten des Services
    # Der Service wird automatisch gestartet, sobald das System hochfährt (systemctl enable)
    systemctl start svws.service

    # Überprüfen des Status des Services
    systemctl status svws.service

	exit 1
fi


if [ -f .env ]; then
 export $(grep -v '^#' .env | xargs)
else

    if [ ! -f "LINUX_INSTALLER_FILE_NAME" ]; then
        DOWNLOAD_PFAD=BASE_DOWNLOAD_URL/LINUX_INSTALLER_FILE_NAME
    fi

    if [[ "$1" == "--default" ]]; then
    	echo "verwende defaults ..."
    else
    	#Konfuguration
    	echo "MariaDB-Konfiguration:"

    	read -p "Möchten Sie MariaDB installieren? (j/N): " CREATE_MARIADB

    	if [ "$CREATE_MARIADB" = "j" ] || [ "$CREATE_MARIADB" = "J" ]; then
    		read -p "MARIADB_ROOT_PASSWORD (default: '${password1}'): " MARIADB_ROOT_PASSWORD
    		export MARIADB_ROOT_PASSWORD=${MARIADB_ROOT_PASSWORD:-${password1}}
    		read -p "MARIADB_HOST (default: 'localhost'): " MARIADB_HOST
    		export MARIADB_HOST=${MARIADB_HOST:-localhost}
    	else
    		read -p "MARIADB_ROOT_PASSWORD: " MARIADB_ROOT_PASSWORD
    		export MARIADB_ROOT_PASSWORD=${MARIADB_ROOT_PASSWORD}
    		read -p "MARIADB_HOST: " MARIADB_HOST
    		export MARIADB_HOST=${MARIADB_HOST}
    	fi

    	echo "Installationspfade:"
    	read -p "APP_PATH (default: '/opt/app/svws'): " APP_PATH
    	export APP_PATH=${APP_PATH:-/opt/app/svws}
    	read -p "CONF_PATH (default: '/etc/app/svws/conf'): " CONF_PATH
    	export CONF_PATH=${CONF_PATH:-/etc/app/svws/conf}

    	read -p "APP_PORT (default: 8443): " APP_PORT
    	export APP_PORT=${APP_PORT:-8443}

    	if [ $APP_PORT -lt 1024 ]; then
            echo "Hinweis: Ports unter 1024 erfordern Root-Rechte und müssen entsprechend freigeschaltet/weitergeleitet werden."
        fi

    	read -p "Möchten Sie einen Keystore erstellen? (j/N): " CREATE_KEYSTORE
    	if [ "$CREATE_KEYSTORE" = "j" ] || [ "$CREATE_KEYSTORE" = "J" ]; then
    		echo "Keystore für TLS:"
    		read -p "SVWS_TLS_KEYSTORE_PATH (default: '$CONF_PATH/keystore'): " SVWS_TLS_KEYSTORE_PATH
    		export SVWS_TLS_KEYSTORE_PATH=${SVWS_TLS_KEYSTORE_PATH:-$CONF_PATH/keystore}
    		read -p "SVWS_TLS_KEYSTORE_PASSWORD (default: 'test123'): " SVWS_TLS_KEYSTORE_PASSWORD
    		export SVWS_TLS_KEYSTORE_PASSWORD=${SVWS_TLS_KEYSTORE_PASSWORD:-test123}
    		read -p "SVWS_TLS_KEY_ALIAS (default: 'alias1'): " SVWS_TLS_KEY_ALIAS
    		export SVWS_TLS_KEY_ALIAS=${SVWS_TLS_KEY_ALIAS:-alias1}
			echo "Bitte geben Sie die folgenden Informationen für den Distinguished Name (dname) ein:"
			read -p "Common NAME (CN): " INPUT_COMMON_NAME
			export INPUT_COMMON_NAME=${INPUT_COMMON_NAME}
			read -p "Organizational Unit (OU): " INPUT_ORGANIZATIONAL_UNIT
			export INPUT_ORGANIZATIONAL_UNIT=${INPUT_ORGANIZATIONAL_UNIT}
			read -p "Organization (O): " INPUT_ORGANIZATION
			export INPUT_ORGANIZATION=${INPUT_ORGANIZATION}
			read -p "Locality (L): " INPUT_LOCALITY
			export INPUT_LOCALITY=${INPUT_LOCALITY}
			read -p "State (S): " INPUT_STATE
			export INPUT_STATE=${INPUT_STATE}
			read -p "Country (C): " INPUT_COUNTRY
			export INPUT_COUNTRY=${INPUT_COUNTRY}
			read -p "Gültigkeitsdauer des Zertifikats in Tagen: " validity_days
			export VALIDITY_DAYS=${VALIDITY_DAYS}
    	else
    		echo "Keystore für TLS:"
    		read -p "SVWS_TLS_KEYSTORE_PATH: " SVWS_TLS_KEYSTORE_PATH
    		export SVWS_TLS_KEYSTORE_PATH=${SVWS_TLS_KEYSTORE_PATH}
    		read -p "SVWS_TLS_KEYSTORE_PASSWORD: " SVWS_TLS_KEYSTORE_PASSWORD
    		export SVWS_TLS_KEYSTORE_PASSWORD=${SVWS_TLS_KEYSTORE_PASSWORD}
    		read -p "SVWS_TLS_KEY_ALIAS: " SVWS_TLS_KEY_ALIAS
    		export SVWS_TLS_KEY_ALIAS=${SVWS_TLS_KEY_ALIAS}
    	fi


    	echo ""
    	echo "Installation auf: "
        echo "Host: $(hostname) - $(hostname -I | cut -d' ' -f1)"
    	echo ""
    	echo "MariaDB-Konfiguration:"
    	echo "  MARIADB_ROOT_PASSWORD: $MARIADB_ROOT_PASSWORD"

    	echo ""
    	echo "Installationspfade:"
    	echo "  APP_PATH: $APP_PATH"
    	echo "  CONF_PATH: $CONF_PATH"
    	echo "  APP_PORT: $APP_PORT"

    	echo ""
    	echo "Keystore für TLS:"
    	echo "  SVWS_TLS_KEYSTORE_PATH: $SVWS_TLS_KEYSTORE_PATH"
    	echo "  SVWS_TLS_KEYSTORE_PASSWORD: $SVWS_TLS_KEYSTORE_PASSWORD"
    	echo "  SVWS_TLS_KEY_ALIAS: $SVWS_TLS_KEY_ALIAS"
		echo "  Common Name (CN): $INPUT_COMMON_NAME"
		echo "  Organizational Unit (OU): $INPUT_ORGANIZATIONAL_UNIT"
		echo "  Organization (O): $INPUT_ORGANIZATION"
		echo "  Locality (L): $INPUT_LOCALITY"
		echo "  State (S): $INPUT_STATE"
		echo "  Country (C): $INPUT_COUNTRY"
        echo "  Gültigkeitsdauer des Zertifikats: $validity_days Tage"

    	read -p "Sind alle Einstellungen korrekt? (j/N): " CONFIRM

    	if [ "$CONFIRM" = "n" ] || [ "$CONFIRM" = "N" ] || [ "$CONFIRM" = "" ]; then
    		echo "Bitte Skript erneut ausführen";
    		exit 0;
    	fi
    fi

    # Erstelle .env-Datei und schreibe Konfiguration hinein
    echo "Erstelle .env-Datei und schreibe Konfiguration hinein"
    touch .env
    echo "CREATE_MARIADB=$CREATE_MARIADB" >> .env
    echo "CREATE_KEYSTORE=$CREATE_KEYSTORE" >> .env
    echo "MARIADB_ROOT_PASSWORD=$MARIADB_ROOT_PASSWORD" >> .env
    echo "APP_PATH=$APP_PATH" >> .env
    echo "CONF_PATH=$CONF_PATH" >> .env
    echo "APP_PORT=$APP_PORT" >> .env
    echo "SVWS_TLS_KEYSTORE_PATH=$SVWS_TLS_KEYSTORE_PATH" >> .env
    echo "SVWS_TLS_KEYSTORE_PASSWORD=$SVWS_TLS_KEYSTORE_PASSWORD" >> .env
    echo "SVWS_TLS_KEY_ALIAS=$SVWS_TLS_KEY_ALIAS" >> .env
    echo "INPUT_COMMON_NAME=$INPUT_COMMON_NAME" >> .env
    echo "INPUT_ORGANIZATIONAL_UNIT=$INPUT_ORGANIZATIONAL_UNIT" >> .env
    echo "INPUT_ORGANIZATION=$INPUT_ORGANIZATION" >> .env
    echo "INPUT_LOCALITY=$INPUT_LOCALITY" >> .env
    echo "INPUT_STATE=$INPUT_STATE" >> .env
    echo "INPUT_COUNTRY=$INPUT_COUNTRY" >> .env
    echo "validity_days=$validity_days" >> .env
fi

# Laden von Abhängigkeiten
echo "Lade Abhängigkeiten ..."
# Paketliste aktualisieren ohne Ausgabe
apt update
# Installieren von Abhängigkeiten in ruhigem Modus (-qq)
apt-get -y install gettext zip wget curl software-properties-common dirmngr gnupg2 apt-transport-https sed grep
mkdir -p /etc/apt/keyrings
wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | tee /etc/apt/keyrings/adoptium.asc
osrelease=$(awk -F= '/^NAME/{print$2}' /etc/os-release)
if [[ "$osrelease" == "\"Debian GNU/Linux\"" ]] || [[ "$osrelease" == "Debian GNU/Linux" ]]; then
	echo "deb [signed-by=/etc/apt/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list
else
	echo "deb [signed-by=/etc/apt/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb $(awk -F= '/^UBUNTU_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list
fi
apt -y update
apt -y install temurin-21-jdk

# Installation der Datenbank

# Prüfen, ob die Installation der MariaDB-Datenbank gewünscht ist
if [ "$CREATE_MARIADB" = "j" ] || [ "$CREATE_MARIADB" = "J" ]; then
    echo "Lade MariaDB 10.9 ..."

    # Herunterladen des Skripts zum Einrichten des MariaDB-Repositorys und Ausführen des Skripts
    curl -LsS https://downloads.mariadb.com/MariaDB/mariadb_repo_setup | bash -s -- --mariadb-server-version=10.9 --skip-maxscale --skip-tools

    # Installieren der MariaDB-Datenbank
    apt-get -y install mariadb-server

    # Ausgabe der Erfolgsmeldung
    echo "MariaDB 10.9 erfolgreich installiert."
fi

# SVWS laden und auspacken
echo "Lade SVWS ..."

# Wenn DOWNLOAD_PFAD gesetzt ist, lade Datei herunter
if [ ! -z "$DOWNLOAD_PFAD" ]; then
  echo "Lade Datei herunter von $DOWNLOAD_PFAD..."
  wget $DOWNLOAD_PFAD
  echo "Herunterladen abgeschlossen."
fi

# Entpacke die SVWS-Installationsdatei
tar xzf ./LINUX_INSTALLER_FILE_NAME

# Erstelle Verzeichnisse
mkdir -p $APP_PATH
mkdir $APP_PATH/client
mkdir $APP_PATH/adminclient
mkdir $APP_PATH/conf
mkdir -p ${CONF_PATH%/*}

# Kopiere App, Konfigurationen und Zertifikate
cp -r ./svws/app $APP_PATH
cp -r ./svws/conf $CONF_PATH

# Entpacke den Client in das Client-Verzeichnis
unzip -d $APP_PATH/client $APP_PATH/app/SVWS-Client*.zip

# Lösche die entpackte Client-Datei
rm -rf $APP_PATH/app/SVWS-Client*.zip

# Entpacke den Admin-Client in das Admin-Client-Verzeichnis
unzip -d $APP_PATH/adminclient $APP_PATH/app/SVWS-Admin-Client*.zip

# Lösche die entpackte Admin-Client-Datei
rm -rf $APP_PATH/app/SVWS-Admin-Client*.zip

# Erstelle Service-Datei und kopiere sie in das System-Verzeichnis
envsubst < ./svws/svws-template.service > ./svws/svws.service
cp ./svws/svws.service /etc/systemd/system/

# Wenn CREATE_KEYSTORE gesetzt ist, erstelle Keystore
if [ "$CREATE_KEYSTORE" = "j" ] || [ "$CREATE_KEYSTORE" = "J" ]; then
	# Keystore erstellen
	#mkdir -p $SVWS_TLS_KEYSTORE_PATH
    echo "Erstelle Keystore in $SVWS_TLS_KEYSTORE_PATH/keystore ..."
    export HOSTNAME=`hostname`
    keytool -genkey -noprompt -alias $SVWS_TLS_KEY_ALIAS -validity $validity_days -dname "CN=${INPUT_COMMON_NAME}, OU=${INPUT_ORGANIZATIONAL_UNIT}, O=${INPUT_ORGANIZATION}, L=${INPUT_LOCALITY}, S=${INPUT_STATE}, C=${INPUT_COUNTRY}" -ext "SAN=DNS:localhost,IP:127.0.0.1,DNS:${HOSTNAME}" -keystore $SVWS_TLS_KEYSTORE_PATH/keystore -storepass $SVWS_TLS_KEYSTORE_PASSWORD -keypass $SVWS_TLS_KEYSTORE_PASSWORD  -keyalg RSA
    keytool -export -keystore $SVWS_TLS_KEYSTORE_PATH/keystore -alias $SVWS_TLS_KEY_ALIAS -file ./SVWS.cer -storepass $SVWS_TLS_KEYSTORE_PASSWORD
else
	mv  $SVWS_TLS_KEYSTORE_PATH $APP_PATH
fi

# Erstelle Konfigurationsdatei
echo "Erstelle Konfiguration ..."

envsubst < $CONF_PATH/svwsconfig-template.json > $CONF_PATH/svwsconfig.json

rm $CONF_PATH/svwsconfig-template.json

# Erstelle einen symbolischen Link zur Konfigurationsdatei
ln -s $CONF_PATH/svwsconfig.json $APP_PATH/svwsconfig.json


# Prüfen, ob die Installation der MariaDB-Datenbank gewünscht ist
if [ "$CREATE_MARIADB" = "j" ] || [ "$CREATE_MARIADB" = "J" ]; then
	    envsubst < ./init-scripts/init-template.sql > ./init-scripts/init.sql
        # Importiere die Daten in die MariaDB
        mysql < ./init-scripts/init.sql
fi


cd $script_dir

# Lösche das Verzeichnis 'svws' im Home-Verzeichnis
echo "Lösche das Verzeichnis 'svws' im Home-Verzeichnis..."
rm -r ./svws

# Lösche das Verzeichnis 'init-scripts' im Home-Verzeichnis
echo "Lösche das Verzeichnis 'init-scripts' im Home-Verzeichnis..."
rm -r ./init-scripts

# Einrichten des SVWS-Service als Systemd-Service
echo "richte SVWS als Service ein ..."

# Erstellen der Gruppe "svws" und des Nutzers "svws" ohne Login Shell (-s /bin/false)
# Der Nutzer wird der Gruppe "svws" zugewiesen und besitzt Lese-/Schreibzugriff auf die relevanten Verzeichnisse
/usr/sbin/groupadd -r svws
/usr/sbin/useradd -r -s /bin/false -g svws svws

chown -R svws:svws $APP_PATH
chown -R svws:svws $CONF_PATH

# Aktualisieren der Systemd-Konfigurationen und Starten des Services
# Der Service wird automatisch gestartet, sobald das System hochfährt (systemctl enable)
systemctl daemon-reload
systemctl start svws.service
systemctl enable svws.service

# Überprüfen des Status des Services
systemctl status svws.service --no-pager

