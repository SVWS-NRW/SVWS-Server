# Umgebungsvariablen README

Dieses README-Dokument erklärt die verschiedenen Umgebungsvariablen, die in der .env-Datei für die Docker Compose-Konfiguration verwendet werden. Diese Umgebungsvariablen sind entscheidend für die Konfiguration und das Verhalten der Docker-Dienste in Ihrer Anwendungsinfrastruktur.

## Umgebungsvariablen

### INIT_SCRIPTS_DIR

- **Beschreibung**: Das Verzeichnis, in dem Initialisierungsskripte für Ihre Anwendung abgelegt sind.
- **Default**: `/etc/app/svws/init-scripts`
- **Hinweis**: `kann per volumen mit eigenen Skripten benutzt werden`

### TESTDB_PASSWORD

- **Beschreibung**: Das Passwort für einen Test-Datenbankbenutzer (nicht spezifiziert).
- **Wert**: Ihr spezifisches Passwort (ersetzen Sie `xxx` durch Ihr tatsächliches Passwort).
- **Hinweis**: `wenn angegeben wird, werden beim starten TestDB-Daten eingespielt`

### MariaDB_ROOT_PASSWORD

- **Beschreibung**: Das Root-Passwort für die MariaDB-Datenbank.
- **Wert**: Ihr spezifisches MariaDB Root-Passwort (ersetzen Sie `#################` durch Ihr tatsächliches Passwort).

### MariaDB_DATABASE

- **Beschreibung**: Der Name der Datenbank, die von der Anwendung verwendet wird.
- **Wert**: `gymabi`

### MariaDB_HOST

- **Beschreibung**: Der Hostname oder die IP-Adresse, unter der der MariaDB-Dienst erreichbar ist.
- **Wert**: `mariadb`

### MariaDB_USER

- **Beschreibung**: Der Benutzername, der für die Verbindung zur MariaDB-Datenbank verwendet wird.
- **Wert**: Ihr spezifischer Datenbankbenutzername (ersetzen Sie `#################` durch Ihren tatsächlichen Benutzernamen).

### MariaDB_PASSWORD

- **Beschreibung**: Das Passwort für den Datenbankbenutzer, der für die Verbindung zur MariaDB-Datenbank verwendet wird.
- **Wert**: Ihr spezifisches Datenbankbenutzerpasswort (ersetzen Sie `#################` durch Ihr tatsächliches Passwort).

### SVWS_TLS_KEYSTORE_PATH

- **Beschreibung**: Der Pfad zur Keystore-Datei für die TLS-Verschlüsselung der Anwendung.
- **Default**: `/etc/app/svws/conf/keystore`
- **Hinweis**: `kann per Volumen mit eigener Keystore-Datei benutzt werden`

### SVWS_TLS_KEYSTORE_PASSWORD

- **Beschreibung**: Das Passwort für den Keystore zur TLS-Verschlüsselung der Anwendung.
- **Default**: test123

### SVWS_TLS_KEY_ALIAS

- **Beschreibung**: Der Aliasname für den TLS-Verschlüsselungsschlüssel der Anwendung.
- **Default**: alias1

### SVWS_TLS_KEYSTORE_CREATE

- **Beschreibung**: Diese Variable steuert die Erstellung eines Keystores für die TLS-Verschlüsselung der Anwendung.
- **Wert**: `true` (oder `false`, je nachdem, ob ein Keystore erstellt werden soll oder nicht).

	- `true`: Die Anwendung wird so konfiguriert, dass sie einen Keystore erstellt, der für die TLS-Verschlüsselung verwendet wird.
	- `false`: Es wird kein Keystore erstellt, und die Anwendung verwendet wahrscheinlich einen vorhandenen Keystore.

**Hinweis**: Wenn `SVWS_TLS_KEYSTORE_CREATE` auf "true" gesetzt ist, sollten Sie sicherstellen, dass die erforderlichen Keystore-Informationen wie `SVWS_TLS_KEYSTORE_PATH`, `SVWS_TLS_KEYSTORE_PASSWORD` und `SVWS_TLS_KEY_ALIAS` korrekt konfiguriert sind, da sie für die Erstellung des Keystores benötigt werden.

---

Bitte stellen Sie sicher, dass Sie die Werte der Umgebungsvariablen entsprechend Ihrer Anwendungsanforderungen konfigurieren. Beachten Sie auch die Sicherheitsempfehlungen und bewahren Sie sensible Informationen wie Passwörter sicher auf.
