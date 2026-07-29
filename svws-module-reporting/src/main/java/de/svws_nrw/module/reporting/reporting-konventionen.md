# Reporting – Konventionen & Invarianten

Verbindliche Regeln für alle Änderungen im Modul `svws-module-reporting`. Diese Datei ist die
**normative Referenz** — kompakt genug, um sie bei jeder Änderung vollständig zu lesen.
Die *Beschreibung* der Architektur (Schichten, Klassen, Datenfluss) steht in
[`reporting-architektur.md`](reporting-architektur.md); die Anleitung für Vorlagen-Autoren in
[`reporting-template-erstellung.md`](reporting-template-erstellung.md). Bei Konflikt gilt diese
Datei; Regel-Änderungen werden hier gepflegt.

---

## 1. Schichtentrennung & Datenzugriff

- **Nur Domänen-Repositories** rufen `new DataXxx(...)` aus dem `svws-db`-Paket auf oder setzen
  Queries gegen `conn()` ab. `conn()` ist package-private im `repositories`-Paket.
- **Proxy-Reporting-Typen, HtmlContexts und alle übrigen Schichten** greifen ausschließlich über
  Repository-Methoden auf Daten zu — nie direkt auf die DB.
- **`ProxyReporting…`-Objekte instanziiert ausschließlich das zuständige Repository.** Dadurch
  existiert jedes Reporting-Objekt pro Lauf nur einmal im Cache; `==`-Identität für gleiche IDs
  ist garantiert. (Sub-Proxys innerhalb der `types/`-Schicht, die nicht identitäts-gecacht
  werden, sind davon ausgenommen.)
- **`ReportingContext` hält keine fachlichen Daten und keinen eigenen Cache** — er delegiert an
  die neun Domänen-Repositories. Während ihrer Initialisierung im Konstruktor dürfen
  Repositories nur die Infrastruktur-Getter (`conn()`, `logger()`, `sortierungService()`,
  `filterService()`) nutzen.

## 2. Reporting-Typen & Null-Sicherheit

- Reporting-Typen sind **immutable POJOs**, erben von `ReportingBaseType` und kennen weder DB
  noch `ReportingContext`.
- **Getter liefern non-null per Default** — nullable ist die dokumentierte Ausnahme:
  - **String-/Datums-Felder** werden im **Basis-Konstruktor** per `ersetzeNullBlankTrim(...)`
    auf `""` normalisiert (nicht nur im Proxy). Datums-Strings: leer = „nicht gesetzt“,
    Folge-Logik per `isEmpty()` statt `== null`.
  - **Listen** als gefilterte, veränderbare Defensivkopie:
    `this.x = (x != null) ? new ArrayList<>(x.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();`
    — **Maps** als Defensivkopie `new HashMap<>(x)` bzw. leere Map.
  - **Objekt-/Enum-/Boxed-Getter** (`klasse()`, `folgeklasse()`, `note()`, …) dürfen fachlich
    `null` sein — dann ist das im `@return`-JavaDoc dokumentiert und die Templates sichern den
    Zugriff ab (Abschnitt 5).
- **Ausnahme Rückreferenz-Befüllung:** Listen, die das Repository erst **nach** der Konstruktion
  über die übergebene Referenz füllt (GOSt-Kursplanung: `kurse`/`schienen` des
  Blockungsergebnisses), vertragen **keine** Defensivkopie im Konstruktor — dort übernimmt ein
  Setter am **Ende** des Aufbaus die fertige Liste (und erzeugt erst dann die gefilterte Kopie).
  Wer Konstruktoren dieser Typen anfasst (z. B. Builder-Refactoring), muss diese Fälle explizit
  kennen.

## 3. Filterung & Sortierung

- **Beides wird zentral in den Listen-Methoden der Repositories angewandt** — nie im
  HtmlContext. Der HtmlContext sortiert nur (`HtmlContextSortierung`), filtert nicht.
- **Single-Object-Getter** (`schueler(id)`, `lehrer(id)`, `klasse(id)`, `kurs(id)`) liefern bei
  aktivem User-Filter `null`. Aufrufer und Proxies müssen null-safe sein; **kein `null` in
  Listen/Maps** einfügen.
- **Repositories geben ihre Cache-Maps nicht heraus.** Einzelzugriffe laufen über die nachladenden
  Lookup-Methoden (`schueler(id)`, `klasse(id)`, `ort(id)`, `erzieherart(id)` …), Mengenzugriffe
  über Methoden, die eine **unveränderliche Liste** liefern (`faecher()`, `jahrgaenge()`,
  `alleLehrer()` …). Ein Getter, der die interne `Map` zurückgibt, wäre ein Rückschritt: Er lässt
  den Cache von außen verändern und umgeht die Filter-Konvention.
- Konfiguration typsicher über die Konstanten `Reporting<Typ>.SORTIERUNG` /
  `Reporting<Typ>.FILTER`; die Begleit-Dateien `Reporting<Typ>Sortierung` /
  `Reporting<Typ>Filter` liegen direkt neben dem Typ.
- Typname an die Services immer per `Reporting<Typ>.class.getSimpleName()` übergeben.

## 4. Fehlerbehandlung & Logging

- Logging nur über `reportingContext.logger()` / `reportingContext.log()`.
- Fehler als `ApiOperationException` (ist `RuntimeException`). In Caller-Lambdas der
  `ReportingRepositoryUtils` **kein eigenes try/catch** — das würde den pro-ID-Fallback
  unterbinden.
- **Reports melden Datenfehler nicht sichtbar**; eine NPE in der Druckausgabe ist immer ein Bug,
  kein „sichtbarer Datenfehler“. Fehlende Daten führen zu einer sauberen Lücke (leer oder
  neutraler Platzhalter).

### 4.1 Fehlercode-Matrix (HTTP-Status der `ApiOperationException`)

Verbindliche Zuordnung von Fehlersituation zu HTTP-Status in allen Factories des Moduls:

| Situation | HTTP-Status |
|---|---|
| Client-Input fehlt (Pflichtfeld, leere ID-Liste, nicht übergebenes Parameter-Objekt) | `BAD_REQUEST` |
| Client-Input ungültig (Wert außerhalb Wertebereich, ungültige Enum-Bezeichnung, Bulk-ID nicht vorhanden) | `BAD_REQUEST` |
| Geschäfts-Voraussetzung verletzt (Schule ohne GOSt, Vorlage passt nicht zu Daten) | `BAD_REQUEST` |
| Berechtigung fehlt | `FORBIDDEN` |
| Eine konkret per ID adressierte Einzel-Ressource existiert nicht (z. B. Stundenplan zur ID) | `NOT_FOUND` |
| Server-internes Problem (DB-Verbindung `null`, Template-Datei nicht lesbar, nicht implementierter Pfad) | `INTERNAL_SERVER_ERROR` |

Ergänzende Regeln:

- **Bulk-IDs:** Werden IDs im Request-Body übergeben und ein Eintrag liegt nicht vor, ist das
  Client-Input-Validierung → `BAD_REQUEST`. `NOT_FOUND` bleibt der einzelnen, direkt
  adressierten Ressource vorbehalten.
- **Catch-Blöcke:** Generische `catch (Exception e)`-Blöcke, die zu `INTERNAL_SERVER_ERROR`
  verpacken, brauchen davor einen eigenen Zweig `catch (ApiOperationException aoe)`, der die
  Exception mit ihrem ursprünglichen Status durchreicht — sonst werden 400/403/404 am API-Rand
  zu 500 verschluckt (Muster: `ReportingFactory`).
- **Log-Level:** Zu jedem geworfenen Fehler gehört ein Log-Eintrag mit `LogLevel.ERROR`
  (nicht `DEBUG`/`INFO`).

## 5. Templates & OGNL-Grenzen

Die Templates laufen gegen Thymeleaf-Standard-OGNL (ohne Spring). Es gibt **keine**
Safe-Navigation (`?.`) und kein Elvis (`?:`) — jede Null-Absicherung muss ausgeschrieben werden.

### 5.1 Null-Regeln für Templates (verbindlich)

**Geltungsbereich:** alle Dateien, die durch die Template-Engine laufen — die `.html`-Vorlagen
**und** die `.name.tpl`-Dateinamensvorlagen. Für beide gelten dieselben Grenzen.

- **N1 — Ketten absichern.** Ein Zugriff mit mehr als einem Aufruf (`a.b().c()`) ist nur zulässig,
  wenn jedes Zwischenglied vorher geprüft wurde. Bei tiefen Ketten die Zwischenwerte einmalig per
  `th:with` binden und einzeln prüfen:
  ```html
  <td th:with="la=${schueler.auswahlLernabschnitt()},kl=${la != null ? la.klasse() : null}"
      th:text="${kl != null ? kl.kuerzel() : ''}"></td>
  ```
- **N2 — Was nullable ist, steht im JavaDoc.** Maßgeblich ist das `@return` des Getters:
  Objekt-, Enum- und Boxed-Getter dürfen fachlich `null` liefern und sind dort so dokumentiert
  (Abschnitt 2). Genau diese Getter brauchen einen Guard. Weil das JavaDoc damit die normative
  Quelle ist, gilt: **Ein Getter, der `null` liefern kann, ohne es zu dokumentieren, ist ein
  Fehler im Typ, nicht in der Vorlage.** Einheitliche Schreibweise, damit die Angabe auffindbar
  bleibt: `; kann {@code null} sein, wenn …`.
- **N3 — Listen und Maps sind nie `null`** (Abschnitt 2). Sie brauchen **keinen** Null-Guard; wo
  es fachlich nötig ist, auf *leer* prüfen (`isEmpty()`), nicht auf `null`.
- **N4 — String- und Datumsfelder sind nie `null`, sondern `""`** (Abschnitt 2). Folge-Logik prüft
  mit `isEmpty()`, nicht mit `== null`.
- **N5 — Indizierter Listenzugriff nur nach Größenprüfung.** Betrifft **alle** Zugriffsformen:
  fester Index (`liste[0]`), Variablen-Index (`liste[i]`) und `liste.get(n)`. Der Zugriff ist erst
  zulässig, wenn die Größe feststeht — etwa `#lists.size(liste) == 1`, `i < liste.size()`,
  `liste.size() > 0` oder `iterState.first`.

**Ein Guard darf am umschließenden Element stehen.** `th:if`/`th:unless` an einem Elternelement
sichert alle Ausdrücke darin ab, weil dessen Inhalt bei `false` nicht ausgewertet wird — auch das
`th:with` am selben Element (Attribut-Präzedenz: `th:each` → `th:if` → `th:with`). Ein einzelner
Guard am Wurzelblock einer Vorlage ist damit ein zulässiger und oft der bessere Weg gegenüber
Dutzenden Einzelprüfungen. Wer prüft, muss den umgebenden Baum ansehen; ein rein zeilenweiser
Abgleich erzeugt hier Fehlalarme.

**Keine Ausnahmen aus fachlicher Plausibilität.** „Dieses Feld kann in der Praxis nicht `null`
sein" rechtfertigt keinen Verzicht auf N1/N2. Bei aktivem Benutzerfilter liefern die
Single-Object-Getter `null` (Abschnitt 3), und das propagiert in alle Backrefs — ein Schüler ohne
Klasse entsteht dann, obwohl fachlich jeder Schüler eine Klasse hat.

Die Regeln sind bewusst so formuliert, dass sie ohne Kenntnis des Einzelfalls prüfbar sind, auch
automatisiert. Der abarbeitbare Prüfschritt für neue und geänderte Vorlagen steht in
[`reporting-template-erstellung.md`](reporting-template-erstellung.md), Abschnitt „Null-Prüfung
der Vorlage“.

### 5.2 Ausgeschlossene Alternativen

Alle Alternativen wurden in einer Spike-Serie (2026-05/06) empirisch ausgeschlossen oder sind
projektpolitisch blockiert — **nicht erneut versuchen**:

| Ansatz | Befund |
|---|---|
| Safe-Navigation `?.` / Elvis `?:` | `TemplateInputException` beim Parsing — in OGNL ohne SpEL syntaktisch nicht erlaubt. |
| SpEL (`SpringTemplateEngine`, hätte `?.`/`?:`) | Umsetzung wäre minimal (Dependency + Engine-Tausch), aber **blockiert durch Projektvorgabe: keine Spring-Abhängigkeit im Modul**. |
| Globaler `ognl.NullHandler` | Greift nur bei End-`null`; bei Zwischen-`null` in Ketten ab Tiefe 3 wirft OGNL hartcodiert `OgnlException: source is null for getProperty(...)`, **bevor** der Handler konsultiert wird. |
| Statische EMPTY-Konstanten via `@FQClass@FELD` | Thymeleaf-Parser lehnt die OGNL-Syntax kategorisch ab. |
| OGNL-Lambda `:[ … ]` als Pfad-Träger für einen Helfer | Parser bricht an Methoden-Klammern `()` ab — für die klammerbasierten Accessoren der Reporting-Typen unbrauchbar. |

**Notlösung für tiefe Ketten (bewusst nicht umgesetzt):** Ein `#safe.path('a.b().c()',
fallback)`-Dialekt ist machbar (OGNL-Auswertung im Helper; Zwischen-`null` → Fallback; unbekannte
Roots und Tippfehler laut werfen). Er hängt jedoch an OGNL-**Exception-Message-Präfixen**
(`source is null for getProperty(null,` bzw. `target is null for method `) und ist damit brüchig
gegenüber OGNL-Versionswechseln. Nur einführen, falls die `th:with`-Vorprüfungen in den tiefen
Clustern (GOST-Kursplanung, Abitur-Anlagen, Stundenplanung) unwartbar werden — dann zwingend mit
Unit-Tests an genau diesen Fehlerfällen. Zweite, nicht verifizierte Notlösung: Wrapper um den
`OgnlVariableExpressionEvaluator` auf Thymeleaf-Ebene.

## 6. CSS, Templates-Ausgabe & Code-Stil

- **CSS wird inline als XML geparst** (`#inline.css(...)` bzw. `th:utext`): keine rohen `<`,
  `>`, `&` — auch nicht in Kommentaren. Sonderzeichen brechen die PDF-Erzeugung.
- **Layout nur mit Tabellen** (OpenHtmlToPdf kennt kein Flexbox/Grid); Layout-Tabellen brauchen
  einen versteckten `<thead th:if="${false}">` mit je einem `<th scope="col">` pro Spalte
  (SonarQube). Details in `reporting-template-erstellung.md`, Abschnitt 10.
- Auch einzeilige `if`/`else`-Bodys werden mit geschweiften Klammern geschrieben (Linter).
