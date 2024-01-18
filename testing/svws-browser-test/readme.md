# SVWS Testumgebung für Browsertests
Hier sind Änderungen zwischen der Ausführung von  Browsertests und den Integrationtests dokumentiert. Für undokumentierte Punkte vgl. [/testing/readme.md](../readme.md)

# SVWS Testumgebung
1. [Testumgebung einrichten](#testumgebung-einrichten)
2. [Aufbau des Projekts](#aufbau-des-projekts)
3. [Änderungen an anderen Gradle-Modulen](#aenderungen-an-anderen-gradle-modulen)

## Testumgebung einrichten
### Testumgebung einrichten - kurz
1. [Docker Installation](https://docs.docker.com/desktop/)
2. `docker network create gitlab_runner_network`
3. Kopieren und anpassen von `local.properties` aus `local.properties.example` nach `testing/svws-browser-test/`
4. `./gradlew build`
5. In `svws-webclient/browser-test/` sowohl `npm playwright install` als auch `sudo npm playwright install-deps` für die Browser-Installation ausführen, zweiteres benötigt für Rootrechte und funktioniert nur unter Linuxdistributionen mit apt. // TODO Ob install-deps auf Mac und Windows nötig sind und welche Berechtigung erforderlich ist.
6. `./gradlew testing:svws-browser-test:runBrowserTest`

### Testumgebung einrichten - lang
[/testing/readme.md](../readme.md)

#### Ausführen von Tests
Es gibt eine Reihe von Gradle-Tasks, welche für die lokale Ausführung von Bedeutung sind. Vor Ausführung ist derzeit ein lokaler Build zu empfehlen, um Konflikte in der Ausführungsreihenfolge zu vermeiden.

`./gradlew testing:svws-browser-test:runBrowserTest` ist der grundlegende Task, um alle Tests der Subprojekte auszuführen. Dabei werden:

1. die Docker-Container mit den nötigen Umgebungsvariablen vorbereitet
2. die Testumgebungen mit DB und SVWS-Server hochgefahren,
3. die Tests gegen die definierten Systeme ausgeführt,
4. die Testergebnisse (junit.xml) eingesammelt
5. und im Anschluss die Testumgebungen runtergefahren
6. sollten die Tests `failure` oder `error`gehabt haben, gibt es folgende Fehlermeldung:
`"The build finished, but tests resulted with ${totalErrors} errors and ${totalFailures} failures! "`.
7. sollten keine Fehler im junit.xml gefunden werden, aber npm trotzdem einen ExitCode ungleich 0 ausgeben, gibt es folgende Fehlermeldung: `"Test returned non-zero Exit-Value $execResult"`
Testergebnisse finden sich in ``SVWS-Server/svws-webclient/browser-test/build/testresults/``
Weiterhin gibt es von fehlgeschlagenen Tests Screenshots und .webm-Videos unter ``SVWS-Server/svws-webclient/browser-test/build/playwright-reports/``

**Wichtig:** Fehlerhafte/Tests führen nicht zum Abbruch der Gradle-Ausführung, sondern werden am Ende über die junit.xml zusammengefasst.

Darüber hinaus gibt es einzelne Tasks, mit denen die Container individuell gesteuert werden können.

`testing:svws-browser-test:startTestumgebung` führt die notwendigen Schritte zur Erstellung der Container durch und fährt diese hoch.
``docker ps`` zeigt den Status der Container, ``docker logs <id>`` das aktuelle sysout der startup Skripte des jeweiligen Containers.

`testing:svws-browser-test:stopTestumgebung` stoppt die Container.


## Aufbau des Projekts
[/testing/readme.md](../readme.md)

Zusätzlich werden mit `npm playwright install` browser-binaries nach `SVWS-Server/downloads/playwright/` geladen und mit `npm playwright install-deps` die notwendigen Abhängigkeiten für diese Dependencies. 

## Aenderungen an anderen Gradle-Modulen
### svws-webclient/browser-test
Die vorhandenen Browser-Tests wurden parametriert. Dies wird mit den Umgebungsvariablen am Task `svws-webclient:browser-test` durch die vorgenommene Konfiguration in `testing:svws-browser-test:browserTest` erreicht:

```
environment NODE_TLS_REJECT_UNAUTHORIZED: 0
environment PLAYWRIGHT_svws_testing_api_host: targetHost
environment PLAYWRIGHT_svws_testing_api_port: targetPort
environment CI: true
```

Verwendet werden die Variablen über `process.env`.


Die excludes werden im `svws-webclient/build.gradle` als Properties deklariert (`project.ext.npm_properties`):

```
	npm_exclude_from_tests: 'api-test","browser-test',
	npm_exclude_from_api_tests: 'core-test","client-test","browser-test',
```

und in den jeweiligen copyConfig*-Tasks verwendet.

Darüber hinaus wird der npmRun-Task `svws-webclient:browser-test:testApi` aus dem `testing:svws-browser-test:browserTest` heraus umkonfiguriert, um o.g. Umgebungsvariablen zu nutzen, den Exit-Value des npmRun zu ignorieren und eine Wiederholbarkeit der Tests trotz vorhandenem Result zu ermöglichen.