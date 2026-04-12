# Readme

Dieses Skript ist für die Installation von SVWS auf einem Debian-basierten System gedacht.

## Voraussetzungen

- Ein Debian-basiertes Betriebssystem
- Zugriff mit Root-Rechten
- Internetzugriff für Paketdownloads
- Für Seed-Installationen zusätzlich Internetzugriff auf `SVWS-TestMDBs`

## Verwendung

- Laden Sie das Skript auf den Zielcomputer herunter.
- Öffnen Sie die Terminalanwendung und navigieren Sie zum Verzeichnis, in dem sich das Skript befindet.
- Machen Sie das Skript ausführbar: `chmod +x ./install.sh`
- Starten Sie das Skript: `./install.sh`
- Folgen Sie den Anweisungen im Skript.

Wenn Sie die Standardeinstellungen verwenden möchten, können Sie den Installer auch mit `./install.sh --default` ausführen.
Liegt im Arbeitsverzeichnis bereits eine `.env`, verwendet der Installer jedoch diese Werte weiter. Für einen echten Default-Lauf muss daher keine alte `.env` vorhanden sein.

## Seed-Installation mit vollständigen Testdaten

Der Linux-Installer kann optional vollständige Testdaten importieren.

Bei aktivem Testdatenimport gilt:

- die Quelle entspricht dem Docker-Pfad
- das Archiv wird von `SVWS-TestMDBs` geladen
- standardmäßig wird `GOST_Abitur/Abi-Test-Daten-01/GymAbi.sqlite` verwendet
- der Import erfolgt einmalig während der Installation
- der Import verwendet `de.svws_nrw.db.utils.app.ImportDB`
- für eine Seed-Installation müssen `MARIADB_DATABASE`, `MARIADB_USER` und `MARIADB_PASSWORD` gesetzt sein

Der Testdatenimport wird interaktiv über `CREATE_TESTDATA` aktiviert oder über eine vorbereitete `.env`-Datei gesteuert.
Im interaktiven Modus fragt der Installer die nötigen Schema-Werte bei aktivem Seed-Import ab. In `.env`-basierten Läufen müssen diese Werte bereits vollständig vorhanden sein.

## Relevante Variablen

### Allgemein

- `CREATE_MARIADB`
- `CREATE_KEYSTORE`
- `CREATE_TESTDATA`
- `MARIADB_ROOT_PASSWORD`
- `MARIADB_HOST`
- `APP_PATH`
- `CONF_PATH`
- `APP_PORT`

### Seed-Import

- `MARIADB_DATABASE`
- `MARIADB_USER`
- `MARIADB_PASSWORD`
- `TESTDATA_DOWNLOAD_URL`
- `TESTDATA_SQLITE_RELATIVE_PATH`
- `FORCE_TESTDATA_IMPORT`

Standardwerte für Seed-Installationen:

- `MARIADB_DATABASE=GymAbi01`
- `MARIADB_USER=svwsadmin`
- `TESTDATA_DOWNLOAD_URL=https://github.com/SVWS-NRW/SVWS-TestMDBs/archive/refs/heads/main.zip`
- `TESTDATA_SQLITE_RELATIVE_PATH=SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.sqlite`
- `FORCE_TESTDATA_IMPORT=N`

## Schutz vor stiller Überschreibung

Existiert das Zielschema bereits, bricht der Seed-Import standardmäßig mit einer klaren Fehlermeldung ab.

Nur wenn `FORCE_TESTDATA_IMPORT=J` gesetzt ist, wird der Import trotz vorhandenen Schemas versucht.

Damit sollen unbeabsichtigte Überschreibungen bestehender Test- oder Produktivdaten vermieden werden.

## Konfigurationsdateien

Der Installer verwendet zwei Konfigurations-Templates:

- DB-fähige Variante für Seed-Installationen
- No-DB-Variante für normale Installationen ohne vorkonfiguriertes Schema

Damit bleibt normales Installer-Verhalten erhalten, während Seed-Installationen direkt mit einer schemafähigen `svwsconfig.json` ausgestattet werden.

## Automatisierte Seed-Installation per `.env`

Ein vorbereiteter Installationslauf kann über eine `.env`-Datei gesteuert werden. Relevante Einträge sind insbesondere:

```env
CREATE_MARIADB=J
CREATE_KEYSTORE=J
CREATE_TESTDATA=J
MARIADB_ROOT_PASSWORD=changeit-root
MARIADB_HOST=localhost
MARIADB_DATABASE=GymAbi01
MARIADB_USER=svwsadmin
MARIADB_PASSWORD=changeit-schema
TESTDATA_DOWNLOAD_URL=https://github.com/SVWS-NRW/SVWS-TestMDBs/archive/refs/heads/main.zip
TESTDATA_SQLITE_RELATIVE_PATH=SVWS-TestMDBs-main/GOST_Abitur/Abi-Test-Daten-01/GymAbi.sqlite
FORCE_TESTDATA_IMPORT=N
APP_PATH=/opt/app/svws
CONF_PATH=/etc/app/svws/conf
APP_PORT=8443
```

Wenn eine `.env` vorhanden ist, verwendet der Installer diese Werte direkt für einen reproduzierbaren Lauf.

## Wichtige Hinweise

- Verwenden Sie dieses Skript auf eigene Gefahr.
- Führen Sie das Skript nur auf einem Testsystem durch, bevor Sie es auf einem Produktivsystem verwenden.
- Stellen Sie sicher, dass alle Konfigurationen und Passwörter sicher gespeichert und aufbewahrt werden.
- Der Seed-Pfad benötigt Internetzugriff für den Download des Testdatenarchivs.
- Der Update-Pfad führt keinen automatischen Re-Seed durch.
