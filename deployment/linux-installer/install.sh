#!/bin/bash

set -euo pipefail

IS_DEBIAN_LIKE=false
if [ -f "/etc/debian_version" ] || [ -f "/etc/debian_release" ]; then
	IS_DEBIAN_LIKE=true
fi

if ! $IS_DEBIAN_LIKE; then
	echo "Dieses Skript wird nicht auf einem Debian-basierten System ausgeführt. Beende Ausführung ..."
	exit 1
fi

if [[ $EUID -ne 0 ]]; then
	echo "Dieses Skript muss als Root ausgeführt werden."
	exit 1
fi

script_dir="$PWD"
installer_archive="LINUX_INSTALLER_FILE_NAME"
default_download_path="BASE_DOWNLOAD_URL/LINUX_INSTALLER_FILE_NAME"
DOWNLOAD_PFAD=""

# Variablen für Passwortlänge und erlaubte Zeichen
LENGTH=12
CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

generate_password() {
	head /dev/urandom | tr -dc "$CHARS" | fold -w "$LENGTH" | head -n 1
}

is_yes() {
	case "${1:-}" in
		j|J|y|Y|yes|YES|Yes)
			return 0
			;;
		*)
			return 1
			;;
	esac
}

load_env_file() {
	if [ -f .env ]; then
		set -a
		. ./.env
		set +a
	fi

	if [ -z "${VALIDITY_DAYS:-}" ] && [ -n "${validity_days:-}" ]; then
		export VALIDITY_DAYS="${validity_days}"
	fi
}

append_env_var() {
	local key="$1"
	local value="${2-}"
	printf '%s=' "$key" >> .env
	printf '%q\n' "$value" >> .env
}

persist_env_file() {
	: > .env
	append_env_var CREATE_MARIADB "$CREATE_MARIADB"
	append_env_var CREATE_KEYSTORE "$CREATE_KEYSTORE"
	append_env_var CREATE_TESTDATA "$CREATE_TESTDATA"
	append_env_var MARIADB_ROOT_PASSWORD "$MARIADB_ROOT_PASSWORD"
	append_env_var MARIADB_HOST "$MARIADB_HOST"
	append_env_var MARIADB_DATABASE "$MARIADB_DATABASE"
	append_env_var MARIADB_USER "$MARIADB_USER"
	append_env_var MARIADB_PASSWORD "$MARIADB_PASSWORD"
	append_env_var TESTDATA_DOWNLOAD_URL "$TESTDATA_DOWNLOAD_URL"
	append_env_var TESTDATA_SQLITE_RELATIVE_PATH "$TESTDATA_SQLITE_RELATIVE_PATH"
	append_env_var FORCE_TESTDATA_IMPORT "$FORCE_TESTDATA_IMPORT"
	append_env_var APP_PATH "$APP_PATH"
	append_env_var CONF_PATH "$CONF_PATH"
	append_env_var APP_PORT "$APP_PORT"
	append_env_var SVWS_TLS_KEYSTORE_PATH "$SVWS_TLS_KEYSTORE_PATH"
	append_env_var SVWS_TLS_KEYSTORE_PASSWORD "$SVWS_TLS_KEYSTORE_PASSWORD"
	append_env_var SVWS_TLS_KEY_ALIAS "$SVWS_TLS_KEY_ALIAS"
	append_env_var INPUT_COMMON_NAME "$INPUT_COMMON_NAME"
	append_env_var INPUT_ORGANIZATIONAL_UNIT "$INPUT_ORGANIZATIONAL_UNIT"
	append_env_var INPUT_ORGANIZATION "$INPUT_ORGANIZATION"
	append_env_var INPUT_LOCALITY "$INPUT_LOCALITY"
	append_env_var INPUT_STATE "$INPUT_STATE"
	append_env_var INPUT_COUNTRY "$INPUT_COUNTRY"
	append_env_var VALIDITY_DAYS "$VALIDITY_DAYS"
	chmod 600 .env
}

render_svws_config() {
	local template_file="$CONF_PATH/svwsconfig-template-nodb.json"

	if is_yes "$CREATE_TESTDATA" && [ -n "${MARIADB_DATABASE:-}" ] && [ -n "${MARIADB_USER:-}" ] && [ -n "${MARIADB_PASSWORD:-}" ]; then
		template_file="$CONF_PATH/svwsconfig-template.json"
	fi

	echo "Erstelle Konfiguration aus $(basename "$template_file") ..."
	envsubst < "$template_file" > "$CONF_PATH/svwsconfig.json"
	rm -f "$CONF_PATH/svwsconfig-template.json" "$CONF_PATH/svwsconfig-template-nodb.json"
}

schema_exists() {
	local result
	result=$(mysql -h "$MARIADB_HOST" -u root "-p$MARIADB_ROOT_PASSWORD" -Nse "SHOW DATABASES LIKE '$MARIADB_DATABASE';" 2>/dev/null || true)
	[ "$result" = "$MARIADB_DATABASE" ]
}

import_testdata() {
	if ! is_yes "$CREATE_TESTDATA"; then
		return 0
	fi

	if [ -z "${MARIADB_DATABASE:-}" ] || [ -z "${MARIADB_USER:-}" ] || [ -z "${MARIADB_PASSWORD:-}" ]; then
		echo "Testdatenimport aktiviert, aber Schema-Benutzer oder Passwort fehlen."
		exit 1
	fi

	if schema_exists && ! is_yes "$FORCE_TESTDATA_IMPORT"; then
		echo "Das Ziel-Schema '$MARIADB_DATABASE' existiert bereits. Abbruch ohne Überschreibung."
		echo "Setzen Sie FORCE_TESTDATA_IMPORT=J, wenn der Seed-Import dennoch ausgeführt werden soll."
		exit 1
	fi

	local temp_dir archive_path extract_dir sqlite_file
	temp_dir=$(mktemp -d)
	archive_path="$temp_dir/testdaten.zip"
	extract_dir="$temp_dir/databases"
	trap 'rm -rf "$temp_dir"' RETURN

	echo "Lade Testdatenarchiv herunter ..."
	wget -q -O "$archive_path" "$TESTDATA_DOWNLOAD_URL"

	echo "Extrahiere SQLITE-Datenbanken ..."
	unzip -q "$archive_path" -d "$extract_dir" "*.sqlite"

	sqlite_file="$extract_dir/$TESTDATA_SQLITE_RELATIVE_PATH"
	if [ ! -f "$sqlite_file" ]; then
		echo "Erwartete SQLite-Datei nicht gefunden: $sqlite_file"
		exit 1
	fi

	echo "Importiere Testdaten aus: $sqlite_file"
	java -cp "$APP_PATH/app/*:$APP_PATH/app/lib/*" de.svws_nrw.db.utils.app.ImportDB -j -r -1 -td "MARIA_DB" \
		-cp "$CONF_PATH" \
		-f "$sqlite_file" \
		-tl "$MARIADB_HOST" \
		-ts "$MARIADB_DATABASE" \
		-tu "$MARIADB_USER" \
		-tp "$MARIADB_PASSWORD" \
		-tr "$MARIADB_ROOT_PASSWORD"

	echo "Testdatenimport erfolgreich abgeschlossen."
}

ensure_download_path() {
	if [ ! -f "$installer_archive" ]; then
		DOWNLOAD_PFAD="$default_download_path"
	fi
}

download_installer_if_needed() {
	if [ -n "$DOWNLOAD_PFAD" ]; then
		echo "Lade Datei herunter von $DOWNLOAD_PFAD ..."
		wget -N "$DOWNLOAD_PFAD"
		echo "Herunterladen abgeschlossen."
	fi
}

export CREATE_MARIADB=J
export CREATE_KEYSTORE=J
export CREATE_TESTDATA=N
export MARIADB_ROOT_PASSWORD="$(generate_password)"
export MARIADB_HOST=localhost
export MARIADB_DATABASE=GymAbi01
export MARIADB_USER=svwsadmin
export MARIADB_PASSWORD="$(generate_password)"
export TESTDATA_DOWNLOAD_URL="https://github.com/SVWS-NRW/SVWS-TestMDBs/archive/refs/heads/main.zip"
export TESTDATA_SQLITE_RELATIVE_PATH="SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.sqlite"
export FORCE_TESTDATA_IMPORT=N

export APP_PATH=/opt/app/svws
export CONF_PATH=/etc/app/svws/conf
export APP_PORT=8443
export SVWS_TLS_KEYSTORE_PATH="$CONF_PATH/keystore"
export SVWS_TLS_KEYSTORE_PASSWORD="$(generate_password)"
export SVWS_TLS_KEY_ALIAS=svws
export INPUT_COMMON_NAME=""
export INPUT_ORGANIZATIONAL_UNIT=""
export INPUT_ORGANIZATION=""
export INPUT_LOCALITY=""
export INPUT_STATE=""
export INPUT_COUNTRY=""
export VALIDITY_DAYS=3650

# Überprüfe, ob der Service läuft
if systemctl is-active --quiet svws.service; then
	if [[ "${1:-}" == "--update" ]]; then
		echo "Der SVWS ist aktiv -> Der SVWS-Service wird nun für das Update beendet!"
		systemctl stop svws.service
	else
		echo "SVWS ist bereits installiert und gestartet! Zum updaten bitte --update verwenden."
		exit 1
	fi
fi

if [[ "${1:-}" == "--update" ]]; then
	load_env_file
	ensure_download_path

	echo "Lade SVWS ..."
	download_installer_if_needed

	echo "stoppe SVWS ..."
	systemctl stop svws

	echo "lösche SVWS ..."
	rm -rf "$APP_PATH/app" "$APP_PATH/client" "$APP_PATH/adminclient"

	tar xzf "./$installer_archive"

	mkdir -p "$APP_PATH" "$APP_PATH/client" "$APP_PATH/adminclient"

	cp -r ./svws/app "$APP_PATH"

	unzip -d "$APP_PATH/client" "$APP_PATH/app"/SVWS-Client*.zip
	rm -rf "$APP_PATH/app"/SVWS-Client*.zip

	unzip -d "$APP_PATH/adminclient" "$APP_PATH/app"/SVWS-Admin-Client*.zip
	rm -rf "$APP_PATH/app"/SVWS-Admin-Client*.zip

	ln -sf "$CONF_PATH/svwsconfig.json" "$APP_PATH/svwsconfig.json"

	cd "$script_dir"

	echo "Lösche das Verzeichnis 'svws' im Home-Verzeichnis ..."
	rm -rf ./svws

	echo "Lösche das Verzeichnis 'init-scripts' im Home-Verzeichnis ..."
	rm -rf ./init-scripts

	systemctl start svws.service
	systemctl status svws.service
	exit 0
fi

if [ -f .env ]; then
	load_env_file
else
	ensure_download_path

	if [[ "${1:-}" == "--default" ]]; then
		echo "verwende defaults ..."
	else
		echo "MariaDB-Konfiguration:"

		read -r -p "Möchten Sie MariaDB installieren? (j/N): " CREATE_MARIADB

		if is_yes "$CREATE_MARIADB"; then
			read -r -p "MARIADB_ROOT_PASSWORD (default: '$MARIADB_ROOT_PASSWORD'): " input_root_password
			export MARIADB_ROOT_PASSWORD="${input_root_password:-$MARIADB_ROOT_PASSWORD}"
			read -r -p "MARIADB_HOST (default: 'localhost'): " input_db_host
			export MARIADB_HOST="${input_db_host:-localhost}"
		else
			read -r -p "MARIADB_ROOT_PASSWORD: " input_root_password
			export MARIADB_ROOT_PASSWORD="${input_root_password}"
			read -r -p "MARIADB_HOST: " input_db_host
			export MARIADB_HOST="${input_db_host}"
		fi

		read -r -p "Möchten Sie vollständige Testdaten importieren? (j/N): " CREATE_TESTDATA
		if is_yes "$CREATE_TESTDATA"; then
			read -r -p "MARIADB_DATABASE (default: '$MARIADB_DATABASE'): " input_db_name
			export MARIADB_DATABASE="${input_db_name:-$MARIADB_DATABASE}"
			read -r -p "MARIADB_USER (default: '$MARIADB_USER'): " input_db_user
			export MARIADB_USER="${input_db_user:-$MARIADB_USER}"
			read -r -p "MARIADB_PASSWORD (default: '$MARIADB_PASSWORD'): " input_db_password
			export MARIADB_PASSWORD="${input_db_password:-$MARIADB_PASSWORD}"
			read -r -p "FORCE_TESTDATA_IMPORT (default: '$FORCE_TESTDATA_IMPORT'): " input_force_import
			export FORCE_TESTDATA_IMPORT="${input_force_import:-$FORCE_TESTDATA_IMPORT}"
		fi

		echo "Installationspfade:"
		read -r -p "APP_PATH (default: '/opt/app/svws'): " input_app_path
		export APP_PATH="${input_app_path:-/opt/app/svws}"
		read -r -p "CONF_PATH (default: '/etc/app/svws/conf'): " input_conf_path
		export CONF_PATH="${input_conf_path:-/etc/app/svws/conf}"

		read -r -p "APP_PORT (default: 8443): " input_app_port
		export APP_PORT="${input_app_port:-8443}"

		if [ "$APP_PORT" -lt 1024 ]; then
			echo "Hinweis: Ports unter 1024 erfordern Root-Rechte und müssen entsprechend freigeschaltet/weitergeleitet werden."
		fi

		read -r -p "Möchten Sie einen Keystore erstellen? (j/N): " CREATE_KEYSTORE
		if is_yes "$CREATE_KEYSTORE"; then
			echo "Keystore für TLS:"
			read -r -p "SVWS_TLS_KEYSTORE_PATH (default: '$CONF_PATH/keystore'): " input_keystore_path
			export SVWS_TLS_KEYSTORE_PATH="${input_keystore_path:-$CONF_PATH/keystore}"
			read -r -p "SVWS_TLS_KEYSTORE_PASSWORD (default: '$SVWS_TLS_KEYSTORE_PASSWORD'): " input_keystore_password
			export SVWS_TLS_KEYSTORE_PASSWORD="${input_keystore_password:-$SVWS_TLS_KEYSTORE_PASSWORD}"
			read -r -p "SVWS_TLS_KEY_ALIAS (default: 'svws'): " input_key_alias
			export SVWS_TLS_KEY_ALIAS="${input_key_alias:-svws}"
			echo "Bitte geben Sie die folgenden Informationen für den Distinguished Name (dname) an,"
			echo "Umlaute, Sonderzeichen und Leerzeichen sind nicht erlaubt:"
			read -r -p "Common NAME (CN): " INPUT_COMMON_NAME
			export INPUT_COMMON_NAME="${INPUT_COMMON_NAME}"
			read -r -p "Organizational Unit (OU): " INPUT_ORGANIZATIONAL_UNIT
			export INPUT_ORGANIZATIONAL_UNIT="${INPUT_ORGANIZATIONAL_UNIT}"
			read -r -p "Organization (O): " INPUT_ORGANIZATION
			export INPUT_ORGANIZATION="${INPUT_ORGANIZATION}"
			read -r -p "Locality (L): " INPUT_LOCALITY
			export INPUT_LOCALITY="${INPUT_LOCALITY}"
			read -r -p "State (S): " INPUT_STATE
			export INPUT_STATE="${INPUT_STATE}"
			read -r -p "Country (C): " INPUT_COUNTRY
			export INPUT_COUNTRY="${INPUT_COUNTRY}"
			read -r -p "Gültigkeitsdauer des Zertifikats in Tagen (default: '$VALIDITY_DAYS'): " input_validity_days
			export VALIDITY_DAYS="${input_validity_days:-$VALIDITY_DAYS}"
		else
			echo "Keystore für TLS:"
			read -r -p "SVWS_TLS_KEYSTORE_PATH: " SVWS_TLS_KEYSTORE_PATH
			export SVWS_TLS_KEYSTORE_PATH="${SVWS_TLS_KEYSTORE_PATH}"
			read -r -p "SVWS_TLS_KEYSTORE_PASSWORD: " SVWS_TLS_KEYSTORE_PASSWORD
			export SVWS_TLS_KEYSTORE_PASSWORD="${SVWS_TLS_KEYSTORE_PASSWORD}"
			read -r -p "SVWS_TLS_KEY_ALIAS: " SVWS_TLS_KEY_ALIAS
			export SVWS_TLS_KEY_ALIAS="${SVWS_TLS_KEY_ALIAS}"
		fi

		echo ""
		echo "Installation auf: "
		echo "Host: $(hostname) - $(hostname -I | cut -d' ' -f1)"
		echo ""
		echo "MariaDB-Konfiguration:"
		echo "  CREATE_MARIADB: $CREATE_MARIADB"
		echo "  MARIADB_ROOT_PASSWORD: $MARIADB_ROOT_PASSWORD"
		echo "  MARIADB_HOST: $MARIADB_HOST"

		echo ""
		echo "Testdatenimport:"
		echo "  CREATE_TESTDATA: $CREATE_TESTDATA"
		if is_yes "$CREATE_TESTDATA"; then
			echo "  MARIADB_DATABASE: $MARIADB_DATABASE"
			echo "  MARIADB_USER: $MARIADB_USER"
			echo "  MARIADB_PASSWORD: $MARIADB_PASSWORD"
			echo "  TESTDATA_DOWNLOAD_URL: $TESTDATA_DOWNLOAD_URL"
			echo "  TESTDATA_SQLITE_RELATIVE_PATH: $TESTDATA_SQLITE_RELATIVE_PATH"
			echo "  FORCE_TESTDATA_IMPORT: $FORCE_TESTDATA_IMPORT"
		fi

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
		echo "  Gültigkeitsdauer des Zertifikats: $VALIDITY_DAYS Tage"

		read -r -p "Sind alle Einstellungen korrekt? (j/N): " CONFIRM
		if ! is_yes "$CONFIRM"; then
			echo "Bitte Skript erneut ausführen."
			exit 0
		fi
	fi

	echo "Erstelle .env-Datei und schreibe Konfiguration hinein:"
	persist_env_file
fi

echo "Lade Abhängigkeiten ..."
apt update
apt-get -y install gettext unzip wget curl dirmngr gnupg2 apt-transport-https sed grep mariadb-client
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

if is_yes "$CREATE_MARIADB"; then
	echo "Lade MariaDB 11.7 ..."
	curl -LsS https://downloads.mariadb.com/MariaDB/mariadb_repo_setup | bash -s -- --mariadb-server-version=11.7 --skip-maxscale --skip-tools
	apt-get -y install mariadb-server
	echo "MariaDB 11.7 erfolgreich installiert."
fi

echo "Lade SVWS ..."
download_installer_if_needed

tar xzf "./$installer_archive"

mkdir -p "$APP_PATH" "$APP_PATH/client" "$APP_PATH/adminclient" "$APP_PATH/conf" "$CONF_PATH"

cp -r ./svws/app "$APP_PATH"
cp -r ./svws/conf/. "$CONF_PATH"

unzip -d "$APP_PATH/client" "$APP_PATH/app"/SVWS-Client*.zip
rm -rf "$APP_PATH/app"/SVWS-Client*.zip

unzip -d "$APP_PATH/adminclient" "$APP_PATH/app"/SVWS-Admin-Client*.zip
rm -rf "$APP_PATH/app"/SVWS-Admin-Client*.zip

envsubst < ./svws/svws-template.service > ./svws/svws.service
cp ./svws/svws.service /etc/systemd/system/

if is_yes "$CREATE_KEYSTORE"; then
	echo "Erstelle Keystore in $SVWS_TLS_KEYSTORE_PATH/keystore ..."
	export HOSTNAME
	HOSTNAME=$(hostname)
	keytool -genkey -noprompt -alias "$SVWS_TLS_KEY_ALIAS" -validity "$VALIDITY_DAYS" \
		-dname "CN=${INPUT_COMMON_NAME}, OU=${INPUT_ORGANIZATIONAL_UNIT}, O=${INPUT_ORGANIZATION}, L=${INPUT_LOCALITY}, S=${INPUT_STATE}, C=${INPUT_COUNTRY}" \
		-ext "SAN=DNS:localhost,IP:127.0.0.1,DNS:${HOSTNAME}" \
		-keystore "$SVWS_TLS_KEYSTORE_PATH/keystore" \
		-storepass "$SVWS_TLS_KEYSTORE_PASSWORD" \
		-keypass "$SVWS_TLS_KEYSTORE_PASSWORD" \
		-keyalg RSA
	keytool -export -keystore "$SVWS_TLS_KEYSTORE_PATH/keystore" -alias "$SVWS_TLS_KEY_ALIAS" -file ./SVWS.cer -storepass "$SVWS_TLS_KEYSTORE_PASSWORD"
else
	if [ ! -f "$SVWS_TLS_KEYSTORE_PATH/keystore" ]; then
		echo "Keystore-Datei '$SVWS_TLS_KEYSTORE_PATH/keystore' nicht gefunden."
		exit 1
	fi
fi

render_svws_config
ln -sf "$CONF_PATH/svwsconfig.json" "$APP_PATH/svwsconfig.json"

if is_yes "$CREATE_MARIADB"; then
	envsubst < ./init-scripts/init-template.sql > ./init-scripts/init.sql
	mysql < ./init-scripts/init.sql
fi

import_testdata

cd "$script_dir"

echo "Lösche das Verzeichnis 'svws' im Home-Verzeichnis ..."
rm -rf ./svws

echo "Lösche das Verzeichnis 'init-scripts' im Home-Verzeichnis ..."
rm -rf ./init-scripts

echo "richte SVWS als Service ein ..."
if ! getent group svws >/dev/null 2>&1; then
	/usr/sbin/groupadd -r svws
fi
if ! id -u svws >/dev/null 2>&1; then
	/usr/sbin/useradd -r -s /bin/false -g svws svws
fi

chown -R svws:svws "$APP_PATH"
chown -R svws:svws "$CONF_PATH"

systemctl daemon-reload
systemctl start svws.service
systemctl enable svws.service
systemctl status svws.service --no-pager
