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

export CREATE_MariaDB=J
export CREATE_KEYSTORE=J
export CREATE_TESTDATA=J
export MariaDB_ROOT_PASSWORD=${password1}
export MariaDB_DATABASE=svwsdb
export MariaDB_HOST=localhost
export MariaDB_USER=svwsadmin
export MariaDB_PASSWORD=${password2}

export APP_PATH=/opt/app/svws
export CONF_PATH=/etc/app/svws/conf

export APP_PORT=8443

export SVWS_TLS_KEYSTORE_PATH=$CONF_PATH/keystore
export SVWS_TLS_KEYSTORE_PASSWORD=test123
export SVWS_TLS_KEY_ALIAS=


export TMP_DIR=/tmp/svws
export MDBFILE=${MDBFILE:-$TMP_DIR/databases/SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.mdb}

export INIT_EMPTY_DB=N


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

    	read -p "Möchten Sie MariaDB installieren? (j/N): " CREATE_MariaDB

    	if [ "$CREATE_MariaDB" = "j" ] || [ "$CREATE_MariaDB" = "J" ]; then
    		read -p "MariaDB_ROOT_PASSWORD (default: '${password1}'): " MariaDB_ROOT_PASSWORD
    		export MariaDB_ROOT_PASSWORD=${MariaDB_ROOT_PASSWORD:-${password1}}
    		read -p "MariaDB_DATABASE (default: 'svwsdb'): " MariaDB_DATABASE
    		export MariaDB_DATABASE=${MariaDB_DATABASE:-svwsdb}
    		read -p "MariaDB_HOST (default: 'localhost'): " MariaDB_HOST
    		export MariaDB_HOST=${MariaDB_HOST:-localhost}
    		read -p "MariaDB_USER (default: 'svwsadmin'): " MariaDB_USER
    		export MariaDB_USER=${MariaDB_USER:-svwsadmin}
    		read -p "MariaDB_PASSWORD (default: '${password2}'): " MariaDB_PASSWORD
    		export MariaDB_PASSWORD=${MariaDB_PASSWORD:-${password2}}
    	else
    		read -p "MariaDB_ROOT_PASSWORD: " MariaDB_ROOT_PASSWORD
    		export MariaDB_ROOT_PASSWORD=${MariaDB_ROOT_PASSWORD}
    		read -p "MariaDB_DATABASE: " MariaDB_DATABASE
    		export MariaDB_DATABASE=${MariaDB_DATABASE}
    		read -p "MariaDB_HOST: " MariaDB_HOST
    		export MariaDB_HOST=${MariaDB_HOST}
    		read -p "MariaDB_USER: " MariaDB_USER
    		export MariaDB_USER=${MariaDB_USER}
    		read -p "MariaDB_PASSWORD: " MariaDB_PASSWORD
    		export MariaDB_PASSWORD=${MariaDB_PASSWORD}
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
			read -p "Common Name (CN): " INPUT_common_name
			export INPUT_common_name=${INPUT_common_name}
			read -p "Organizational Unit (OU): " INPUT_organizational_unit
			export INPUT_organizational_unit=${INPUT_organizational_unit}
			read -p "Organization (O): " INPUT_organization
			export INPUT_organization=${INPUT_organization}
			read -p "Locality (L): " INPUT_locality
			export INPUT_locality=${INPUT_locality}
			read -p "State (S): " INPUT_state
			export INPUT_state=${INPUT_state}
			read -p "Country (C): " INPUT_country
			export INPUT_country=${INPUT_country}
			read -p "Gültigkeitsdauer des Zertifikats in Tagen: " validity_days
			export validity_days=${validity_days}
    	else
    		echo "Keystore für TLS:"
    		read -p "SVWS_TLS_KEYSTORE_PATH: " SVWS_TLS_KEYSTORE_PATH
    		export SVWS_TLS_KEYSTORE_PATH=${SVWS_TLS_KEYSTORE_PATH}
    		read -p "SVWS_TLS_KEYSTORE_PASSWORD: " SVWS_TLS_KEYSTORE_PASSWORD
    		export SVWS_TLS_KEYSTORE_PASSWORD=${SVWS_TLS_KEYSTORE_PASSWORD}
    		read -p "SVWS_TLS_KEY_ALIAS: " SVWS_TLS_KEY_ALIAS
    		export SVWS_TLS_KEY_ALIAS=${SVWS_TLS_KEY_ALIAS}
    	fi

    	read -p "Möchten Sie Testdaten importieren? (j/N): " CREATE_TESTDATA
    	if [ "$CREATE_TESTDATA" = "j" ] || [ "$CREATE_TESTDATA" = "J" ]; then
    		echo "Testdaten:"
    		read -p "MDBFILE (default: 'Abitur-Test-Daten.mdb'): " MDBFILE_USER
    		echo
    		export TMP_DIR=/tmp/svws
    		MDBFILE_USER=${MDBFILE_USER:-/databases/SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.mdb}
    		MDBFILE="${TMP_DIR}${MDBFILE_USER}"
    	else
    		if [ "$CREATE_MariaDB" = "j" ] || [ "$CREATE_MariaDB" = "J" ]; then
    			export INIT_EMPTY_DB=J
    		fi
    	fi

    	echo ""
    	echo "Installation auf: "
        echo "Host: $(hostname) - $(hostname -I | cut -d' ' -f1)"
    	echo ""
    	echo "MariaDB-Konfiguration:"
    	echo "  MariaDB_ROOT_PASSWORD: $MariaDB_ROOT_PASSWORD"
    	echo "  MariaDB_DATABASE: $MariaDB_DATABASE"
    	echo "  MariaDB_HOST: $MariaDB_HOST"
    	echo "  MariaDB_USER: $MariaDB_USER"
    	echo "  MariaDB_PASSWORD: $MariaDB_PASSWORD"

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
		echo "  Common Name (CN): $INPUT_common_name"
		echo "  Organizational Unit (OU): $INPUT_organizational_unit"
		echo "  Organization (O): $INPUT_organization"
		echo "  Locality (L): $INPUT_locality"
		echo "  State (S): $INPUT_state"
		echo "  Country (C): $INPUT_country"
        echo "  Gültigkeitsdauer des Zertifikats: $validity_days Tage"

		if [ "$CREATE_TESTDATA" = "j" ] || [ "$CREATE_TESTDATA" = "J" ]; then
			echo ""
			echo "Testdaten import:"
			echo "  MDBFILE: $MDBFILE"
		fi

    	read -p "Sind alle Einstellungen korrekt? (j/N): " CONFIRM

    	if [ "$CONFIRM" = "n" ] || [ "$CONFIRM" = "N" ] || [ "$CONFIRM" = "" ]; then
    		echo "Bitte Skript erneut ausführen";
    		exit 0;
    	fi
    fi

    # Erstelle .env-Datei und schreibe Konfiguration hinein
    echo "Erstelle .env-Datei und schreibe Konfiguration hinein"
    touch .env
    echo "CREATE_MariaDB=$CREATE_MariaDB" >> .env
    echo "CREATE_KEYSTORE=$CREATE_KEYSTORE" >> .env
    echo "CREATE_TESTDATA=$CREATE_TESTDATA" >> .env
    echo "MariaDB_ROOT_PASSWORD=$MariaDB_ROOT_PASSWORD" >> .env
    echo "MariaDB_DATABASE=$MariaDB_DATABASE" >> .env
    echo "MariaDB_HOST=$MariaDB_HOST" >> .env
    echo "MariaDB_USER=$MariaDB_USER" >> .env
    echo "MariaDB_PASSWORD=$MariaDB_PASSWORD" >> .env
    echo "APP_PATH=$APP_PATH" >> .env
    echo "CONF_PATH=$CONF_PATH" >> .env
    echo "APP_PORT=$APP_PORT" >> .env
    echo "SVWS_TLS_KEYSTORE_PATH=$SVWS_TLS_KEYSTORE_PATH" >> .env
    echo "SVWS_TLS_KEYSTORE_PASSWORD=$SVWS_TLS_KEYSTORE_PASSWORD" >> .env
    echo "SVWS_TLS_KEY_ALIAS=$SVWS_TLS_KEY_ALIAS" >> .env
    echo "INPUT_common_name=$INPUT_common_name" >> .env
    echo "INPUT_organizational_unit=$INPUT_organizational_unit" >> .env
    echo "INPUT_organization=$INPUT_organization" >> .env
    echo "INPUT_locality=$INPUT_locality" >> .env
    echo "INPUT_state=$INPUT_state" >> .env
    echo "INPUT_country=$INPUT_country" >> .env
    echo "validity_days=$validity_days" >> .env
	echo "TMP_DIR=$TMP_DIR" >> .env
	if [ "$CREATE_TESTDATA" = "j" ] || [ "$CREATE_TESTDATA" = "J" ]; then
		echo "MDBFILE=$MDBFILE" >> .env
	fi
	echo "INIT_EMPTY_DB=$INIT_EMPTY_DB" >> .env
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
if [ "$CREATE_MariaDB" = "j" ] || [ "$CREATE_MariaDB" = "J" ]; then
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
    keytool -genkey -noprompt -alias $SVWS_TLS_KEY_ALIAS -validity $validity_days -dname "CN=${INPUT_common_name}, OU=${INPUT_organizational_unit}, O=${INPUT_organization}, L=${INPUT_locality}, S=${INPUT_state}, C=${INPUT_country}" -ext "SAN=DNS:localhost,IP:127.0.0.1,DNS:${HOSTNAME}" -keystore $SVWS_TLS_KEYSTORE_PATH/keystore -storepass $SVWS_TLS_KEYSTORE_PASSWORD -keypass $SVWS_TLS_KEYSTORE_PASSWORD  -keyalg RSA
    keytool -export -keystore $SVWS_TLS_KEYSTORE_PATH/keystore -alias $SVWS_TLS_KEY_ALIAS -file ./SVWS.cer -storepass $SVWS_TLS_KEYSTORE_PASSWORD
else
	mv  $SVWS_TLS_KEYSTORE_PATH $APP_PATH
fi

# Erstelle Konfigurationsdatei
echo "Erstelle Konfiguration ..."

if [ "$CREATE_TESTDATA" = "j" ] || [ "$CREATE_TESTDATA" = "J" ]; then
	envsubst < $CONF_PATH/svwsconfig-template.json > $CONF_PATH/svwsconfig.json
else
	if [ "$CREATE_MariaDB" = "j" ] || [ "$CREATE_MariaDB" = "J" ]; then
    	envsubst < $CONF_PATH/svwsconfig-template-nodb.json > $CONF_PATH/svwsconfig.json
    else
    	envsubst < $CONF_PATH/svwsconfig-template.json > $CONF_PATH/svwsconfig.json
    fi
fi

rm $CONF_PATH/svwsconfig-template.json
rm $CONF_PATH/svwsconfig-template-nodb.json

# Erstelle einen symbolischen Link zur Konfigurationsdatei
ln -s $CONF_PATH/svwsconfig.json $APP_PATH/svwsconfig.json


# Prüfen, ob die Installation der MariaDB-Datenbank gewünscht ist
if [ "$CREATE_MariaDB" = "j" ] || [ "$CREATE_MariaDB" = "J" ]; then
	    envsubst < ./init-scripts/init-template.sql > ./init-scripts/init.sql
        # Importiere die Daten in die MariaDB
        mysql < ./init-scripts/init.sql
fi

# Überprüfe, ob Testdaten erstellt werden sollen
if [ "$CREATE_TESTDATA" = "j" ] || [ "$CREATE_TESTDATA" = "J" ]; then

    # Erstelle init.sql Datei aus der Vorlage init-template.sql, dabei werden Umgebungsvariablen ersetzt
    echo "importiere Testdaten ..."

    # Erstelle ein temporäres Verzeichnis
    mkdir $TMP_DIR
    cd $TMP_DIR

    # Lade Testdatenbanken von GitHub als zip-Datei herunter
    echo "Lade Testdatenbanken herunter ..."
    wget https://github.com/SVWS-NRW/SVWS-TestMDBs/archive/refs/heads/main.zip -O $TMP_DIR/databases.zip

    # Extrahiere Access-Datenbanken aus den heruntergeladenen zip-File
    echo "Extrahiere Access-Datenbanken ..."
    unzip $TMP_DIR/databases.zip -d $TMP_DIR/databases "*.mdb"
    rm -rf $TMP_DIR/databases.zip

    # Führe den Import (MigrateDB) der Access-Datenbank(en) durch ...

    # Setze den Dateinamen der ersten Access-Datenbank
    MDBFILE= $TMP_DIR$MDBFILE

    # Wechsle in das Verzeichnis der Anwendung
    cd $APP_PATH

    # Importiere die Datenbank(en) mittels der MigrateDB Klasse
    echo "Importiere Datenbank: ${MDBFILE} ..."
    java -cp "svws-server-app-*.jar:${APP_PATH}/app/*:${APP_PATH}/app/lib/*" de.svws_nrw.db.utils.app.MigrateDB -j -r -1 -sd "MDB" \
       -sl "${MDBFILE}" \
       -td "MARIA_DB" \
       -tl ${MariaDB_HOST} \
       -ts ${MariaDB_DATABASE} \
       -tu ${MariaDB_USER} \
       -tp ${MariaDB_PASSWORD} \
       -tr ${MariaDB_ROOT_PASSWORD}
fi

# Überprüfe, ob eine leere Datenbank erstellt werden sollen
if [ "$INIT_EMPTY_DB" = "j" ] || [ "$INIT_EMPTY_DB" = "J" ]; then

	# Wechsle in das Verzeichnis der Anwendung
	cd $APP_PATH

	# Erstelle leere Datenbank ...
    echo "Erstelle leere Datenbank ..."
    java -cp "svws-server-app-*.jar:${APP_PATH}/app/*:${APP_PATH}/app/lib/*" de.svws_nrw.db.utils.app.CreateSchema -j -r -1 \
       -td "MARIA_DB" \
       -tl ${MariaDB_HOST} \
       -ts ${MariaDB_DATABASE} \
       -tu ${MariaDB_USER} \
       -tp ${MariaDB_PASSWORD} \
       -tr ${MariaDB_ROOT_PASSWORD}
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

