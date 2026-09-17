# Authentifizierung zwischen WeNoM-Client und WeNoM-Server

## Schritt 1: Anmeldung über den Login-Endpunkt (BasicAuth mit Benutzername und Kennwort)
``` mermaid
sequenceDiagram
	participant User as Benutzer (WeNoM-Client)
	participant Server as WeNoM-PHP-Server
	participant DB as SQLite-Datenbank

	Note over User, DB: Anmeldung über Endpunkt login mit EMail-Adresse und Kennwort (erster Faktor)
	User->>Server: Login (EMail-Adresse, Passwort)
	Server->>DB: Hole die Lehrer-Daten zur EMail-Adresse
	Server->>DB: Prüfe Sperrung aufgrund von Fehlversuchen
	DB-->>Server: der Passwort-Hash
	Server->Server: Prüfe das Passwort gegen den Hash, ggf. mit ungültigem Hash wegen pot. Timing-Angriffen
	alt Passwort falsch
		break
			Server->>DB: Aktualisiere Fehlversuche für IP und Lehrer-ID
			Server-->>User: HTTP 401 - Unauthorized
		end
	else Passwort korrekt
		Server->>DB: Entferne Fehlversuche für IP und Lehrer-ID
		Server->>DB: Erhöhe die Lehrer-Token-Version (invalidiert alte Verbindungen)
		alt Erstanmeldung
			break
				Server->>Server: Erstelle JWT-Token für Kennwort-Änderung (Header.Payload.Signature) mit neuen generiertem Kennwort
				Server-->>User: HTTP 202 Accepted (mit ChangePasswort-JWT-Token)
				Note right of User: Benutzer muss sein Kennwort anpassen
			end
		else 2FA deaktiviert
			break
				Server-->>Server: Erstelle JWT-Token für Client-Zugriff (Header.Payload.Signature)
				Server-->>User: HTTP 200 - OK (mit Client-JWT-Token)
				Note right of User: Benutzer ist eingeloggt
			end
		else 2FA mit TOTP aktiv
			Server-->>Server: Erstelle JWT-Token für TOTP-Anmeldung (Header.Payload.Signature)
			Server-->>User: HTTP 202 - Accepted (mit TOTP-JWT-Token) <br> bei Erstanmeldung mit TOTP-Shared-Secret
			Note right of User: Benutzer muss seinen zweiten Faktor (TOTP) eingeben
		end
	end
```

## Schritt 2: Kennwort-Änderung bei Erst-Login als Fortführung des Logins

``` mermaid
sequenceDiagram
	participant User as Benutzer (WeNoM-Client)
	participant Server as WeNoM-PHP-Server
	participant DB as SQLite-Datenbank

	Note over User, DB: Kennwort-Änderung bei Erst-Login über den Endpunkt change_password
	User->>Server: Change Password (ChangePassword-JWT-Token)

	rect rgb(245,245,255)
		Note over User, DB: Validierung der ChangePassword-Session

		Server->>Server: Prüfe JWT-Token auf Gültigkeit (mit ChangePassword-Session-Key)
		break Token ungültig
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>DB: Hole Lehrer-Daten anhand der Lehrer-ID
		DB-->>Server: die Lehrer-Daten

		Server->>Server: Prüfe, anhand der Lehrer-Daten, ob es sich noch um eine Erstanmeldung handelt
		break Keine Erstanmeldung
			Server-->>User: HTTP 403 Forbidden, ggf. Replay-Attacke
		end

		Server->>Server: Prüfe Hardened Cookie, falls konfiguriert
		break Cookie-Prüfung fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Prüfe IP-Adresse bei IP-Pinning, falls konfiguriert
		break IP-Adresse fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Erhöhe die Lehrer-Token-Version
		break Lehrer-Token-Version fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
	end

	rect rgb(245,245,255)
		Note over User, DB: Kennwort-Änderung

		Server->>Server: Entnehme das neue signierte Kennwort aus dem Request-Body -> ggf 400
		break Kennwort nicht im Request-Body enthalten
			Server-->>User: HTTP 404 Not Found
		end
		break Kennwort im Request-Body nicht enthalten oder nicht korrekt
			Server-->>User: HTTP 400 BadRequest
		end
		Server->>DB: Setze Kennwort für die Lehrer-ID aus dem JWT-Token

		alt 2FA deaktiviert
			Server-->>Server: Erstelle JWT-Token für Client-Zugriff (Header.Payload.Signature)
			Server-->>User: HTTP 200 - OK (mit Client-JWT-Token)
			Note right of User: Benutzer ist eingeloggt
		else 2FA mit TOTP aktiv
			Server-->>Server: Erstelle JWT-Token für TOTP-Anmeldung (Header.Payload.Signature)
			Server-->>User: HTTP 202 - Accepted (mit TOTP-JWT-Token) <br> bei Erstanmeldung mit TOTP-Shared-Secret
			Note right of User: Benutzer muss seinen zweiten Faktor (TOTP) eingeben
		end
	end
```

## Schritt 3: Eingabe des zweiten Faktors als Fortführung und Abschluss des Logins

``` mermaid
sequenceDiagram
    participant User as Benutzer (WeNoM-Client)
    participant TOTP as Authenticator App
    participant Server as WeNoM-PHP-Server
    participant DB as SQLite-Datenbank

    Note over User, DB: Zweiten Faktor (TOTP) über Endpunkt totp prüfen
    User->>TOTP: Authenticator-App öffnen und 6-stelligen Code ablesen
    TOTP-->>User: Zeigt Code (z.B. 123456)
    User->>Server: TOTP (TOTP-JWT-Token, Code)

	rect rgb(245,245,255)
		Note over User, DB: Validierung der TOTP-Session

		Server->>Server: Prüfe JWT-Token auf Gültigkeit (mit TOTP-Session-Key)
		break Token ungültig
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>DB: Hole Lehrer-Daten anhand der Lehrer-ID
		DB-->>Server: die Lehrer-Daten
		break Lehrer-Daten nicht gefunden
			Server-->>User: HTTP 401 Unauthorized
		end

		Server->>DB: Prüfe Sperrung aufgrund von Fehlversuchen für Lehrer-ID und IP-Adresse
		break Zu viele Fehlversuche / IP-Sperre aktiv
			Server-->>User: HTTP 429 Too Many Request
		end

		Server->>Server: Prüfe Hardened Cookie, falls konfiguriert
		break Cookie-Prüfung fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Prüfe IP-Adresse bei IP-Pinning, falls konfiguriert
		break IP-Adresse fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Erhöhe die Lehrer-Token-Version 
		break Lehrer-Token-Version fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
	end

	rect rgb(245,245,255)
		Note over User, DB: TOTP-Code-Prüfung und Abschließen des Logins

		Server->>Server: Lese TOTP-Code aus dem Request-Body
		Server->>Server: Prüfe, ob der TOTP-Code vorhanden ist und aus 6 Ziffern besteht
		break TOTP-Code fehlt oder besteht nicht auf 6 Ziffern
			Server-->>User: HTTP 400 Bad Request
		end

		Server->>Server: Prüfe, ob der TOTP-Code im aktuellen Zeitfenster gültig ist
		alt Code ungültig
			Server->>DB: Speichere Fehlversuch für die IP-Adresse und die Lehrer-ID
			Server-->>User: HTTP 403 Forbidden
			Note right of User: Zugriff verweigert
		else
			Server->>DB: Entferne Fehlversuche für IP und Lehrer-ID
			alt Wenn es sich um eine Erstanmeldung handelt
				Server->>DB: Entferne beim Lehrer das Flag für die Erstanmeldung
			end

			Server->>Server: Erstelle JWT-Token für den Client-Zugriff (Header.Payload.Signature)
			Server-->>User: HTTP 200 OK (mit Client-JWT-Token)
			Note right of User: Benutzer ist eingeloggt
		end
	end
```

## Sonstige API Aufrufe

``` mermaid
sequenceDiagram
    participant User as Benutzer (WeNoM-Client)
    participant Server as WeNoM-PHP-Server
    participant DB as SQLite-Datenbank

    Note over User, DB: Sonstige API-Aufrufe
    User->>Server: Sonstige Anfrage an API-Endpunkt mit Client-JWT-Token

	rect rgb(245,245,255)
		Note over User, DB: Validierung der Client-Session

		Server->>Server: Prüfe JWT-Token auf Gültigkeit (mit Client-Session-Key)
		break Token ungültig
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>DB: Hole Lehrer-Daten anhand der Lehrer-ID
		DB-->>Server: die Lehrer-Daten
		break Lehrer-Daten nicht gefunden
			Server-->>User: HTTP 401 Unauthorized
		end

		Server->>DB: Prüfe Sperrung aufgrund von Fehlversuchen für Lehrer-ID und IP-Adresse
		break Zu viele Fehlversuche / IP-Sperre aktiv
			Server-->>User: HTTP 429 Too Many Request
		end

		Server->>Server: Prüfe Hardened Cookie, falls konfiguriert
		break Cookie-Prüfung fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Prüfe IP-Adresse bei IP-Pinning, falls konfiguriert
		break IP-Adresse fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Prüfe die Lehrer-Token-Version
		break Lehrer-Token-Version stimmt nicht überein
			Server-->>User: HTTP 401 Unauthorized
		end
	end

	rect rgb(245,245,255)
		Note over User, DB: Bearbeitung der Anfrage
		Server->>Server: Bearbeitung der Anfrage
		Server-->>User: ??? ggf. Ergebnis der Anfrage
	end
```

## Token Refresh

``` mermaid
sequenceDiagram
    participant User as Benutzer (WeNoM-Client)
    participant Server as WeNoM-PHP-Server
    participant DB as SQLite-Datenbank

    Note over User, DB: Token Refresh kurz vor Ablauf der Gültigkeit über Endpunkt refresh_token
    User->>Server: Aufruf von refresh_token mit Client-JWT-Token

	rect rgb(245,245,255)
		Note over User, DB: Validierung der Client-Session

		Server->>Server: Prüfe JWT-Token auf Gültigkeit (mit Client-Session-Key)
		break Token ungültig
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>DB: Hole Lehrer-Daten anhand der Lehrer-ID
		DB-->>Server: die Lehrer-Daten
		break Lehrer-Daten nicht gefunden
			Server-->>User: HTTP 401 Unauthorized
		end

		Server->>DB: Prüfe Sperrung aufgrund von Fehlversuchen für Lehrer-ID und IP-Adresse
		break Zu viele Fehlversuche / IP-Sperre aktiv
			Server-->>User: HTTP 429 Too Many Request
		end

		Server->>Server: Prüfe Hardened Cookie, falls konfiguriert
		break Cookie-Prüfung fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Prüfe IP-Adresse bei IP-Pinning, falls konfiguriert
		break IP-Adresse fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end

		Server->>Server: Prüfung, ob der Aufruf in dem definierten Zeitintervall kurz vor Ablauf des Tokens liegt
		break Der Aufruf liegt nicht im definierten Zeitinterval
			Server-->>User: HTTP 401 Unauthorized
		end

		Server->>Server: Erhöhe die Lehrer-Token-Version
		break Lehrer-Token-Version fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
	end

	rect rgb(245,245,255)
		Note over User, DB: Bearbeitung der Anfrage
		Server->>Server: Erstelle neues JWT-Token für den Client-Zugriff (Header.Payload.Signature)
		Server-->>User: HTTP 200 OK (mit Client-JWT-Token)
		Note right of User: Client-Session wurde verlängert
	end
```


## Logout

``` mermaid
sequenceDiagram
    participant User as Benutzer (WeNoM-Client)
    participant Server as WeNoM-PHP-Server
    participant DB as SQLite-Datenbank

    Note over User, DB: Logout
    User->>Server: Aufruf von logout mit Client-JWT-Token

	rect rgb(245,245,255)
		Note over User, DB: Validierung der Client-Session

		Server->>Server: Prüfe JWT-Token auf Gültigkeit (mit Client-Session-Key)
		break Token ungültig
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>DB: Hole Lehrer-Daten anhand der Lehrer-ID
		DB-->>Server: die Lehrer-Daten
		break Lehrer-Daten nicht gefunden
			Server-->>User: HTTP 401 Unauthorized
		end

		Server->>DB: Prüfe Sperrung aufgrund von Fehlversuchen für Lehrer-ID und IP-Adresse
		break Zu viele Fehlversuche / IP-Sperre aktiv
			Server-->>User: HTTP 429 Too Many Request
		end

		Server->>Server: Prüfe Hardened Cookie, falls konfiguriert
		break Cookie-Prüfung fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Prüfe IP-Adresse bei IP-Pinning, falls konfiguriert
		break IP-Adresse fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
		Server->>Server: Erhöhe die Lehrer-Token-Version
		break Lehrer-Token-Version fehlerhaft
			Server-->>User: HTTP 401 Unauthorized
		end
	end

	rect rgb(245,245,255)
		Note over User, DB: Logout des angemeldeten Benutzers
		Server->>Server: Hinweis: Der eigentliche Logout wurde durch Erhöhen der Lehrer-Token-Version bereits vorgenommen
		alt Falls Hardened Cookie konfiguriert sind
			Server->>Server: Setze die Ablaufzeit in die Vergangenheit
		end
		Server-->>User: HTTP 204
		Note right of User: Client-Session wurde beendet
	end
```
