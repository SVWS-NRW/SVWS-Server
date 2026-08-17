# Reporting HTML-Snapshot-Tests

Diese Tests erzeugen über die laufende SVWS-Server-API HTML-Reports und vergleichen sie
mit abgelegten Snapshots. Der zugehörige Test ist `tests/APIReporting.test.ts`.

## Aufbau eines Testfalls

Jeder Unterordner ist ein eigener Testfall. Ein Ordner wird nur als Testfall erkannt,
wenn er sowohl eine `params.json` als auch eine `config.json` enthält:

```
reporting/
  <Fallname>/
    config.json            # Beschreibung + Datenbank-Schema für den Fall
    params.json            # ReportingParameter (idSchuljahresabschnitt, ausgabeformat, reportvorlage, ...)
    snapshot.html          # automatisch erzeugter HTML-Snapshot (Vergleichsbasis)
    snapshot-defaults.html # nur bei "pruefeDefaults": Snapshot des Laufs mit den Katalog-Defaults
    dateiname-alle.txt     # nur bei "pruefeDateinamen": Dateiname beim Aufruf mit allen IDs
    dateiname-einzeln.txt  # nur bei "pruefeDateinamen": Dateiname beim Aufruf mit einer ID
```

`config.json`:

```json
{
  "beschreibung": "Schulbescheinigung an den Schüler",
  "schema": "ReportingGymAbi"
}
```

- `beschreibung` wird im Testtitel angezeigt.
- `schema` legt fest, gegen welche Datenbank der Report erzeugt wird.
- `benutzer` (optional) und `passwort` (optional) erlauben fallspezifische Zugangsdaten.
  Fehlen sie, wird der Standard-Admin-Zugang (`Admin` mit leerem Passwort) verwendet.
  Nur setzen, wenn ein Fall wirklich andere Zugangsdaten braucht — Klartext-Passwörter
  in committeten Dateien sind heikel.
- `pruefeDateinamen` (optional) schaltet zusätzlich die Prüfung der Dateinamen ein
  (siehe „Dateinamen aus den `.name.tpl`").
- `pruefeDefaults` (optional) schaltet zusätzlich einen Lauf gegen die Katalog-Defaults ein
  (siehe „Läufe gegen die Katalog-Defaults").

## Voraussetzungen

1. **Server im DEV-Modus mit aktiviertem Festdatum starten.** Nur so liefert der `#aktuell`-Dialect
   ein deterministisches „Gedruckt am …" / „Ort, den …" im Report:

   ```
   SVWS_REPORTING_FIXED_DATE=true ./gradlew :svws-server-app:runServer
   ```

   Die Umgebungsvariable `SVWS_REPORTING_FIXED_DATE=true` aktiviert das fest im Dialect hinterlegte
   Datum (29.02.1948). Sie muss bei allen Läufen gesetzt sein — auch beim Aktualisieren der Snapshots —
   und wirkt ausschließlich im ServerMode `DEV`. In der Docker-/Release-Umgebung wird sie automatisch
   im Container gesetzt; nur beim manuell gestarteten lokalen Server muss sie selbst gesetzt werden.

2. **Die in den `config.json` referenzierten Schemata** müssen auf dem Datenbankserver
   vorhanden sein (derzeit durchgehend `ReportingGymAbi`).

   > **Achtung, nicht umkehrbar:** Der Server aktualisiert ein veraltetes Schema beim Start
   > selbsttätig auf die aktuelle Revision. Steht das Testschema auf einem älteren Stand als der
   > getestete Code, wird es dabei migriert — ohne Sicherung nicht rückgängig zu machen, und eine
   > solche Migration kann die Reportinhalte verändern. Vor einem Lauf gegen einen deutlich neueren
   > Codestand lohnt deshalb ein Blick in das schemabezogene Logfile unter `svws-server-app/logs/`;
   > der Serverstart meldet zudem je Schema, ob migriert wurde.

3. Backend-URL und Zugangsdaten kommen aus `config/tests/config.json`
   (Fallback: `config/tests/config_default.json`). Authentifizierung erfolgt als `Admin`
   mit leerem Passwort.

## Tests ausführen

Aus dem Verzeichnis `tests/tests-server-reporting`:

```
npx vitest run tests/APIReporting.test.ts
```

### Vorher- und Nachher-Lauf

Eine Änderung wird durch **zwei** Läufe abgesichert: einen vor dem ersten ausgabewirksamen
Arbeitsschritt und einen nach dessen Abschluss, spätestens vor der Review. Der Vorher-Lauf ist die
Vergleichsbasis; einen davon losgelösten Referenzlauf braucht es nicht.

**Nur ein vollständiger Lauf zählt.** Wurde ein Fall übersprungen, weil sein Schema fehlt, ist der
Lauf nicht aussagekräftig — der Test „Abschluss: kein Testfall wurde übersprungen" macht daraus
einen sichtbaren Fehlschlag.

### Einen roten Lauf bewerten

Die Fehlermeldung unterscheidet die drei Fälle:

| Befund | Bedeutung |
|---|---|
| `Fetch failed` oder ein HTTP-Fehler | Der Server hat nicht geantwortet oder abgebrochen. Kein Snapshot-Problem — zuerst Serverlog und Serverstart prüfen |
| Snapshot-Diff, Änderung nicht beabsichtigt | Regression. Bis zur fachlichen Klärung ein Fehler; die Snapshots werden **nicht** aktualisiert |
| Snapshot-Diff, Änderung beabsichtigt | Nach fachlicher Prüfung die betroffenen Snapshots bewusst neu erzeugen und die Abweichung in der Review benennen |

**Ein roter Lauf ist zuerst gegen Datendrift zu prüfen, nicht gegen die eigene Änderung.** Das
Testschema ist dauerhaft vorhanden; Reste aus manuellen Tests darin verändern die Ausgaben, ohne
dass am Code etwas falsch wäre. Ein einzelner abweichender Wert lässt sich über die API
nachrechnen.

## Snapshots neu erzeugen (Refresh)

Soll der aktuelle HTML-Output zur neuen Vergleichsbasis werden, den Lauf mit `-u`
(Update-Flag) starten. Damit werden alle `snapshot.html` der Reporting-Testfälle
überschrieben:

```
npx vitest run tests/APIReporting.test.ts -u
```

Achtung: Auch beim Refresh muss der Server mit `SVWS_REPORTING_FIXED_DATE=true` laufen, sonst
wandert das gedruckte Datum auf das jeweilige Tagesdatum.

### Nur einen einzelnen Snapshot aktualisieren

Ändert sich nur eine Vorlage, kann der Refresh mit `-t` auf einen Fall eingegrenzt werden.
`-t` matcht einen Teilstring des Testnamens `htmlReport - <beschreibung> (<schema>)`:

```
npx vitest run tests/APIReporting.test.ts -u -t "Lehrer - Stammdatenliste"
```

Es wird dann nur die `snapshot.html` dieses Falls neu geschrieben; alle anderen bleiben
unberührt (jeder Fall hat eine eigene Snapshot-Datei, keine gemeinsame `.snap`-Datei).

Hinweise:
- `-t` matcht über **alle** Fälle. `-t "Leistungsdaten"` trifft z. B. mehrere Fälle
  gleichzeitig – für genau einen Fall einen möglichst eindeutigen Teil der Beschreibung
  wählen.
- **Der Testname ist gekürzt.** Vitest schneidet die eingesetzte Beschreibung nach rund 40 Zeichen
  ab (`htmlReport - 'Klassenliste - Schüler mit detaillier…'`). Ein `-t`-Muster, das auf ein Wort
  weiter hinten in der Beschreibung zielt, findet deshalb **keinen** Test — der Lauf meldet dann
  „97 skipped" statt eines Fehlers. Immer auf den **Anfang** der Beschreibung filtern.
- Betrifft eine Änderung ein gemeinsames Fragment (z. B. Kopf-/Fußzeile), sind mehrere
  Snapshots betroffen – dann besser ohne `-t` alle mit `-u` neu erzeugen.
- Auch beim gezielten Refresh muss der Server mit `SVWS_REPORTING_FIXED_DATE=true` laufen.

## Neuen Testfall anlegen

1. Neuen Ordner unter `reporting/` anlegen.
2. `config.json` mit `beschreibung` und `schema` hinterlegen.
3. `params.json` mit den gewünschten `ReportingParameter` befüllen.
4. Server mit `SVWS_REPORTING_FIXED_DATE=true` starten und den Test einmal mit `-u` laufen lassen,
   um die initiale `snapshot.html` zu erzeugen.
5. Erzeugten Snapshot prüfen und mit committen.

---

## Festlegungen zum Testkonzept

Dieser Abschnitt hält fest, **warum** die Tests so aussehen, wie sie aussehen — damit verworfene
Wege nicht erneut vorgeschlagen werden.

### Verworfene Wege

- **Snapshot-Tests ohne eigenes Vitest-Projekt** (reiner HTTP-Test) und **In-Process-Variante gegen
  eine SQLite-Datei**: beide zugunsten des heutigen Aufbaus gegen einen laufenden Server verworfen.
- **Handgebaute Fixtures** statt echter Datenbankdaten: Die Reporting-Objekte lassen sich zwar
  theoretisch von Hand bauen, aber die Konstruktoren haben bis zu ~60 Parameter, und die komplexen
  Contexts (Leistungsdaten-Matrix, Stundenplan, Klausurplan) greifen im Konstruktor doch auf
  Repository und Lazy-Loader zu. Der Weg trägt nicht.
- **Gezielte Null-Pfad-Testfälle** über ein eigenes Schema mit manipulierten Datensätzen: verworfen
  zugunsten verbindlicher, prüfbarer Null-Regeln für Templates (siehe `reporting-konventionen.md`,
  Abschnitt 5.1, sowie den Prüfschritt in `reporting-template-erstellung.md`). Die Null-Sicherheit
  wird damit durch Konstruktion und Prüfung hergestellt statt durch Stichproben — das skaliert mit
  wachsender Vorlagenzahl, gepflegte Testdaten tun das nicht. Damit entfallen auch ein zweites
  Schema und ein Seed-Skript.

### Bewusst nicht getestet

- **PDF-Inhalt:** kein Binärvergleich der erzeugten PDFs — aufwändig und volatil.
  **Ausgenommen sind Dateinamen** (siehe unten).
- **E-Mail-Versand.**
- **Einzelausgabe über die HTML-Ausgabe:** strukturell nicht möglich. Der Server erzwingt für das
  Ausgabeformat HTML `einzelausgabeDaten = false` (und für E-Mail `true`); nur bei PDF gilt der
  übermittelte Wert. Ein HTML-Testfall mit `true` wäre wirkungslos — der Wert wird serverseitig
  überschrieben. Der Fall, dass der Parameter **gar nicht** übermittelt wird, ist dagegen abgedeckt
  (siehe „Gezielte Fälle für Filterung und fehlende Parameter").
- **Ein gespeicherter Benutzerwert, der bei einer HTML-Anfrage durchschlägt:** nicht abgedeckt. Im
  Test existiert keine gespeicherte Benutzer-Konfiguration, also greift stets der Katalog-Default.
  Diesen Pfad zu prüfen hieße, vor dem Report eine Benutzer-Config über die API zu schreiben und
  danach aufzuräumen — ein Eingriff in den Zustand des Schemas, den die Suite bewusst vermeidet.

### Dateinamen aus den `.name.tpl`

Die Dateinamensvorlagen erzeugen den Download-Dateinamen. Er taucht im HTML-Snapshot **nicht** auf,
weil die HTML-Antwort kein `Content-Disposition` setzt — der Name entsteht nur im PDF-Pfad. Geprüft
wird er deshalb über PDF-Anfragen, bei denen **nur der Dateiname** verglichen wird, nicht die Bytes.
Der API-Client liefert ihn bereits fertig dekodiert in `ApiFile.name`.

Die `.name.tpl` verzweigen typischerweise nach der **Anzahl der Datensätze** (`anzahl == 1` gegen
`anzahl > 1`). Deshalb prüft der Test je Fall zwei Aufrufe:

| Aufruf | Parameter | Snapshot | trifft |
|---|---|---|---|
| Gesamtaufruf | alle IDs aus der `params.json` | `dateiname-alle.txt` | den `> 1`-Zweig |
| Einzelaufruf | nur die erste ID | `dateiname-einzeln.txt` | den `== 1`-Zweig |

Beide liefern genau **eine** PDF-Datei — kein ZIP, denn die Aufteilung in Einzeldateien
(`einzelausgabeDaten`) wird hier nicht eingeschaltet. Die `params.json` bleibt unverändert; das
Ausgabeformat und die gekürzte ID-Liste setzt der Test nur auf einer Kopie.

Damit nicht jeder Fall zwei zusätzliche PDF-Erzeugungen auslöst, ist die Prüfung ein Opt-in über
`"pruefeDateinamen": true` in der `config.json`. Ein eigener Vollständigkeits-Test stellt sicher,
dass **jede Reportvorlage** durch mindestens einen solchen Fall abgedeckt ist — jede `.name.tpl`
wird also mindestens einmal ausgeführt.

Zwei Einschränkungen, damit niemand auf falsche Fährten gerät:

- Manche Vorlagen verzweigen über die **geladenen Daten** statt über die Parameter. Dort ändert das
  Kürzen der ID-Liste nichts, und beide Snapshots sind gleich — das ist kein Fehler:
  - **GOSt-Kursplanung:** die Auswahl steuert `idHauptdatenObjekt` (das Blockungsergebnis), die
    Vorlage zählt `GostBlockungsergebnis.kurse()` bzw. `.schueler()`.
  - **GOSt-Klausurplanung:** die `idsHauptdaten` sind Kombinationen aus Abiturjahr und Halbjahr,
    keine Schüler-IDs; die Vorlage zählt `GostKlausurplan.schueler()`.
  - Vorlagen ohne Verzweigung nach Anzahl (z. B. die Lehrer-Stammdatenliste) liefern ohnehin
    immer denselben Namen.
- Hat ein Fall nur eine einzige ID, wird nur der `== 1`-Zweig geprüft. Wo beide Zweige abgedeckt
  sein sollen, braucht der markierte Fall mehrere Datensätze.
- Der Pfad der Einzelausgabe (mehrere Dateien im ZIP) wird hier **nicht** geprüft.

### Läufe gegen die Katalog-Defaults

Die `params.json` bildet ab, was der WebClient sendet: den **vollständigen** Parametersatz. Damit
überschreibt jeder Testfall sämtliche Katalog-Werte — eine Änderung an einem Default im Core würde
keinen einzigen Snapshot bewegen, und die SOLL-Vorauswahlen der Vorlage (etwa die Filtergruppe zum
Schülerstatus) kämen nie zum Tragen, weil der Server sie nicht ergänzt, wenn der Client die Gruppe
weglässt.

Deshalb gibt es je Reportvorlage einen zweiten Lauf, dessen Parameter aus dem **Vorlagenkatalog**
stammen (`ReportingReportvorlage.getReportingParameter()`). Aus der `params.json` wird dabei nur die
Auswahl übernommen — `idSchuljahresabschnitt`, `idHauptdatenObjekt`, `idsHauptdaten`,
`idsDetaildaten` —, sodass derselbe Report erzeugt wird. **Der Diff zwischen `snapshot.html` und
`snapshot-defaults.html` zeigt damit genau, was die übermittelten Parameter bewirken.**

Zwei Dinge, die dabei zu beachten sind:

- **Die benutzerweiten Parameter werden mitgesendet.** Sie bilden einen eigenen, von den
  Vorlagenparametern disjunkten Katalog (`ReportingReportvorlageKonfigurationBenutzerweit`) und
  stecken deshalb nicht in `getReportingParameter()`. Ohne sie würden die **gespeicherten**
  benutzerweiten Einstellungen des angemeldeten Benutzers greifen — der Lauf wäre dann vom Zustand
  der Client-Konfiguration im Schema abhängig und nicht mehr reproduzierbar.
- **Default-Snapshots reagieren auf Katalog-Änderungen.** Das ist ihr Zweck. Wer einen Default im
  Core ändert, muss die betroffenen `snapshot-defaults.html` neu erzeugen und den Diff prüfen — er
  zeigt, wie sich die Änderung auf die Ausgabe auswirkt.

Ein eigener Vollständigkeits-Test stellt sicher, dass **jede Reportvorlage** durch einen solchen Fall
abgedeckt ist. Gezielt aktualisieren lassen sich diese Snapshots über den Testnamen:

```
npx vitest run tests/APIReporting.test.ts -u -t "Katalog-Defaults"
```

### Gezielte Fälle für Filterung und fehlende Parameter

Zwei Fälle prüfen nicht eine Vorlage, sondern ein **Verhalten**. Sie leiten sich vom Basisfall
`Klasse-Liste-Schueler-Leistungsdaten-Detailliert` ab und übernehmen dessen IDs, Vorlage und Schema,
damit jeweils nur der eine Unterschied wirkt:

| Fall | Unterschied zur `params.json` des Basisfalls | Aussage |
|---|---|---|
| `…-NurExterneSchueler` | zusätzliche Filtergruppe „Schülerstatus" mit der Auswahl nur `Extern` | Der Schülerstatus-Filter wirkt: **1** Schüler statt 208 |
| `…-OhneAusgabeparameter` | der Parameter `einzelausgabeDaten` fehlt in der Gruppe „Ausgabeoptionen" | Das Weglassen ändert bei HTML nichts: Snapshot **byte-identisch** zum Basisfall |

Zwei Punkte, damit die Fälle nicht falsch gepflegt werden:

- **Die übermittelte Filtergruppe braucht keine `filterDefinitionenOptionen`.** Der Server liest die
  Options-Kataloge ausschließlich auf der SOLL-Gruppe der Vorlage; von der übermittelten Gruppe
  verwendet er nur `bezeichnung`, `typ` und die Auswahl in `filterDefinitionen`. Die Bezeichnung muss
  dafür **exakt** der SOLL-Gruppe entsprechen (hier: `Schülerstatus`).
- **Der zweite Fall ist absichtlich ein Duplikat des Basis-Snapshots.** Weicht er eines Tages ab, ist
  das die Meldung: Dann wirkt der nicht übermittelte Parameter plötzlich doch auf die HTML-Ausgabe.

### Reproduzierbarkeit

Das gepinnte Test-DB-Image ist der Versionierungsmechanismus: Es friert Datensätze, referenzierte
IDs und das Schullogo ein. Ein Test-Mode-Flag für ein Platzhalter-Logo bleibt Reserve.
