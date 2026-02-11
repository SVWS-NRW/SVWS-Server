SVWS Docker Compose Deployment
==============================

1. Erstelle eine `.env` Datei (oder setze die Variablen in deiner Umgebung) und definiere mindestens:
   - `MARIADB_ROOT_PASSWORD`, `MARIADB_DATABASE`, `MARIADB_USER`, `MARIADB_PASSWORD`
   - `SVWS_TLS_KEYSTORE_PASSWORD`, `SVWS_TLS_KEY_ALIAS`
   - optional `SVWS_SERVER_IMAGE`, `SVWS_HTTP_PORT`, `SVWS_HTTPS_PORT`
2. Nutze das vorbereitete Initialisierungs-SQL unter `deployment/docker/init-db/` oder ergänze eigene Dateien in diesem Ordner (sie werden automatisch beim ersten DB-Start ausgeführt).
3. Starte die Umgebung mit:
   ```bash
   docker compose -f deployment/docker/docker-compose.yaml up -d
   ```
4. Nach dem Start ist der Server über `http://localhost:8080` bzw. `https://localhost:8443` erreichbar (Ports konfigurierbar).

Hinweise:
- Passe die Passwörter/Portwerte vor dem ersten Start an.
- Standard-Host-Ports sind 8080/8443. Falls diese bereits belegt sind, überschreibe sie per `SVWS_HTTP_PORT` und `SVWS_HTTPS_PORT` (z. B. 18080/18443).
- Der Keystore wird beim ersten Start automatisch im Volume `svws-keystore` erzeugt.
- Für ein komplettes Zurücksetzen: `docker compose -f deployment/docker/docker-compose.yaml down -v`
