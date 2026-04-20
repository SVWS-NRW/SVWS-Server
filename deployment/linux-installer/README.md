# Readme

Dieses Skript ist für die Installation von SVWS auf einem Debian-basierten System gedacht.
## Voraussetzungen

- Ein Debian-basiertes Betriebssystem
- Zugriff mit Root-Rechten

## Verwendung

- Laden Sie das Skript auf den Zielcomputer herunter.
- Öffnen Sie die Terminalanwendung und navigieren Sie zum Verzeichnis, in dem sich das Skript befindet.
- Geben Sie den Befehl chmod +x /install.sh ein und drücken Sie die Eingabetaste.
- Geben Sie den Befehl ./install.sh ein und drücken Sie die Eingabetaste.
- Folgen Sie den Anweisungen im Skript.

## Konfiguration

Das Skript bietet verschiedene Optionen zur Konfiguration. Wenn Sie die Standardeinstellungen verwenden möchten, können Sie die Option **--default** verwenden, um die Konfiguration zu vereinfachen.

Folgende Konfigurationen können vorgenommen werden:

- MariaDB-Konfiguration
- Installationspfade
- Erstellung eines Keystores für TLS

## Hinweis zu Testdaten

Der Installer legt bewusst keine Testdaten an. Für den produktiven Betrieb
wird ein leeres Schema erstellt; Testdaten sind nicht mehr Teil des
Installationspfades. Wer eine befüllte Datenbank für Testzwecke benötigt,
kann ein bestehendes Schema über den Admin-Client bzw. den Migrations-/
Import-Endpunkt (`POST /api/schema/import/{schema}/sqlite`) einspielen.


## Wichtige Hinweise

- Verwenden Sie dieses Skript auf eigene Gefahr.
- Führen Sie das Skript nur auf einem Testsystem durch, bevor Sie es auf einem Produktivsystem verwenden.
- Stellen Sie sicher, dass alle Konfigurationen und Passwörter sicher gespeichert und aufbewahrt werden.
- Beachten Sie, dass das Skript eine Internetverbindung benötigt, um bestimmte Pakete herunterzuladen und zu installieren.

