# Integrationstests

In diesem Paket sind verschiedene Integrationstests implementiert. Hier können verschiedene Bereiche der Anwendung geprüft werden:

1. Ende-zu-Ende-Tests: Umfangreiche, langsame Tests gegen beliebige Teile der Anwendung, die das Verhalten eines Users simulieren.
2. API-Tests: Tests, welche Endpunkte des SVWS-Server-Backends testen. Dabei wird die transpilierte Klasse ApiServer.ts verwendet.
3. DAV-Tests: Tests, welche Endpunkte des DAV-Backends testen. Hier werden WebDAV-Endpunkte mit Hilfsfunktionen getestet.
4. ENM-Tests: Überprüft Endpunkte des ENM-Servers. Hier wird zusätzlich ein ENM-Server bereitgestellt und synchronisiert.

## Funktionsweise
Die Integrationstests in diesem Paket sind infrastrukturell ähnlich aufgebaut. Dabei gibt es drei Möglichkeiten, die Integrationstests auszuführen:

### Möglichkeit 1: Integrationstests über Gradle gegen für den Test-Durchlauf temporär aufgebaute Anwendungen
Hier wird für jedes Subpaket ein SVWS-Backend und eine Datenbank als Docker-Container gestartet. Dann laufen alle Tests vom Host-System gegen diese Container. Alle Integrationstests werden gleichzeitig gestartet. Dabei kann der Task über die IDE (Task > Verification > integrationTest) oder über die Konsole gestartet werden. Dieser Task wird von der GitLab CI/CD-Pipeline ausgeführt.

```
    ./gradlew :tests:integrationTest
```
Gradle führt nun für jedes Subpaket gleichzeitig folgende Schritte durch:

1. Überprüfen und ggf. Herunterladen von Abhängigkeiten
2. Erstellen von Docker-Images für die Anwendung und Datenbank
3. Starten der Anwendung(en) und Datenbank als Docker-Container
4. Befüllen der Datenbank mit Testdaten
5. Testen mit Vitest vom Host-System gegen die einzelnen Test-Dockercontainer
6. Rückbau aller Container und temporären Dateien
7. Aggregieren der Logs aus allen Tests und Subpaketen in einer Zusammenfassung

Dieser Durchlauf ist langsam, dafür sind alle Tests reproduzierbar. Da für jeden Durchlauf alle Daten neu bereitgestellt und dann aufgeräumt werden, können auch destruktive Tests genutzt werden.

> Dieser Gradle Task wird in der CICD Pipeline bei jedem Commit ausgeführt. Es sollte daher vor einem Commit lokal geprüft werden ob die Integrationstests scheitern.

### Möglichkeit 2: Integrationstests über Gradle gegen laufende Anwendung
Alle Integrationstests können gleichzeitig oder einzeln gestartet werden. Dabei kann der Task über die IDE (Task > Verification > no_auto_docker_integrationTest) oder über die Konsole gestartet werden. Wichtig: Hier muss die Anwendung und die Datenbank bereits laufen. Hier wird für den Test keine Anwendung gestartet, sondern alles gegen die aktuell laufende gerichtet. ENM-Tests werden hier nicht unterstützt.

Gebündelte Ausführung
```
   ./gradlew tests:no_auto_docker_integrationTest
```

Gradle führt nun für jedes Subpaket (außer ENM) gleichzeitig folgende Schritte durch:

1. Überprüfen der Abhängigkeiten
2. Testen mit Vitest vom Host-System gegen die laufende Anwendung und Datenbank. Destruktive Tests werden hier übersprungen.
3. Aggregieren der Logs aus allen Tests und Subpaketen in einer Zusammenfassung

Einzelne Ausführungen
```
   ./gradlew tests:no_auto_docker_APIServer
   ./gradlew tests:no_auto_docker_DAV
   ./gradlew tests:no_auto_docker_E2E
```

> Diese Möglichkeit benötigt einiges an manueller Vorbereitung. Sowohl müssen die Datenbank als auch das Backend bereits laufen, als auch die passenden Testdaten manuell bereitgestellt werden. Bei der Featureentwicklung verwenden, da schneller in der Ausführung.

### Möglichkeit 3: Integrationstests direkt über IDE ohne Gradle gegen laufende Anwendung
Tests können direkt in der IDE gestartet werden, insofern dies von der IDE unterstützt wird. Dabei werden die Tests ebenfalls wie in zweiten Möglichkeit gegen eine lokale Anwendung ausgeführt. So kann für die Testentwicklung auf die langsame Infrastruktur verzichtet werden. Hier muss die Anwendung bereits lokal laufen. ENM wird auch hier nicht unterstützt.

> Diese Möglichkeit benötigt einiges an manueller Vorbereitung. Sowohl müssen die Datenbank als auch das Backend bereits laufen, als auch die passenden Testdaten manuell bereitgestellt werden. Bei der Featureentwicklung verwenden, da schneller in der Ausführung.



# Erweiterte technische Dokumentation
Dieser Abschnitt geht weiter auf technische Besonderheiten ein

### Gradle Subprojects
Gradle wird häufig für Integrationstests verwendet. Dabei handelt es sich um ein [Multi-project build](https://docs.gradle.org/current/userguide/multi_project_builds.html).

- `tests/build.gradle`: Diese Datei enthält Gradle-Tasks, die einmalig vor und nach allen Tests ausgeführt werden. Hier sind unter anderem der Endpunkt zum Starten der Integrationstests, die Validierung der Infrastruktur sowie die Aggregation der Ergebnisse enthalten.

- `tests/subproject-plugin.gradle`: In dieser Datei werden Tasks definiert, die für jedes Subprojekt ausgeführt werden. Dazu gehören beispielsweise das Erstellen und Starten der Docker-Dateien.

- `tests/tests-*-*/build.gradle` in jedem Subprojekt: Diese Datei enthält spezifische Tasks und Konfigurationen, die für das jeweilige Subprojekt erforderlich sind.

### Docker
Docker wird bei den Integrationstests genutzt, um Testumgebungen für Testdurchläufe bereitzustellen. Dockerfiles und Docker Compose Files werden entweder auf der Grundlage eines Templates oder direkt im Gradle-Task erstellt. In beiden Fällen entstehen im jeweiligen Build-Ordner des Subprojekts die entsprechenden Docker-Dateien. Gradle startet diese Dateien, sodass Docker-Container auf verschiedenen Ports eine Anwendung und eine Datenbank bereitstellen. Nachdem die Tests durchgelaufen sind, räumt Gradle die entsprechenden Container wieder ab.

### Seeden der Datenbanken
Für die verschiedenen Tests werden unterschiedliche Testdatenbanken benötigt. Unter `config/tests/config.json` werden die downloadbaren Testdatenbanken definiert. In der jeweiligen `tests/tests-*-*/build.gradle` Datei kann dann angegeben werden, welche der verfügbaren Testdatenbanken geladen werden. Gradle nutzt in Folge die gleichen Endpunkte wie der Admin-Client, um die entsprechende Testdaten in das Backend zu laden, welches im Docker-Container läuft. Bei vielen der in Vitest geschrieben Tests kann dann der Name der Zieldatenbank hinterlegt werden. Das erlaubt, dass verschiedene Tests gegen verschiedene Datenbanken ausgeführt werden.
## Besonderheiten ENM
Der ENM Server ist eine eigenständige Applikation. Zusätzlich zum SVWS Backend und der Datenbank braucht es also einen weiteren Docker-Container, auf dem der ENM Server läuft. Die Infrastruktur für das Erzeugen und Starten der Docker-Dateien findet sich unter `\tests\tests-server-enm\custom-tasks.gradle`. Das Bereitstellen des ENM Servers wird automatisch bei der Ausführung der Integrationstests vorgenommen. Nachdem alle drei Container laufen, steuert Gradle eine Synchronisation zwischen dem SVWS Server und dem ENM Server, damit der ENM Server getestet werden kann. Dies geschieht über nacheinander ausgeführte Vitest-Tests.
## Besonderheiten Ende zu Ende
Ende zu Ende Tests können aufgezeichnet werden mit dem Gradle Task
```
   ./gradlew tests:test-e2e-client:recordE2ETest
```

## Entwicklung neuer Tests
Für die Entwicklung neuer Integrationstests kann entweder eines der bestehenden Subpakete verwendet oder ein neues Subpaket erstellt werden. Falls ein bestehendes erweitert werden soll, müssen lediglich die Vitest-Testdateien um die neuen Tests erweitert werden. Falls ein neues Subpaket erstellt wird, sollten folgende Schritte abgehandelt werden:
1. Erstellung eines neuen Ordners in gleicher Struktur
2. Kopiere eine der `build.gradle` und passe die Namen, Pfade und Konfigurationen an
3. Ergänze in `tests/build.gradle` die neuen Tasks in den Tasks *integrationTest*, *no_auto_docker_integrationTest* und *checkJUnitResults*

## Troubleshooting

Folgende Punkte können durchgeführt werden, um Komplikationen mit den Integrationstests zu vermeiden:
- Lösche folgende Ordner:
  - `tests/tests-e2e-client/build`
  - `tests/tests-server-dav/build`
  - `tests/tests-server-api/build`
  - `tests/tests-server-enm/build`
  - `svws-webclient/build`
  - `svws-webclient/enmserver/build`
  - `download/testDatenbanken`
- Entferne alle laufenden Docker-Container, die mit dem Projekt zu tun haben
- Entferne alle Docker-Builds und -Images, die mit dem Projekt zu tun haben
- Eventuell - nur wenn die Konsequenzen klar sind - `docker system prune`
- Schließe alle laufenden Datenbanken und Anwendungen, die mit dem Projekt zu tun haben
- Schließe alle IDEs
- Eventuell: `git pull`
- Neues Build vom Projekt
- Neue Ausführung der Integrationstests

