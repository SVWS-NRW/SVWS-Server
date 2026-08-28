# Reporting – Konventionen & Invarianten

Verbindliche Regeln für alle Änderungen im Modul `svws-module-reporting`. Diese Datei ist die
**normative Referenz** — kompakt genug, um sie bei jeder Änderung vollständig zu lesen.
Die *Beschreibung* der Architektur (Schichten, Klassen, Datenfluss) steht in
[`reporting-architektur.md`](reporting-architektur.md); die Anleitung für Vorlagen-Autoren in
[`reporting-template-erstellung.md`](reporting-template-erstellung.md), die Anleitung zu Sortierung
und Filterung in [`reporting-sortierung-und-filterung.md`](reporting-sortierung-und-filterung.md).
Bei Konflikt gilt diese Datei; Regel-Änderungen werden hier gepflegt.

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
  noch `ReportingContext`. Ausgenommen sind Felder, die allein einen aus den eigenen Daten
  abgeleiteten Wert beim ersten Zugriff festhalten: Sie ändern keinen fachlichen Zustand und
  sparen die Berechnung, wenn eine Ausgabe denselben Wert mehrfach abfragt.
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

### 4.1 Verbindliche Grundsätze (G-1 bis G-6)

Die sechs Grundsätze sind das Fundament der Regeln und der Fehlercode-Matrix in diesem Abschnitt.
Ihre Kennungen sind stabile Bezeichner, die auch Tests und Arbeitsdokumente referenzieren.

- **G-1 — Der Status folgt der Verantwortung.** `4xx` bezeichnet einen Fehler des Aufrufers, `5xx`
  einen des Servers. `404` gilt allein der adressierten Hauptressource des Reports; welche das
  ist, bestimmt ihre fachliche Rolle und nicht das technische Parameterfeld. In der
  GOSt-Laufbahnplanung ist der Abiturjahrgang die Hauptressource, obwohl er in `idsHauptdaten`
  transportiert wird; in der GOSt-Klausurplanung sind die Stufen aus Abiturjahrgang und Halbjahr
  Nutzlast wie die IDs eines Listenreports — ein nicht vorhandener Abiturjahrgang wird dort
  ausgelassen und gemeldet. Ein Listenreport über Schüler, Klassen oder Lehrkräfte hat kein
  einzelnes Hauptdatenobjekt — seine IDs sind die Nutzlast.
- **G-2 — Fehlende untergeordnete Daten führen zu einer unvollständigen Ausgabe.** Untergeordnete
  Daten, die fachlich nicht vorhanden oder datensatzbezogen nicht ladbar sind, werden auf genau
  der betroffenen Ebene ausgelassen: Fehlt ein Schüler, entfällt dieser Schüler; fehlen Teildaten
  eines geladenen Schülers, entfallen nur diese Teildaten. Der Report bleibt erfolgreich, auch
  wenn dadurch kein angeforderter Datensatz übrig bleibt.
- **G-2a — Die Ursache entscheidet, nicht die Anzahl.** Ein datensatzbezogener Fehler bleibt auch
  dann tolerierbar, wenn er sämtliche angeforderten Datensätze betrifft; eine
  Infrastrukturstörung bricht auch dann ab, wenn nur ein Datensatz betroffen ist. Eine nicht
  zuverlässig klassifizierbare Ursache gilt bei untergeordneten Daten als datensatzbezogen; beim
  Hauptdatenobjekt bleibt ein nicht sicher tolerierbarer technischer Fehler ein Abbruch.
- **G-3 — Das Hauptdatenobjekt ist die Ausnahme.** Existiert das fachlich adressierte
  Hauptdatenobjekt nicht, endet die Ausgabe mit `404`; existiert es, ist aber technisch nicht
  ladbar, mit `500`. Ein leeres Dokument wäre in beiden Fällen irreführend.
- **G-4 — Nicht darstellbare Werte werden maßgleich ersetzt.** Ein vorhandener Wert, der sich
  nicht darstellen lässt, bricht die Ausgabe nicht ab; an seiner Stelle entsteht eine
  Ersatzdarstellung in den Standardmaßen der Ausgabeart, damit der Fehlerfall das Layout nicht
  verschiebt. Ob sie als Fehlerbild erkennbar ist oder als leere Fläche erscheint, entscheidet die
  Vorlage (ausgestaltet in der Regel „Nicht darstellbare Werte“, Abschnitt 4.2).
- **G-5 — Der Ladezustand wird explizit geführt.** Ein Datenzugriff unterscheidet
  `Geladen(wert)`, `NichtVorhanden` und `Fehlgeschlagen(ursache, exception)` — im Code
  `ReportingLadezustand`. Eine leere Collection ist ein geladener Wert; `Geladen(null)` ist
  unzulässig; ein fehlender Cache-Eintrag bedeutet „noch nicht geladen“ und ist kein eigener
  Ladezustand; ein technischer Fehler wird nie allein durch `null` oder eine leere Collection
  dargestellt, und die auslösende Exception lebt so lange wie der zugehörige Cache-Eintrag — ein
  neuer `ReportingContext` darf den Zugriff erneut versuchen. Der Ladezustand kennt weder
  fachlichen Schlüssel noch HTTP-Status, und der Benutzerfilter gehört ausschließlich in die
  Auswahl, nie in den Ladezustand. **Reichweite:** Verbindlich für jeden neu angefassten und jeden
  inventarisierten Ladezustand. Einen modulweiten Vollständigkeitsnachweis verlangt der Grundsatz
  nicht: Der Architekturtest sichert das Map-Cache-Muster; Einzelfelder, Listen-Caches außerhalb
  des Musters und stille Rückfallwerte in Proxys bleiben ungeprüft.
- **G-6 — Ein erfolgreicher Report enthält keinen `ERROR`-Eintrag.** `ERROR` ist dem endgültigen
  Abbruch vorbehalten. Tolerierte Ausgabeprobleme erzeugen höchstens `WARNING`, ein erfolgreicher
  Einzel-Fallback nach gescheitertem Bulk-Zugriff `INFO`, eine Auswahlentscheidung höchstens ein
  zusammenfassendes `DEBUG`.

### 4.2 Melde- und Logging-Regeln

**Der Fehlervertrag:** Ein Abbruch hat **eine Meldungsquelle — die Meldung der Exception.**
Protokolliert wird an der **Abschlussgrenze**, nicht an der Wurfstelle.

- **Die Wurfstelle wirft und schweigt.** Den Block aus Beschreibung, Fehlertyp, Meldung, Ursachenkette
  und Stacktrace gibt die Ebene aus, die den Fehler abschließend behandelt — über
  `ReportingExceptionUtils.logException(…)`. Eine eigene Log-Zeile neben dem `throw` führte denselben
  Satz ein zweites Mal im Log und damit auch in der Fehlerantwort an den Client. Ausgenommen ist allein
  die technische Angabe, die den Befund erst benennt — der letzte Punkt dieser Liste.
- **Eine Quelle heißt nicht eine Ausgabe.** Der Satz erscheint zweimal: als Kopfzeile ganz oben in der
  Fehlerantwort und als Meldungszeile im Fehlerblock. Beide entstehen aus derselben Meldung. Die Zeile
  im Fehlerblock lässt sich nicht streichen, denn `logException` bedient auch die geduldeten Befunde,
  die keine Kopfzeile bekommen.
- **Jeder Wurf unterhalb der Abschlussgrenze trägt seine Meldung als String-Body.** Nur ein String-Body
  wird zur Meldung der `ApiOperationException`. Wer dort stattdessen eine Fehlerantwort als Body
  mitgibt, hinterlässt eine Exception ohne Meldung; die Kopfzeile fällt dann auf einen Ersatztext
  zurück und nennt die Phase statt des Grundes. Die fertige Fehlerantwort gibt allein die
  Abschlussgrenze als Body mit — ohne sie erhielte der Client das Log nicht.
- **Die Meldung sagt dem Anwender, was los ist; der Fehlerblock sagt es dem Betreiber.** Die Meldung
  beginnt mit `### FEHLER: `, nennt die Ursache statt der Stelle und kommt mit kurzen Sätzen aus. Pfade,
  Klassennamen und interne Bezeichnungen gehören nicht hinein. Ein veränderlicher Wert gehört hinein,
  **soweit der Anwender ihn kennt** — der angefragte Vorlagenname ja, der Pfad einer Datei auf dem
  Server nein.
- **Ein zweiter Satz steht nur dort, wo er eine Handlung nennt, die der Leser ausführen kann** — der
  Anwender berichtigt seine eigene Eingabe, der Betreiber prüft etwas an der Installation. Formeln wie
  „Das ist ein Programmfehler im Server.“ oder „Die Anfrage ist zu berichtigen.“ entfallen: Die
  Anfrage baut der Client, und den Statuscode trägt die Kopfzeile ohnehin. Sagt eine Meldung zu wenig,
  liegt der Mangel im Hauptsatz und ist dort zu beheben, nicht in einem Zusatz.
- **Der Kontext der Anfrage wird einmal am Eingang protokolliert.** Die Meldungen tragen keine
  technischen Werte; die Kenndaten der Anfrage hält deshalb eine `DEBUG`-Zeile in der
  `ReportingFactory` fest, bevor die erste inhaltliche Prüfung läuft. Sie arbeitet auf ungeprüften
  Daten aus dem Request und ist deshalb in jedem Feld nullsicher; freien Text gibt sie maskiert und
  längenbegrenzt aus.
- **Eine technische Angabe, die den Befund erst benennt, bekommt eine eigene Log-Zeile.** Das ist die
  einzige Ausnahme vom Schweigen der Wurfstelle: Die Zeile steht neben dem `throw`, trägt das Level des
  Abbruchs und nennt **allein** diese Angabe — die Meldung wiederholt sie nicht. Sie ist dort am Platz,
  wo weder die Meldung noch die Ursachenkette noch das Eingangsprotokoll den Befund trägt, etwa beim
  beanstandeten einzelnen Element einer übergebenen Liste. Prüft eine Methode ohne Reporting-Context, steht
  die Zeile bei ihrem kontexttragenden Aufrufer: Er führt die Schleife und kennt als einziger den
  beanstandeten Wert. Die Zeile nennt diesen einen Wert, nie die ganze Liste — sonst stünde eine Anfrage mit
  vielen IDs ungekürzt im Log.
- **Die Form der Meldungen prüft ein Quelltexttest, kein Wortlaut-Test.** `TestArchitekturFehlermeldungen`
  liest alle Quelldateien und verlangt je Meldung den Marker, ein Satzzeichen am Schluss, keinen internen
  Begriff und keine ID; dieselbe Klasse verbietet jeder Protokollzeile den Marker, gleich wie sie ihr Level
  erhält, denn damit wiederholte sie eine Exception-Meldung — ausgenommen allein `ReportingExceptionUtils`, das
  die Exception-Blöcke schreibt. Die Regeln gelten dadurch auch für Klassen, die es
  heute nicht gibt. Ein Test, der einen einzelnen Satz festschreibt, leistet das nicht und macht jede
  spätere Umformulierung zur Teständerung.
- **Der Marker kennzeichnet eine Exception-Meldung, nicht das Level und keinen Abbruch.** `### FEHLER:` steht
  vor der Meldung einer Exception und im Exception-Block, den `ReportingExceptionUtils` schreibt — auf
  `ERROR` für den Abbruch an der Abschlussgrenze, auf `WARNING` oder `INFO` für einen hingenommenen Fehler.
  Ob ein Fehler hingenommen wurde, zeigt allein das Level: `ERROR` ist dem Abbruch vorbehalten; ein Block auf
  `WARNING` dokumentiert ein hingenommenes Ausgabeproblem, einer auf `INFO` einen gelungenen Rückfall. An
  einer gewöhnlichen Wurfstelle steht keine zweite Meldung.
- **Der Quelltexttest liest Text und hat darin seine Grenze.** Er findet nicht, was erst ein Vergleich
  zeigt: eine Protokollzeile, die den Meldungstext ohne Marker wiederholt, und einen Catch, der die
  gefangene Exception nicht als Ursache mitgibt oder ihre Meldung durch eine eigene ersetzt. Er sieht auch
  keine Meldung, die als Variable oder als Konstante einer anderen Datei am Wurf ankommt — aufgelöst wird
  nur eine Konstante derselben Datei. Das sichern die Verhaltenstests an der jeweiligen Stelle: die Zusicherung auf ein leeres Log neben dem Wurf und der
  Vergleich der Meldung, die durch eine Zwischenschicht hindurch am Ende ankommt. Wo eine solche Schicht
  dazwischenliegt, bindet ein Test den Wortlaut deshalb bewusst ganz — das ist dort kein Wortlaut-Test,
  sondern die Prüfung, dass nichts ersetzt wurde.

**Die übrigen Melde- und Logging-Regeln:**

- Logging nur über `reportingContext.logger()` / `reportingContext.log()`. **Thymeleaf-Dialekte**
  sind einmalig an der geteilten `TemplateEngine` registriert und können die Meldefassade nicht als
  Feld halten; sie erhalten sie über die Context-Variable `ReportBuilderUtils.VARIABLE_PROBLEMMELDER`,
  die `ReportBuilderUtils.mergeHtmlContexts` für beide Template-Pfade — HTML-Report und
  Dateinamensvorlage — aus den Daten-Contexts setzt, und melden darüber wie jede andere Stelle.
  Abgelegt wird bewusst nur der schmale `ReportingProblemmelder` und nicht der Reporting-Context: Der
  wäre per OGNL für jede Vorlage erreichbar und öffnete deren Zugriff auf die Repositories.
  `Logger.global()` ist der Abschlussgrenze vorbehalten; kein Dialekt und keine andere Stelle schreibt
  hinein.
- Fehler als `ApiOperationException` (ist `RuntimeException`). In Caller-Lambdas der
  `ReportingRepositoryUtils` **kein eigenes try/catch** — das würde den pro-ID-Fallback
  unterbinden.
- **Reports melden Datenfehler nicht sichtbar**; eine NPE in der Druckausgabe ist immer ein Bug,
  kein „sichtbarer Datenfehler“. Fehlende Daten werden ausgelassen; die betroffene Stelle bleibt
  leer oder zeigt einen neutralen Platzhalter.
- **Ein `ERROR`-Logeintrag ist dem Abbruch vorbehalten** (Konkretisierung von G-6). Ein
  erfolgreicher Report hinterlässt keinen einzigen; `ERROR` steht allein auf dem Weg eines Abbruchs,
  und wo der Eintrag entsteht, regelt der Fehlervertrag. Wer einen Befund hinnehmen und weiterlaufen
  will, protokolliert höchstens `WARNING` — ein `ERROR`
  gäbe einem hingenommenen Ausgabeproblem die Dringlichkeit eines Abbruchs und machte das Log als
  Abbruchspur unbrauchbar. Ein Architekturtest hält die Regel: Kein Catch-Block protokolliert
  `ERROR`, ohne zu werfen. Er prüft allein diese Reihenfolge und nicht, woher die Meldung stammt; dem
  Fehlervertrag widerspricht er damit nicht. Ein Catch-Block mit einer technischen `ERROR`-Zeile endet
  deshalb mit einem Wurf.
- **Das Log-Level gilt für den gesamten Fehlerblock.** `ReportingExceptionUtils.logException()`
  schreibt Beschreibung, Fehlertyp, Meldung, Ursachenkette und Stacktrace auf dem übergebenen Level.
  Ein Aufruf mit `WARNING` hinterlässt damit keinen ERROR-Eintrag.
- **Ein Ladefehler wird dort bewertet, wo die Daten gebraucht werden.** Repositories, die der
  `ReportingContext` für jeden Report aufbaut, protokollieren einen Ladefehler nicht bei der
  Initialisierung, sondern halten ihn als `ApiOperationException` fest — ein `boolean` verlöre die
  Ursache. Erst der Zugriff kennt die Bedeutung: Sind die Daten das angeforderte Hauptobjekt, wirft er
  und gibt den festgehaltenen Fehler als `cause` mit — den `ERROR`-Block schreibt die Abschlussgrenze;
  sind sie Beiwerk, meldet er den Befund über die Fassade `reportingContext.meldeAusgabeproblem(…)`
  und die Ausgabe läuft ohne diese Daten weiter (Muster: `ReportingRepositoryStundenplan`). Die
  Fassade protokolliert ihn mit `WARNING` und dedupliziert innerhalb des Reporting-Aufrufs über
  Ursache, Auswirkung und Schlüssel. Wer an der meldenden Stelle selbst mitzählte, würde damit Befunde
  zu anderen Schlüsseln unterdrücken. Allein ein Verbindungsabbruch endet auch hier im Wurf; dazu der
  Punkt zur Klassifikation weiter unten.
- **Der Hinweis-Header entsteht bei jedem Erfolg — aus dem gemeldeten Ausgabeumfang.** Eine
  erfolgreiche HTML-, PDF- oder ZIP-Antwort trägt `SVWS-Reporting-Hinweise`; zum Stand seiner
  Auslieferung siehe 4.4. Voraussetzung ist der
  am `ReportingContext` gemeldete Ausgabeumfang. Jeder Datenaufbau meldet ihn genau einmal — dort,
  wo die Zählwerte entstehen — und benennt seine Meldestelle über
  `HtmlContextInitializer.meldetAusgabeumfangImContextAufbau()`; ein neuer Datenaufbau wird erst
  registriert, wenn seine Hinweis- und Zähllogik geprüft ist. Fehlt die Meldung, bricht die
  `HtmlFactory` nach dem Context-Aufbau mit einem Serverfehler ab: Ein Header, dessen Zählwerte
  niemand ermittelt hat, bescheinigte eine geprüfte Vollständigkeit, die es nicht gibt. Für den
  Client bedeutet ein fehlender Header „unbekannt“, nie „nachweislich vollständig“. Gesetzt wird
  er an genau einer Stelle (`ReportingHinweiseHeader`); ein zweiter Ort für dieselbe Entscheidung
  ließe die Ausgabewege auseinanderlaufen.
- **Ein Ladefehler von Teildaten wird gemeldet und nicht selbst protokolliert.** Scheitert das Laden
  von Daten **unterhalb** eines Datensatzes — Erzieher, Sprachbelegungen, Lernabschnitte,
  Leistungsdaten, Telefonkontakte —, bleibt der Datensatz in der Ausgabe und es fehlen allein diese
  Teildaten. Das gilt für jeden Fehler außer einem Verbindungsabbruch; zu dessen Erkennung siehe
  weiter unten. Der Befund läuft über `reportingContext.meldeAusgabeproblem(…)` mit der Auswirkung
  `TEILDATEN_FEHLEN` und in aller Regel der Ursache `DATENSATZBEZOGENER_LADEFEHLER`; die Fassade
  protokolliert ihn dann dedupliziert und auf dem Level, das die Ausgabe fortsetzen lässt. Die
  Repositories melden einheitlich über `ReportingRepositoryUtils.meldeTeildatenLadefehler(…)`, damit
  die Wortwahl im Log nicht je Repository abweicht. Ein eigener `logException`-Aufruf daneben führte
  denselben Fehler zweimal im Log, und mit `ERROR` höbe er den eigenen Rückfallwert wieder auf.
- **Der Schlüssel bestimmt, was einmal zählt.** Er führt die Objektart der ausgelassenen Teildaten
  und die ID des Datensatzes, zu dem sie gehören: So zählt jede Art von Teildaten je Datensatz genau
  einmal. Betrifft der Zugriff keinen einzelnen Datensatz — etwa die Leistungsdaten aller
  Lernabschnitte eines Schuljahresabschnitts —, bleibt der Schlüssel ohne ID und zählt einmal je
  Aufruf.
- **Ein tolerierender Pfad fängt `Exception`** und nicht einen einzelnen Fehlertyp: Sonst hinge das
  Auslassen davon ab, welche Art Fehler der Zugriff gerade erzeugt.
- **Der try-Block umfasst nur den Datenzugriff.** Cache-Einträge und der Aufbau von
  Reporting-Objekten aus den geladenen Daten stehen außerhalb: Ein Fehler dort ist ein
  Programmierfehler, kein Datenfehler, und beendet die Ausgabe. Im generischen Bulk-/Einzel-Fallback
  der `ReportingRepositoryUtils` gehört die Aufbereitung des einzelnen Datensatzes im Loader-Lambda
  dagegen zum Laden genau dieses Datensatzes: Scheitert sie an dessen Daten, isoliert der
  Einzel-Fallback den Datensatz — genau das ist ein datensatzbezogener Fehler.
- **Die Ursache bestimmt die Meldestelle nicht selbst.** Sie übergibt den Fehler, und
  `ReportingProblemursache.fuerLadefehler(…)` klassifiziert ihn als einzige Stelle des Moduls. Ein
  Verbindungsabbruch in der Ursachenkette ergibt `INFRASTRUKTURSTOERUNG`, und die Fassade wirft dann
  einen Serverfehler, statt den Befund zu sammeln. Erkannt wird er an
  `SQLNonTransientConnectionException`, `SQLTransientConnectionException` und `ConnectException`.
  Diese drei genügen, weil der Server auf MariaDB läuft und dessen Treiber jeden Verbindungsfehler
  darauf abbildet; die übrigen DBMS erscheinen allein in Sicherungen und Migrationen. Jeder andere
  Fehler bleibt `DATENSATZBEZOGENER_LADEFEHLER`: Ein unsicherer Fall wird hingenommen, statt eine
  ganze Ausgabe an einem einzelnen Datensatz scheitern zu lassen.
- **Nicht darstellbare Werte sind ein gemeldetes Ausgabeproblem.** Ein vorhandener Wert, der sich
  nicht ausgeben lässt — etwa ein Barcode-Inhalt mit Zeichen, die der Zeichensatz des Codes nicht
  kennt, oder ein Inhalt jenseits der Längengrenze des Codes —, bricht die Ausgabe nicht ab.
  Die verantwortliche Erzeugungsstelle meldet über die Fassade `NICHT_DARSTELLBAR` mit der
  Auswirkung `TEILDATEN_FEHLEN` samt auslösender Exception; die übrige Ausgabe entsteht. Wie die
  Vorlage die betroffene Stelle kennzeichnet, ist ihre Darstellungsentscheidung — ein Fehlertext
  oder eine leere Fläche in den **Standardmaßen der jeweiligen Ausgabeart**, damit der Fehlerfall
  das Layout nicht gegenüber dem Erfolgsfall verschiebt. Für die Codes des `#convert`-Dialekts
  gilt: Alle nicht erzeugbaren Codes eines Aufrufs teilen sich einen Schlüssel und zählen zusammen
  als ein Hinweis, denn welcher Datensatz betroffen ist, weiß allein die Vorlage.
- **Die signierte Schulbescheinigung unterscheidet ihre Fehlerquellen** (Muster:
  `SchulbescheinigungQrFactory`). Nicht darstellbar ist dort allein das Rendern der QR-Codes; es
  wird als `NICHT_DARSTELLBAR` gemeldet. Ein Signierfehler, der nur einzelne Schüler betrifft, ist
  dagegen ein `DATENSATZBEZOGENER_LADEFEHLER`: Die Signatur ist ein fehlender Wert, kein
  Darstellungsproblem. Beides bricht nicht ab — die Vorlage zeigt je Schüler den Zustand
  `DATENFEHLER` oder `SIGNIERFEHLER` als Fehlerbild, und das Dokument sieht damit nicht signiert
  aus. Der Dienst selbst bricht dagegen ab: ein erkennbarer Anmeldefehler mit `400`, eine fehlende
  Berechtigung mit `403`, ein Ausfall oder ein Stapel ohne eine einzige verwertbare Signatur mit
  `500` (siehe die Fehlercode-Matrix in 4.3).

### 4.3 Fehlercode-Matrix (HTTP-Status der `ApiOperationException`)

Verbindliche Zuordnung von Fehlersituation zu HTTP-Status in allen Factories, Buildern und Renderern
des Moduls:

| Situation | HTTP-Status |
|---|---|
| Client-Input fehlt (Pflichtfeld, leere ID-Liste, nicht übergebenes Parameter-Objekt) | `BAD_REQUEST` |
| Client-Input ungültig (Wert außerhalb Wertebereich, ungültige Enum-Bezeichnung) | `BAD_REQUEST` |
| Geschäfts-Voraussetzung verletzt (Schule ohne GOSt, Vorlage passt nicht zu Daten, Vorlage an dieser Schulform nicht vorgesehen) | `BAD_REQUEST` |
| Berechtigung fehlt | `FORBIDDEN` |
| Eine konkret per ID adressierte Einzel-Ressource existiert nicht (z. B. Stundenplan zur ID) | `NOT_FOUND` |
| Eine adressierte Einzel-Ressource existiert, ihre Daten sind aber nicht ladbar | `INTERNAL_SERVER_ERROR` |
| Der übergebene Schuljahresabschnitt gehört nicht zur Schule (auch der nicht gesetzte Wert `-1`) | `BAD_REQUEST` |
| Server-internes Problem (DB-Verbindung `null`, Template-Datei nicht lesbar, nicht implementierter Pfad) | `INTERNAL_SERVER_ERROR` |
| Interne Renderer-Ressource fehlt (Template-Engine, HTML-Template, Ressourcen-Root-Pfad, Schriftart) | `INTERNAL_SERVER_ERROR` |

Ergänzende Regeln:

- **Bulk-IDs** (Konkretisierung von G-2): Werden IDs im Request-Body übergeben und ein Eintrag
  liegt nicht vor, wird dieser Datensatz **ausgelassen**; der Report entsteht aus den übrigen. Der
  Befund wird als Ausgabeproblem gemeldet und höchstens mit `WARNING` protokolliert. Das gilt auch
  dann, wenn dadurch kein Datensatz übrig bleibt — eine leere Ausgabe ist eine gültige Antwort.
  `BAD_REQUEST` bleibt allein der **im Request leeren** ID-Liste vorbehalten: Dort hat der
  Aufrufer nichts angefordert, was etwas anderes ist als eine Liste, die erst durch das Auslassen
  leer wird. `NOT_FOUND` bleibt der einzelnen, direkt adressierten Ressource vorbehalten.
- **Die Schulform ist keine Berechtigungsfrage.** Eine Reportvorlage nennt die Schulformen, an denen
  sie genutzt werden darf; eine leere Liste gilt für alle. Passt die Schulform der Schule nicht dazu,
  ist das `BAD_REQUEST` und nicht `FORBIDDEN`: Die Schulform gehört der Schule und nicht dem
  Benutzer, niemand an dieser Schule darf die Vorlage ausgeben, und keine zusätzliche Kompetenz
  ändert daran etwas. Ein `403` schickte den Anwender eine Berechtigung suchen, die es nicht gibt.
  Lässt sich die Schulform der Schule nicht ermitteln, ist das dagegen `INTERNAL_SERVER_ERROR` — der
  Aufrufer hat daran keinen Anteil. Geprüft wird in der `HtmlFactory` neben den Kompetenzen, und nur
  dann, wenn die Vorlage überhaupt Schulformen nennt.
- **Interne Ressourcen sind niemals Client-Input:** Template-Engine, HTML-Template, Root-Pfad und
  Schriftarten baut der Server selbst auf; der Client benennt sie nicht. Ihr Fehlen ist deshalb
  weder `BAD_REQUEST` noch `NOT_FOUND`, sondern `INTERNAL_SERVER_ERROR`. Dasselbe gilt für alle
  Werte der Builder-Kontexte (statischer Dateiname, gerenderter Inhalt, Content-Type, Logger) und
  für die Metadaten der Reportvorlage — etwa den Typ eines Vorlage-Parameters, den die SOLL-Struktur
  vorgibt, während der Request nur den Wert liefert.
- **Catch-Blöcke:** Generische `catch (Exception e)`-Blöcke, die zu `INTERNAL_SERVER_ERROR`
  verpacken, brauchen davor einen eigenen Zweig `catch (ApiOperationException aoe)`, der die
  Exception mit ihrem ursprünglichen Status durchreicht — sonst werden 400/403/404 am API-Rand
  zu 500 verschluckt (Muster: `ReportingFactory`). Das gilt auch dann, wenn die eigene Meldung
  innerhalb desselben `try`-Blocks geworfen wird: Sonst ersetzt der allgemeine Catch sie durch seine
  eigene, unspezifische Meldung.
- **Ursache übergeben:** Wird eine unerwartete Exception neu verpackt, gehört sie als `cause` in
  die `ApiOperationException`. Für bewusst erzeugte Validierungsexceptions gilt das nicht — sie
  haben konstruktionsbedingt keine Ursache.
- **Status nicht verschärfen:** Ein Builder, der einen Renderer aufruft, reicht dessen Status
  weiter, statt ihn erneut zu verpacken. Die Fehlerquelle ist dort bereits bekannt, der Aufrufer
  kennt sie nicht besser.
- **Log-Level:** Zu einem Abbruch gehört ein Log-Eintrag mit `LogLevel.ERROR` (nicht `DEBUG`/`INFO`).
  Er entsteht an der Abschlussgrenze. Ein geworfener Fehler, den ein tolerierender Pfad auffängt, ist
  kein Abbruch und bekommt keinen — dort meldet die Fassade.
- **Wer protokolliert:** Ein Fehler wird **einmal an der Abschlussgrenze** protokolliert — dort, wo er
  abschließend behandelt und zur Fehlerantwort wird (Fehlervertrag, 4.2). Die Wurfstelle schreibt
  höchstens ihre technische Angabe. Eine Ebene dazwischen protokolliert nur dann, wenn sie einen
  Zusammenhang beiträgt, den die Wurfstelle nicht kennt — etwa den Dateinamen des betroffenen
  Dokuments in einer Sammelausgabe. Sie gibt
  dann **allein diesen Zusammenhang** aus und hängt nicht `e.getMessage()` an: Die Meldung der Quelle
  reist mit der Exception weiter und steht im Fehlerblock. Der Log-Block wird als
  `SimpleOperationResponse` an den Client ausgeliefert; jede Wiederholung steht dort ebenfalls.
  Für tolerierte Ausgabeprobleme protokolliert der Problemsammler den Block aus Fehlertyp,
  Ursachenkette und Stacktrace **einmal je Fehler-Instanz** — reist dieselbe Instanz mit einem
  weiteren Befund, erhält dessen Meldung nur einen Verweis auf den ersten Eintrag.

### 4.4 Öffentlicher Hinweisvertrag

Was intern gemeldet wird (4.2), erreicht den Aufrufer über den Response-Header
`SVWS-Reporting-Hinweise` und über die Beilage `HINWEISE.txt` im ZIP-Archiv. Beides ist Vertrag:

```http
SVWS-Reporting-Hinweise: v=1, angefordert=120, ausgegeben=117, hinweise=3, datensaetze=3
```

- **Den Header trägt jede erfolgreiche Dokumentantwort** — HTML, einzelne PDF-Datei und ZIP-Archiv.
  Die JSON-Startantwort des E-Mail-Versands trägt ihn nicht; sie liefert kein Dokument. Eine
  Fehlerantwort trägt ihn ebenfalls nie.
- **Gesetzt wird er an genau einer Stelle** (`ReportingHinweiseHeader`). Ein zweiter Ort für
  dieselbe Entscheidung ließe die Ausgabewege auseinanderlaufen.
- **Ein fehlender Header bedeutet „unbekannt"**, niemals „nachweislich vollständig". Der Download
  läuft ohne Meldung weiter.
- **Pflichtfelder sind `v`, `angefordert`, `ausgegeben` und `hinweise`**, alle als nicht negative
  ganze Zahlen. Zugesagt ist `ausgegeben <= angefordert`. Die Kategorien `datensaetze`, `angaben`
  und `darstellung` sind optional, erscheinen nur mit einem Wert größer als null, und ihre Summe
  ergibt stets `hinweise`. Die Reihenfolge der Felder ist fest, jedes Feld kommt höchstens einmal
  vor.
- **`hinweise` ist eine Diagnosegröße, keine Mengenangabe.** Die Zahl ist weder die Zahl fehlender
  Datensätze noch die fehlender Dokumente.
- **Die Differenz aus `angefordert` und `ausgegeben` ist nicht zwingend durch Hinweise erklärbar.**
  Ein Benutzerfilter verkleinert die Ausgabe, ohne ein Ausgabeproblem zu sein (siehe 4.2). Der
  Vertrag verspricht keine Arithmetik zwischen den Feldern.
- **Jeder Datenaufbau legt seine Zähleinheit fest und sichert sie mit einem Test ab.** Was eine
  Einheit ist — eine angeforderte ID, ein Schüler, ein Termin, ein Fach —, bestimmt der Aufbau; die
  Zahl der angeforderten Einheiten stammt dabei aus einer Quelle **vor** Filterung und Ladefehlern.
  Ohne gemeldeten Ausgabeumfang bricht der Aufbau mit `500` ab: Ein Header, dessen Zählwerte
  niemand ermittelt hat, bescheinigte eine geprüfte Vollständigkeit, die es nicht gibt.
- **Nach außen gelangen ausschließlich Zählwerte, Kategorie und Anzahl.** Weder IDs, Namen und
  Freitexte noch Fehlermeldungen oder Stacktraces verlassen den Server — Header und Beilage
  begleiten Dokumente, die weitergegeben und archiviert werden.
- **Erweiterungen sind nur additiv.** Ein Feld oder eine Kategorie zu entfernen, umzubenennen oder
  anders zu deuten erfordert eine neue Vertragsversion. Die Schlüssel sind Vertrag, die Namen der
  Enum-Konstanten sind es nicht. Unbekannte Einträge und Versionen ignoriert ein Verbraucher.
- **Die `HINWEISE.txt` entsteht im ZIP-Archiv**, sobald es etwas zu erklären gibt — bei mindestens
  einem Hinweis oder bei einer zulässig leeren Ausgabe. Sie ist der einzige Weg, auf dem die
  Hinweise den Anwender ohne Clientanpassung erreichen; besonders ein Archiv ohne PDF-Datei braucht
  sie. Sie nennt die Zählwerte, Anzahlen und lesbare Kategorien und unterliegt denselben
  Datenschutzgrenzen wie der Header.

**Auslieferungszustand (2026-08-20):** Der Header wird derzeit nur im Server-Modus `DEV` gesetzt;
seine Freigabe wartet auf die Entscheidung über die Auswertung im Webclient. Ermittlung, Prüfung
und interne Meldung laufen in jedem Modus. Das ist ein Zustand, keine Vertragsregel — er endet mit
der Freigabe, der Vertrag nicht.

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

## 7. Prüfung ausgabewirksamer Änderungen (Snapshot-Suite)

Das Projekt `tests/tests-server-reporting` erzeugt die Ausgaben gegen einen laufenden Server und
vergleicht sie mit hinterlegten Snapshots — die erzeugte HTML-Ausgabe, die Wirkung der
Katalog-Defaults und den Dateinamen je Reportvorlage. Damit bemerkt es unbeabsichtigte Änderungen
am Ausgabeinhalt, die Modultests nicht sehen. **Das gerenderte PDF prüft es nicht:** Seitenumbrüche,
Ränder und alles, was erst OpenHtmlToPdf entscheidet, bleiben eine Sichtprüfung.

**Wann die Suite läuft.** Bei jeder Änderung mit möglicher Auswirkung auf eine Ausgabe:

- Vorlagen und ihr Umfeld: `.html`, CSS, `.name.tpl`, Vorlagenparameter und deren Defaults;
- die Datenpipeline dahinter: Contexts und Initializer, Repositories, Reporting-Typen und Proxys,
  Dialekte, Builder, Renderer und Factories.

**Ein Lauf ist gültig, wenn er vollständig war und kein Fall übersprungen wurde.** Ein
übersprungener Fall bedeutet ein fehlendes Schema; der Lauf zählt dann nicht als Absicherung. Die
Suite prüft das selbst und meldet es als Fehlschlag — eine feste Testanzahl gehört deshalb nicht
in diese Regel.

**Gebraucht werden zwei Läufe:** einer vor dem ersten ausgabewirksamen Arbeitspaket als
Vergleichsbasis und einer nach jedem abgeschlossenen Paket, spätestens vor der Review. Ein davon
losgelöster Referenzlauf ist nicht nötig.

**Eine unerwartete Abweichung gilt bis zur fachlichen Klärung als Fehler.** Sie wird nicht durch
Aktualisieren der Snapshots beseitigt. Eine beabsichtigte Änderung der Ausgabe wird dagegen
fachlich geprüft, die Snapshots werden bewusst neu erzeugt, und die Abweichung wird in der Review
benannt.

Ausführung, Voraussetzungen und der gezielte Refresh einzelner Snapshots stehen in der README des
Testprojekts unter `tests/tests-server-reporting/tests/reporting/`.
