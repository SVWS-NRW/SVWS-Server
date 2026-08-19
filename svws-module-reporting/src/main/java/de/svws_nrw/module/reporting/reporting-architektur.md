# Struktur des Reporting-Moduls

Diese Dokumentation beschreibt den Ablauf der Report-Erzeugung im SVWS-Server und die Verantwortung der einzelnen Klassen-Typen. Sie richtet sich an Entwickler, die das Reporting-Modul erweitern oder warten.

Die Doku des Reporting-Moduls ist auf drei Dateien verteilt:

- **`reporting-architektur.md`** (diese Datei) — *beschreibt* Schichten, Klassen und Datenfluss.
- **[`reporting-konventionen.md`](reporting-konventionen.md)** — die *verbindlichen* Regeln und Invarianten (Schichtentrennung, Null-Sicherheit, Fehlercodes, OGNL-Grenzen). **Vor jeder Änderung am Modul lesen**; bei Konflikt gilt die Konventionen-Datei.
- **[`reporting-template-erstellung.md`](reporting-template-erstellung.md)** — Schritt-für-Schritt-Anleitung für Vorlagen-Autoren.

---

## 1. Einleitung

Das Reporting-Modul (`svws-module-reporting`) erzeugt aus Datenbankinhalten formatierte Reports — typischerweise auf Basis von HTML-Vorlagen, die über Thymeleaf gerendert werden. Aus dem gerenderten HTML lassen sich anschließend PDF-Dateien erzeugen oder per E-Mail versenden.

Architekturisch arbeitet das Modul in vier Schichten:

1. **API-Schicht** (`svws-openapi`) nimmt den Request entgegen.
2. **Factory-Schicht** orchestriert den Ablauf, validiert Eingaben und übergibt an Format-Factories.
3. **Daten-Schicht** (`ReportingContext` + Domänen-Repositories) lädt und cached die fachlichen Daten aus der Datenbank.
4. **Render-Schicht** (Builder + Renderer + Format-Factories) erzeugt die finale Ausgabe (HTML, PDF, ZIP) bzw. reicht sie an den E-Mail-Versand weiter.

---

## 2. Schichten im Überblick

Die Pipeline einer Report-Erzeugung ist immer dieselbe — unabhängig vom Ausgabeformat:

```
APIReporting
    │ instanziiert
    ▼
ReportingFactory ─── validiert Eingaben, baut den Kontext
    │ erzeugt
    ▼
ReportingContext (+ 9 Domänen-Repositories, Sortierung, Filterung)
    │ wird übergeben an
    ▼
HtmlFactory ── erzeugt HtmlContexts (Daten gemäß Template) ──► ReportBuilderHtml ── ReportRendererHtml ──► HTML
    │ (bei PDF/E-Mail)
    ▼
PdfFactory ── ReportBuilderPdf ── ReportRendererPdf ──► PDF
    │ (bei E-Mail)
    ▼
EmailFactory ── erzeugt EmailJob mit PDF-Anhängen ──► asynchroner Versand
```

---

## 3. Aufrufkette von der API

### 3.1 `APIReporting`

Pfad: `svws-openapi/.../api/server/APIReporting.java`

Die API-Klasse stellt drei JAX-RS-Endpunkte bereit:

- **HTML-Ausgabe**: `POST /db/{schema}/reporting/html` — gibt den Report als selbsttragendes HTML-Dokument zurück.
- **PDF-Ausgabe**: `POST /db/{schema}/reporting/ausgabe` — gibt den Report als PDF-Datei bzw. ZIP-Archiv zurück.
- **E-Mail-Versand**: `POST /db/{schema}/reporting/email` — startet den Versand als asynchronen Job.

Alle Endpunkte instanziieren die `ReportingFactory` und delegieren ihr die eigentliche Arbeit. Die API-Klasse selbst enthält keine Reporting-Logik — sie ist nur der Eintrittspunkt und stellt die Datenbankverbindung sowie die Berechtigungen bereit.

### 3.2 `ReportingFactory`

Pfad: `module.reporting.factories.ReportingFactory`

Die zentrale Eingangs-Factory. Im Konstruktor:

1. Validiert sie alle übergebenen Parameter (Datenbankverbindung, `ReportingParameter`, `ReportingAusgabeformat`, `ReportingReportvorlage`, IDs der Haupt- und Detaildaten).
2. Prüft, ob die Reportvorlage zum gewählten Ausgabeformat passt.
3. Erzeugt den zentralen `ReportingContext` (siehe Abschnitt 4) — alle weiteren Stufen arbeiten ausschließlich gegen dieses Kontext-Objekt.

Ihre Methode `createReportResponse()` dispatcht auf das Ausgabeformat:

| Ausgabeformat | Ablauf |
|---------------|--------|
| `HTML`        | `HtmlFactory.createHtmlResponse()` |
| `PDF`         | `HtmlFactory.createHtmlBuilders()` → `PdfFactory.createPdfResponse()` |
| `EMAIL`       | `HtmlFactory.createHtmlBuilders()` → `PdfFactory` → `EmailFactory.sendEmails(pdfFactory)` |

---

## 4. Datenebene

### 4.1 `ReportingContext`

Pfad: `module.reporting.repositories.ReportingContext`

Der `ReportingContext` ist der zentrale Kontext-Container, der durch die gesamte Reporting-Pipeline gereicht wird. Er hält:

**Infrastruktur**
- `conn()` — `DBEntityManager` für DB-Zugriffe
- `reportingParameter()` — typisierte Parameter (siehe 4.4)
- `logger()` / `log()` — Logging-Infrastruktur (siehe Abschnitt 9)
- `sortierungService()` / `filterService()` — Querschnitts-Services für Sortierung und Filterung

**Zugriff auf Domänen-Repositories** (siehe 4.2)
- `repositorySchule()`, `repositoryKataloge()`, `repositoryLehrer()`, `repositorySchueler()`, `repositoryLerngruppen()`, `repositoryStundenplan()`, `repositoryGost()`, `repositoryGostKlausurplanung()`, `repositoryGostKursplanung()`

**Aktuell angemeldeter Benutzer**
- `benutzer()` liefert einen `ProxyReportingBenutzer`, der den angemeldeten Benutzer der DB-Verbindung bündelt (Benutzerdaten, E-Mail-Daten, benutzerbezogener `EmailJobManagerContext`). Der Proxy wird im Konstruktor des Contexts initialisiert und steht damit allen nachgelagerten Schichten zur Verfügung — z. B. für E-Mail-Versand im Namen des Benutzers und für Berechtigungs-/Identitäts-Anzeigen in Reports.

Der `ReportingContext` selbst hält keine fachlichen Daten und keinen eigenen Cache — er delegiert vollständig an die Domänen-Repositories. Sein Konstruktor instanziiert in fester Reihenfolge die neun Domänen-Repositories und reicht jedem davon `this` weiter, sodass jedes Sub-Repository im Bedarfsfall auf die Infrastruktur und auf die anderen Domänen zugreifen kann.

### 4.2 Die neun Domänen-Repositories

Alle Repositories liegen unter `module.reporting.repositories`. Jedes ist verantwortlich für eine fachliche Domäne. Die meisten Repositories arbeiten konsequent lazy: die Konstruktoren halten lediglich die Referenz auf den `ReportingContext`, alle DB-Zugriffe erfolgen erst beim ersten Aufruf des jeweiligen Getters bzw. der ID-basierten Lookup-Methode und werden anschließend intern gecached. Lediglich `ReportingRepositorySchule` und `ReportingRepositoryStundenplan` laden im Konstruktor kleine, ohnehin in jedem Report benötigte Metadaten (Schulstammdaten, Abschnitt, Stundenplandefinitionen). Die beiden GOSt-Subdomain-Repositories `ReportingRepositoryGostKlausurplanung` und `ReportingRepositoryGostKursplanung` verwalten je einen Manager pro Reporting-Request, der über eine explizite `initManager(...)`-Methode initialisiert wird — der Aufruf erfolgt aktuell aus dem Konstruktor des jeweils zuständigen HtmlContext (`HtmlContextGostKlausurplanungKlausurplan` bzw. `HtmlContextGostKursplanungBlockungsergebnis`); alle Reporting-Objekte (Klausurtermine/Kursklausuren/Schülerklausuren bzw. Blockungsergebnis/Schienen/Kurse) werden anschließend zentral im Repository aufgebaut und gecached. **Achtung Temporalkopplung:** Zugriffe auf diese Repositories vor der Context-Erzeugung (z. B. aus der `EmailFactory`) setzen voraus, dass der Manager bereits initialisiert wurde.

| Repository | Verantwortung | Typische Methoden |
|------------|---------------|-------------------|
| `ReportingRepositorySchule`              | Schulstammdaten, Schullogo, aktiver, kontextueller und ausgewählter Schuljahresabschnitt, Berechnungsmethoden auf Abschnittsebene | `stammdaten()`, `schullogoBase64()`, `schuljahresabschnitt(id)`, `aktuellerSchuljahresabschnitt()`, `auswahlSchuljahresabschnitt()` |
| `ReportingRepositoryKataloge`            | Fächerkatalog, Jahrgänge, Erzieherarten, weitere stammdaten-nahe Listen | `faecher()`, `jahrgaenge()`, `erzieherart(id)` |
| `ReportingRepositoryLehrer`              | Lehrkräfte, Kollegium, Leitungsfunktionen, Lehrer-spezifische Unterrichts-Aufstellungen | `lehrer(id)`, `lehrer(ids)`, `alleLehrer()`, `existiertLehrer(id)` |
| `ReportingRepositorySchueler`            | Schüler-Stammdaten, Lernabschnitte, Erzieher, Leistungsdaten, Sprachenfolgen | `schueler(id)`, `waehleAus(ids)`, `lernabschnitte(id)`, `erzieherStammdaten(id)` |
| `ReportingRepositoryLerngruppen`         | Klassen und Kurse | `klasse(id)`, `kurs(id)`, `waehleKlassenAus(ids)`, `klassen(idSchuljahresabschnitt)` |
| `ReportingRepositoryStundenplan`         | Stundenpläne inkl. Pausen, Räume und Aufsichten | `stundenplan(id)` (strikt), `stundenplan(datum)` (optional), `manager(id)` |
| `ReportingRepositoryGost`                | Allgemeine Daten der gymnasialen Oberstufe: Abiturjahrgänge, Jahrgangsdaten und FächerManager, Laufbahn-Beratungsdaten, Abiturdaten, Fachwahlstatistik | `abiturjahrgaenge()`, `jahrgangsdaten(abiturjahr)`, `faecherManager(abiturjahr)`, `beratungsdaten(id)`, `schuelerAbiturdaten(id)`, `fachwahlen(abiturjahr)` |
| `ReportingRepositoryGostKlausurplanung`  | GOSt-Klausurplanung: zentral aufgebauter `GostKlausurplanManager` sowie alle daraus abgeleiteten Reporting-Objekte (Klausurtermine, Kursklausuren, Schülerklausuren). Schüler und Kurse werden über die zentralen Schüler- und Lerngruppen-Repositories bezogen. | `initManager(selection)`, `manager()`, `klausurtermine()`/`klausurtermin(id)`, `kursklausuren()`/`kursklausur(id)`, `schuelerklausuren()`/`schuelerklausur(id)`, `schueler()`, `kurse()` |
| `ReportingRepositoryGostKursplanung`     | GOSt-Kursplanung: zentral aufgebauter `GostBlockungsergebnisManager` sowie alle daraus abgeleiteten Reporting-Objekte (Blockungsergebnis, Schienen, Kurse). Schüler/Lehrer werden über die zentralen Repositories bezogen. | `initManager(idBlockungsergebnis)`, `manager()`, `blockungsergebnis()`/`blockungsergebnis(id)`, `schienen()`/`schiene(id)`, `kurse()`/`kurs(id)` |

Die Repositories sind die einzigen Stellen im Modul, die `new DataXxx(...)` aus dem Server-DB-Paket aufrufen oder direkt Queries gegen `conn()` absetzen. Alle übrigen Schichten — insbesondere die Proxy-Reporting-Typen — gehen ausschließlich über die Repository-Methoden.

Auch das Instanziieren der zugehörigen `ProxyReporting…`-Objekte (z. B. `ProxyReportingSchueler`, `ProxyReportingLehrer`, `ProxyReportingKlasse`, `ProxyReportingKurs`) ist ausschließlich dem jeweiligen Repository vorbehalten. Damit existiert jedes Reporting-Objekt pro Reporting-Lauf nur einmal im Cache und `==`-Identität für gleiche IDs ist garantiert.

**Konvention zur Filter-Anwendung:** Listen- und Single-Object-Getter wenden den vom Anwender gesetzten `Reporting<Typ>.FILTER` zentral im Repository an (analog zur Sortierung). Listen-Getter liefern eine bereits gefilterte Liste; Single-Object-Getter (`schueler(id)`, `lehrer(id)`, `klasse(id)`, `kurs(id)`) geben `null` zurück, wenn das Objekt durch den User-Filter ausgeschlossen wurde. Damit verschwindet ein gefiltertes Objekt automatisch auch aus allen Backrefs (`schueler.klasse`, `klasse.klassenlehrer`, …). Aufrufer müssen entsprechend null-safe sein; Thymeleaf rendert `null` als leer. Cache-Befüllung und repo-interne Auflösung laufen unverändert ohne Filter — gefilterte Objekte bleiben im Cache, nur die öffentliche API-Rückgabe ist gefiltert.

### 4.3 Querschnitts-Services und Begleit-Dateien: Sortierung und Filterung

Sortierung und Filterung sind im Reporting-Modul typsicher pro Reporting-Typ konfiguriert. Für jeden Reporting-Typ existiert eine **Begleit-Datei** neben dem Typ, die dessen Registry (Whitelist erlaubter Attribute) und — bei Sortierung — die Standardsortierung beschreibt. Der zugehörige Reporting-Typ exponiert die fertigen Konfigurationen als statische Konstanten `SORTIERUNG` und `FILTER` mit den Typen `ReportingSortierung<T>` bzw. `ReportingFilterung<T>`.

Auf API-Seite übergibt der Endnutzer in den `ReportingParameter` für jeden Reporting-Typ eine eigene **Gruppe** (Schlüssel ist der einfache Klassenname, z. B. `"ReportingSchueler"`). Zwei zustandslose Helfer-Services im `ReportingContext` — `ReportingSortierungService` und `ReportingFilterService` — extrahieren aus den Parametern die Gruppe zum gewünschten Typ. Die typisierte Umsetzung in einen `Comparator<T>` bzw. `Predicate<T>` erfolgt anschließend über die `SORTIERUNG`/`FILTER`-Konstante des Reporting-Typs.

Neue Reporting-Typen werden eingebunden, indem eine `Reporting<Typ>Sortierung`- und/oder `Reporting<Typ>Filter`-Begleit-Datei angelegt und am Reporting-Typ eine `SORTIERUNG`/`FILTER`-Konstante exponiert wird — ohne Eingriff in die Services.

#### 4.3.1 `ReportingSortierung<T>` und Begleit-Datei `Reporting<Typ>Sortierung`

Pfad: `module.reporting.sortierung.ReportingSortierung`

Bündelt für einen Reporting-Typ `T` die `SortierungRegistry<T>` und die Standardsortierung in einer typsicheren Konfiguration. Wird über einen Builder erzeugt (`ReportingSortierung.<T>builder().registry(...).standard(...).build()`) und stellt folgende Methoden bereit:

- `registry()` — die zugrundeliegende `SortierungRegistry<T>` (z. B. für `importiereRegistryEintraege(...)` in einer übergeordneten Registry).
- `standardsortierung()` — Liste der Attributnamen der Standardsortierung in Sortierreihenfolge.
- `comparator(List<String> attribute, List<String> validierungsfehler)` — baut einen `Comparator<T>` aus extern übergebenen Attributen. Unbekannte Attribute werden in `validierungsfehler` gesammelt.
- `comparatorStandard()` — baut einen `Comparator<T>` aus der Standardsortierung. Da diese aus dem Code stammt, wäre ein unbekanntes Attribut ein Programmierfehler und wird als `IllegalStateException` sichtbar.
- `comparatorIdentitaet()` — Comparator, der die Eingabereihenfolge unverändert lässt (macht „keine Sortierung" an der Aufrufstelle explizit).

Die Begleit-Datei `Reporting<Typ>Sortierung.java` (z. B. `ReportingSchuelerSortierung`, `ReportingKursSortierung`, `ReportingGostKlausurplanungSchuelerklausurSortierung`) liegt direkt neben dem Reporting-Typ und enthält:

- eine private `buildRegistry()`-Methode, die alle erlaubten Attribute mit ihren Extraktoren registriert,
- eine `public static final ReportingSortierung<T> SORTIERUNG`-Konstante mit Registry und Standardsortierung,
- einen privaten Konstruktor, der die Begleit-Klasse als nicht-instanziierbar markiert.

Der Reporting-Typ selbst exponiert die Konstante mit kurzer Schreibweise weiter, z. B.:

```java
public static final ReportingSortierung<ReportingSchueler> SORTIERUNG = ReportingSchuelerSortierung.SORTIERUNG;
```

#### 4.3.2 `SortierungRegistry<T>`

Pfad: `module.reporting.sortierung.SortierungRegistry`

Generische Registry, die eine Map aus normierten Attributnamen auf typsichere Extraktor-Funktionen verwaltet und Builder-Helfer bereitstellt:

- `registiereString(name, extractor)` / `registiereString(SerializableFunction)` — Attribute mit String-Wert (locale-aware deutsche Sortierung).
- `registiereComparable(name, extractor)` / `registiereComparable(SerializableFunction)` — Attribute mit numerischem/Date-/Enum-Wert.
- `importiereRegistryEintraege(prefix, sub-registry, navigator)` — übernimmt Einträge einer fremden Registry mit Pfad-Präfix (z. B. `auswahlLernabschnitt.klasse.kuerzel`). Genutzt, um die Attribute eines verschachtelten Reporting-Typs unter einem Prefix in die aktuelle Registry zu spiegeln.

Methodennamen werden über `ReportingTypesUtils.methodeToString(SerializableFunction)` typsicher aus `SerializedLambda` extrahiert — das vermeidet Magic-Strings in der Standardsortierung und in Import-Prefixen.

#### 4.3.3 `ReportingSortierungService`

Pfad: `module.reporting.sortierung.ReportingSortierungService`

Zustandsloser Service. Einstiegsmethode:

```java
List<String> getSortierungsAttribute(String typ, List<String> fallbackStandardsortierung)
```

Sie sucht die `ReportingSortierungDefinitionGruppe` für den angegebenen Typnamen in den Reporting-Parametern und löst sie wie folgt auf:

1. Liegt eine benutzerdefinierte Definition vor (`verwendeStandardsortierung == false`, mindestens ein Attribut), werden deren *bereinigte* Attributnamen zurückgegeben (Whitespace und nachgestellte `()` werden entfernt).
2. Wird explizit die Standardsortierung gewünscht (`verwendeStandardsortierung == true`) ODER fehlt eine passende Definition ganz, wird die übergebene `fallbackStandardsortierung` zurückgegeben.
3. Bei `fallbackStandardsortierung == null` und ohne benutzerdefinierte Sortierung liefert die Methode eine leere Liste — interpretiert als „keine Sortierung".

Der Service kennt selbst keine Reporting-Typen und keine Registries — die Standardsortierung wird ausschließlich von der Aufrufstelle übergeben (typischerweise `Typ.SORTIERUNG.standardsortierung()`).

#### 4.3.4 `ComparatorFactory`

Pfad: `module.reporting.sortierung.ComparatorFactory`

Stellt die einzige zentrale Methode bereit, die Service und Begleit-Konstante zusammenbringt:

```java
<T> Comparator<T> buildComparator(
        ReportingSortierungService sortierungService,
        Logger logger,
        String typName,
        ReportingSortierung<T> sortierung,
        boolean erzeugeComparatorZuSortierung)
```

Ablauf:

1. Wenn `erzeugeComparatorZuSortierung == false` oder kein Service vorhanden ist, wird `sortierung.comparatorIdentitaet()` zurückgegeben.
2. Sonst fragt die Methode `sortierungService.getSortierungsAttribute(typName, sortierung.standardsortierung())` ab.
3. Ist die Attributliste leer, wird ebenfalls die Identität zurückgegeben.
4. Sonst wird `sortierung.comparator(attribute, validierungsfehler)` aufgerufen; eventuelle Validierungsfehler werden ins Log geschrieben.

Die Domänen-Repositories (z. B. `ReportingRepositoryLehrer.lehrer(List<Long>, boolean)`) nutzen diese Methode, um beim Bulk-Load direkt sortierte Listen zurückzugeben. Die `HtmlContext`-Subklassen sortieren über `HtmlContextSortierung`, die ebenfalls an diese Methode delegiert (siehe Abschnitt 6.3).

#### 4.3.5 `ReportingFilterung<T>` und Begleit-Datei `Reporting<Typ>Filter`

Pfad: `module.reporting.filterung.ReportingFilterung`

Bündelt für einen Reporting-Typ `T` dessen `FilterRegistry<T>` in einer typsicheren Konfiguration. Wird über einen Builder erzeugt (`ReportingFilterung.<T>builder().registry(...).build()`) und stellt bereit:

- `registry()` — die zugrundeliegende `FilterRegistry<T>`.
- `bedingung(ReportingFilterDefinitionGruppe gruppe, List<String> validierungsfehler)` — baut aus einer Filtergruppe ein `Predicate<T>`:
  - Ist die Gruppe `null` oder leer, liefert die Methode ein Pass-Through `t -> true`.
  - Enthält die Gruppe genau eine `ReportingFilterDefinition`, wird sie 1:1 verwendet.
  - Bei mehreren Definitionen werden sie gemäß `gruppe.multiselectVerknuepfung` (`AND`/`OR`) kombiniert.

Die Begleit-Datei `Reporting<Typ>Filter.java` (z. B. `ReportingSchuelerFilter`, `ReportingKursFilter`, `ReportingGostKursplanungKursFilter`) liegt direkt neben dem Reporting-Typ und enthält:

- eine private `buildRegistry()`-Methode mit allen `registriereAttribut(...)`-Aufrufen,
- eine `public static final ReportingFilterung<T> FILTER`-Konstante,
- einen privaten Konstruktor zur Markierung als nicht-instanziierbar.

Der Reporting-Typ exponiert die Konstante weiter, z. B.:

```java
public static final ReportingFilterung<ReportingSchueler> FILTER = ReportingSchuelerFilter.FILTER;
```

#### 4.3.6 `FilterRegistry<T>`

Pfad: `module.reporting.filterung.FilterRegistry`

Generische Auswertungs-Engine:

- `registriereAttribut(name, Function<T, ?>)` / `registriereAttribut(SerializableFunction)` — registriert ein filterbares Attribut (case-insensitiv gespeichert).
- `erstelleFilter(ReportingFilterDefinition, validierungsfehler)` — Hauptmethode, baut aus einer Definition ein `Predicate<T>`. Eine Definition besteht aus *Kriterien*; ein Kriterium aus *Einträgen* (Attribut+Operation+Werte) und optionalen *Unterkriterien*. Innerhalb eines Kriteriums werden Einträge gemäß `verknuepfung` (`AND`/`OR`) verknüpft, optional negiert. Unterkriterien werden rekursiv ausgewertet.
- Unterstützte Operationen (siehe `ReportingFilterOperation`): `EQUAL`, `NOT_EQUAL`, `CONTAINS`, `STARTS_WITH`, `ENDS_WITH`, `GREATER`, `GREATER_OR_EQUAL`, `LESS`, `LESS_OR_EQUAL`, `IN`, `BETWEEN`.
- Typ-Anpassung: Der String-Wert aus dem Filter wird abhängig vom Laufzeit-Typ des Attributwerts konvertiert (`String`, `Number`, `Boolean`, `LocalDate`, `LocalDateTime`, generische `Comparable`-Typen). String-Vergleiche sind case-insensitiv.
- Unbekannte Attribute werden ignoriert (Predicate liefert `true`); der Name wird in `validierungsfehler` gesammelt und kann später z. B. ins Log ausgegeben werden.

#### 4.3.7 `ReportingFilterService`

Pfad: `module.reporting.filterung.ReportingFilterService`

Zustandsloser Service mit zwei Methoden:

```java
ReportingFilterDefinitionGruppe getFilter(String typ)
boolean                         hatFilter(String typ)
```

- `getFilter(typ)` liefert den Filter in Form einer `ReportingFilterDefinitionGruppe` aus den `ReportingParameter.filterDefinitionenGruppen` für den angegebenen Reporting-Typ — oder `null`, wenn keine Gruppe vorhanden ist. Das Ergebnis wird direkt an `Typ.FILTER.bedingung(gruppe, validierungsfehler)` weitergegeben, um daraus das `Predicate<T>` zu bauen.
- `hatFilter(typ)` liefert `true`, sobald für den Typ mindestens eine Filterdefinition mit mindestens einem Kriterium existiert. Wird typischerweise im Reporting-Datenaufbau benötigt, um zu entscheiden, ob ein gefilterter Sub-Datensatz separat aufgebaut werden muss (z. B. „nur die selektierten Kurse" zusätzlich zu „alle Kurse" im selben Report).

#### 4.3.8 Definitions-Klassen und Factories (Paket `core.data.reporting` / `core.utils.reporting`)

Die *Definitions*-Datenklassen werden im Core-Modul gepflegt, damit der WebClient sie über die transpiliert verfügbar hat:

- `ReportingSortierungDefinitionGruppe` — Gruppe pro Reporting-Typ, enthält `typ`, `ortsteil`, Liste der ausgewählten `sortierungDefinitionen` und der zur Auswahl stehenden `sortierungDefinitionenOptionen`.
- `ReportingSortierungDefinition` — eine einzelne Sortierung mit `attribute: List<String>` und Flag `verwendeStandardsortierung`.
- `ReportingFilterDefinitionGruppe` — analog für Filter; zusätzlich `uiIstMultiselect` und `multiselectVerknuepfung` (`AND`/`OR`).
- `ReportingFilterDefinition` — eine einzelne Definition mit Liste von `ReportingFilterKriterium`.
- `ReportingFilterKriterium` — Baustein mit `eintraege` (Attribut + Operation + Werte), `unterkriterien`, `verknuepfung` und Negations-Flag.

Zum bequemen programmatischen Erzeugen dieser Strukturen — vor allem im Client, der über transpilierte Versionen verfügt — stehen Factory-Klassen bereit:

- `ReportingSortierungDefinitionFactory` / `ReportingSortierungDefinitionGruppeFactory`
- `ReportingFilterDefinitionFactory` (mit statischen Helfern wie `definition(...)`, `and(...)`, `or(...)`, `eq(...)`, `in(attribut, werte...)`)
- `ReportingFilterDefinitionGruppeFactory` mit zwei Convenience-Methoden:
  - `gruppe(bezeichnung, typ, uiIstSichtbar, definitionen...)` — Standardfall.
  - `gruppeAusIds(bezeichnung, typ, uiIstSichtbar, List<Long> idsListe)` — Kurzform für eine ID-basierte Auswahl: erzeugt automatisch eine Definition mit einem `IN`-Filter auf das Attribut `"id"`.

#### 4.3.9 Typischer Einsatz

Filterung und Sortierung werden zentral in den Listen-Methoden der Domänen-Repositories angewandt. Das Muster sieht — analog zu `ComparatorFactory.buildComparator(...)` für die Sortierung — so aus:

```java
// Filter-Predicate aus dem Service holen — Typname per ClassName
final Predicate<ReportingLehrer> filter = ReportingLehrer.FILTER.bedingung(
        reportingContext.filterService().getFilter(ReportingLehrer.class.getSimpleName()),
        null);

// Comparator analog
final Comparator<ReportingLehrer> comparator = ComparatorFactory.buildComparator(
        reportingContext.sortierungService(),
        reportingContext.logger(),
        ReportingLehrer.class.getSimpleName(),
        ReportingLehrer.SORTIERUNG,
        sortiereListe);

// Beides wird an die zentrale Helper-Methode übergeben, die laden → filtern → sortieren übernimmt:
ReportingRepositoryUtils.waehleAus(
        ids, mapStammdaten, mapReportingObjekte,
        stammdatenLoader, reportingObjektErsteller, idExtractor,
        comparator, filter, "Lehrer", reportingContext.logger(), ladefehlerLehrerStammdaten);
```

Damit propagieren Filter und Sortierung automatisch in alle Aggregate (Klassen, Kurse, Klausurplan, Blockungsergebnis, Stundenplanung …), sobald sie ihre Aggregat-Listen über die Repos auflösen. Die Aufrufer benötigen keine eigene Filter-Logik mehr.

Innerhalb von Konstruktoren oder Sub-Listen werden die Sortier-Attributnamen häufig direkt über den Service geholt und an einen Sub-Konstruktor übergeben — die Standardsortierung kommt dabei explizit aus der `SORTIERUNG`-Konstante des Typs:

```java
reportingContext.sortierungService().getSortierungsAttribute(
        ReportingSchueler.class.getSimpleName(),
        ReportingSchueler.SORTIERUNG.standardsortierung());
```

### 4.4 Parameter

- **`ReportingParameter`** (Paket `core.data.reporting`) — POJO aus dem Core-Modul mit den über die API übergebenen Steuerungsdaten (Reportvorlage, Ausgabeformat, IDs, Sortierungs- und Filterangaben).
- **`ReportingParameterTypisiert`** (Paket `module.reporting.parameter`) — typisierter Wrapper, der die untypisierten Felder (z. B. enum-Werte als String) in stark typisierte Werte umsetzt und Komfort-Getter wie `idHauptdatenObjekt()`, `idsHauptdaten()`, `idsDetaildaten()`, `reportVorlage()`, `einzelausgabeDaten()` bereitstellt.

### 4.5 Validierung der Eingabeparameter

Die Eingabe-Validierung liegt in der paketprivaten Hilfsklasse `HtmlContextValidierung` (Paket `html.contexts.initializer`) und wird von den Initializern vor dem Bau der `HtmlContext`-Instanzen aufgerufen. Ihre Methoden sind statisch und nehmen den `ReportingContext` als ersten Parameter — nur so sind sie sowohl aus den Initializern als auch als Methodenreferenz aus der request-unabhängigen Konfiguration der Registry heraus verwendbar. Neben den allgemeinen Prüfungen enthält sie die je Datenaufbau gebündelten Zusatzprüfungen (`pruefungenGostAbitur(...)`, `pruefungenGostLaufbahnplanung(...)`), die in der Registry als Methodenreferenz eingetragen sind:

- `pruefeUndMeldeAuswahl(...)` — prüft, dass die Anfrage überhaupt Hauptdaten benennt (eine im Request leere ID-Liste ergibt `BAD_REQUEST`), und meldet je ausgelassener ID der Auswahl ein Ausgabeproblem mit der Ursache aus ihrem Ladezustand. Über diese Prüfung laufen die Datenaufbauten nach dem Listen-Muster und die fünf Sichtweisen der Stundenplanung: Eine ID, die sich nicht auflösen lässt, wird ausgelassen, statt den Report abzubrechen.
- `validiereSchuleMitGost()` — delegiert an `repositorySchule().istSchuleMitGost()` und wirft bei `false` eine `ApiOperationException`.
- `validiereParameterFuerAbiturjahrgangUndHalbjahre(boolean paarweise)` — vereint die Validierungen für die GOSt-Klausurplanung; je nach Flag werden Abiturjahre und Halbjahre paarweise (z. B. (2026, EF.1), (2026, EF.2)) oder unabhängig validiert. Stützt sich auf `validiereAbiturjahr(...)`, `validiereHalbjahr(...)`, `validiereParameterPaarweise(...)` und `validiereParameterEinzeln(...)`.

Alle Validierer werfen bei Fehlern eine `ApiOperationException` und behalten ihr bisheriges Logging-Verhalten: die Prüfung der leeren ID-Liste und `validiereSchuleMitGost(...)` protokollieren zuvor über `reportingContext.logger()`, `validiereParameterFuerAbiturjahrgangUndHalbjahre(...)` und die von ihr genutzten Prüfungen werfen ohne eigenen Log-Eintrag. Die Prüf-Logik steht damit an einer Stelle und ist nicht an die `HtmlFactory` gebunden.

### 4.6 Signierte Schulbescheinigung (QR-Code) — Paket `signing/`

Für die fälschungssichere Schulbescheinigung erzeugt das Paket `module.reporting.signing` zwei QR-Codes pro Schüler: einen mit den komprimierten Bescheinigungsdaten und einen mit deren digitaler Signatur. Die Pipeline ist bewusst aus der Repository-Schicht herausgelöst und in einer eigenen Factory gebündelt; das Repository verantwortet nur das Caching.

- **`SchulbescheinigungQrFactory`** — Einstiegspunkt. `erzeuge(List<Long> idsSchueler)` durchläuft die mehrstufige Pipeline für einen ganzen Batch: Ausstellungsdaten ermitteln → je Schüler XSchule-XML erzeugen → alle XMLs in **einem** Aufruf des Signierdienstes signieren → QR-Codes als SVG rendern. Der Signier-Service wird lazy über einen `Supplier<SignatureService>` (Default: `SignatureServiceFactory`/it.NRW) bezogen, damit Konfigurationsfehler erst bei tatsächlichem Bedarf greifen. Scheitern XML-Erzeugung, Signierung oder Rendering trotz geladener Ausgangsdaten, legt die Factory je betroffenem Schüler einen Eintrag mit Fehlermeldung an und meldet das Ausgabeproblem. Fehler beim Aufbau der gemeinsamen Ausstellungsdaten fängt sie dagegen bewusst nicht: Sie propagieren zum Lade-Fallback des aufrufenden Repositories, das die Exception je Schüler festhält und nach außen weiterhin einen Fehlereintrag statt `null` liefert.
- **`SchulbescheinigungXmlFactory`** — `erzeugeXml(ReportingSchueler, ReportingSchule, …)` baut das XSchule-konforme XML einer einzelnen Bescheinigung.
- **`SchulbescheinigungQrDaten`** — `record(String qr1Svg, String qr2Svg, String fehlermeldung)`: das Ergebnis pro Schüler (Daten-QR, Signatur-QR, optionale Fehlermeldung).
- **`SchulbescheinigungQrEinstellungen`** — zentrale Konstanten: Präfixe `DATAV1:` (QR 1, Daten) und `SIGNV1:` (QR 2, Signatur), Maße (`QR_BREITE_MM`/`QR_HOEHE_MM` = 40 mm) und Fehlerkorrektur-Level.

**Anbindung:** Die Templates greifen nicht direkt auf das `signing/`-Paket zu. Stattdessen liefert `ReportingRepositorySchueler.schulbescheinigungQrDaten(long idSchueler)` die `SchulbescheinigungQrDaten` und cached sie in `mapSchulbescheinigungQrDaten`; der Bulk-Aufbau über `SchulbescheinigungQrFactory.erzeuge(...)` läuft nach demselben Lazy-/Cache-Muster wie die übrigen Repository-Daten. Im Reporting-Typ werden die SVGs über `ProxyReportingSchueler` / `ReportingSchueler` bereitgestellt.

---

## 5. Reporting-Typen

Die Reporting-Typen sind die fachlichen Datenobjekte, die in den Templates verwendet werden. Sie liegen unter `module.reporting.types/`, gruppiert nach Domäne (`schueler/`, `lehrer/`, `lerngruppen/`, `gost/`, `stundenplanung/`, `schule/`, …).

### 5.1 `ReportingBaseType`

Pfad: `module.reporting.types.ReportingBaseType`

Utility-Basisklasse aller Reporting-Typen. Stellt Hilfsmethoden für die Template-Ausgabe bereit, z. B. `ersetzeNullBlankTrim(...)` und `ersetzeStringNullDurchEmpty(...)`, mit denen `null`-Werte vorlagensicher in leere Strings umgesetzt werden.

### 5.2 Reporting-Types (POJOs)

Die "normalen" Reporting-Typen wie `ReportingSchueler`, `ReportingLehrer`, `ReportingKlasse`, `ReportingKurs` sind reine, immutable POJOs. Sie erben von `ReportingBaseType` und enthalten

- Felder für alle Datenpunkte, die in Templates verwendet werden,
- einen vollständigen Konstruktor, der alle Felder setzt,
- Getter, die die Werte unverändert bzw. via `ersetze*` aufbereitet zurückgeben.

Sie kennen weder die Datenbank noch den `ReportingContext`. Sie sind das, was Thymeleaf am Ende rendert.

**Null-Sicherheit (modulweit umgesetzt 2026-06):** Getter liefern non-null per Default — String-/Datums-Felder werden im Basis-Konstruktor auf `""` normalisiert, Listen/Maps auf leere Defensivkopien; Objekt-/Enum-/Boxed-Getter dürfen dokumentiert `null` sein. Die verbindlichen Konstruktor-Regeln inkl. der Rückreferenz-Ausnahme stehen in [`reporting-konventionen.md`](reporting-konventionen.md), Abschnitt 2.

### 5.3 Proxy-Reporting-Types (Lazy Loading)

Für jeden "schweren" Reporting-Typ existiert eine `ProxyReporting…`-Subklasse (z. B. `ProxyReportingSchueler`, `ProxyReportingLehrer`, `ProxyReportingKlasse`). Diese Proxy-Typen:

- Erben vom POJO und überschreiben einzelne Getter mit Lazy-Loading-Logik.
- Halten ein einziges privates Feld `private final ReportingContext reportingContext`.
- Werden ausschließlich mit den Stammdaten initialisiert (z. B. bei `ProxyReportingSchueler` mit `SchuelerStammdaten`); alle weiteren Daten werden erst auf Anfrage über den Kontext nachgeladen.
- **Greifen niemals direkt auf `conn()` zu und instanziieren keine `DataXxx`-Klassen aus dem `svws-db`-Paket.** Stattdessen rufen sie ausschließlich Methoden der Domänen-Repositories auf, z. B. `reportingContext.repositoryLerngruppen().klasse(idKlasse)` oder `reportingContext.repositorySchueler().lernabschnitt(idLernabschnitt)`.
- **Sind null-safe gegen gefilterte Aggregate.** Da Single-Object-Getter der Repos (`schueler(id)`, `lehrer(id)`, `klasse(id)`, `kurs(id)`) bei aktivem User-Filter `null` liefern (siehe Abschnitt 4.2), prüfen Proxy-Typen den Rückgabewert vor jeder Verwendung — z. B. mit `if (x != null)`-Guards oder `.filter(Objects::nonNull)` in Streams. Null darf nicht in Listen oder Maps eingefügt werden; gefilterte IDs werden übersprungen.

Damit sind die Proxy-Typen reine Adapter zwischen den Reporting-Templates und den Domänen-Repositories — die gesamte DB-Logik liegt in der Repository-Schicht. Diese Trennung erlaubt es, die Repositories einzeln zu testen und Daten effizient (cached) bereitzustellen.

#### Typische Paket-Struktur unter `types/`

- `schule/` — `ReportingSchule`, `ReportingSchuljahresabschnitt`, `ReportingBenutzer`, NRW-Schulkatalog, …
- `fach/` — `ReportingFach`, `ReportingStatistikFach`
- `jahrgang/` — `ReportingJahrgang`
- `ankreuzkompetenz/` — `ReportingAnkreuzkompetenz`
- `lehrer/` — `ReportingLehrer`, `ReportingLehrerLeitungsfunktion`, Factory-Klassen für Unterrichtsaufstellungen
- `schueler/` — `ReportingSchueler`, Lernabschnitte (`lernabschnitte/`), Erzieher (`erzieher/`), Sprachen, Telefon, Schulbesuch, GOSt-Daten
- `lerngruppen/` — `ReportingKlasse`, `ReportingKurs`
- `gost/` — Abitur, Fachwahlstatistik, Kursplanung, Klausurplanung, Laufbahnplanung
- `stundenplanung/` — Stundenpläne, Pausen, Räume, Aufsichten
- `person/` — gemeinsame Basis für Personen (Schüler, Lehrer, Erzieher)

> Hinweis: Die Katalog-nahen Typen liegen — anders als das gebündelte `ReportingRepositoryKataloge` — in eigenen, fachlich getrennten Paketen (`fach/`, `jahrgang/`, `ankreuzkompetenz/`); ein gemeinsames Paket `kataloge/` existiert nicht.

---

## 6. HTML-Erzeugung

### 6.1 `HtmlFactory`

Pfad: `module.reporting.factories.HtmlFactory`

Aufgaben:

1. Validiert die HTML-Vorlage und prüft die Benutzer-Kompetenzen gegen die in der Vorlage hinterlegten Pflicht-Kompetenzen.
2. Baut über `erzeugeContexts()` eine Map `mapHtmlContexts: String → HtmlContext<?>`. Die Schlüssel sind interne Bezeichnungen der Context-Map und dienen insbesondere dem Nachschlagen und Ersetzen des Haupt-Contexts bei der Einzelausgabe. Sie sind nicht mit den Thymeleaf-Variablennamen gleichzusetzen und stehen als Konstanten in `HtmlContextSchluessel` (Paket `html.contexts.initializer`), auf die sowohl die `HtmlFactory` als auch die Registry zugreifen.
3. Holt sich über die `HtmlContextInitializerRegistry` den zum `ReportingReportvorlageDatenContext` gehörenden Initializer und stößt dessen Aufbau an — die Factory kennt die einzelnen Datenaufbauten nicht mehr. Der `ReportingReportvorlageDatenContext` benennt mit seinen 16 Werten je genau einen Ablauf des Datenaufbaus; die 28 Reportvorlagen verteilen sich auf diese 16 Werte, so dass zu einer Vorlage eindeutig feststeht, welche Daten geladen und welche Prüfungen durchgeführt werden. Details siehe Abschnitt 6.2.
4. Erzeugt mit `createHtmlBuilders()` bzw. `createHtmlResponse()` die `ReportBuilderHtml`-Instanzen und liefert das HTML als Response. Ein ZIP entsteht ausschließlich im PDF-Pfad; die HTML-Ausgabe liefert stets genau eine Datei.
5. Gibt über `bewusstLeer()` die Auskunft des Initializers heraus, ob die Auswahl bewusst keinen Datensatz enthält, und ergänzt die erfolgreiche Antwort über `ReportingHinweiseHeader` um den öffentlichen Hinweis-Header — sofern der Datenaufbau an die Diagnose angebunden ist (Abschnitt 9.3). **HTML bildet dabei keinen Sonderpfad** — es trägt den Header unter denselben Bedingungen wie PDF und ZIP. Dass der heutige generierte Client die Response-Metadaten verwirft und ihn deshalb nicht anzeigt, ändert am Serververtrag nichts.

Die Factory wird ausschließlich über die statische Methode `HtmlFactory.erzeuge(reportingContext)` erzeugt; der Konstruktor ist privat. Damit ist jede erreichbare `HtmlFactory` vollständig initialisiert — ein Objekt mit geprüfter Vorlage, aber ohne aufgebaute Contexts, ist strukturell unerreichbar.

Die `HtmlFactory` unterstützt zwei Modi:

- **Aggregierte Ausgabe** — alle Datensätze landen in einem einzigen HTML-Dokument.
- **Einzelausgabe** (`reportingParameter.einzelausgabeDaten()`) — pro Datensatz wird ein separates HTML-Dokument erzeugt; der zugehörige `HtmlContext` muss dafür `HtmlContextAufteilbar` implementieren. Unter welchem Schlüssel der Haupt-Context dabei ersetzt wird, liefert der Initializer über `einzelContextBezeichnung()`; Datenaufbauten ohne Einzelausgabe erben die Standard-Implementierung, die einen `BAD_REQUEST` wirft.

### 6.2 Der Aufbau der Daten-Contexts (`html/contexts/initializer/`)

Welche Daten ein Report lädt und welche Prüfungen dabei laufen, hängt nicht an der Reportvorlage, sondern an ihrem **Datenaufbau** (`ReportingReportvorlageDatenContext`, 16 Werte). Die 28 Vorlagen verteilen sich auf diese 16 Abläufe, die sich wiederum auf **fünf Ablaufmuster** zurückführen lassen. Neue Vorlagen mit bekanntem Datenaufbau brauchen deshalb kein Java.

Das Paket trennt konsequent zwischen der request-unabhängigen **Konfiguration** und dem **Initializer**, der sie für einen konkreten Request ausführt:

| Typ | Rolle |
|-----|-------|
| `HtmlContextInitializerRegistry` | Unveränderliche Zuordnung Datenaufbau → Konfiguration; eine Zeile je Datenaufbau. Nachschlagen über `aufbau(reportingContext, datenContext)`. Führt außerdem je Datenaufbau die Entscheidung, ob er an den Hinweisvertrag angebunden ist, abrufbar über `istAnHinweisvertragAngebunden(datenContext)`. |
| `HtmlContextAufbau` | Schnittstelle der Konfigurationen: `contextSchluessel()`, `unterstuetztEinzelausgabe()`, `initializer(...)`. Die Metadaten sind **ohne Reporting-Context lesbar** — genau darauf setzen die Registry-Tests auf. |
| `HtmlContextInitializer` | `init()` baut die Contexts auf, `einzelContextBezeichnung()` benennt den Haupt-Context. |
| `HtmlContextInitializerBasis` | Gemeinsame Felder plus die Standard-Einzelausgabe für Datenaufbauten, die sie nicht unterstützen. |
| `HtmlContextSchluessel` | Die Schlüssel der Context-Map als Konstanten. |
| `HtmlContextValidierung` | Alle Prüfungen der Eingabeparameter (siehe Abschnitt 4.5). |

Die fünf Ablaufmuster mit ihren Konfigurationstypen:

| Muster | Datenaufbauten | Worin sich die Zeilen unterscheiden |
|--------|----------------|-------------------------------------|
| `HtmlContextInitializerListe` | 6 (Schüler ×3, Klassen, Kurse, Lehrer) | Beschriftungen, Objektart, Auswahl der Hauptdaten, Context-Erzeuger und fachliche Einschränkung — über den Typparameter aneinander gebunden und damit compile-geprüft |
| `HtmlContextInitializerStundenplan` | 5 (Fach, Klassen, Lehrer, Raum, Schüler) | Beschriftungen, Objektart, Auswahl der Hauptdaten und Context-Erzeuger; das Laden des Stundenplans steht einmal im Initializer. Klassen, Lehrkräfte und Schüler wählen über ihr Repository aus, Fächer und Räume gegen den Bestand des geladenen Stundenplans |
| `HtmlContextInitializerGostKursplanung` | 2 (Kurs-, Schüler-Sicht) | nur der Context-Typ |
| `HtmlContextInitializerGostKlausurplanung` | 2 (Schüler-, Termin-Sicht) | nur der Context-Typ |
| `HtmlContextInitializerGostLaufbahnplanung` | 1 | Einzelfall ohne Konfiguration; einziger Datenaufbau ohne Einzelausgabe |

Nach außen sichtbar sind nur `HtmlContextInitializerRegistry`, `HtmlContextAufbau`, `HtmlContextInitializer` und `HtmlContextSchluessel` — alles Musterspezifische ist paketprivat.

**Ein neuer Datenaufbau** bedeutet einen neuen Enum-Wert plus eine Registry-Zeile; eine neue Klasse braucht es nur bei einem neuen Ablaufmuster. Die Tests in `TestHtmlContextInitializerRegistry` prüfen ohne Datenbank, dass jeder Enum-Wert einen Eintrag hat und dass Zuordnung, Map-Schlüssel, Einzelausgabe-Metadaten und Anbindung an den Hinweisvertrag den Sollwerten entsprechen. Ein weiterer Test verlangt zu jedem Enum-Wert eine geführte Entscheidung über die Anbindung, damit ein neuer Datenaufbau nicht unbemerkt ohne Header bleibt.

**Die Registry führt auch die Anbindung an den Hinweisvertrag.** Zu jedem Datenaufbau steht dort ausdrücklich `true` oder `false`, und ein fehlender Eintrag wird als „nicht angebunden“ beantwortet: Ein vergessener Eintrag darf nicht wie eine geprüfte, vollständige Ausgabe aussehen. Angebunden sind die vier Datenaufbauten nach dem Listen-Muster (`SCHUELER`, `KLASSEN`, `KURSE`, `LEHRER`) und die fünf Sichtweisen der Stundenplanung — neun von sechzehn. Die GOSt-Datenaufbauten lesen zusätzlich GOSt-Daten, deren Zugriffe noch nicht vollständig melden, und bleiben deshalb auf `false`.

### 6.3 `HtmlContext<T>`

Pfad: `module.reporting.html.contexts.HtmlContext`

Generische Basisklasse aller HTML-Kontexte. Sie kapselt:

- den Thymeleaf-`Context` (Variablen für die Vorlage),
- eine Liste `contextData: List<T>` mit den fachlichen Reporting-Objekten,
- eine Referenz `protected final ReportingContext reportingContext` für den Daten-Zugriff.

Subklassen befüllen den Thymeleaf-Context unter ihrem festen Variablennamen (PascalCase, z. B. `Schueler`, `Klassen`, `Schule`, `Parameter`, `GostBlockungsergebnis`). Häufig genutzte Subklassen:

- `HtmlContextBasisdaten` — wird bei jedem Report mitgeliefert (Schule, Schuljahresabschnitt, Parameter)
- `HtmlContextSchueler`, `HtmlContextLehrer`, `HtmlContextKlassen`, `HtmlContextKurse`
- `HtmlContextGostKursplanungBlockungsergebnis`, `HtmlContextGostKlausurplanungKlausurplan`, `HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken`
- `HtmlContextStundenplanungFachStundenplan`, `…KlassenStundenplan`, `…LehrerStundenplan`, `…RaumStundenplan`, `…SchuelerStundenplan`
- `HtmlContextAufteilbar` — Marker-Interface für Kontexte, die eine Einzelausgabe pro Datensatz erlauben

Innerhalb der Subklassen wird ausschließlich über den `reportingContext` auf Daten zugegriffen — analog zu den Proxy-Typen sind direkte DB-Zugriffe verboten.

Die Sortierung der Context-Daten läuft über die Basisklassen-Methoden `setContextDataSortiert(liste, SORTIERUNG, typ)` bzw. `erzeugeContextSortiert(...)`, die an die zustandslose Utility `HtmlContextSortierung.sortiere(...)` (Paket `module.reporting.html.contexts`) delegieren. Diese nutzt die `ComparatorFactory` und die `SORTIERUNG`-Konstante (`Reporting<Typ>.SORTIERUNG`) des jeweiligen Reporting-Typs. Liegt in den Reporting-Parametern eine benutzerdefinierte Sortierung für den Typ vor, wird diese verwendet, sonst die Standardsortierung des Typs.

**Filterung wird nicht im HtmlContext angewandt.** Alle Reporting-Typen werden zentral in den Repositories gefiltert (FILTER-Companion, siehe Abschnitt 4.3); die List-Contexts übernehmen die bereits gefilterten Listen unverändert.

### 6.4 Builder und Renderer für HTML

- **`ReportBuilderContextHtml`** (Paket `module.reporting.builders`) — Builder-Pattern-Container für Template-Code, HTML-Kontexte, IDs, Dateiname-Vorlage, Logger.
- **`ReportBuilderHtml`** — kapselt einen einzelnen HTML-Reportlauf: Dateiname, Content-Type, Inhalt. Delegiert die eigentliche Renderung an den Renderer.
- **`ReportRendererHtml`** — mergt alle Variablen aus den `HtmlContext`-Instanzen in einen einzigen `Context` und ruft `engine.process(template, ctx, writer)` auf. Die `TemplateEngine` selbst wird zentral in **`ReportBuilderUtils`** (Paket `module.reporting.builders`) konfiguriert; dort werden auch die eigenen Thymeleaf-Dialekte registriert (siehe unten).

Das Ergebnis ist der gerenderte HTML-String, den die `HtmlFactory` entweder direkt als Response liefert oder an die `PdfFactory` weiterreicht.

### 6.5 Eigene Thymeleaf-Dialekte (`html/dialects/`)

Zur Erweiterung des Funktionsumfangs der Templates registriert `ReportBuilderUtils` beim Aufbau der `TemplateEngine` drei SVWS-eigene Expression-Dialekte aus dem Paket `module.reporting.html.dialects`. Jeder Dialekt stellt ein Expression-Objekt bereit, das im Template über `#<name>` aufgerufen wird:

- **`ConvertExpressionDialect`** (`#convert`) — Konvertierungs- und Encoding-Helfer aus `ConvertExpressionHelper`: Datums-Formatierung (`toDateDE`, `toDateDELong`, `toWochentagDE`, `toKalenderwocheDE`, …), Checkbox-/Barcode-/QR-Code-SVGs (`toCheckboxSVG`, `toBarcodeCode128AsSvgHtmlImageSource`, `to2DCodeQRCodeAsSvgHtmlImageSource`) sowie GZip-Kompression und Base32/45/64-Codierung.
- **`InlineExpressionDialect`** (`#inline`) — über `InlineExpressionHelper.css(relativerCssPfad)` wird eine CSS-Datei inline in das HTML überführt. Das ist Voraussetzung für die PDF-Erzeugung und die iframe-Vorschau im WebClient (vgl. `feedback_reporting_css_inline_xml`).
- **`IconExpressionDialect`** (`#icon`) — über `IconExpressionHelper` werden Icons als SVG-Data-URI in einem `<img>`-Element erzeugt (Ausgabe per `th:utext`): `get(name)`, `get(name, groessePx)`, `get(name, groessePx, farbe)` sowie der Spezial-Helfer `getExtern(...)` für die Kennzeichnung externer Schüler inkl. optionalem Stammschul-Kürzel. Der Icon-Katalog (RemixIcon-Pfaddaten) liegt in `ReportingIcon` und wird aus der Ressource `icons/icons.json` geladen; neue Icons werden dort ergänzt. Standardgröße 14 px, Standardfarbe `black`; erzeugte Data-URIs werden gecacht.

Jeder Dialekt besteht aus drei Klassen: `…Dialect` (Registrierung + Dialekt-Name), `…Factory` (`IExpressionObjectFactory`, liefert die Expression-Namen und das Helper-Objekt) und `…Helper` (die eigentlichen, aus dem Template aufrufbaren Java-Methoden).

---

## 7. PDF-Erzeugung

### 7.1 `PdfFactory`

Pfad: `module.reporting.factories.PdfFactory`

Wird mit der Liste der bereits erzeugten `ReportBuilderHtml`-Instanzen, dem Kennzeichen `bewusstLeer` und dem `ReportingContext` initialisiert. Die Factory erzeugt aus jedem HTML-Builder einen `ReportBuilderPdf`. Die Ausgabe folgt der **Zahl der Dokumente**:

| Dokumente | Ergebnis |
|---|---|
| eines | die PDF-Datei direkt als Response, Dateiname aus dem Builder |
| mehrere | ZIP-Archiv |
| keines, `bewusstLeer` gesetzt | ZIP-Archiv **ohne** PDF-Datei |
| keines, `bewusstLeer` nicht gesetzt | `INTERNAL_SERVER_ERROR` |

**`bewusstLeer` erlaubt den Leerfall, erzwingt ihn aber nicht.** Es stammt aus dem Auswahlergebnis des Datenaufbaus und wird über `HtmlContextInitializer.bewusstLeer()` und `HtmlFactory.bewusstLeer()` durchgereicht (siehe Abschnitt 9.2). Ohne dieses Kennzeichen wäre eine leere Builder-Liste nicht von einem Ausfall der Dokumenterzeugung zu unterscheiden, und ein Serverfehler ginge als leere Ausgabe durch. Umgekehrt ist es bei einer **Sammelausgabe** über eine leere Auswahl ebenfalls gesetzt — dort entsteht aber genau ein Builder mit leerem fachlichem Inhalt, und die Ausgabe bleibt ein Dokument. Deshalb entscheidet zuerst die Zahl der Builder und erst danach das Kennzeichen.

**Der Dateiname des ZIP-Archivs stammt aus der Reportvorlage**, nicht aus dem ersten Builder. Beide liefern denselben Wert — der statische Dateiname wird von der Vorlage über die Builder-Kette nur weitergereicht —, aber ohne ein erzeugtes Dokument gäbe es diesen Weg nicht. Der Name einer fachlich leeren Ausgabe darf nicht davon abhängen, dass wenigstens ein Dokument entstanden ist.

Die Factory gibt `bewusstLeer()` nach außen, weil der **E-Mail-Pfad** dieselbe Angabe anders auswertet: Dort wird kein Job eingereiht (Abschnitt 8.1).

Beide Ausgabewege — einzelne PDF-Datei und ZIP-Archiv — ergänzen ihre erfolgreiche Antwort über `ReportingHinweiseHeader` um den öffentlichen Hinweis-Header, sofern der Datenaufbau an die Diagnose angebunden ist (Abschnitt 9.3).

### 7.2 Builder und Renderer für PDF

- **`ReportBuilderContextPdf`** — Builder-Pattern-Container für die PDF-Erzeugung (Eingabe-HTML, Metadaten, Schriftarten).
- **`ReportBuilderPdf`** — kapselt einen einzelnen PDF-Reportlauf.
- **`ReportRendererPdf`** — verwendet OpenHtmlToPdf, um aus dem HTML inkl. CSS, eingebetteten Fonts und Metadaten ein PDF-Byte-Array zu erzeugen.

---

## 8. E-Mail-Versand

### 8.1 `EmailFactory`

Pfad: `module.reporting.factories.EmailFactory`

Aufgaben:

1. Liest aus dem `ReportingParameter` den E-Mail-Empfängertyp (`ReportingEMailEmpfaengerTyp`) und die Liste der Empfänger-IDs aus.
2. Ermittelt über die Domänen-Repositories (`repositorySchueler()`, `repositoryLehrer()`, …) die zugeordneten Personen und deren E-Mail-Adressen — ggf. inkl. Erzieher-Adressen.
3. Filtert Adressen aus geblockten Domains heraus (Blacklist `BLOCKED_EMAIL_DOMAINS`, z. B. die Anonymisierungs-Domains `smail.de` und `lmail.de`).
4. Erzeugt einen `EmailJob` mit den PDF-Builds als `EmailJobAttachment` und reiht ihn über den `EmailJobManager` als asynchronen Hintergrundjob ein. Die Response enthält die Job-ID (HTTP 202 Accepted), über die Status und Log später abgefragt werden können.

Der eigentliche Versand findet in einem Hintergrund-Worker statt — die API-Antwort kommt zurück, sobald der Job eingereiht ist.

**Bei einer bewusst leeren Auswahl wird kein Job eingereiht.** Ist `pdfFactory.bewusstLeer()` gesetzt, endet der Versand direkt nach der Parameterprüfung mit einer Startantwort ohne Job-ID (HTTP 200, `success = true`), deren Log beschreibt, dass nichts zu versenden war. Ein eingereihter Job ohne Anhänge sähe erfolgreich aus, ohne etwas zu versenden: Der Anwender erhielte eine Job-ID, verfolgte deren Status und erfährt nie, dass nichts unterwegs war. Ein Fehlerstatus wäre dort ebenso falsch — eine leere Auswahl ist eine gewollte Auswahlentscheidung. Die Prüfung steht **nach** der Parameterprüfung, damit eine fehlerhafte Anfrage weiterhin ihren `BAD_REQUEST` erhält, und **vor** dem Aufbau von SMTP-Sitzung und Absenderadresse, die für einen ausbleibenden Versand nicht benötigt werden.

---

## 9. Querschnittliches

### 9.1 Logging

Das Reporting-Modul nutzt durchgängig das Logger-Framework aus `core.logger`:

- **`Logger`** — Multiplexer mit Log-Levels (`DEBUG`, `INFO`, `WARNING`, `ERROR`).
- **`LogConsumerList`** — Consumer, der Log-Einträge in einer Liste sammelt; wird im `ReportingContext` registriert und zusammen mit einer `ApiOperationException` an den Aufrufer zurückgegeben.

Beide werden im `ReportingContext` initialisiert — wenn der API-Aufrufer keinen Logger übergibt, erzeugt der Kontext einen neuen. Alle Schichten unterhalb davon greifen ausschließlich über `reportingContext.logger()` und `reportingContext.log()` auf das Logging zu.

### 9.2 Diagnose (`diagnose/`)

Paket `module.reporting.diagnose`. Es bündelt die Typen, die beschreiben, **warum** Daten in einer Ausgabe fehlen, und was daraus folgt:

- **`ReportingLadezustand<T>`** — das Ergebnis eines Datenzugriffs: `Geladen(wert)`, `NichtVorhanden` oder `Fehlgeschlagen(ursache, exception)`. Eine leere Collection ist ein geladener Wert; `Geladen(null)` ist unzulässig. Der Zustand kennt weder fachlichen Schlüssel noch HTTP-Status.
- **`ReportingAuswahlergebnis<T>`** — die Auswahl der Hauptdaten mit angeforderten, ausgewählten, ausgelassenen und — davon getrennt — vom Benutzerfilter ausgefilterten IDs sowie `bewusstLeer()`. Unveränderlich; Einschränkungen entstehen über `nurMitGeladenen(...)`.
- **`ReportingProblemursache`, `ReportingProblemauswirkung`, `ReportingProblemSchluessel`, `ReportingProblem`** — der interne Befund eines hingenommenen Ausgabeproblems. Der Schlüssel führt Objektart und ID und bildet eine Proxy-Klasse auf ihre Basisklasse zurück.
- **`ReportingProblemSammler`** — sammelt die Befunde eines Aufrufs, dedupliziert nach Ursache, Auswirkung und Schlüssel und protokolliert ein neues Problem einmalig. Den Block aus Fehlertyp, Ursachenkette und Stacktrace schreibt er je Fehler-Instanz nur einmal ins Log (Vergleich über Objektidentität); jeder weitere Befund derselben Instanz erhält seine Meldung mit einem Verweis auf den ersten Eintrag. Gemeldet wird nicht direkt, sondern über die Fassade `ReportingContext.meldeAusgabeproblem(…)`.
- **`ReportingHinweisKategorie`** — der kleine öffentliche Kategorienkatalog: `DATENSAETZE_FEHLEN`, `ANGABEN_FEHLEN`, `WERT_NICHT_DARSTELLBAR`, jeweils mit ihrem Header-Schlüssel. `fuer(problem)` ist die **einzige** Stelle der Projektion vom internen Befund auf die öffentliche Kategorie; sie folgt der Auswirkung, mit dem nicht darstellbaren Wert als eigener Kategorie. Die Zuordnung ist vollständig — eine nicht zugeordnete Kombination müsste im Diagnosepfad behandelt werden.
- **`ReportingHinweisSerializer`** — bildet den Wert des Response-Headers `SVWS-Reporting-Hinweise` nach RFC 9651, derzeit mit `VERTRAGSVERSION = 0`. Gezählt werden die deduplizierten internen Ausgabeprobleme; die Kategorienzahlen zerlegen dieselbe Menge, ihre Summe ergibt stets `gesamt`. Kategorien ohne Befund fehlen im Wert. Nach außen gelangen nur Kategorie und Anzahl — keine IDs, Namen, Freitexte oder Stacktraces. **Ob** eine Antwort den Header trägt, entscheidet der Aufrufer anhand der Diagnoseabdeckung und nicht dieser Serializer.

**Die Typen liegen bewusst in einem gemeinsamen Paket.** Ladezustand, Auswahl und Ausgabeproblem sind Stationen desselben Vorgangs: Der Zugriff stellt fest, was fehlt, die Auswahl entscheidet, was in die Ausgabe gelangt, und das Problem beschreibt, was davon zu melden ist. Getrennte Pakete erzwängen für diesen Fluss öffentliche Sichtbarkeit, wo package-privat genügt.

**Eine Auswahlentscheidung ist kein Ausgabeproblem.** Ein vom Benutzerfilter ausgeschlossener Datensatz fehlt, weil der Anwender es so wollte: Er läuft nicht über die Meldefassade, erhöht keinen Zähler und erzeugt keine `WARNING`.

### 9.3 Der öffentliche Hinweisvertrag

Eine erfolgreiche HTML-, PDF- oder ZIP-Antwort eines an die Diagnose angebundenen Datenaufbaus trägt den Response-Header `SVWS-Reporting-Hinweise`. Er meldet, ob die Ausgabe vollständig ist, ohne den Download zu behindern: Wer ihn nicht kennt, arbeitet unverändert weiter.

**Dieser Abschnitt ist die maßgebliche Spezifikation des Vertrags.** Ein Verbraucher — der Webclient oder ein anderer API-Nutzer — braucht keine weitere Quelle.

#### Der Headerwert

Der Wert ist ein Dictionary nach RFC 9651. Ein Boolean wird dort als `?0` oder `?1` geschrieben, nicht als `true` oder `false`:

```http
SVWS-Reporting-Hinweise: v=0, gesamt=5, leer=?0, datensaetze=3, angaben=2
```

| Feld | Bedeutung |
|-----|-------|
| `v` | Vertragsversion. `0` ist die ausdrücklich vorläufige Fassung |
| `gesamt` | Zahl der deduplizierten Hinweise. Immer vorhanden, auch als `gesamt=0` |
| `leer` | `?1`, wenn Datensätze angefordert waren und nach der Auswahl keiner übrig blieb |
| `datensaetze` | angeforderte Datensätze erscheinen nicht in der Ausgabe |
| `angaben` | die Datensätze erscheinen, ihnen fehlen einzelne Angaben |
| `darstellung` | ein vorhandener Wert ließ sich nicht ausgeben, etwa eine Signatur |

Für einen Parser gilt: **Jedes Feld kommt höchstens einmal vor.** `v`, `gesamt` und `leer` sind stets vorhanden — `v` als ganze Zahl, derzeit `0` oder `1`, `gesamt` als nicht negative ganze Zahl, `leer` als Boolean. Die drei Kategorien sind optional, ebenfalls nicht negative ganze Zahlen, und erscheinen nur mit einem Wert größer als null. Sie zerlegen dieselbe Menge, ihre Summe ergibt stets `gesamt`. Die Reihenfolge der Felder ist fest, damit derselbe Sachverhalt denselben Wert ergibt.

#### Regeln für Verbraucher

- **Ein fehlender Header bedeutet „unbekannt“**, niemals „nachweislich vollständig“. Der Download läuft ohne Meldung weiter.
- **Eine unbekannte Version oder ein syntaktisch ungültiger Header wird ignoriert.** Der Download darf daran nicht scheitern.
- **`gesamt` ist eine Diagnosegröße, keine Mengenangabe.** Die Zahl der Hinweise ist weder die Zahl fehlender Datensätze noch die fehlender Dokumente. Oberflächentexte sprechen deshalb allgemein von Hinweisen auf Unvollständigkeit und behaupten keine bestimmte Zahl ausgelassener Dateien.
- **`gesamt=0, leer=?1` ist gültig** und bezeichnet den reinen Filterfall: Der Benutzerfilter hat alle Datensätze ausgeschlossen, ohne dass etwas fehlt oder fehlschlug. Dieser Fall verdient eine neutrale Information ohne Fehler- oder Unvollständigkeitsbehauptung. Bei `gesamt=0, leer=?0` gibt es nichts zu melden.
- **Unbekannte Einträge werden ignoriert**, nicht als Fehler behandelt. Der Katalog darf additiv wachsen.
- **Bei `v=0` wertet ein Verbraucher allein `gesamt` und `leer` aus** und ignoriert die Kategorien. Nur so bindet er sich nicht an einen Katalog, der sich noch ändern darf.
- **Nach außen gelangen ausschließlich Kategorie und Anzahl.** Weder IDs, Namen und Freitexte noch Fehlermeldungen oder Stacktraces verlassen den Server.
- **Ab `v=1` sind Kategorien nur noch additiv.** Eine Kategorie zu entfernen, umzubenennen oder anders zu deuten erfordert eine neue Vertragsversion. Die Schlüssel sind Vertrag, die Namen der Enum-Konstanten sind es nicht.

| Baustein | Rolle |
|-----|-------|
| `ReportingHinweisKategorie` | der kleine öffentliche Kategorienkatalog samt Projektion vom internen Befund (Abschnitt 9.2) |
| `ReportingHinweisSerializer` | bildet den Headerwert nach RFC 9651 und den Text der Hinweisdatei; `HEADER_NAME`, `VERTRAGSVERSION` und `DATEINAME_HINWEISE` stehen dort |
| `ReportingHinweiseHeader` (Paket `factories`, paketprivat) | **einzige** Stelle, die über das Setzen entscheidet; von HTML-, PDF- und ZIP-Ausgabe aufgerufen |
| CORS-Freigabe (`Access-Control-Expose-Headers`) | folgt erst mit dem Client-Vorhaben, das den ersten Verbraucher bringt. Bis dahin liest ein Browser den Header nur unter demselben Ursprung; im getrennten Entwicklungsaufbau bleibt er ungelesen |
| `APIReporting` | beschreibt den Header in der OpenAPI-Dokumentation der Endpunkte `/html` und `/ausgabe` |

**Der Header fehlt, solange der Datenaufbau nicht angebunden ist.** Gesetzt wird er nur, wenn die Registry den Datenaufbau als angebunden führt — `HtmlContextInitializerRegistry.istAnHinweisvertragAngebunden(...)`, siehe Abschnitt 6.2. Ein Header mit `gesamt=0` an einem Pfad, dessen Datenzugriffe noch nicht melden, bescheinigte eine geprüfte Vollständigkeit, die niemand festgestellt hat; ein fehlender Header bedeutet dagegen „Abdeckung unbekannt" und niemals „nachweislich vollständig". Diese Unterscheidung ist ein Migrationsinstrument und entfällt, sobald alle vorgesehenen Datenaufbauten angebunden sind.

**`v=0` ist eine ausdrücklich vorläufige Fassung.** Der strukturelle Kern — Name, `gesamt`, `leer`, Datenschutzregeln, Verhalten bei unbekannten Einträgen — ist stabil; der Kategorienkatalog darf sich bis `v=1` noch ändern. Ein Verbraucher wertet bei `v=0` deshalb nur `gesamt` und `leer` aus.

**Eine ZIP-Ausgabe erhält zusätzlich die Beilage `HINWEISE.txt`**, sobald es etwas zu erklären gibt — bei mindestens einem Hinweis oder bei einer Auswahl, aus der kein Datensatz übrig blieb. Sie ist der **einzige Weg, auf dem die Hinweise den Anwender ohne Clientanpassung erreichen**: Den Response-Header wertet der heutige generierte Client nicht aus, eine Beilage im Archiv sieht dagegen jeder, der es öffnet. Besonders ein Archiv ohne PDF-Datei braucht sie, sonst stünde der Anwender vor einem leeren Download. Der Text nennt Anzahlen und lesbare Kategorien, aber weder IDs noch Namen, Fehlermeldungen oder Stacktraces; der reine Filter-Leerfall bleibt ohne Fehlerbehauptung. **Anders als der Header hängt die Datei nicht an der Diagnoseabdeckung**: Sie erklärt, was bekannt ist, und behauptet keine Vollständigkeit.

**E-Mail trägt den Header nicht.** Der Versand antwortet mit einer JSON-Startantwort und nicht mit einem Dokument; ob und wo Hinweise dort erscheinen, ist eine eigene fachliche Entscheidung.

### 9.4 Utilities

Paket `module.reporting.utils`:

- **`ReportingExceptionUtils`** — protokolliert Fehlerblöcke einheitlich: `logException(...)` schreibt Beschreibung, Fehlertyp, Ursachenkette und Stacktrace auf dem übergebenen Log-Level, und `getLogAsSimpleOperationResponse(...)` überführt das gesammelte Log in eine `SimpleOperationResponse` für die Fehlerantwort der API. Exceptions erzeugt die Klasse nicht.
- Weitere `Reporting*`-Utility-Klassen für Datums-, String- und Format-Helfer.

Paket-privater Helper im `repositories`-Paket:

- **`ReportingRepositoryUtils`** (`module.reporting.repositories.ReportingRepositoryUtils`) — bündelt generisches Listen-Erzeugen und Bulk-Nachladen in einer Klasse. Wird ausschließlich von den Domänen-Repositories verwendet und ist deshalb package-private. Statische Methoden:
  - `waehleAus(ids, mapStammdaten, mapReportingObjekte, stammdatenLoader, reportingObjektErsteller, idExtractor, comparator, filter, datentyp, logger, ladefehler)` — kombiniert ID-Bereinigung (`null`/Duplikate raus), Bulk-Load fehlender Stammdaten, Cache-Eintrag der Reporting-Objekte (`putIfAbsent` gegen zirkuläre Abhängigkeiten), Filterung (`Predicate<R>`; `null` schaltet Filter aus) und **danach** Sortierung (`Comparator<R>`; ein Identitäts-Comparator lässt die Reihenfolge unverändert). Stammdaten- und Reporting-Maps werden vollständig befüllt — Filter und Sortierung wirken nur auf die Auswahl. Die Reihenfolge ist verbindlich: Die Comparatoren greifen auf nachladende Getter zu, sodass eine Sortierung vor dem Filtern auch für ausgeschlossene Datensätze Datenzugriffe, Logeinträge und Fehler auslöste. Wird in den List-Repositories (`Lehrer`, `Schueler`, `Lerngruppen`) als zentrale Methode für die `xxx(List<Long>, boolean)`-Lookups eingesetzt.
    Rückgabe ist ein `ReportingAuswahlergebnis<R>`: Es führt neben den Objekten die **ausgelassenen** IDs samt Ladezustand und, davon getrennt, die vom Benutzerfilter **ausgefilterten**. Nur so ist später unterscheidbar, ob ein Datensatz fehlt, weil er nicht vorhanden oder nicht ladbar ist (Ausgabeproblem, wird gemeldet), oder weil der Anwender ihn ausgeschlossen hat (Auswahlentscheidung, wird nicht gemeldet). Eine negative ID gilt dabei als unbekannt und wird ausgelassen.
    `ladefehler` ist die Fehler-Map des jeweiligen Repositories und lebt so lange wie dessen Cache: Der Fehler-Marker `put(id, null)` verhindert jeden weiteren Ladeversuch, sodass ein späterer Zugriff die Exception nicht erneut erzeugen könnte.
  - `ladeFehlendeWerteInRepositoryMap(ids, repositoryMap, bulkLoader, datenbezeichnung, logger)` — für `Map<Long, T>`-Caches; trägt im Fehlerfall (nach Bulk- und pro-ID-Fallback) `null` ein, damit erneute Anfragen für dieselbe ID nicht in Endlosschleifen laufen.
  - `ladeFehlendeWerteInRepositoryMap(…, logger, fehlerJeId)` — dieselbe Ladung mit Rückkanal: Der Fehler des endgültig gescheiterten Einzelzugriffs wird je ID festgehalten **statt** protokolliert. Er entsteht dann bei der Meldung des Ausgabeproblems, gemeinsam dedupliziert und auf dem dafür vorgesehenen Level. Der Fehler des **gesammelten** Zugriffs bleibt unabhängig davon im Log — er ist kein Ausgabeproblem, solange die Einzelzugriffe gelingen, und ist damit die Spur, wenn niemand den Einzelfehler bewertet. Die Map gehört dem Repository und lebt so lange wie dessen Cache.
  - `ladeFehlendeListenInRepositoryMap(ids, repositoryMap, bulkLoader, datenbezeichnung, logger)` — für `Map<Long, List<T>>`-Caches; trägt im Fehlerfall eine leere Liste ein, sodass Konsumenten nie `null` sehen.
  - `ladeFehlendeListenInRepositoryMap(…, logger, fehlerJeId)` — dieselbe Ladung mit Rückkanal. Bei Listen ist er die **einzige** Spur des Fehlers: Der Cache enthält danach die leere Liste und ist damit nicht mehr von einer ID zu unterscheiden, zu der es fachlich keine Daten gibt. Wer daraus ein Ausgabeproblem meldet, erkennt den Fehlschlag an dieser Map und nicht am Cache-Eintrag. Der Logeintrag des Einzelfehlers unterbleibt dann; der Fehler des gesammelten Zugriffs bleibt im Log.
  - `zustaendeAus(ids, repositoryMap, fehlerJeId)` — leitet aus einer Repository-Map den `ReportingLadezustand` je ID ab: Wert vorhanden → geladen, Eintrag ohne Wert → gescheiterter Ladevorgang samt festgehaltener Exception, kein Eintrag → nicht vorhanden. Die Map allein trennt diese Fälle nicht, und wer sie gleich behandelt, macht aus einer Störung eine fachlich fehlende Akte. Wird dort gebraucht, wo eine Prüfung über zusätzlich benötigte Daten entscheidet (`ReportingRepositoryGost.zustaende…`).
  - `meldeFehlgeschlageneAuslassungen(reportingContext, auswahl, objektart, bezeichnung)` — meldet je ausgelassener ID einer Auswahl, deren Laden gescheitert ist, ein Ausgabeproblem. Die `xxx(List<Long>)`-Listen-Zugriffe der Repositories geben nur die Objekte heraus und rufen diese Meldung selbst auf: Ohne sie bliebe ein endgültig gescheiterter Einzelzugriff völlig still, weil der Rückkanal den Logeintrag des Ladefehlers unterdrückt. Nicht vorhandene und vom Benutzerfilter ausgefilterte IDs werden nicht gemeldet.

  Alle Lademethoden folgen demselben Fallback-Muster: zunächst Bulk-Load aller fehlenden IDs; schlägt der gesammelte Aufruf fehl, wird jede ID einzeln nachgeladen, um fehlerhafte Datensätze zu isolieren. Da `ApiOperationException` eine `RuntimeException` ist, propagieren Caller-Lambdas Datenbank-Fehler unverändert in den Helper; ein eigenes try/catch in der Lambda ist nicht nötig und würde den pro-ID-Fallback unterbinden.

---

## 10. Verweis auf weitere Dokumentation

- **[`reporting-konventionen.md`](reporting-konventionen.md)** — die verbindlichen Regeln und Invarianten des Moduls (Schichtentrennung, Null-Sicherheit der Typen, Filter-/Sortier-Regeln, Fehlercode-Matrix, OGNL-Grenzen, CSS-/Stil-Regeln). **Normative Referenz** — vor jeder Änderung am Modul lesen; bei Konflikt mit dieser Beschreibung gilt die Konventionen-Datei.
- **[`reporting-template-erstellung.md`](reporting-template-erstellung.md)** — Schritt-für-Schritt-Anleitung zum Erstellen von Reportvorlagen (HTML + Thymeleaf). Richtet sich an Vorlagen-Autoren (auch ohne tiefe Java-Kenntnisse) und ist bewusst eigenständig lesbar, ohne dieses Architektur-Dokument vorauszusetzen.

> Die frühere Kurzreferenz „Abschnitt 11 — Invarianten & Konventionen“ ist vollständig in
> `reporting-konventionen.md` aufgegangen und dort um die Fehlercode-Matrix, die
> Null-Sicherheits-Konstruktor-Regeln und die OGNL-Grenzen erweitert worden.
