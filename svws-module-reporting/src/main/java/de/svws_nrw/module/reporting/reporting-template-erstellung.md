# Reporting – Report-Vorlagen (HTML-Templates) erstellen

Diese Anleitung zeigt **Schritt für Schritt**, wie man im SVWS-Server eine neue Report-Vorlage (z. B. eine Schülerliste, eine Bescheinigung, einen Stundenplan) erstellt.

**Für wen ist diese Anleitung?** Für alle, die eine neue Druck-/PDF-Vorlage bauen wollen. Du brauchst **gute HTML-Kenntnisse** sowie **Kenntnisse in [Thymeleaf](https://www.thymeleaf.org)** (die Template-Sprache, mit der die Platzhalter gefüllt werden) und nur **Grundkenntnisse in Java** – die wenigen Java-Stellen sind hier komplett vorgegeben und müssen nur nach Muster kopiert werden.

> **Wichtig zu Thymeleaf:** Im Reporting wird Thymeleaf **ohne Spring** eingesetzt. Es stehen daher **nur die Basis-Funktionen** von Thymeleaf zur Verfügung – Spring-spezifische Erweiterungen (z. B. `th:field`, Spring-EL-Features, Formular-Bindung) funktionieren hier **nicht**. Wer Thymeleaf aus Spring-Projekten kennt, sollte das im Hinterkopf behalten.

> **Thymeleaf-Dokumentation:** Eine Einführung und die vollständige Referenz der Basis-Funktionen findest du unter [thymeleaf.org/documentation.html](https://www.thymeleaf.org/documentation.html) (insbesondere „Using Thymeleaf"). Diese Anleitung setzt deren Grundlagen voraus und erklärt nur die SVWS-Besonderheiten.

> **Schwester-Dokument:** Wie das Reporting-Modul *intern* aufgebaut ist (Schichten, Repositories, Datenfluss), steht in [`reporting-architektur.md`](reporting-architektur.md). Für das Erstellen von Vorlagen musst du das **nicht** lesen – diese Anleitung reicht.

---

## Inhalt

1. [Wie ein Report entsteht – das Grundprinzip](#1-wie-ein-report-entsteht--das-grundprinzip)
2. [Wichtige Begriffe (Glossar)](#2-wichtige-begriffe-glossar)
3. [Schnellstart: eine bestehende Vorlage kopieren](#3-schnellstart-eine-bestehende-vorlage-kopieren)
4. [Schritt 1 – Die Vorlage registrieren (Java)](#4-schritt-1--die-vorlage-registrieren-java)
5. [Schritt 2 – Die Dateien anlegen](#5-schritt-2--die-dateien-anlegen)
6. [Schritt 3 – Das HTML-Grundgerüst](#6-schritt-3--das-html-grundgerüst)
7. [Schritt 4 – Daten in die Vorlage einsetzen](#7-schritt-4--daten-in-die-vorlage-einsetzen)
8. [Schritt 5 – Optionen des Nutzers (VorlageParameter)](#8-schritt-5--optionen-des-nutzers-vorlageparameter)
9. [Schritt 6 – Datum, QR-Codes & Co. (Dialekt #convert)](#9-schritt-6--datum-qr-codes--co-dialekt-convert)
10. [Schritt 7 – Layout & CSS](#10-schritt-7--layout--css)
11. [Schritt 8 – Der Dateiname der Ausgabe (.name.tpl)](#11-schritt-8--der-dateiname-der-ausgabe-nametpl)
12. [Schritt 9 – Testen](#12-schritt-9--testen)
13. [Referenz: Verfügbare Daten-Contexts](#13-referenz-verfügbare-daten-contexts)
14. [Referenz: Der Dialekt #convert](#14-referenz-der-dialekt-convert)
15. [Goldene Regeln & häufige Fehler](#15-goldene-regeln--häufige-fehler)
16. [Checkliste für eine neue Vorlage](#16-checkliste-für-eine-neue-vorlage)

---

## 1. Wie ein Report entsteht – das Grundprinzip

Ein Report ist im Kern eine **HTML-Datei mit Platzhaltern**. Die Platzhalter werden zur Laufzeit mit echten Daten aus der Schuldatenbank gefüllt. Aus dem fertigen HTML wird dann – je nach Wunsch – direkt HTML angezeigt, ein **PDF** erzeugt oder eine **E-Mail** verschickt. Das PDF wird ebenfalls aus dem HTML erzeugt (mit der Bibliothek *OpenHtmlToPdf*).

Die Platzhalter-Sprache heißt **[Thymeleaf](https://www.thymeleaf.org)**. Thymeleaf-Anweisungen stehen als zusätzliche Attribute im HTML, fast immer beginnend mit `th:`. Beispiel:

```html
<!-- Statischer Text "Beispiel" wird beim Rendern durch den echten Nachnamen ersetzt -->
<td th:text="${schueler.nachname()}">Beispiel</td>
```

- Ohne Daten (z. B. Vorschau im Browser) sieht man den statischen Text `Beispiel`.
- Mit Daten ersetzt Thymeleaf ihn durch den Wert von `schueler.nachname()`.

Die Daten kommen aus sogenannten **Contexts** (Datenquellen). Welche Contexts gefüllt werden, hängt davon ab, in welchem „Bereich" deine Vorlage liegt (Schüler, Klassen, Lehrer …). Mehr dazu in [Abschnitt 13](#13-referenz-verfügbare-daten-contexts).

**Drei Dateien gehören zu einer Vorlage** (alle mit demselben Basisnamen, im selben Ordner):

| Datei | Zweck | Pflicht? |
|-------|-------|----------|
| `MeinReport.html` | Die eigentliche Vorlage | ✅ ja |
| `MeinReport.css` | Eigene Styles nur für diese Vorlage | empfohlen |
| `MeinReport.name.tpl` | Bestimmt den Dateinamen der Ausgabedatei | ✅ ja |

Dazu kommt **ein** Java-Eintrag, der die Vorlage im System bekannt macht (siehe Schritt 1).

---

## 2. Wichtige Begriffe (Glossar)

- **Report-Vorlage** – Eine konkrete Vorlage (z. B. „Klassenliste mit Kontaktdaten"). Jede Vorlage hat genau einen Eintrag in der Java-Enum `ReportingReportvorlage`.
- **Context (Datenquelle)** – Eine benannte Sammlung von Daten, die im Template zur Verfügung steht, z. B. `Schueler`, `Klassen`, `Schule`. Im Template greift man darauf mit `${...}` zu.
- **Reporting-Typ** – Ein Java-Datenobjekt wie `ReportingSchueler` oder `ReportingKlasse`. Seine Werte holst du über **Methoden mit Klammern**, z. B. `schueler.vorname()`. (Anders als bei normalen Webseiten gibt es hier keine „Felder ohne Klammern".)
- **VorlageParameter** – Optionen, die der Nutzer vor dem Druck einstellt (Checkboxen, Textfelder …), z. B. „mit Foto". Im Template abgefragt mit `VorlageParameter.get('mitFoto')`.
- **Fragment** – Ein wiederverwendbarer HTML-Baustein (z. B. der Seitenkopf). Du bindest ihn ein, statt ihn zu kopieren.
- **Dialekt** – Eine SVWS-Erweiterung von Thymeleaf mit Zusatzfunktionen. Wichtig: `#convert` (Datum, QR-Codes …), `#icon` (Symbole als Bild) und `#inline` (CSS einbetten – brauchst du nur indirekt).
- **`.name.tpl`** – Eine kleine Vorlage, die den **Dateinamen** der erzeugten Datei festlegt.

---

## 3. Schnellstart: eine bestehende Vorlage kopieren

Der schnellste Weg zu einer neuen Vorlage ist, eine **ähnliche bestehende** zu kopieren und anzupassen. Das spart das Grundgerüst und zeigt funktionierende Muster.

1. Suche unter `svws-module-reporting/src/main/resources/de/svws_nrw/module/reporting/` eine Vorlage, die deinem Ziel ähnelt (z. B. eine Liste im Ordner `schueler/listen/`).
2. Kopiere die drei Dateien (`.html`, `.css`, `.name.tpl`) und gib ihnen einen neuen Basisnamen.
3. Arbeite dann die Schritte 1–9 durch.

Die folgenden Schritte erklären jeden Bestandteil – auch, wenn du kopiert hast, solltest du sie einmal lesen.

---

## 4. Schritt 1 – Die Vorlage registrieren (Java)

Damit der SVWS-Client die Vorlage anbieten kann, muss sie in einer **Java-Enum** eingetragen werden. Das ist die einzige Java-Stelle – sie folgt einem festen Muster.

**Datei:** `svws-core/src/main/java/de/svws_nrw/core/types/reporting/ReportingReportvorlage.java`

Füge einen neuen Enum-Eintrag hinzu. Orientiere dich an einem vorhandenen Eintrag aus demselben Bereich. Ein Eintrag sieht so aus:

```java
/** Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher */
KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER("Klasse-Liste-Schueler-Kontaktdaten-Erzieher",
        "Klassenliste mit Kontaktdaten",                          // uiTitel: Titel im Client
        "Eine Liste mit den Kontaktdaten ... erzeugen.",          // uiBeschreibung: Text im Client
        ReportingReportvorlageDatenContext.KLASSEN,               // welcher Datenaufbau? -> bestimmt die Contexts
        "klassen/KlasseListeSchuelerKontaktdatenErzieher.html",   // Pfad zur HTML-Datei (relativ zum resources-Ordner)
        "Klasse-Liste-Schueler-Kontaktdaten-Erzieher",            // statischer Dateiname (Fallback ohne .name.tpl)
        List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN), // wer darf das? (mind. eine dieser Kompetenzen)
        ReportingReportvorlageKonfigurationKlassen.getKlassenVListeSchuelerKontaktdatenerzieher() // Optionen (Schritt 5)
),
```

Die Parameter im Einzelnen:

| Parameter | Bedeutung |
|-----------|-----------|
| **Enum-Name** (`KLASSEN_V_…`) | Technischer Name nach dem Schema `HAUPTDATEN_V_DETAILDATEN`. Nur Großbuchstaben, Ziffern und `_`. |
| **bezeichnung** (1. String) | Eindeutige technische Bezeichnung. **Keine Leerzeichen**, nur Buchstaben/Ziffern/`-`/`_`. |
| **uiTitel** | Wird dem Nutzer im Client als Titel angezeigt. |
| **uiBeschreibung** | Erklärtext im Client. |
| **datenContext** | **Wichtig:** Wählt den Datenaufbau aus `ReportingReportvorlageDatenContext` und entscheidet, **welche Contexts gefüllt und welche Prüfungen durchgeführt werden** (siehe [Abschnitt 13](#13-referenz-verfügbare-daten-contexts)). |
| **pfadHtmlTemplate** | Pfad zur HTML-Datei relativ zu `…/module/reporting/`. |
| **dateiname** | Statischer Dateiname (ohne Endung) als Rückfallebene, falls keine `.name.tpl` greift. |
| **benutzerKompetenzen** | Liste von `BenutzerKompetenz`. Der Nutzer braucht **mindestens eine** davon (ODER-Verknüpfung). |
| **reportingParameter / Konfiguration** | Verweist auf die Methode, die die Nutzer-Optionen (VorlageParameter) definiert – siehe Schritt 5. |

> **Mögliche Werte für `datenContext`:** `SCHUELER`, `SCHUELER_GOST_LAUFBAHNPLANUNG`, `SCHUELER_GOST_ABITUR`, `LEHRER`, `KLASSEN`, `KURSE`,
> `GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG`, `GOST_KURSPLANUNG_KURSE`, `GOST_KURSPLANUNG_SCHUELER`, `GOST_KLAUSURPLANUNG_SCHUELER`,
> `GOST_KLAUSURPLANUNG_TERMINE`, `STUNDENPLANUNG_FACH`, `STUNDENPLANUNG_KLASSEN`, `STUNDENPLANUNG_LEHRER`, `STUNDENPLANUNG_RAUM`,
> `STUNDENPLANUNG_SCHUELER`.
>
> Jeder Wert benennt genau einen Ablauf des Datenaufbaus — welche Daten geladen und welche Prüfungen dabei durchgeführt werden. Wähle den Wert,
> der zu den Daten deiner Vorlage passt; einen neuen Wert brauchst du nur, wenn keiner der bestehenden Abläufe deine Daten liefert.

---

## 5. Schritt 2 – Die Dateien anlegen

Alle Template-Dateien liegen unter:

```
svws-module-reporting/src/main/resources/de/svws_nrw/module/reporting/
```

Dort gibt es eine **thematische Ordnerstruktur**, u. a.:

```
css/          – gemeinsame CSS-Dateien (Seitenformate, globale Styles, PDF-Einstellungen)
fonts/        – die eingebetteten Schriftarten (für das PDF)
fragments/    – wiederverwendbare Bausteine (Seitenkopf/-fuß, HTML-Head)
schueler/     – Vorlagen rund um Schüler (z. B. listen/, anschreiben/, gost/)
klassen/      – Vorlagen rund um Klassen
kurse/        – Vorlagen rund um Kurse
lehrer/       – Vorlagen rund um Lehrkräfte
gost/         – Oberstufe (kursplanung/, klausurplanung/, laufbahnplanung/)
stundenplanung/ – Stundenpläne
```

Lege deine drei Dateien in den **passenden Themenordner** (oder einen neuen Unterordner). Der Pfad muss zum Eintrag aus Schritt 1 passen. Beispiel:

```
schueler/listen/MeineSchuelerliste.html
schueler/listen/MeineSchuelerliste.css
schueler/listen/MeineSchuelerliste.name.tpl
```

> **Wichtig:** HTML- und CSS-Datei müssen **denselben Basisnamen** haben und im **selben Ordner** liegen. Das System bindet die zugehörige CSS-Datei automatisch ein, indem es im HTML-Pfad `.html` durch `.css` ersetzt. Heißt deine HTML-Datei `MeineSchuelerliste.html`, wird automatisch `MeineSchuelerliste.css` gesucht.

---

## 6. Schritt 3 – Das HTML-Grundgerüst

Jede Vorlage benutzt zwei fertige **Fragmente**: eines für den `<head>` (bindet alle CSS automatisch ein) und eines für **Seitenkopf und -fuß** im Druck. Du musst dich darum also nicht selbst kümmern.

Hier ein vollständiges, kommentiertes Grundgerüst zum Kopieren:

```html
<!DOCTYPE html>
<html lang="de" xmlns:th="http://www.thymeleaf.org"
      th:with="istDruckausgabe = ${Parameter.istDruckausgabe()}">

    <!-- Diese Zeile hilft nur der IDE bei der Autovervollständigung des #convert-Dialekts: -->
    <!--/* @thymesVar id="#convert" type="de.svws_nrw.module.reporting.html.dialects.ConvertExpressionHelper" */-->

    <!-- KOPF: Bindet automatisch alle CSS-Dateien ein (gemeinsame + die eigene zur Vorlage). -->
    <head th:replace="~{fragments/reportHtmlHead :: htmlHead(
        title='Titel meiner Vorlage',
        htmlTemplatePfad=${Parameter.reportVorlage().getPfadHtmlTemplate()},
        namePageCSS='reporting-A4-hoch.css')}">
        <title></title>
    </head>

    <body>

        <!-- SEITENKOPF/-FUSS (erscheint nur im Druck/PDF auf jeder Seite). -->
        <th:block th:replace="~{fragments/reportPageHeaderFooter :: pageHeaderFooter(
            headerRight='Meine Überschrift')}">
        </th:block>

        <!-- AB HIER kommt dein eigentlicher Inhalt: -->
        <table class="fixed-100 mp-0">
            <!-- ... -->
        </table>

    </body>
</html>
```

Was die drei `htmlHead`-Parameter bedeuten:

- **`title`** – der Titel des Dokuments (`<title>`).
- **`htmlTemplatePfad`** – immer `${Parameter.reportVorlage().getPfadHtmlTemplate()}` übernehmen. Daraus wird die vorlagen­eigene CSS-Datei abgeleitet.
- **`namePageCSS`** – der Dateiname des **Seitenformats** aus dem `css/`-Ordner. Wähle eines (siehe [Abschnitt 10](#10-schritt-7--layout--css)), z. B. `reporting-A4-hoch.css` (Hochformat) oder `reporting-A4-quer.css` (Querformat). Du kannst das Format auch dynamisch wählen (siehe unten).

> **Hinweis:** Du fügst **keine** `<link rel="stylesheet">`-Zeilen mehr ein. Das Head-Fragment bettet alle CSS direkt in das HTML ein – das ist Voraussetzung für die PDF-Erzeugung und die Vorschau im Client.

---

## 7. Schritt 4 – Daten in die Vorlage einsetzen

### Einen einzelnen Wert ausgeben

```html
<td th:text="${schueler.nachname()}">Mustermann</td>
```

`th:text` ersetzt den Inhalt des Elements. Der statische Text (`Mustermann`) dient nur der Vorschau und wird beim Rendern überschrieben.

### HTML-Inhalt ausgeben (z. B. mit Zeilenumbruch) – `th:utext`

`th:text` zeigt alles als reinen Text. Wenn dein Wert **HTML** enthalten soll (etwa ein `<br/>`), nimm `th:utext`:

```html
<td th:utext="${schueler.strassennameHausnummer() + '<br/>' + schueler.plzOrt()}">
    Hauptstraße 13a<br/>12345 Musterstadt
</td>
```

> ⚠️ Bei `th:utext` muss der erzeugte Inhalt **gültiges XML** sein: schreibe `<br/>` (nicht `<br>`) und vermeide nackte `<`, `>`, `&`. Siehe [Goldene Regeln](#15-goldene-regeln--häufige-fehler).

### Über eine Liste iterieren (Schleife)

So gibst du z. B. alle Schüler nacheinander als Tabellenzeilen aus:

```html
<tr th:each="schueler, i : ${Schueler}">
    <td th:text="${i.count + '.'}">1.</td>
    <td th:text="${schueler.nachnameVorname()}">Mustermann, Max</td>
</tr>
```

- `${Schueler}` ist der Context (die Liste).
- `schueler` ist die Laufvariable (ein einzelnes `ReportingSchueler`-Objekt).
- `i` ist die Status-Variable mit nützlichen Werten: `i.count` (1-basiert), `i.index` (0-basiert), `i.first`, `i.last`, `i.even`, `i.odd`.

### Etwas nur unter einer Bedingung anzeigen

```html
<td th:if="${schueler.istVolljaehrig()}">volljährig</td>
<td th:unless="${schueler.istVolljaehrig()}">minderjährig</td>
```

### Auf Unterobjekte zugreifen – und auf `null` achten

Reporting-Objekte hängen zusammen (ein Schüler hat einen Lernabschnitt, der eine Klasse hat). Diese Unterobjekte können **`null`** sein. Es gibt hier **keine** Kurzschreibweise wie `?.` – du musst `null` selbst abfragen:

```html
<td th:text="${(schueler.auswahlLernabschnitt() != null
                && schueler.auswahlLernabschnitt().klasse() != null)
               ? schueler.auswahlLernabschnitt().klasse().kuerzel()
               : ''}">06C</td>
```

Das Muster ist immer gleich: **erst auf `!= null` prüfen, dann den Wert holen, sonst `''`**.

Bei längeren Ketten wird das unleserlich. Binde die Zwischenwerte dann einmalig mit `th:with` und
prüfe sie einzeln – das ist auch die schnellere Variante, weil jeder Wert nur einmal geholt wird:

```html
<td th:with="la=${schueler.auswahlLernabschnitt()},kl=${la != null ? la.klasse() : null}"
    th:text="${kl != null ? kl.kuerzel() : ''}">06C</td>
```

#### Was `null` sein kann – und was nicht

Nicht jeder Wert braucht eine Prüfung. Die Reporting-Typen sind so gebaut, dass du dich darauf
verlassen kannst:

| Art des Werts | `null` möglich? | Was du tust |
|---|---|---|
| Unterobjekt, Enum oder Zahl-Objekt (`klasse()`, `note()`, `fachlehrer()`) | **ja** | auf `!= null` prüfen |
| Text und Datum (`vorname()`, `geburtsdatum()`) | nein – leer ist `''` | mit `isEmpty()` prüfen, nicht mit `== null` |
| Liste oder Map (`leistungsdaten()`, `erzieher()`) | nein – höchstens leer | kein Null-Guard; bei Bedarf `isEmpty()` |

Ob ein Unterobjekt `null` sein darf, steht in der Java-Klasse am Getter im `@return`-Kommentar.

Bei indiziertem Zugriff auf eine Liste prüfe vorher die Größe – eine Liste kann leer sein, und dann
läuft der Zugriff ins Leere. Das gilt für alle Formen: `liste[0]`, `liste[i]` und `liste.get(n)`:

```html
<span th:if="${#lists.size(schuelerListe) == 1}"
      th:text="${schuelerListe[0].nachname()}">Mustermann</span>
```

> **Wichtig: keine Ausnahmen aus Erfahrungswerten.** „Jeder Schüler hat eine Klasse, da brauche ich
> keine Prüfung" ist trügerisch. Setzt der Nutzer einen Filter, blenden die Daten-Getter gefilterte
> Objekte aus und liefern `null` – auch für Verweise, die sonst immer gefüllt sind. Sichere
> Unterobjekte deshalb **immer** ab, auch wenn sie fachlich nie fehlen können.

Erfahrungsgemäß fehlen in der Praxis am häufigsten: die Kursleitung eines Kurses, die bewertende
Lehrkraft eines Unterrichts, die Fachlehrkraft einer Leistung, die Leistungsdaten zu einem Schüler
in einem bestimmten Kurs sowie Klasse und Lernabschnitt eines Schülers.

### Praktische Standard-Helfer von Thymeleaf

Diese „`#`-Objekte" sind eingebaut und oft nützlich:

- `#strings.isEmpty(text)`, `#strings.trim(text)`, `#strings.length(text)`
- `#dates.format(#aktuell.jetztAlsDate(), 'dd.MM.yyyy HH:mm')` – aktuelles Datum/Uhrzeit. Nutze
  hierfür immer `#aktuell.jetztAlsDate()`, **nicht** `#dates.createNow()` – nur der
  `#aktuell`-Dialect liefert im DEV-Modus mit `SVWS_REPORTING_FIXED_DATE=true` ein festes Datum
  und macht Snapshot-Tests damit deterministisch.
- Für SVWS-Datumswerte (z. B. Geburtsdatum) nimm aber `#convert` – siehe Schritt 6.

### Zwischenvariablen mit `th:with`

Wird ein Ausdruck oft gebraucht oder wird er kompliziert, berechne ihn einmal vorab:

```html
<body th:with="schuljahr = ${Schule.auswahlSchuljahresabschnitt() != null
                            ? Schule.auswahlSchuljahresabschnitt().schuljahr() : 0}">
    ...
    <span th:text="${schuljahr}">2025</span>
</body>
```

> **Welche Methoden hat ein Objekt?** Die verfügbaren Werte eines Reporting-Typs stehen in seiner Java-Klasse, z. B. `ReportingSchueler` unter `…/module/reporting/types/schueler/ReportingSchueler.java`. Jede öffentliche Methode (`public ... vorname()`) kannst du im Template als `schueler.vorname()` verwenden.

---

## 8. Schritt 5 – Optionen des Nutzers (VorlageParameter)

Oft soll der Nutzer eine Vorlage anpassen können („mit Foto", „mit Geburtsdatum", eine eigene Überschrift …). Solche Optionen heißen **VorlageParameter**.

### Im Template abfragen

Der Context `VorlageParameter` ist immer verfügbar. Du liest eine Option mit ihrem Namen:

```html
<!-- Praktisch: einmal oben in Variablen ablegen ... -->
<body th:with="mitFoto = ${VorlageParameter.get('mitFoto')},
               ueberschrift = ${VorlageParameter.get('ueberschrift')}">

    <!-- ... und dann nutzen: -->
    <td th:if="${mitFoto}"> ... Foto ... </td>
    <h1 th:text="${ueberschrift}">Überschrift</h1>
</body>
```

`get('name')` liefert den Wert im passenden Typ zurück (Boolean für Checkboxen, String für Textfelder, Zahl für Zahlenfelder).

### Die Optionen definieren (Java)

Welche Optionen es gibt, steht in einer **Konfigurationsdatei** im Core, eine pro Bereich:

```
svws-core/.../core/types/reporting/reportvorlagekonfiguration/
    ReportingReportvorlageKonfigurationSchueler.java
    ReportingReportvorlageKonfigurationKlassen.java
    ...
```

Dort legst du pro Vorlage eine Methode an (auf die der Enum-Eintrag aus Schritt 1 verweist). Eine Option sieht so aus:

```java
ReportingReportvorlageUtils.erzeugeVorlageParameter(
    "mitFoto",                              // Name -> so fragst du ihn im Template ab
    "mit Foto",                             // Beschriftung im Client
    ReportingReportvorlageParameterTyp.BOOLEAN, // Typ: BOOLEAN / STRING / INTEGER
    "" + false,                             // Standardwert
    true,                                   // im Client sichtbar?
    ReportingUIKomponentenTyp.CHECKBOX,     // Bedienelement: CHECKBOX / INPUT / NUMBERPICKER ...
    1)                                      // Anzeige-Breite/Spalten im Client
```

Mehrere Optionen werden zu einer **Parametergruppe** (z. B. „Inhaltsoptionen") zusammengefasst. Am einfachsten kopierst du eine vorhandene Methode aus derselben Datei und passt Namen, Typen und Standardwerte an.

> **Merke:** Der Name in `erzeugeVorlageParameter("mitFoto", …)` und der Name in `VorlageParameter.get('mitFoto')` **müssen exakt übereinstimmen**.

---

## 9. Schritt 6 – Datum, QR-Codes & Co. (Dialekt #convert)

Thymeleaf kann von Haus aus nicht alles, was Reports brauchen. Dafür gibt es den SVWS-Dialekt **`#convert`**. Du rufst seine Funktionen wie ein Objekt mit Methoden auf:

```html
<!-- Ein ISO-Datum (z. B. "2008-12-31") deutsch formatieren: -->
<td th:text="${#convert.toDateDE(schueler.geburtsdatum())}">31.12.2008</td>
```

Die wichtigsten Funktionen:

| Aufruf | Ergebnis |
|--------|----------|
| `#convert.toDateDE(iso)` | `31.12.2008` |
| `#convert.toDateDELong(iso)` | `31. Dezember 2008` |
| `#convert.toWochentagDE(iso)` | `Mittwoch` |
| `#convert.toKalenderwocheDE(iso)` | Kalenderwoche |
| `#convert.toCheckboxSVG(boolean)` | ein angekreuztes/leeres Kästchen als Bild |
| `#convert.to2DCodeQRCodeAsSvgHtmlImageSource(inhalt, breiteMM, hoeheMM)` | QR-Code als Bildquelle |
| `#convert.toBarcodeCode128AsSvgHtmlImageSource(inhalt, breiteMM, hoeheMM)` | Barcode als Bildquelle |

Die vollständige Liste steht in der Java-Klasse [`html/dialects/ConvertExpressionHelper.java`](html/dialects/ConvertExpressionHelper.java) – jede öffentliche Methode dort ist als `#convert.methodenName(...)` aufrufbar. Siehe auch [Abschnitt 14](#14-referenz-der-dialekt-convert).

**Barcodes und QR-Codes immer über `#convert` einbinden.** Die beiden Funktionen liefern **immer** eine
Bildquelle – auch dann, wenn der Inhalt leer ist oder sich nicht darstellen lässt. Letzteres kommt vor:
Ein Code128-Barcode und ein QR-Code kennen ohne festen Zeichensatz nur den Zeichenvorrat bis
ISO-8859-1; ein Name in kyrillischer oder chinesischer Schrift lässt sich damit nicht kodieren. In
diesen Fällen erscheint an der Stelle eine leere Fläche in den angeforderten Maßen, der Report wird
aber fertig gedruckt, und der Grund steht als Warnung im Log.

Das gilt nur für den Weg über `#convert`. Die zugrunde liegende Klasse `ReportingBarcodeUtils` direkt
aufzurufen ist **nicht vorgesehen**: Sie meldet einen nicht darstellbaren Inhalt als Fehler, weil
Aufrufer außerhalb der Vorlagen – etwa die Signatur-QR-Codes der Schulbescheinigung – darauf angewiesen
sind. In einer Vorlage würde derselbe Fehler die gesamte Ausgabe abbrechen.

### Icons einbinden – der Dialekt #icon

Für kleine Symbole (z. B. die Kennzeichnung externer Schüler) gibt es den Dialekt **`#icon`**. Er liefert ein fertiges `<img>`-Element mit dem Icon als eingebettetem SVG – deshalb immer mit `th:utext` ausgeben (nicht `th:text`):

```html
<!-- Icon in Standardgröße (14 px): -->
<span th:utext="${#icon.get('external')}"></span>

<!-- Mit Größe (px) und Farbe (CSS-Farbwert): -->
<span th:utext="${#icon.get('external', 12, '#c00')}"></span>

<!-- Spezialfall: Extern-Kennzeichnung eines Schülers inkl. Kürzel der Stammschule: -->
<span th:utext="${#icon.getExtern(12, schueler, true, ' ', '')}"></span>
```

- Bei unbekanntem Icon-Namen kommt ein leerer String zurück – der Report läuft sauber durch.
- Welche Icon-Namen es gibt, steht in der Ressource `icons/icons.json` (neue Icons werden dort mit ihren SVG-Pfaddaten ergänzt, Quelle: RemixIcon).
- `#icon.getExtern(...)` erzeugt nur dann eine Ausgabe, wenn der übergebene Schüler den Status EXTERN hat – eine eigene `th:if`-Prüfung ist nicht nötig.

> Es gibt außerdem den Dialekt `#inline`, der CSS einbettet. Den brauchst du nicht direkt – er steckt schon im Head-Fragment.

---

## 10. Schritt 7 – Layout & CSS

### Wichtigste Einschränkung: Layout über Tabellen

Das PDF wird mit **OpenHtmlToPdf** erzeugt. Diese Bibliothek unterstützt **kein** modernes CSS-Layout (kein Flexbox, kein Grid). **Für die Anordnung von Inhalten musst du HTML-Tabellen verwenden** – auch dort, wo man auf einer Webseite `div`-Boxen nehmen würde. Das ist der größte Unterschied zur normalen Webentwicklung.

### SonarQube-Regel: Layout-Tabellen brauchen einen (versteckten) Tabellenkopf

Die statische Code-Analyse **SonarQube** verlangt aus Barrierefreiheits-Gründen, dass jede Tabelle Kopfzellen (`<th scope="col">`) besitzt. Reine **Layout-Tabellen** haben aber gar keine sichtbare Kopfzeile – sie würden die Regel verletzen.

Die Lösung in den bestehenden Vorlagen: ein **per `th:if="${false}"` ausgeblendeter `<thead>`** direkt zu Beginn der Tabelle. Er erfüllt die Regel der Analyse, wird aber **nie gerendert** (die Bedingung ist immer `false`) und erscheint somit weder in der HTML-Ausgabe noch im PDF:

```html
<table class="fixed-100 mp-0">
    <thead th:if="${false}"><!-- Header nur für SonarQube, keine Ausgabe --><tr><th scope="col">Col1</th><th scope="col">Col2</th></tr></thead>
    <tr>
        <td>...</td>
        <td>...</td>
    </tr>
</table>
```

Regeln dazu:

- **Pro Spalte ein `<th scope="col">`** im versteckten `<thead>` (oben sind es zwei Spalten → zwei `<th>`). Die Beschriftung ist egal (`Col1`, `Col2`, …), da nichts ausgegeben wird.
- Der Kommentar `<!-- Header nur für SonarQube, keine Ausgabe -->` macht den Zweck für die nächste Person klar – bitte mit übernehmen.
- Hat eine Tabelle **eine echte, sichtbare Kopfzeile** (mit `<thead>` und sinnvollen `<th>`), brauchst du diesen Trick **nicht** – dann ist die Regel ohnehin erfüllt.

### Die drei Arten von CSS

1. **Gemeinsame Basis-CSS** (im Ordner `css/`) – werden automatisch über das Head-Fragment eingebunden:
   - `reporting-pdf.css` – Grundeinstellungen für die PDF-Bibliothek.
   - `reporting-styles.css` – die **wiederverwendbaren Utility-Klassen** (s. u.).
2. **Das Seitenformat** (ebenfalls im Ordner `css/`) – genau eine Format-Datei, die du über den Parameter `namePageCSS` im Head-Fragment auswählst (siehe Tabelle unten).
3. **Deine eigene Vorlagen-CSS** (`MeinReport.css`, neben der HTML-Datei) – wird automatisch eingebunden und **überschreibt** bei gleichem Selektor die gemeinsamen Styles. Hier kommen Styles rein, die nur diese eine Vorlage braucht.

### Seitenformate (`namePageCSS`)

Übergib im Head-Fragment einen dieser Werte aus dem `css/`-Ordner:

| Datei | Format |
|-------|--------|
| `reporting-A4-hoch.css` | A4 Hochformat |
| `reporting-A4-quer.css` | A4 Querformat |
| `reporting-A4-hoch-duplex.css` / `…-quer-duplex.css` | A4 mit beidseitigem Druck (Duplex) |
| `reporting-A4-hoch-minimal-rand.css` / `…-quer-minimal-rand.css` | A4 mit minimalem Rand |
| `reporting-A4-DIN5008.css` | A4 nach Brief-Norm DIN 5008 |
| `reporting-A3-quer-minimal-rand.css` | A3 Querformat |

Das Format darf auch **dynamisch** anhand von Optionen gewählt werden, z. B.:

```html
<head th:replace="~{fragments/reportHtmlHead :: htmlHead(
    title='Schülerliste',
    htmlTemplatePfad=${Parameter.reportVorlage().getPfadHtmlTemplate()},
    namePageCSS='reporting-A4' + (${querformat} ? '-quer' : '-hoch')
                + (${duplex} ? '-duplex' : '') + '.css')}">
```

### Utility-Klassen aus `reporting-styles.css`

In den bestehenden Vorlagen tauchen kurze Klassennamen auf. Sie kommen aus `reporting-styles.css` und sparen viel eigenes CSS. Beispiele:

- `fixed-100` – Tabelle auf 100 % Breite mit festem Layout.
- `mp-0` – ohne Außen-/Innenabstand (margin/padding 0).
- `ta-l`, `ta-c`, `ta-r` – Text links/zentriert/rechts; Varianten mit `t`/`b` für oben/unten (z. B. `ta-lb` = links + unten).
- `bo-t-grey`, `bo-b-grey` – graue Linie oben/unten.
- `p-tb-05`, `p-lr-1` – Innenabstand oben/unten bzw. links/rechts (Zahl = Stärke).
- `f-bold` – fett; `bg-lightgrey` – hellgrauer Hintergrund.
- `head-small-grey` – kleine graue Spaltenüberschrift.

> Schau am besten in `css/reporting-styles.css` und in eine bestehende Vorlage, um die jeweils passenden Klassen zu finden. Maße gibst du in **Millimetern** an (`mm`), das passt zum Druck (z. B. `style="width: 58mm;"`).

### Seitenkopf und -fuß

Kopf und Fuß jeder gedruckten Seite kommen aus dem Fragment `reportPageHeaderFooter` (Schritt 3). Es füllt automatisch Schulbezeichnung, Schuljahresabschnitt, Schulnummer, Druckdatum/-benutzer und Seitenzahlen. Über den Parameter `headerRight` gibst du den Text oben rechts vor. Diese Bereiche erscheinen nur bei der Druckausgabe (gesteuert über `Parameter.istDruckausgabe()`).

---

## 11. Schritt 8 – Der Dateiname der Ausgabe (.name.tpl)

Wird der Report als Datei (oder ZIP mit mehreren Dateien) ausgegeben, bestimmt die `.name.tpl`-Datei den **Dateinamen**. Auch sie ist eine kleine Thymeleaf-Vorlage und hat Zugriff auf dieselben Contexts.

Sie nutzt die kompakte „Text-Schreibweise" von Thymeleaf (`[# ...]` statt HTML-Tags). Beispiel:

```text
[# th:if="${Schueler.isEmpty()}"]
    Schueler-Liste-Kontaktdaten-Erzieher
[/]
[# th:if="${!Schueler.isEmpty()}"]
    [# th:each="schueler,iterState : ${Schueler}"]
        [# th:if="${iterState.first}"]
            Schueler-Liste-Kontaktdaten-Erzieher_[(${ #dates.format(#aktuell.jetztAlsDate(), 'yyyyMMdd-HHmm') })]
        [/]
    [/]
[/]
```

- `[# th:... ]…[/]` ist eine Bedingung/Schleife.
- `[(${ ... })]` gibt einen Wert aus (hier das aktuelle Datum als `20250131-1430`).

Für den Anfang reicht es oft, eine vorhandene `.name.tpl` zu kopieren und den festen Textteil anzupassen.

---

## 12. Schritt 9 – Testen

Es gibt zwei Wege, eine Vorlage zu prüfen:

1. **Statische Vorschau in der IDE / im Browser** – Öffne die `.html`-Datei direkt. Du siehst nur das **statische** Gerüst mit den Platzhalter-Beispieltexten, **keine echten Daten**. Gut, um grobes Layout und HTML-Fehler zu sehen.
2. **Echter Test im SVWS-Client** – mit echten Daten. Im Client unter **Schule → Reporting** die Vorlage auswählen, Optionen setzen und ausgeben (HTML/PDF). Nur so siehst du das tatsächliche Ergebnis inklusive Daten, Seitenumbrüchen und Druck-Layout.

> Da Browser und OpenHtmlToPdf unterschiedlich rendern, ist für das **endgültige Aussehen immer das im Client erzeugte PDF maßgeblich** – nicht die Browser-Vorschau. Print-CSS-Effekte (Seitenränder, Kopf-/Fußzeilen) zeigt die Browser-Vorschau oft gar nicht oder fehlerhaft an.

### Snapshot-Tests ausführen

Zusätzlich zu diesen beiden Wegen gibt es die Snapshot-Suite unter `tests/tests-server-reporting`.
Sie vergleicht die erzeugte HTML-Ausgabe mit einer hinterlegten Fassung und bemerkt damit
Änderungen, die man beim Draufschauen übersieht.

**Für jede neue und jede geänderte Vorlage ist ein Lauf verbindlich.** Das gilt nicht nur für die
`.html`-Datei, sondern ebenso für Änderungen an CSS, an der `.name.tpl`, an den Vorlagenparametern
und an deren Defaults im Katalog. Jede Vorlage braucht dabei mindestens einen Snapshot-Fall, einen
Default-Fall und einen Dateinamen-Fall — die Suite prüft das selbst und meldet eine Vorlage, der
einer davon fehlt.

**Ein unerwarteter Unterschied ist ein Fehler**, kein Anlass zum Aktualisieren. Erst wenn geklärt
ist, dass die Änderung fachlich gewollt ist, werden die betroffenen Snapshots bewusst neu erzeugt
und die Abweichung in der Review benannt.

**Der Snapshot ersetzt die Sichtprüfung des PDFs nicht.** Er vergleicht HTML; Seitenumbrüche,
Ränder und alles, was erst OpenHtmlToPdf entscheidet, sieht er nicht. Ein grüner Lauf bei
verändertem Layout ist deshalb kein Beleg für ein korrektes Dokument.

Ausführung, Voraussetzungen und das gezielte Erneuern einzelner Snapshots stehen in der README des
Testprojekts unter `tests/tests-server-reporting/tests/reporting/`.

### Null-Prüfung der Vorlage

Ein Testlauf zeigt nur, was die verwendeten Daten hergeben. Fehlt ein Wert erst beim Echteinsatz,
bricht die Ausgabe ab – deshalb wird die Null-Sicherheit **nicht** ertestet, sondern geprüft. Diese
Prüfung ist für jede neue und jede geänderte Vorlage verbindlich und umfasst die `.html`-Datei
**und** die zugehörige `.name.tpl`.

Sie ist bewusst so formuliert, dass sie ohne Kenntnis der Daten abarbeitbar ist – von Hand oder
KI-gestützt. In letzterem Fall gib die Vorlage zusammen mit dieser Liste zur Prüfung; die Antwort
soll jeden Fund mit Zeilennummer und verletzter Regel benennen.

- [ ] **N1 – Ketten:** Jeder Ausdruck mit mehr als einem Aufruf (`a.b().c()`) prüft jedes
      Zwischenglied auf `!= null`, oder die Zwischenwerte sind per `th:with` gebunden und einzeln
      geprüft. **Ein `th:if` am umschließenden Element zählt als Guard** für alles darin – prüfe
      also den umgebenden Baum, nicht nur die Zeile.
- [ ] **N2 – Unterobjekte:** Jeder Zugriff auf ein Unterobjekt, Enum oder Zahl-Objekt hat einen
      Guard. Maßgeblich ist der `@return`-Kommentar des Getters in der Java-Klasse.
- [ ] **N3 – Listen/Maps:** Kein überflüssiger Null-Guard auf Listen oder Maps – die sind nie
      `null`. Wo nötig, wird auf *leer* geprüft.
- [ ] **N4 – Text/Datum:** Leere Texte und Datumswerte werden mit `isEmpty()` geprüft, nicht mit
      `== null`.
- [ ] **N5 – Indizierter Zugriff:** Jedes `liste[0]`, `liste[i]` und `liste.get(n)` steht hinter
      einer Größenprüfung.
- [ ] **Keine Ausnahmen:** Kein Guard wurde mit der Begründung weggelassen, der Wert könne
      fachlich nicht fehlen (siehe den Hinweis zum Filter in Schritt 4).

Findet die Prüfung nichts, ist die Vorlage in diesem Punkt fertig. Findet sie etwas, ist es ein
Fehler in der Vorlage – ein Absturz bei der Ausgabe ist nie ein „normaler Datenzustand".

---

## 13. Referenz: Verfügbare Daten-Contexts

**Immer verfügbar** (unabhängig vom Bereich):

| Context | Inhalt |
|---------|--------|
| `Schule` | Daten der Schule (Bezeichnung, Schulnummer, Schuljahresabschnitt …). |
| `Parameter` | Die technischen Reporting-Parameter, u. a. `Parameter.istDruckausgabe()`, `Parameter.reportVorlage()`, `Parameter.duplexdruck`. |
| `VorlageParameter` | Die vom Nutzer gesetzten Optionen (`VorlageParameter.get('…')`, siehe Schritt 5). |
| `Benutzer` | Der angemeldete Benutzer (z. B. `Benutzer.benutzername()`). |

**Bereichsabhängig** – gefüllt je nach `datenContext` aus Schritt 1:

| `datenContext` | gefüllter Context | Inhalt (Reporting-Typ) |
|----------------|-------------------|------------------------|
| `SCHUELER` | `Schueler` | Liste von `ReportingSchueler` |
| `SCHUELER_GOST_LAUFBAHNPLANUNG` | `Schueler` | Liste von `ReportingSchueler`; zusätzlich geprüft: GOSt vorhanden, Beratungs- und Abiturdaten der Laufbahnplanung |
| `SCHUELER_GOST_ABITUR` | `Schueler` | Liste von `ReportingSchueler`; zusätzlich geprüft: GOSt vorhanden, Abiturdaten |
| `KLASSEN` | `Klassen` | Liste von `ReportingKlasse` |
| `KURSE` | `Kurse` | Liste von `ReportingKurs` |
| `LEHRER` | `Lehrer` | Liste von `ReportingLehrer` |
| `GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG` | `GostLaufbahnplanungAbiturjahrgangFachwahlStatistiken` | Fachwahlstatistiken |
| `GOST_KURSPLANUNG_KURSE` | `GostBlockungsergebnis` | Blockungsergebnis der Kursplanung aus Sicht der Kurse |
| `GOST_KURSPLANUNG_SCHUELER` | `GostBlockungsergebnis` | Blockungsergebnis der Kursplanung aus Sicht der Schüler |
| `GOST_KLAUSURPLANUNG_SCHUELER` | `GostKlausurplan` | Klausurplan aus Sicht der Schüler |
| `GOST_KLAUSURPLANUNG_TERMINE` | `GostKlausurplan` | Klausurplan aus Sicht der Klausurtermine |
| `STUNDENPLANUNG_FACH` | `FaecherStundenplaene` | Stundenpläne aus Sicht der Fächer |
| `STUNDENPLANUNG_KLASSEN` | `KlassenStundenplaene` | Stundenpläne aus Sicht der Klassen |
| `STUNDENPLANUNG_LEHRER` | `LehrerStundenplaene` | Stundenpläne aus Sicht der Lehrkräfte |
| `STUNDENPLANUNG_RAUM` | `RaeumeStundenplaene` | Stundenpläne aus Sicht der Räume |
| `STUNDENPLANUNG_SCHUELER` | `SchuelerStundenplaene` | Stundenpläne aus Sicht der Schüler |

> Jeder dieser Contexts ist ein **Java-Objekt bzw. eine Liste davon**. Welche Werte du abrufen kannst, steht in der jeweiligen `Reporting…`-Klasse unter `…/module/reporting/types/`.

---

## 14. Referenz: Der Dialekt #convert

Aufruf im Template: `#convert.<methode>(<argumente>)`. Die Methoden stehen in `html/dialects/ConvertExpressionHelper.java`. Überblick nach Themen:

- **Datum** (Eingabe ist ein ISO-Datum als String, z. B. `2008-12-31`):
  `toDateDE`, `toDateDELong`, `toWochentagDE`, `toWochentagKurzDE`, `toKalenderwocheDE`, `toDateObject`.
- **Grafische Elemente** (Ergebnis ist ein SVG, das du in `th:utext` oder als `img`-Quelle einsetzt):
  `toCheckboxSVG(boolean)`, `toCheckboxSVG(boolean, groesse)`,
  `to2DCodeQRCodeAsSvgHtmlImageSource(inhalt, breiteMM, hoeheMM)`,
  `toBarcodeCode128AsSvgHtmlImageSource(inhalt, breiteMM, hoeheMM)`.
  Die beiden Code-Funktionen liefern immer eine Bildquelle; bei leerem oder nicht darstellbarem Inhalt
  eine leere Fläche statt eines Abbruchs (siehe [Abschnitt 9](#9-schritt-6--datum-qr-codes--co-dialekt-convert)).
- **Kompression & Codierung** (für QR-/Barcode-Inhalte): `compressGZipString`, `decompressGZipString`,
  `encodeBase64`/`decodeBase64`, `encodeBase45`/`decodeBase45`, `encodeBase32`/`decodeBase32`.

**Der Dialekt `#icon`** (siehe auch Schritt 6): `#icon.get(name)`, `#icon.get(name, groessePx)`, `#icon.get(name, groessePx, farbe)` liefern ein `<img>`-Element mit dem Icon als SVG-Data-URI (Ausgabe per `th:utext`); `#icon.getExtern(groessePx, schueler, mitKuerzel, fuehrenderText, folgenderText)` erzeugt die Extern-Kennzeichnung eines Schülers. Die Methoden stehen in `html/dialects/IconExpressionHelper.java`, der Icon-Katalog in `icons/icons.json`.

> Die meisten IDE-Plugins kennen diese Dialekte nicht und zeigen sie als „unbekannt" an. Das ist normal – zur Laufzeit funktionieren sie trotzdem. Die `@thymesVar`-Kommentarzeile aus dem Grundgerüst hilft der IDE etwas bei der Autovervollständigung.

---

## 15. Goldene Regeln & häufige Fehler

- **Werte immer mit Klammern abrufen.** Reporting-Daten sind Methoden: `schueler.vorname()`, nicht `schueler.vorname`.
- **Keine `?.`- oder `?:`-Kurzschreibweise.** Diese „Null-Sicherheits"-Operatoren gibt es hier nicht. Prüfe `null` immer ausdrücklich: `${x != null ? x.wert() : ''}`.
- **Null-Guards auch dort, wo der Wert „nie fehlt".** Ein gesetzter Filter kann jeden Verweis auf ein anderes Objekt leeren – auch die Klasse eines Schülers.
- **`liste[0]` nur nach Größenprüfung.** Listen können leer sein; ein indizierter Zugriff darauf bricht ab.
- **Layout nur mit Tabellen.** Kein Flexbox/Grid – OpenHtmlToPdf unterstützt das nicht.
- **Layout-Tabellen brauchen einen versteckten `<thead th:if="${false}">`** mit je einem `<th scope="col">` pro Spalte – sonst meckert SonarQube (siehe Schritt 7).
- **`th:utext` braucht gültiges XML.** Schreibe `<br/>` statt `<br>`. Nackte `<`, `>`, `&` brechen die Erzeugung – auch in Kommentaren innerhalb von eingebettetem CSS/HTML. Im Zweifel `&lt;`, `&gt;`, `&amp;` verwenden.
- **HTML-Datei und CSS-Datei: gleicher Name, gleicher Ordner.** Sonst wird deine Vorlagen-CSS nicht gefunden.
- **Parametername muss exakt passen** – in der Java-Konfiguration und im `VorlageParameter.get('…')`.
- **Maße in Millimetern** (`mm`) angeben – das ist druckgerecht.
- **Endergebnis immer im Client/PDF prüfen**, nicht nur in der Browser-Vorschau.
- **Fehlende Daten dürfen nicht abstürzen.** Wenn ein Wert fehlt, soll die Zelle leer bleiben (`''`), der Report aber sauber durchlaufen. Ein Absturz bei der Ausgabe ist immer ein Fehler in der Vorlage (oder im Modul), kein „normaler" Datenzustand.

---

## 16. Checkliste für eine neue Vorlage

- [ ] **Enum-Eintrag** in `ReportingReportvorlage.java` angelegt (Name nach `HAUPTDATEN_V_DETAILDATEN`, korrekter `datenContext`, Pfad, Kompetenzen).
- [ ] **Konfigurationsmethode** im passenden `ReportingReportvorlageKonfiguration…` angelegt und im Enum-Eintrag referenziert (auch wenn es keine Optionen gibt).
- [ ] **Drei Dateien** im richtigen Themenordner: `.html`, `.css`, `.name.tpl` – gleicher Basisname.
- [ ] **HTML-Grundgerüst** mit `reportHtmlHead`- und `reportPageHeaderFooter`-Fragment übernommen.
- [ ] **Seitenformat** (`namePageCSS`) gewählt.
- [ ] **Inhalt** mit `th:text`/`th:utext`, `th:each`, `th:if` umgesetzt.
- [ ] **Null-Prüfung** für `.html` und `.name.tpl` durchgeführt (Liste in Schritt 9) – ohne Befund.
- [ ] **Layout-Tabellen** mit verstecktem `<thead th:if="${false}">` (ein `<th scope="col">` pro Spalte) gegen die SonarQube-Regel abgesichert.
- [ ] **Optionen** über `VorlageParameter.get('…')` eingebunden (falls vorhanden).
- [ ] **Datum/Sonderelemente** über `#convert` formatiert.
- [ ] **Im Client getestet** (HTML **und** PDF), mit und ohne gesetzte Optionen, auch mit leerer Datenmenge.
- [ ] **Snapshot-Testfall angelegt** – je ein Fall für Snapshot, Katalog-Defaults und Dateiname; die Suite meldet eine Vorlage, der einer davon fehlt.
- [ ] **Snapshot-Suite vollständig gelaufen** und grün, kein Fall übersprungen. Ein unerwarteter Unterschied ist ein Fehler, kein Anlass zum Aktualisieren.

---

## Weiterführend

- [`reporting-architektur.md`](reporting-architektur.md) – innerer Aufbau des Reporting-Moduls (für Entwickler, die das Modul selbst erweitern).
- [Thymeleaf-Dokumentation](https://www.thymeleaf.org/documentation.html) – die zugrunde liegende Template-Sprache.
- [Print-CSS (SELFHTML)](https://wiki.selfhtml.org/wiki/Print-CSS) – Hintergrund zu druckspezifischem CSS.
