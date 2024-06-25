# SVWS-Server

![logo](https://doku.svws-nrw.de/assets/wappenzeichen-nrw_farbig_rgb_300.iMHmQGsd.png)

Willkommen zum offiziellen Repository des SVWS-Servers (Schulverwaltungssoftware Nordrhein-Westfalen). Dieses Repository enthält den Code für den Server, der die Verwaltung und Speicherung schulischer Daten ermöglicht.

## Inhaltsverzeichnis

- [Über das Projekt](#über-das-projekt)
- [Releases](#releases)
- [Installation für Entwickler](#installationfürEntwickler)
- [Verwendung](#verwendung)
- [Beitragen](#beitragen)
- [Lizenz](#lizenz)
- [Kontakt](#kontakt)

## Über das Projekt

Der SVWS-Server ist ein zentraler Bestandteil der Schulverwaltungssoftware Nordrhein-Westfalen (SVWS). 

Das Projekt hat sich das Zeil gesetzt, für die Zukunft eine plattformunabhängige Web-Applikation für die Schulen in NRW zu schaffen.

Dieses Repository enthält den Quellcode des Servers sowie Anleitungen zur Installation und Nutzung.

Weitere Informationen findest du in der [offiziellen Dokumentation](https://doku.svws-nrw.de/).

Das Handbuch für den Webclient werden wir hier aufbauen:
[Anwenderhandbuch](https://help.svws-nrw.de/) 

## Releases

Die fertigen (Pre-) Releases zur Installation findest du hier:
https://github.com/SVWS-NRW/SVWS-Server/releases

Hier findet man den zu den Releases gehörigen Container:
https://hub.docker.com/r/svwsnrw/svws-server

Die npm-Packages, die auch in anderen Projekten verwendet werden dürfen:
https://www.npmjs.com/~svws-nrw

Maven-Packages ebenfalls zur freien Verwendung:
https://central.sonatype.com/namespace/de.svws-nrw

## Installation für Entwickler

### Voraussetzungen

Stelle sicher, dass die folgenden Voraussetzungen erfüllt sind, bevor du mit der Installation beginnst:
- Git
- Java 21
- Gradle 8 (enthalten)
- MariaDB 10.6 oder höher

### Schnell-Anleitung

1. Klone das Repository:
    ```bash
    git clone https://github.com/SVWS-NRW/SVWS-Server.git
    ```
2. Navigiere in das Projektverzeichnis:
    ```bash
    cd SVWS-Server
    ```
3. Richte die `svwsconfig.json` ein gemäß der [Einrichtungsanleitung](https://doku.svws-nrw.de/Deployment/Einrichtung/).
4. Erstelle das Projekt mit Gradle:
    ```bash
    ./gradlew build
    ```
5. Starte den Server:
    ```bash
    ./SVWS-Server/svws-server-app/startserver.sh
    ```
Beispiele für die Installation der Entwicklungsumgebung:
https://doku.svws-nrw.de/Entwicklungsumgebungen/

## Verwendung

Nach dem Starten des Servers kannst du über `https://localhost` auf die Weboberfläche zugreifen. Weitere Konfigurationsoptionen und Anleitungen findest du in der [offiziellen Dokumentation](https://doku.svws-nrw.de/).

## Beitragen

Beiträge zur Verbesserung des SVWS-Servers sind willkommen. Um beizutragen, folge bitte diesen Schritten:

1. Forke das Repository.
2. Erstelle einen neuen Branch:
    ```bash
    git checkout -b feature/NeuesFeature
    ```
3. Nimm deine Änderungen vor und committe sie:
    ```bash
    git commit -m "Beschreibe deine Änderungen"
    ```
4. Push deinen Branch:
    ```bash
    git push origin feature/NeuesFeature
    ```
5. Erstelle einen Pull-Request.

Bitte lies die [Beitragsrichtlinien](CONTRIBUTING.md) für weitere Details.

## Lizenz

Dieses Projekt ist lizenziert unter der BSD 3-Clause Lizenz. Weitere Informationen findest du in der [LICENSE](LICENSE)-Datei.

## Kontakt

Wenn du Fragen oder Anmerkungen hast, eröffne bitte ein Issue auf GitHub.

Vielen Dank für deinen Beitrag zur Verbesserung des SVWS-Servers!
