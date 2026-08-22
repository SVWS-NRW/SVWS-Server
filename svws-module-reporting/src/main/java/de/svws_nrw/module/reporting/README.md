# Reporting-Modul — Einstieg

Dieses Modul erzeugt aus den Daten des SVWS-Servers fertige Dokumente: Klassenlisten,
Bescheinigungen, Zeugnisse, Stundenpläne. Eine HTML-Vorlage wird mit Daten gefüllt und als HTML
ausgeliefert, als PDF erzeugt oder als E-Mail versendet.

Diese Seite ist der Einstieg. Sie erklärt in fünf Minuten, wie ein Report entsteht, und sagt dir,
welche der vier Dokumentationsdateien du brauchst.

---

## Wohin willst du?

| Dein Vorhaben | Lies das |
|---|---|
| **Eine neue Vorlage bauen** oder eine bestehende ändern | [`reporting-template-erstellung.md`](reporting-template-erstellung.md) — Schritt-für-Schritt, beginnt beim Kopieren einer vorhandenen Vorlage |
| **Eine Sortierung oder einen Filter** für einen Typ ergänzen | [`reporting-sortierung-und-filterung.md`](reporting-sortierung-und-filterung.md) |
| **Verstehen, wie das Modul aufgebaut ist** | [`reporting-architektur.md`](reporting-architektur.md) — Schichten, Klassen, Datenfluss |
| **Etwas am Modul ändern** — egal was | [`reporting-konventionen.md`](reporting-konventionen.md) — die verbindlichen Regeln. **Bei Widerspruch gilt diese Datei.** |

Für eine Vorlage mit bereits vorhandenem Datenaufbau brauchst du keine neue Java-Datenpipeline.
Die Registrierung und gegebenenfalls die Vorlagenparameter erfordern aber kleine Änderungen in
`svws-core`.

---

## Der Ablauf in fünf Minuten

Die Kette ist immer dieselbe:

```text
Anfrage → ReportingFactory → ReportingContext → Datenaufbau → Reporting-Typen → Vorlage → HTML/PDF
```

Am Beispiel der Fotoübersicht einer Klasse, durchgespielt mit den echten Namen:

**1. Die Anfrage kommt an.** `APIReporting` im Modul `svws-openapi` nimmt sie entgegen und übergibt
an `ReportingFactory`. Mitgegeben werden die Datenbankverbindung, die `ReportingParameter` (welche
Vorlage, welche IDs, welche Optionen) und das gewünschte Ausgabeformat.

**2. Die Vorlage sagt, was zu tun ist.** In den Parametern steht der Name einer Vorlage. Dahinter
liegt ein Eintrag im Enum `ReportingReportvorlage` — der liegt im Modul `svws-core`, nicht hier:

```java
KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN("Klasse-Liste-Schueler-Fotos-Namen",
        "Fotoübersicht klassenweise",
        ...,
        ReportingReportvorlageDatenContext.KLASSEN,      // welcher Datenaufbau
        "klassen/KlasseListeSchuelerFotosNamen.html",    // welche HTML-Datei
        ...)
```

**3. Der `ReportingContext` entsteht.** Er lebt genau eine Anfrage lang und hält alles, was gebraucht
wird: die Datenbankverbindung, die Parameter, die Diagnose und die neun Domänen-Repositories
(`repositorySchueler()`, `repositoryLehrer()`, `repositoryLerngruppen()`, …). **Nur Repositories
dürfen an die Datenbank.**

**4. Der Datenaufbau wählt die Daten aus.** Welcher Aufbau zu welchem Enum-Wert gehört, steht in
`HtmlContextInitializerRegistry`. Für `KLASSEN` steht dort:

```java
(ctx, ids) -> ctx.repositoryLerngruppen().waehleKlassenAus(ids),   // Auswahl aus den IDs
HtmlContextKlassen::new                                           // Aufbau für die Vorlage
```

`HtmlContextKlassen` legt die Liste unter einem Namen ab, den die Vorlage kennt:

```java
context.setVariable("Klassen", getContextData());
```

**5. Die Reporting-Typen liefern die Werte.** In der Liste stehen `ReportingKlasse`-Objekte —
genauer: `ProxyReportingKlasse`. Der Proxy ist eine Unterklasse des einfachen Typs und lädt fehlende
Daten beim ersten Zugriff nach. Fragt die Vorlage nach `klasse.schueler()`, holt der Proxy sie über
`repositorySchueler()`. **Deshalb muss keine Vorlage wissen, ob Daten schon geladen sind** — sie
fragt einfach. Ob eine Beziehung überhaupt besteht, ist eine andere Frage: Ein Verweis wie
`klasse.jahrgang()` kann `null` sein und wird in der Vorlage geprüft.

**6. Die Vorlage rendert.** `HtmlFactory` gibt den Aufbau an Thymeleaf:

```html
<div th:each="klasse,iterationVarKlasse : ${Klassen}">
    <th th:text="${'Klassenliste ' + klasse.kuerzel()}">Klassenliste 06C</th>
```

**7. Das Format entsteht.** Bei HTML ist hier Schluss. Sonst erzeugt `PdfFactory` die PDF-Dateien —
bei mehreren Dokumenten als ZIP —, und `EmailFactory` versendet sie.

Nebenher läuft die **Diagnose** mit. Sie sammelt, was beim Laden und Rendern aufgefallen ist.
Tolerierbare Lücken in untergeordneten Daten bleiben in der Ausgabe leer und werden gemeldet;
Infrastrukturfehler oder fehlende essenzielle Hauptdaten brechen die Erstellung dagegen mit einem
passenden Fehlerstatus ab.

---

## Vier Dinge heißen „Context"

Das ist die häufigste Stolperfalle beim Lesen. Die Begriffe sind historisch etabliert; insbesondere
`ReportingReportvorlageDatenContext` ist als in den Client transpilierter Enum-Name nicht ohne
Weiteres änderbar. Hier steht deshalb, was jeweils gemeint ist:

| Name | Was es ist |
|---|---|
| `ReportingContext` | Das Objekt für **eine Anfrage**: Verbindung, Parameter, Repositories, Diagnose. Wenn im Modul „der Context" ohne Zusatz steht, ist meistens dieser gemeint |
| `HtmlContext<T>` | Ein Baustein des für Thymeleaf aufgebauten Datenkontexts. Er stellt eine oder mehrere benannte Variablen bereit; `HtmlContextKlassen` beispielsweise `${Klassen}`. Ein Datenaufbau kann mehrere solcher Bausteine erzeugen |
| `org.thymeleaf.context.Context` | Der Container von Thymeleaf, in den `HtmlContext` seine Variablen legt. Fremder Code, nicht unserer |
| `ReportingReportvorlageDatenContext` | Ein **Enum-Wert**, der einen Datenaufbau benennt (`KLASSEN`, `SCHUELER`, …). Der Name führt in die Irre: Es ist kein Objekt mit Daten, sondern nur die Kennung |

Mehrere Vorlagen teilen sich denselben Datenaufbau. Deshalb kommt eine neue Vorlage meist ohne neue
Java-Datenpipeline aus — es bleiben der Eintrag im Enum und gegebenenfalls die Vorlagenparameter in
`svws-core`.

---

## Wo liegt was

| Paket | Inhalt |
|---|---|
| `factories/` | `ReportingFactory` (Einstieg), `HtmlFactory`, `PdfFactory`, `EmailFactory` |
| `repositories/` | `ReportingContext` und die neun Domänen-Repositories — die einzige Stelle mit Datenbankzugriff |
| `types/` | Die Reporting-Typen und ihre `Proxy…`-Unterklassen |
| `html/contexts/` | Die Datenaufbauten und die `…InitializerRegistry` |
| `html/dialects/` | Die eigenen Thymeleaf-Erweiterungen wie `#convert` und `#icon` |
| `filterung/`, `sortierung/` | Benutzerfilter und Sortierung je Typ |
| `diagnose/` | Ausgabeprobleme sammeln und melden |
| `builders/`, `signing/`, `utils/`, `parameter/` | Aufbau der Ausgabe, Signierung, Hilfsmittel |
| `src/main/resources/…/reporting/` | Die HTML-Vorlagen, ihr CSS und die Dateinamen-Vorlagen |

**Nicht in diesem Modul:** das Enum `ReportingReportvorlage` und die Vorlagenkonfiguration liegen in
`svws-core`, damit der Client sie kennt. Eine neue Vorlage braucht deshalb immer auch dort einen
Eintrag. Die API-Schicht liegt in `svws-openapi`.

---

## Bevor du etwas änderst

Vier Dinge, über die alle stolpern. Ausführlich stehen sie in den
[Konventionen](reporting-konventionen.md):

- **In den Vorlagen gibt es kein `?.` und kein `?:`.** Thymeleaf läuft hier gegen OGNL, das kennt
  beides nicht. Prüfe mit `${x != null ? … : …}`.
- **Das CSS wird als XML eingelesen.** Ein `<`, `>` oder `&` bricht die PDF-Erzeugung — auch in
  einem Kommentar.
- **Die Ausgabe erklärt dem Anwender keine technischen Fehler.** Eine tolerierbare Lücke bleibt
  leer; wann stattdessen abgebrochen wird, steht oben. Eine `NullPointerException` in der
  Druckausgabe ist immer ein Fehler im Code, nie ein „sichtbarer Datenfehler".
- **Datumsfelder sind nie `null`, sondern `""`.** Prüfe mit `isEmpty()`, nicht auf `null`.
