# SVWS Testumgebung
# SVWS Testumgebung
1. [Testumgebung einrichten](#testumgebung-einrichten)
2. [Aufbau des Projekts](#aufbau-des-projekts)
   1. [Docker Runner](#docker-runner)
   2. [Compose & Override, .env](#compose-override-und-umgebungsvariablen)
   2. [Gradle Plugin](#subproject-plugin)
   3. [Testing build.gradle](#testing-build-gradle)
   4. [Testspezifisches Gradle](#testspezifisches-gradle)
   5. [Buildumgebungen](#buildumgebungen)
   6. [local.properties](#local-properties)
   7. [gitlab-ci.yml](#gitlab-ci-yml)
   8. [Vergleich local.properties vs gitlab-ci.yml](#vergleich-local-properties-vs-gitlab-ci-yml)
3. [Änderungen an anderen Gradle-Modulen](#aenderungen-an-anderen-gradle-modulen)
4. [Neue Subprojekte zufügen](#neue-subprojekte-zufuegen)

## Testumgebung einrichten
### Testumgebung einrichten - kurz
1. [Docker Installation](https://docs.docker.com/desktop/)
2. `docker network create gitlab_runner_network`
3. Kopieren und anpassen von `local.properties` aus `local.properties.example` nach `testing/svws-*/`
4. `./gradlew build`
5. `./gradlew testing:integrationTest`

### Testumgebung einrichten - lang
Die Testumgebung benötigt eine lokale Dockerinstallation, siehe [Docker Installation](https://docs.docker.com/desktop/).  Installieren und prüfen, ob die Installation geklappt hat:

```
critter@critter-Virtual-Machine:~$ docker --version
Docker version 24.0.4, build 3713ee1
```

Weiterhin benötigen die für die Testumgebung ausgeführten Container ein Dockernetzwerk, um miteinander ohne Umstände über ihre Container-Namen zu kommunizieren:

```
critter@critter-Virtual-Machine:~/git/SVWS-Server$ docker network create gitlab_runner_network
728c2bd6ac200cb8fe8b662862c960406e3bd358a5f57585d5b059dcdbeb2dbb
critter@critter-Virtual-Machine:~/git/SVWS-Server$ docker network ls
NETWORK ID     NAME                    DRIVER    SCOPE
*
*
728c2bd6ac20   gitlab_runner_network   bridge    local
*
*
```

Für die lokale Ausführung sind in den einzelnen Subprojekten lokale Property-Files anzulegen, ein dokumentiertes Beispiel findet sich in `SVWS-Server/testing/local.properties.example`.

**Wichtig:** Mit diesen Propertyfiles werden die Host-Ports zur Testdurchführung gesteuert, so dass mehrere Testumgebungen parallel gestartet sein können. Freie Ports des Hostsystems nutzen, sowohl für die DB-Container als auch für den SVWS-Container.

#### Ausführen von Tests
Es gibt eine Reihe von Gradle-Tasks, welche für die lokale Ausführung von Bedeutung sind. Vor Ausführung ist derzeit ein lokaler Build zu empfehlen, um Konflikte in der Ausführungsreihenfolge zu vermeiden.

`./gradlew testing:integrationTest` ist der grundlegende Task, um alle Tests der Subprojekte auszuführen. Dabei werden:

1. die Docker-Container mit den nötigen Umgebungsvariablen vorbereitet
2. die Testumgebungen mit DB und Container hochgefahren,
3. die Tests gegen die definierten Systeme ausgeführt,
4. die Testergebnisse (junit.xml) eingesammelt
5. und im Anschluss die Testumgebungen runtergefahren
6. sollten die Tests `failure` oder `error`gehabt haben, gibt es folgende Fehlermeldung:
`"The build finished, but tests resulted with ${totalErrors} errors and ${totalFailures} failures! "`.

Testergebnisse finden sich in ``SVWS-Server/testing/subproject/build/test-results/``
**Wichtig:** Fehlerhafte/Tests führen nicht zum Abbruch der Gradle-Ausführung, sondern werden am Ende über die junit.xml zusammengefasst.

Darüber hinaus haben die Subprojekte einzelne Tasks, mit denen die Container für diese Subprojekte individuell gesteuert werden können, beispielhaft für ``svws-webclient-integration-test``

`testing:svws-webclient-integration-test:startTestumgebung` führt die notwendigen Schritte zur Erstellung der Container durch und fährt diese hoch.
``docker ps`` zeigt den Status der Container, ``docker logs <id>`` das aktuelle sysout der startup Skripte des jeweiligen Containers.

`testing:svws-webclient-integration-test:stopTestumgebung` stoppt die Container.

`testing:svws-webclient-integration-test:apiTest` führt die Tests des Subprojekts gegen die konfigurierte Umgebung durch. Dieser Task startet sich eine eigene Testumgebung, auch eine vorhandene Umgebung wird neu gestartet, im Anschluss wird die Testumgebung heruntergefahren.

## Aufbau des Projekts
### Docker Runner
Dieses Repository enthält die Dateien für einen Docker Runner, der in einem Docker Container auf Basis von Eclipse Temurin 17 läuft. Der Docker Runner ist mit der Docker CLI ausgestattet und kann über eine Socket-Verbindung auf den Docker Daemon des Hosts zugreifen. Dies ermöglicht das Erstellen, Bauen und Stoppen von Docker Containern innerhalb der Testumgebung.

Das Image wird in [Docker-Hub](https://hub.docker.com/r/svwsnrw/dockerrunner) gehostet und wird in der CI Pipeline verwendet, ist für die lokale Verwendung derzeit nicht von Bedeutung.

### Compose, Override und Umgebungsvariablen
Die Tests werden mithilfe von Docker Compose ausgeführt, wobei je zwei Container pro Testprojekt gestartet werden: ein Container für die MariaDB-Datenbank und ein Container für die Anwendung, gegen die die Tests durchgeführt werden. Die Compose Files befinden sich unter `/testing/testumgebung`

#### Verwendete Komponenten
- Docker Compose: Docker Compose wird verwendet, um die Container für die Testumgebung bereitzustellen und zu verwalten.

#### Konfiguration der Container
Die Container werden über ein `.env` File mit den benötigten Parametern (Ports, Namen etc.) versorgt. Dieses wird über den Gradle task `subproject:createEnv` (vgl. [subproject-plugin.gradle](#subproject-plugin)) erzeugt. Da nur bei lokaler Ausführung die Ports der Container vom Host erreichbar sein sollen, werden wird das Forwarding in (`testing/testumgebung/docker-compose.override.yml`) ergänzt und kann dann abhängig von der Umgebung (CI vs. lokal) ins Buildverzeichnis übernommen werden.

Für den SVWS-App-Container finden sich in `testing/svws` das `svws-config.json` für den App-Server, das `startup.sh`, welches auf dem Container beim Start ausgeführt wird, sowie die ìnit-scripts`, welche im App-Container beim Start ausgeführt werden, um den Keystore und die Datenbank zu initialisieren.

#### Umgebungsvariablen
Das `.env`-File für Docker-Compose setzt sich wie folgt zusammen:
- INIT_SCRIPTS_DIR: das Verzeichnis, in dem sich die Initscripte, welche beim Start des SVWS-Containers ausgeführt werden, befinden
- MariaDB_DATABASE: der Datenbankname der MariaDB-Instanz
- SVWS_TLS_KEYSTORE_PATH: der Pfad zum Keystore innerhalb des Containers
- SVWS_TLS_KEY_ALIAS*: der Alias des verwendeten Keystores
- SVWS_TLS_KEYSTORE_PASSWORD*: das Passwort des verwendeten Keystores
- MariaDB_ROOT_PASSWORD*: das zu verwendende Root-Passwort für den MariaDB-Container
- MariaDB_USER*: der zu verwendende Nutzer für den MariaDB-Container
- MariaDB_PASSWORD*: das Passwort des MariaDB_USER
- MariaDB_HOST: der Hostname der zu testenden MariaDB, wird an die SVWS-Konfiguration gereicht
- SVWS_CONTAINER_NAME: der Hostname für den SVWS-App-Container innerhalb des Dockernetworks, festgelegt als `${project.name-svws}`
- DB_CONTAINER_NAME: der Hostname für den SVWS-App-Container innerhalb des Dockernetworks, festgelegt als `${project.name-db}`
- TESTDB_PASSWORD*: das bekannte Passwort für die Beispieldatenbanken, ggf. bei der Projektleitung erfragen

Bei lokaler Ausführung weiterhin:
- SVWS_PORT*: der Host-Port an den der SVWS-App-Container seinen internen Port 8443 weiterleitet
- DB_PORT*: der Host-Port an den der DB-Container seinen internen Port 3306 weiterleitet

Die mit * markierten Variablen erhalten ihre Werte aus Gradle-Umgebungsvariablen bzw. den `local.properties`.

### Subproject Plugin
Die Tasks für die einzelnen Subprojekte sind in `testing/subproject-plugin.gradle` definiert. Darüber hinaus wird in diesem Skript die Umgebung anhand des Vorhandenseins der `local.properties` und/oder der Gradle-Umgebungsvariable `-Penvironment` analysiert und die für die Projekte notwendigen Umgebungsvariablen gesetzt.
- wenn `project.hasProperty("environment")` wird dieses genutzt
- sonst wird versucht die `local.properties` zu lesen
- ansonsten gilt die Umgebung als `undefined`

Tasks:
- `createEnv` erstellt das `.env` für das `docker compose` in `testing/svws-*/build/testumgebung`
- `startTestumgebung` kopiert das `docker-compose.yml` und bei lokaler Umgebung das `docker-compose.override.yml` nach `testing/svws-*/build/testumgebung/${project.name}` und führt ein `docker compose up --force-recreate --wait` aus
- `stopTestumgebung` führt ein `docker compose down` aus.


### Testing build.gradle
In `SVWS-Server/testing/build.gradle` sind die auszuführenden Test-Tasks aufgelistet. Außerdem gibt es Tasks:
- `copyArtifacts` zum Kopieren der Build-Artefakte für den SVWS-App-Container,
- `buildSVWSApplication` zum Erstellen des Images des SVWS-App-Containers,
- `analyzeTestResults` zum Analysieren der Testergebnisse in den junit.xmls
- und `integrationTest` zum Manipulieren und Ausführen der aufgelisteten Test-Tasks in den Subprojekten

`integrationTest` fügt dabei jedem gelisteten Test-Task die Abhängigkeit auf das Starten der Testumgebung und das `finalizedBy stopTestumgebung` hinzu und setzt das Property `ignoreFailures=true`, damit fehlerhafte Tests das Ausführen weiterer Tests nicht verhindern.

Zu guter Letzt definiert dieses build.gradle die Variable `ext.excludeFromTestsDuringBuild=true`, womit die globale Testhook nicht mehr Tasks des Typ `Test` innerhalb der Subprojekte ausführt. Dies wird erreicht, indem im `SVWS-Server/build.gradle` folgendes enthalten ist:

```
	test {
		onlyIf {
			!project.hasProperty('excludeFromTestsDuringBuild')
		}
	    useJUnitPlatform()
	}
```


### Testspezifisches Gradle
Die einzelnen Test-Projekte enthalten jeweils ihr eigenes build.gradle, in denen ein Task vom Typ Test definiert ist.

Mit `apply from: ('../subproject-plugin.gradle')` wird das Plugin zugefügt.

Weiterhin wird die hier die Testausführung konfiguriert und ggf. Abhängigkeiten ergänzt.


### Buildumgebungen
Wir haben derzeit einen unfertigen Entwurf für den Umgang mit verschiedenen Ausführungsumgebungen. Uns war wichtig zu unterscheiden zwischen:
- *testing* - Ausführung im Gitlab-CI
- *local* - Ausführung in konfigurierter lokaler Umgebung, wenn lokal die nötigen Parameter vorhanden sind
- *undefined* - ungestörtes Verhalten der vorhandenen Tasks (u.a. build), auch wenn die nötigen Parameter für die Ausführung der Testumgebung nicht vorhanden sind

Das ist ein eingeschränkter Scope, der sich im Projektverlauf erweitern kann - bspw. mit Buildumgebungen für Auto-Deploy, Auto-Publish etc, daher ist das bisherige als Entwurf zu betrachten, der zur Diskussion steht.

### local.properties
Für die lokale Ausführung der Testumgebung muss in jedem Testprojekt ein `local.properties` vorhanden sein, welches die lokal-individuellen Parameter für die jeweilige Testumgebung konfiguriert. Unter `testing/local.properties.example` findet sich ein kommentiertes Beispiel, um die Konfiguration vorzunehmen.

### gitlab-ci.yml
Die `SVWS-Server/gitlab-ci.yml` wurde um eine weitere Stage `integration-tests` ergänzt, welche `./gradlew testing:integration-test` ausführt. Die Stage ist abhängig von der Build-Stage und baut auf die Artefakte auf. Als eigene Artefakte werden die JUnit-Testergebnisse für 7 Tage aufbewahrt. Aus den CI-Variablen des gitlab werden folgende Parameter an das Gradle-Skript gereicht:
- `-Penvironment=testing` (die Bezeichnung der Umgebung)
- `-PMDB_PASSWORD=$MDB_PASSWORD`
- `-PMariaDB_ROOT_PASSWORD=$MariaDB_ROOT_PASSWORD`
- `-PMariaDB_PASSWORD=$MariaDB_PASSWORD`
- `-PSVWS_TLS_KEYSTORE_PASSWORD=$SVWS_TLS_KEYSTORE_PASSWORD`
- `-PMariaDB_USER=$MariaDB_USER`

### Vergleich local.properties vs gitlab-ci.yml
Die in der `gitlab-ci.yaml`(#gitlab-ci-yml) beschriebenen Parameter für das Gradle-Skript finden sich für lokale Ausführung auch im `local.properties`. Darüber hinaus wird im `local.properties` die Ports des Hostsystems für DB- und App-Container des jeweiligen Testprojekts, sowie die der Hostname mit Protokoll angegeben und an die Tests durchgereicht. Da innerhalb der CI die Ausführung der Tests im selben Dockernetwork stattfindet, müssen die Hostnamen und Ports nicht konfigurierbar gehalten werden, sondern sind über den Container-Namen und Standard-Ports (8443 bzw 3306) erreichbar. Die lokale Ausführung kann sowohl aus der Entwicklungsumgebung heraus als auch mit den vorhandenen Gradle-Tasks stattfinden und muss daher konfigurierbar bleiben, bspw. gegen andere DB-Schemas oder andere SVWS-App-Instanzen (sowohl in Containern als auch anderweitig gehostete).

## Aenderungen an anderen Gradle-Modulen
### SVWS-Server
Im `SVWS-Server/build.gradle` musste ergänzt werden, dass sich Integrations- und API-Tests nicht während der normalen Testhook bspw. während des Build ausführen.

```
	test {
		onlyIf {
			!project.hasProperty('excludeFromTestsDuringBuild')
		}
	    useJUnitPlatform()
	}
```

### svws-webclient
Die vorhandenen API-Tests im Client wurden paramtriert. Dies wird mit den Umgebungsvariablen am Task `svws-webclient:testApi` durch die vorgenommene Konfiguration in `testing:svws-webclient-integration-test:apiTest` erreicht:

```
environment NODE_TLS_REJECT_UNAUTHORIZED: 0
environment VITE_svws_testing_api_host: targetHost
environment VITE_svws_testing_api_port: targetPort
```

Verwendet werden die Variablen über `import.meta.env`, das führende `VITE_` ist Konvention.

Die Templates unter `svws-webclient/config/core` wurden um
- einen eigenen Runtask mit dem nötigen Reporter,
- Modulnamen als Platzhalter für die VITest-Ausgaben
- excludes für die VITest-Ausführung

ergänzt. Die excludes werden im `svws-webclient/build.gradle` als Properties deklariert (`project.ext.npm_properties`):

```
	npm_exclude_from_tests: 'api-test","browser-test',
	npm_exclude_from_api_tests: 'core-test","client-test","browser-test',
```

und in den jeweiligen copyConfig*-Tasks verwendet.

Darüber hinaus wird der npmRun-Task `testing:testApi` aus dem `testing:svws-webclient-integration-test:apiTest` heraus umkonfiguriert, um o.g. Umgebungsvariablen zu nutzen, den Exit-Value des npmRun zu ignorieren und eine Wiederholbarkeit der Tests trotz vorhandenem Result zu ermöglichen.

### Settings.gradle
Um die Subprojekte dem Gesamtprojekt zuzufügen, wurden sie in der `SVWS-Server/settings.gradle ergänzt`.

## Neue Subprojekte zufuegen

Anhand der Beispiele `testing/svws-dav-api-test` für Java-Tests und `testing/svws-webclient-test` für Typescript/VI-Tests lassen sich weitere Subprojekte für das Testen in abgeschlossenen Umgebungen aufsetzen. Dazu sind folgende allgemeinen Schritte notwendig:
- anlegen des Unterordners in `testing`
- anlegen des `build.gradle`
  - die Abhängigkeit zum Plugin ergänzen `apply from: ('../subproject-plugin.gradle')` ergänzen
  - einen Task vom Typ `Test` konfigurieren, dieser
    - sollte entweder direkt auf eine Testausführung verweisen (bspw. `useJUnitPlatform()`)
    - oder ein `dependsOn(...)` auf einen vorhandenen Task enthalten
      - muss `ignoreExitValue true` enthalten, damit die gesamte Testausführung durch Fehler nicht abgebrochen wird
      - muss `outputs.upToDateWhen {false}` enthalten, damit Tests wiederholbar ausgeführt werden können trotz vorhandenem Ergebnis
    - benötigt ggf. ein `finalizedBy` zu einem weiteren Task, um die Reports im junit.xml-Format zu kopieren
  - weitere Konfiguration und Abhängigkeiten, bspw. zu anderen Modulen oder Bibliotheken konfigurieren
- den erstellten Task vom Typ Test in `testing/build.gradle` in der Liste `testTasks` anfügen 
- ergänzen des `SVWS-Server/settings.gradle` um die Referenz zum neuen Subprojekt
- optional: für eigene DB-Schemas mit ggf. eigenen Datenbanken bzw. abweichenden Inhalten muss ein entsprechendes Init-Script im Ordner `init-scripts` angelegt werden





Änderungsvorschläge und Diskussionen werden gern entgegengenommen, vgl. #771
