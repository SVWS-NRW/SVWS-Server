# Teststrategien für die Applikation

In diesem Paket sind verschiedene Teststrategien implementiert. Es gibt jeweils drei Arten, wie die Tests aufgerufen werden können, die hier ebenfalls beschrieben sind.

## Integrationstests

Die Integrationstests decken alle Endpunkte der Applikation ab, die über `APIServer.ts` erreichbar sind, sowie alle Ende-zu-Ende-Tests.

### 1. Tests mit temporär gestarteter Docker-Testumgebung

**Vorbedingungen:** Docker läuft auf dem Host-System.

**Ausführung in der Konsole:**
```sh
./gradlew verification:integrationTest
```

**Ausführung in der IDE:**
- Gradle -> Testing -> Verification -> integrationTest

### 2. Tests gegen lokale und bereits laufende Datenbank und Applikation

**Vorbedingungen:** Datenbank (Port 3306) und Applikation (Port 443) laufen.

**Ausführung in der Konsole:**
```sh
./gradlew verification:integrationTestLocalOnlyNoDocker
```

**Ausführung in der IDE:**
- Gradle -> Testing -> Verification -> integrationTestLocalOnlyNoDocker

### 3. Einzelne Tests gegen lokale und bereits laufende Datenbank und Applikation

**Vorbedingungen:** Datenbank (Port 3306) und Applikation (Port 443) laufen.

**Ausführung in der IDE:**
- Direkt in der IDE (IntelliJ unterstützt dies nativ, für VSCode gibt es das 'Vitest'-Plugin)

