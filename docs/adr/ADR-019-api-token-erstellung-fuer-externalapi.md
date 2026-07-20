# ADR-019: API-Token-Erstellung fuer die ExternalAPI

## Status

Vorgeschlagen

## Kontext

Der SVWS-Server stellt mit der ExternalAPI Schnittstellen bereit, die von externen
Diensten wie SVWS-Edugate genutzt werden koennen. Fuer solche Integrationen
werden API-Tokens benoetigt, die automatisiert und in groesserer Zahl erzeugt
werden koennen. Bei einer Bereitstellung fuer mehrere hundert Schulen ist eine
manuelle Token-Erzeugung nicht praktikabel.

Gleichzeitig duerfen automatisiert erzeugte Tokens kein Sicherheitsrisiko
darstellen. Ein kompromittierter Token darf insbesondere nicht zu einem
vollstaendigen administrativen Zugriff auf den SVWS-Server fuehren. Die hohe
Berechtigung liegt beim erzeugenden Benutzer oder Prozess, nicht beim erzeugten
Token.

## Entscheidung

API-Tokens fuer die ExternalAPI werden durch berechtigte Aufrufe der privileged
API erzeugt. Die privileged API darf diese Tokens automatisiert fuer einzelne
Schulen oder im Rahmen eines Provisioning-Laufs fuer viele Schulen erstellen.

Die erzeugten Tokens sind zweckgebunden, schulbezogen, zeitlich begrenzt,
widerrufbar und werden nur gehasht gespeichert. Der Token-Klartext wird nur
einmalig bei der Erzeugung zurueckgegeben.

Fuer SVWS-Edugate wird ein eigener Token-Zweck vorgesehen, zum Beispiel
`SVWS_EDUGATE`. Tokens mit diesem Zweck erhalten nur die fuer SVWS-Edugate
benoetigten Rechte auf der ExternalAPI.

## Token-Format

Ein Token besteht aus einem nicht geheimen Praefix und einem kryptografisch
zufaelligen geheimen Anteil.

Beispiel:

```text
svws-eg-v1_<zufaelliger-token-anteil>
```

Das Praefix dient der Erkennung des Token-Typs, der Version und des Einsatzes im
Betrieb. Es darf nicht fuer die Sicherheitsbewertung herangezogen werden. Die
Sicherheit des Tokens beruht ausschliesslich auf dem zufaelligen geheimen Anteil.

Der geheime Anteil wird mit einem kryptografisch sicheren Zufallszahlengenerator
erzeugt. Die Mindeststaerke betraegt 256 Bit Zufall, zum Beispiel 32 zufaellige
Bytes, Base64URL-kodiert ohne Padding.

Tokens duerfen nicht aus Schulnummern, Zeitstempeln, UUIDs allein, fortlaufenden
Zaehlern oder Hashes vorhersagbarer Daten abgeleitet werden.

## Speicherung

Der SVWS-Server speichert API-Tokens nicht im Klartext. Gespeichert wird nur ein
Hash oder bevorzugt ein HMAC des vollstaendigen Tokens.

Empfohlen:

```text
token_hash = HMAC-SHA-256(serverseitiges-secret, token)
```

Alternativ ist ein SHA-256-Hash des Tokens moeglich, sofern der Token ausreichend
zufaellig ist. Ein HMAC mit serverseitigem Secret reduziert das Risiko bei einem
reinen Datenbankabfluss zusaetzlich.

Der Token-Klartext wird ausschliesslich in der Antwort der Erzeugung
zurueckgegeben. Danach kann der Token nicht erneut angezeigt werden. Bei Verlust
muss ein neuer Token erzeugt und der alte Token widerrufen oder auslaufen
gelassen werden.

## Datenmodell

Fuer API-Tokens werden mindestens folgende Informationen benoetigt:

```text
id
schule_id oder schulnummer
purpose
display_name
token_hash
scopes
created_by_user_id
created_at
expires_at
last_used_at
revoked_at
revoked_by_user_id
revoked_reason
```

`purpose` beschreibt den fachlichen Zweck, zum Beispiel `SVWS_EDUGATE`.
`scopes` beschreiben die technischen Rechte auf der ExternalAPI.
`last_used_at` dient der betrieblichen Kontrolle und der Vorbereitung von
Rotationen.

## Berechtigungen

Die Token-Erzeugung erfolgt ueber die privileged API. Ein Benutzer oder Prozess,
der diese API nutzen darf, besitzt bereits weitreichende administrative Rechte.
Trotzdem duerfen die erzeugten Tokens selbst nur die jeweils benoetigten Rechte
erhalten.

Fuer SVWS-Edugate bedeutet das:

- pro Schule wird ein eigener Token erzeugt
- der Token ist an diese Schule gebunden
- der Token hat den Zweck `SVWS_EDUGATE`
- der Token besitzt nur die fuer SVWS-Edugate notwendigen ExternalAPI-Scopes
- der Token kann einzeln widerrufen werden

Ein landesweiter oder mandantenuebergreifender Token fuer alle Schulen wird
nicht verwendet. Dadurch bleibt der Schaden bei einem Token-Leak auf die
betroffene Schule und den konkreten Zweck begrenzt.

## Zeitliche Begrenzung und Rotation

API-Tokens werden zeitlich begrenzt. Fuer SVWS-Edugate wird eine regulaere
Gueltigkeit von 12 Monaten empfohlen.

Empfohlene Regeln:

- Standardgueltigkeit: 12 Monate
- maximale Gueltigkeit: 18 Monate
- minimale Gueltigkeit: 30 Tage
- Warnhinweis ab 60 Tage vor Ablauf
- dringender Warnhinweis ab 14 Tage vor Ablauf
- optionale Grace-Phase: 14 bis 30 Tage, sofern betrieblich notwendig

Eine Rotation muss ueberlappend moeglich sein:

1. Der bisherige Token ist aktiv.
2. Ein neuer Token wird erzeugt.
3. SVWS-Edugate wird auf den neuen Token umgestellt.
4. Der SVWS-Server erkennt die Nutzung des neuen Tokens ueber `last_used_at`.
5. Der bisherige Token wird widerrufen oder laeuft nach einer Uebergangszeit aus.

Um Token-Wildwuchs zu vermeiden, sollte pro Schule und Zweck nur eine begrenzte
Anzahl aktiver Tokens erlaubt sein. Fuer die Rotation sind maximal zwei aktive
Tokens pro Schule und Zweck ausreichend.

## API-Skizze

Die Erzeugung kann ueber einen expliziten Endpoint der privileged API erfolgen.

Beispiel fuer einen einzelnen Token:

```http
POST /privileged/schulen/{schulnummer}/api-tokens/edugate
```

Beispiel-Request:

```json
{
  "displayName": "SVWS-Edugate",
  "expiresAt": "2027-07-20T00:00:00Z"
}
```

Beispiel-Response:

```json
{
  "tokenId": 42,
  "token": "svws-eg-v1_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
  "purpose": "SVWS_EDUGATE",
  "expiresAt": "2027-07-20T00:00:00Z"
}
```

Der Response enthaelt den Token-Klartext nur bei der Erzeugung.

Fuer die Erzeugung vieler Tokens kann ein Bulk-Endpoint oder ein administratives
CLI-Werkzeug verwendet werden. Der Bulk-Export mit Token-Klartext ist als
hochsensibel zu behandeln und darf nicht geloggt werden.

## Validierung bei API-Aufrufen

Bei jedem Aufruf der ExternalAPI mit API-Token werden mindestens folgende
Pruefungen durchgefuehrt:

1. Token syntaktisch gueltig.
2. Hash oder HMAC des Tokens ist bekannt.
3. Token ist nicht widerrufen.
4. Token ist nicht abgelaufen.
5. Token-Zweck passt zur verwendeten API.
6. Token-Scopes erlauben den konkreten Zugriff.
7. Schulbindung des Tokens passt zum angefragten Kontext.

Fehlschlaege werden ohne Ausgabe sensibler Details beantwortet. Der Token selbst
wird weder in Logs noch in Fehlermeldungen ausgegeben.

## Audit und Betrieb

Die Erzeugung, der Widerruf und die Nutzung von API-Tokens werden auditierbar
gemacht.

Zu protokollieren sind insbesondere:

- wer einen Token erzeugt oder widerrufen hat
- fuer welche Schule der Token gilt
- welcher Zweck und welche Scopes gesetzt wurden
- wann der Token erzeugt wurde
- wann der Token ablaeuft
- wann der Token zuletzt genutzt wurde

Der Token-Klartext darf nicht protokolliert werden. Fuer Support und Betrieb kann
ein nicht geheimer Fingerprint verwendet werden, zum Beispiel die ersten Zeichen
eines Hashes.

Administrativ sollten ablaufende Tokens sichtbar sein, damit Rotationen vor einem
Ausfall geplant werden koennen.

## Konsequenzen

Die Token-Erzeugung kann fuer viele Schulen automatisiert werden, ohne dass
manuelle Einzelanlage erforderlich ist. Durch schulbezogene, zweckgebundene und
zeitlich begrenzte Tokens bleibt das Risiko eines Token-Leaks begrenzt.

Der SVWS-Server benoetigt dafuer ein persistentes Token-Modell, Prueflogik in der
ExternalAPI, Verwaltungsfunktionen in der privileged API sowie betriebliche
Sichten oder Exporte fuer Ablauf und Nutzung.

Die Implementierung muss sicherstellen, dass Token-Klartexte nach der Erzeugung
nicht rekonstruierbar sind und nicht versehentlich in Logs, Audit-Eintraegen oder
Fehlermeldungen auftauchen.
