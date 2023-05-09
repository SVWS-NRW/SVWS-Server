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

script_dir="$PWD"

# Variablen für Passwortlänge und erlaubte Zeichen
LENGTH=12
CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

# Passwort generieren
password2=$(head /dev/urandom | tr -dc $CHARS | fold -w $LENGTH | head -n 1)
password1=$(head /dev/urandom | tr -dc $CHARS | fold -w $LENGTH | head -n 1)

export CREATE_MYSQL=J
export CREATE_KEYSTORE=J
export CREATE_TESTDATA=J
export MYSQL_ROOT_PASSWORD=${password1}
export MYSQL_DATABASE=svwsdb
export MYSQL_HOST=localhost
export MYSQL_USER=svwsadmin
export MYSQL_PASSWORD=${password2}

export APP_PATH=/opt/app/svws
export CONF_PATH=/etc/app/svws/conf

export APP_PORT=8443

export SVWS_TLS_KEYSTORE_PATH=$CONF_PATH/keystore
export SVWS_TLS_KEYSTORE_PASSWORD=test123
export SVWS_TLS_KEY_ALIAS=


export TMP_DIR=/tmp/svws
export MDBFILE=${MDBFILE:-$TMP_DIR/databases/SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.mdb}

if [ -f .env ]; then
 export $(grep -v '^#' .env | xargs)
else

	DOWNLOAD_PFAD=BASE_DOWNLOAD_URL/LINUX_INSTALLER_FILE_NAME

    if [[ "$1" == "--default" ]]; then
    	echo "verwende defaults ..."
    	echo "Testdaten:"
        read -sp "TESTDB_PASSWORD: " TESTDB_PASSWORD
        echo ""
        export TESTDB_PASSWORD=${TESTDB_PASSWORD}
    else
    	#Konfuguration
    	echo "MariaDB-Konfiguration:"

    	read -p "Möchten Sie die MySQL-Datenbank erstellen? (j/N): " CREATE_MYSQL

    	if [ "$CREATE_MYSQL" = "j" ] || [ "$CREATE_MYSQL" = "J" ]; then
    		read -p "MYSQL_ROOT_PASSWORD (default: '${password1}'): " MYSQL_ROOT_PASSWORD
    		export MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD:-${password1}}
    		read -p "MYSQL_DATABASE (default: 'svwsdb'): " MYSQL_DATABASE
    		export MYSQL_DATABASE=${MYSQL_DATABASE:-svwsdb}
    		read -p "MYSQL_HOST (default: 'localhost'): " MYSQL_HOST
    		export MYSQL_HOST=${MYSQL_HOST:-localhost}
    		read -p "MYSQL_USER (default: 'svwsadmin'): " MYSQL_USER
    		export MYSQL_USER=${MYSQL_USER:-svwsadmin}
    		read -p "MYSQL_PASSWORD (default: '${password2}'): " MYSQL_PASSWORD
    		export MYSQL_PASSWORD=${MYSQL_PASSWORD:-${password2}}
    	else
    		read -p "MYSQL_ROOT_PASSWORD: " MYSQL_ROOT_PASSWORD
    		export MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
    		read -p "MYSQL_DATABASE: " MYSQL_DATABASE
    		export MYSQL_DATABASE=${MYSQL_DATABASE}
    		read -p "MYSQL_HOST: " MYSQL_HOST
    		export MYSQL_HOST=${MYSQL_HOST}
    		read -p "MYSQL_USER: " MYSQL_USER
    		export MYSQL_USER=${MYSQL_USER}
    		read -p "MYSQL_PASSWORD: " MYSQL_PASSWORD
    		export MYSQL_PASSWORD=${MYSMYSQL_PASSWORD}
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
    		read -p "SVWS_TLS_KEY_ALIAS (default: ''): " SVWS_TLS_KEY_ALIAS
    		export SVWS_TLS_KEY_ALIAS=${SVWS_TLS_KEY_ALIAS}
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
    		read -sp "TESTDB_PASSWORD: " TESTDB_PASSWORD
    		export TESTDB_PASSWORD=${TESTDB_PASSWORD}
    		read -p "MDBFILE (default: 'Abitur-Test-Daten.mdb'): " MDBFILE_USER
    		export TMP_DIR=/tmp/svws
    		MDBFILE_USER=${MDBFILE_USER:-/databases/SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.mdb}
    		MDBFILE="${TMP_DIR}${MDBFILE_USER}"
    	fi

    	echo ""
    	echo "Installation auf: "
        echo "Host: $(hostname) - $(hostname -I | cut -d' ' -f1)"
    	echo ""
    	echo "MariaDB-Konfiguration:"
    	echo "  MYSQL_ROOT_PASSWORD: $MYSQL_ROOT_PASSWORD"
    	echo "  MYSQL_DATABASE: $MYSQL_DATABASE"
    	echo "  MYSQL_HOST: $MYSQL_HOST"
    	echo "  MYSQL_USER: $MYSQL_USER"
    	echo "  MYSQL_PASSWORD: $MYSQL_PASSWORD"

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

    	echo ""
    	echo "Testdaten import:"
    	echo "  MDBFILE: $MDBFILE"

    	read -p "Sind alle Einstellungen korrekt? (j/N): " CONFIRM

    	if [ "$CONFIRM" = "n" ] || [ "$CONFIRM" = "N" ] || [ "$CONFIRM" = "" ]; then
    		echo "Bitte Skript erneut ausführen";
    		exit 0;
    	fi
    fi

    # Erstelle .env-Datei und schreibe Konfiguration hinein
    echo "Erstelle .env-Datei und schreibe Konfiguration hinein"
    touch .env
    echo "CREATE_MYSQL=$CREATE_MYSQL" >> .env
    echo "CREATE_KEYSTORE=$CREATE_KEYSTORE" >> .env
    echo "CREATE_TESTDATA=$CREATE_TESTDATA" >> .env
    echo "MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD" >> .env
    echo "MYSQL_DATABASE=$MYSQL_DATABASE" >> .env
    echo "MYSQL_HOST=$MYSQL_HOST" >> .env
    echo "MYSQL_USER=$MYSQL_USER" >> .env
    echo "MYSQL_PASSWORD=$MYSQL_PASSWORD" >> .env
    echo "APP_PATH=$APP_PATH" >> .env
    echo "CONF_PATH=$CONF_PATH" >> .env
    echo "APP_PORT=$APP_PORT" >> .env
    echo "SVWS_TLS_KEYSTORE_PATH=$SVWS_TLS_KEYSTORE_PATH" >> .env
    echo "SVWS_TLS_KEYSTORE_PASSWORD=$SVWS_TLS_KEYSTORE_PASSWORD" >> .env
    echo "SVWS_TLS_KEY_ALIAS=$SVWS_TLS_KEY_ALIAS" >> .env
    echo "TESTDB_PASSWORD=$TESTDB_PASSWORD" >> .env
    echo "TMP_DIR=$TMP_DIR" >> .env
    echo "MDBFILE=$MDBFILE" >> .env
    echo "DOWNLOAD_PFAD=$DOWNLOAD_PFAD" >> .env
fi

# Laden von Abhängigkeiten
echo "Lade Abhängigkeiten ..."
# Paketliste aktualisieren ohne Ausgabe
apt update > /dev/null
# Installieren von Abhängigkeiten in ruhigem Modus (-qq)
apt-get -y -qq install gettext zip wget openjdk-17-jre curl software-properties-common dirmngr gnupg2 > /dev/null

# Installation der Datenbank

# Prüfen, ob die Installation der MariaDB-Datenbank gewünscht ist
if [ "$CREATE_MYSQL" = "j" ] || [ "$CREATE_MYSQL" = "J" ]; then
    echo "Lade MariaDB 10.7 ..."

    # Herunterladen des Skripts zum Einrichten des MariaDB-Repositorys und Ausführen des Skripts
    curl -LsS https://downloads.mariadb.com/MariaDB/mariadb_repo_setup | bash -s -- --mariadb-server-version=10.7 --skip-maxscale --skip-tools

    # Installieren der MariaDB-Datenbank
    apt-get -y -qq install mariadb-server > /dev/null

    # Ausgabe der Erfolgsmeldung
    echo "MariaDB 10.7 erfolgreich installiert."
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
mkdir $APP_PATH/conf
mkdir -p ${CONF_PATH%/*}

# Kopiere App, Konfigurationen und Zertifikate
cp -r ./svws/app $APP_PATH
cp -r ./svws/conf $CONF_PATH

# Entpacke den Client in das Client-Verzeichnis
unzip -d $APP_PATH/client $APP_PATH/app/svws-client*.zip

# Lösche die entpackte Client-Datei
rm -rf $APP_PATH/app/svws-client*.zip

# Erstelle Service-Datei und kopiere sie in das System-Verzeichnis
envsubst < ./svws/svws-template.service > ./svws/svws.service
cp ./svws/svws.service /etc/systemd/system/

# Wenn CREATE_KEYSTORE gesetzt ist, erstelle Keystore
if [ "$CREATE_KEYSTORE" = "j" ] || [ "$CREATE_KEYSTORE" = "J" ]; then
	# Keystore erstellen
	#mkdir -p $SVWS_TLS_KEYSTORE_PATH
    echo "Erstelle Keystore in $SVWS_TLS_KEYSTORE_PATH/keystore ..."
    keytool -genkey -noprompt -alias alias1 -dname "CN=test, OU=test, O=test, L=test, S=test, C=test" -keystore $SVWS_TLS_KEYSTORE_PATH/keystore -storepass $SVWS_TLS_KEYSTORE_PASSWORD -keypass $SVWS_TLS_KEYSTORE_PASSWORD  -keyalg RSA
else
	mv  $SVWS_TLS_KEYSTORE_PATH $APP_PATH
fi

# Erstelle Konfigurationsdatei
echo "Erstelle Konfiguration ..."

if [ "$CREATE_TESTDATA" = "j" ] || [ "$CREATE_TESTDATA" = "J" ]; then
	envsubst < $CONF_PATH/svwsconfig-template.json > $CONF_PATH/svwsconfig.json
else
	if [ "$CREATE_MYSQL" = "j" ] || [ "$CREATE_MYSQL" = "J" ]; then
    	envsubst < $CONF_PATH/svwsconfig-template-nodb.json > $CONF_PATH/svwsconfig.json
    else
    	envsubst < $CONF_PATH/svwsconfig-template.json > $CONF_PATH/svwsconfig.json
    fi
fi

rm $CONF_PATH/svwsconfig-template.json
rm $CONF_PATH/svwsconfig-template-nodb.json

# Erstelle einen symbolischen Link zur Konfigurationsdatei
ln $CONF_PATH/svwsconfig.json $APP_PATH/svwsconfig.json


# Prüfen, ob die Installation der MariaDB-Datenbank gewünscht ist
if [ "$CREATE_MYSQL" = "j" ] || [ "$CREATE_MYSQL" = "J" ]; then
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
    java -cp "svws-server-app-*.jar:${APP_PATH}/app/*:${APP_PATH}/app/lib/*" de.svws_nrw.db.utils.app.MigrateDB -j -d -r -1 -sd "MDB" \
       -sl "${MDBFILE}" -sp "${TESTDB_PASSWORD}" \
       -td "MARIA_DB" \
       -tl ${MYSQL_HOST} \
       -ts ${MYSQL_DATABASE} \
       -tu ${MYSQL_USER} \
       -tp ${MYSQL_PASSWORD} \
       -tr ${MYSQL_ROOT_PASSWORD}
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
groupadd -r svws
useradd -r -s /bin/false -g svws svws
chown -R svws:svws $APP_PATH
chown -R svws:svws $CONF_PATH

# Aktualisieren der Systemd-Konfigurationen und Starten des Services
# Der Service wird automatisch gestartet, sobald das System hochfährt (systemctl enable)
systemctl daemon-reload
systemctl start svws.service
systemctl enable svws.service

# Überprüfen des Status des Services
systemctl status svws.service

