# Anleitung: Sortierung und Filterung einbinden

**Zielgruppe:** Entwicklung des Reporting-Moduls
**Verwandte Dokumente:** Der Aufbau ist in
[`reporting-architektur.md`](reporting-architektur.md) beschrieben, die verbindlichen Regeln stehen
in [`reporting-konventionen.md`](reporting-konventionen.md).

Sortierung und Filterung sind je Reporting-Typ typsicher konfiguriert. Diese Anleitung beschreibt
die Bausteine im Einzelnen und zeigt, wie ein neuer Reporting-Typ eingebunden wird. Wer nur wissen
will, wie das Zusammenspiel gedacht ist, findet das kürzer in der Architektur-Doku.

## 1. `ReportingSortierung<T>` und Begleit-Datei `Reporting<Typ>Sortierung`

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

## 2. `SortierungRegistry<T>`

Pfad: `module.reporting.sortierung.SortierungRegistry`

Generische Registry, die eine Map aus normierten Attributnamen auf typsichere Extraktor-Funktionen verwaltet und Builder-Helfer bereitstellt:

- `registiereString(name, extractor)` / `registiereString(SerializableFunction)` — Attribute mit String-Wert (locale-aware deutsche Sortierung).
- `registiereComparable(name, extractor)` / `registiereComparable(SerializableFunction)` — Attribute mit numerischem/Date-/Enum-Wert.
- `importiereRegistryEintraege(prefix, sub-registry, navigator)` — übernimmt Einträge einer fremden Registry mit Pfad-Präfix (z. B. `auswahlLernabschnitt.klasse.kuerzel`). Genutzt, um die Attribute eines verschachtelten Reporting-Typs unter einem Prefix in die aktuelle Registry zu spiegeln.

Methodennamen werden über `ReportingTypesUtils.methodeToString(SerializableFunction)` typsicher aus `SerializedLambda` extrahiert — das vermeidet Magic-Strings in der Standardsortierung und in Import-Prefixen.

## 3. `ReportingSortierungService`

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

## 4. `ComparatorFactory`

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

## 5. `ReportingFilterung<T>` und Begleit-Datei `Reporting<Typ>Filter`

Pfad: `module.reporting.filterung.ReportingFilterung`

Bündelt für einen Reporting-Typ `T` dessen `FilterRegistry<T>` in einer typsicheren Konfiguration. Wird über einen Builder erzeugt (`ReportingFilterung.<T>builder().registry(...).build()`) und stellt bereit:

- `registry()` — die zugrundeliegende `FilterRegistry<T>`.
- `bedingung(ReportingFilterDefinitionGruppe gruppe, List<String> validierungsfehler)` — baut aus einer Filtergruppe ein `Predicate<T>`:
  - Ist die Gruppe `null` oder leer, liefert die Methode ein Pass-Through `t -> true`.
  - Enthält die Gruppe genau eine `ReportingFilterDefinition`, wird sie 1:1 verwendet.
  - Bei mehreren Definitionen werden sie gemäß `gruppe.uiFilterMultiselectVerknuepfung` (`AND`/`OR`) kombiniert.

Die Begleit-Datei `Reporting<Typ>Filter.java` (z. B. `ReportingSchuelerFilter`, `ReportingKursFilter`, `ReportingGostKursplanungKursFilter`) liegt direkt neben dem Reporting-Typ und enthält:

- eine private `buildRegistry()`-Methode mit allen `registriereAttribut(...)`-Aufrufen,
- eine `public static final ReportingFilterung<T> FILTER`-Konstante,
- einen privaten Konstruktor zur Markierung als nicht-instanziierbar.

Der Reporting-Typ exponiert die Konstante weiter, z. B.:

```java
public static final ReportingFilterung<ReportingSchueler> FILTER = ReportingSchuelerFilter.FILTER;
```

## 6. `FilterRegistry<T>`

Pfad: `module.reporting.filterung.FilterRegistry`

Generische Auswertungs-Engine:

- `registriereAttribut(name, Function<T, ?>)` / `registriereAttribut(SerializableFunction)` — registriert ein filterbares Attribut (case-insensitiv gespeichert).
- `erstelleFilter(ReportingFilterDefinition, validierungsfehler)` — Hauptmethode, baut aus einer Definition ein `Predicate<T>`. Eine Definition besteht aus *Kriterien*; ein Kriterium aus *Einträgen* (Attribut+Operation+Werte) und optionalen *Unterkriterien*. Innerhalb eines Kriteriums werden Einträge gemäß `verknuepfung` (`AND`/`OR`) verknüpft, optional negiert. Unterkriterien werden rekursiv ausgewertet.
- Unterstützte Operationen (siehe `ReportingFilterOperation`): `EQUAL`, `NOT_EQUAL`, `CONTAINS`, `STARTS_WITH`, `ENDS_WITH`, `GREATER`, `GREATER_OR_EQUAL`, `LESS`, `LESS_OR_EQUAL`, `IN`, `BETWEEN`.
- Typ-Anpassung: Der String-Wert aus dem Filter wird abhängig vom Laufzeit-Typ des Attributwerts konvertiert (`String`, `Number`, `Boolean`, `LocalDate`, `LocalDateTime`, generische `Comparable`-Typen). String-Vergleiche sind case-insensitiv.
- Unbekannte Attribute werden ignoriert (Predicate liefert `true`); der Name wird in `validierungsfehler` gesammelt und kann später z. B. ins Log ausgegeben werden.

## 7. `ReportingFilterService`

Pfad: `module.reporting.filterung.ReportingFilterService`

Zustandsloser Service mit zwei Methoden:

```java
ReportingFilterDefinitionGruppe getFilter(String typ)
boolean                         hatFilter(String typ)
```

- `getFilter(typ)` liefert den Filter in Form einer `ReportingFilterDefinitionGruppe` aus den `ReportingParameter.filterDefinitionenGruppen` für den angegebenen Reporting-Typ — oder `null`, wenn keine Gruppe vorhanden ist. Das Ergebnis wird direkt an `Typ.FILTER.bedingung(gruppe, validierungsfehler)` weitergegeben, um daraus das `Predicate<T>` zu bauen.
- `hatFilter(typ)` liefert `true`, sobald für den Typ mindestens eine Filterdefinition mit mindestens einem Kriterium existiert. Wird typischerweise im Reporting-Datenaufbau benötigt, um zu entscheiden, ob ein gefilterter Sub-Datensatz separat aufgebaut werden muss (z. B. „nur die selektierten Kurse" zusätzlich zu „alle Kurse" im selben Report).

## 8. Definitions-Klassen und Factories (Paket `core.data.reporting` / `core.utils.reporting`)

Die *Definitions*-Datenklassen werden im Core-Modul gepflegt, damit der WebClient sie über die transpiliert verfügbar hat:

- `ReportingSortierungDefinitionGruppe` — Gruppe pro Reporting-Typ, enthält `typ`, `bezeichnung`, Liste der ausgewählten `sortierungDefinitionen` und der zur Auswahl stehenden `sortierungDefinitionenOptionen`.
- `ReportingSortierungDefinition` — eine einzelne Sortierung mit `attribute: List<String>` und Flag `verwendeStandardsortierung`.
- `ReportingFilterDefinitionGruppe` — analog für Filter; zusätzlich `uiIstFilterMultiselect` und `uiFilterMultiselectVerknuepfung` (`AND`/`OR`).
- `ReportingFilterDefinition` — eine einzelne Definition mit Liste von `ReportingFilterKriterium`.
- `ReportingFilterKriterium` — Baustein mit `eintraege` (Attribut + Operation + Werte), `unterkriterien`, `verknuepfung` und Negations-Flag.

Zum bequemen programmatischen Erzeugen dieser Strukturen — vor allem im Client, der über transpilierte Versionen verfügt — stehen Factory-Klassen bereit:

- `ReportingSortierungDefinitionFactory`
- `ReportingFilterDefinitionFactory` (mit statischen Helfern wie `definition(...)`, `and(...)`, `or(...)`, `eq(...)`, `in(attribut, werte...)`)
- `ReportingFilterDefinitionGruppeFactory` mit zwei Convenience-Methoden:
  - `gruppe(bezeichnung, typ, uiIstSichtbar, definitionen...)` — Standardfall.
  - `gruppeAusIds(bezeichnung, typ, uiIstSichtbar, List<Long> idsListe)` — Kurzform für eine ID-basierte Auswahl: erzeugt automatisch eine Definition mit einem `IN`-Filter auf das Attribut `"id"`.

## 9. Typischer Einsatz

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
