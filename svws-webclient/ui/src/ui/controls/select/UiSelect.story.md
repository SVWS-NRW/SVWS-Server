## UiSelect
UiSelect ist eien Combobox, die zur Auswahl von vorgegebenen Option verwendet werden kann. Sie kann mit einfachen Datentypen wie Strings oder Numbers umgehen, aber auch mit komplexeren wie CoreTypes. Hierfür werden sogenannte `SelectManager` verwendet, von denen einige einfache bereits zur Verfügung stehen. Für komplexere Fälle können eigene SelectManager erstellt werden, die von den vorhandenen ableiten.

<details>
<summary>Inhalt</summary>

- [Props](#props)
- [Tastaturbedienung](#tastaturbedienung)
- [SelectManager](#selectmanager)
  - [SimpleSelectManager](#simpleselectmanager)
  - [BaseSelectManager](#BaseSelectManager)
  - [CoreTypeSelectManager](#coretypeselectmanager)
- [Filter](#filter)
  - [SearchSelectFilter](#searchselectfilter)
  - [FachSelectFilter](#fachselectfilter)

</details>

### Props
Folgende Props können gesetzt werden, um die Komponente zu konfigurieren.

| Prop        | Typ                                               | Default     | Definition                                                                                                                                              |
|-------------|---------------------------------------------------|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| modelValue  | `any` | `null`      | Enthält die aktuelle Selektion. Einzelnes Objekt vom Typ \<T> bei Single-Selektion, `Iterable` bei Multi-Selektion                                                                                                                          |
| label       | `string`                                          | `""`        | Das Label der Komponente                                                                                                                                |
| manager     | `BaseSelectManager<T>`      | `BaseSelectManager<T>` mit Defaultwerten und ohne Optionen           | Der `SelectManager`, der für die Logik der Komponente verantwortlich ist. Stellt Optionen, Selektion und Multi-Selektionslogik bereit. [Mehr](#selectmanager) |
| searchable  | `boolean`                                         | `false`     | Definiert, ob die Optionen des Dropdowns durch Suchbegriffe gefiltert werden können                                                                    |
| disabled    | `boolean`                                         | `false`     | Definiert, ob die Komponente deaktiviert ist                                                                                                            |
| statistics  | `boolean`                                         | `false`     | Definiert, ob die Komponente für die Statistik relevant ist                                                                                             |
| headless    | `boolean`                                         | `false`     | Definiert, ob das Select headless (ohne Rahmen und Hintergrund) dargestellt wird, z. B. für Tabellen                                                    |
| minOptions  | `number`                                          | `undefined` | Nur bei Multiselektion: Definiert die minimale Anzahl an auszuwählenden Optionen. Wird mit `maxOptions` abgeglichen                                    |
| maxOptions  | `number`                                          | `undefined` | Nur bei Multiselektion: Definiert die maximale Anzahl an auswählbaren Optionen. Wird mit `minOptions` abgeglichen                                      |


### Tastaturbedienung
Die Komponente kann mit der Tastatur bedient werden. Wichtig bei der Navigation durch das Dropdown ist, dass dabei nur der visuelle Fokus gesteuert wird. Der DOM-Fokus bleibt auf dem Input.

<details>
  <summary>Geschlossenes Dropdown</summary>

| Eingabe                          | Aktion |
|----------------------------------|--------|
| Alt + ↓                          | Öffnet das Dropdown |
| ↓                                | Öffnet das Dropdown und setzt den visuellen Fokus auf das erste Element |
| ↑                                | Öffnet das Dropdown und setzt den Fokus auf das letzte Element |
| Enter oder Leertaste             | Öffnet das Dropdown |
| Tab                              | Fokussiert das nächste Element (nicht im Dropdown) |
| Home                             | Öffnet das Dropdown und setzt den Fokus auf das erste Element |
| End                              | Öffnet das Dropdown und setzt den Fokus auf das letzte Element |
| Escape (searchable = false)      | Nichts passiert |
| Escape (searchable = true)       | Setzt den Suchtext zurück |
| Druckbare Zeichen (searchable = false) | Öffnet das Dropdown und startet eine Suche nach passenden Einträgen. Mehrfach derselbe Buchstabe: nächster passender Eintrag. Unterschiedliche Buchstaben kurz nacheinander: Suche nach Begriff mit diesen Anfangsbuchstaben. Liste wird nicht gefiltert, sondern passende Einträge fokussiert. |
| Druckbare Zeichen (searchable = true) | Öffnet das Dropdown und filtert die Optionen nach dem eingegebenen Suchbegriff. Nur einfache Suchen möglich. Übereinstimmende Textstücke werden hervorgehoben. |


</details>
<details>
  <summary>Geöffnetes Dropdown</summary>

| Eingabe                          | Aktion |
|----------------------------------|--------|
| Enter oder Leertaste (Element fokussiert) | Wählt das Element aus (Falls `multi = false`: Schließt das Dropdown) |
| Enter oder Leertaste (kein Element fokussiert) | Schließt das Dropdown |
| Tab                              | Wählt das markierte Element aus und schließt das Dropdown |
| Escape                           | Schließt das Dropdown |
| ↓                                | Springt zum nächsten Element in der Liste. Ist bereits das Letzte visuell fokussiert, dann springt es zum Letzten. |
| ↑                                | Springt zum vorherigen Element in der Liste. Ist bereits das Erste visuell fokussiert, dann springt es zum Letzten. |
| Alt + ↑                          | Wählt das markierte Element. Falls `multi = false`: Schließt das Dropdown |
| → / ← (`searchable = false`)       | Nichts passiert |
| → / ← (`searchable = true`)        | Entfernt den visuellen Fokus im Dropdown |
| Home (searchable = false)        | Springt zum ersten Element in der Liste |
| Home (`searchable = true`)         | Bewegt den Cursor im Suchtext an die erste Stelle |
| End (searchable = false)         | Springt zum letzten Element in der Liste |
| End (`searchable = true`)          | Bewegt den Cursor im Suchtext an die letzte Stelle |
| Page Up                          | Springt 10 Elemente nach oben |
| Page Down                        | Springt 10 Elemente nach unten |
| Druckbare Zeichen (searchable = false) | Startet eine Suche nach passenden Einträgen. Mehrfach derselbe Buchstabe: nächster passender Eintrag. Unterschiedliche Buchstaben kurz nacheinander: Suche nach Begriff mit diesen Anfangsbuchstaben. Liste wird nicht gefiltert, sondern passende Einträge fokussiert. |
| Druckbare Zeichen (searchable = true) | Filtert die Optionen nach dem eingegebenen Suchbegriff. Nur einfache Suchen möglich. Übereinstimmende Textstücke werden hervorgehoben. Visueller Fokus wird entfernt. |
| Escape (`searchable = false`)      | Das Dropdown schließt sich |
| Escape (`searchable = true`)       | Setzt den Suchtext zurück und das Dropdown schließt sich |


</details>

## SelectManager
Der SelectManager beinhaltet die Logik der Komponente basierend auf dem Datentyp der Optionen in Dropdown. Er kümmert sich um folgende Punkte:
- speichert alle Optionen des Dropdowns
- speichert die aktuelle Selektion
- speichert aktive Filter und wendet diese an
- gibt an, ob es sich um eine Multi- oder Singleselektion Komponente handelt
- gibt an, ob eine Selektion gelöscht werden kann
- Gibt den Textinhalt für die Optionen und die Selektion im Dropdown zurück

`SelectManager` leiten alle von der Klasse `BaseSelectManager` ab.

### BaseSelectManager\<T>
Mit dem `BaseSelectManager<T>` lassen sich alle einfachen Selects darstellen. Hierfür muss dem Konstruktor eine `BaseSelectManagerConfig<T>` übergeben werden, die alle Grundeinstellungen vornimmt. Die Übergabe einer Config ist optional. Wird keine übergeben, dann wird in dem Select eine leere Liste für die Optionen angezeigt.

#### BaseSelectManagerConfig\<T>

Alle Parameter der Config sind Optional. Wenn eine leere Config an den `SelectManager` übergeben wird, dann erzeugt dieser für die Optionen eine leere Liste.
| Parameter | Typ             | Definition                                               |
|-----------|------------------|----------------------------------------------------------|
| options   | `Iterable<T>` |  Alle Optionen, die das Dropdown beinhaltet.              |
| multi     | `boolean`           | Gibt an, ob es sich um eine Multiselektion handelt.      |
| selected | `any` | Definiert die bereits selektierten Einträge. Bei einer Multi-Selektion muss es ein `Iterable` sein, andernfalls eine einzelne Option |
| removable | `boolean` | Definiert, ob das Löschen von Selektionen erlaubt sein soll. |
| sort | `Comparator<T>` \| `((a: T, b: T) => number)` \| `null`| Gibt eine Sortierfunktion an |
| filters | `Iterable<SelectFilter<T>>` | Setzt die aktiven Filter für die Optionenliste |
| selectionDisplayText | `string` \| `((option: any) => string)` | Gibt an, wie der Selektionstext angezeigt werden soll |
| optionDisplayText | `string` \| `((option: any) => string)` | Gibt an, wie der Optionentext in Dropdown angezeigt werden soll |
| deepSearchAttributes | `string[]` | Definiert Attribute von \<T>, die bei einem gesetzte Suchfilter durchsucht werden sollen. Werden keine Attribute festgelegt, dann werden ausschließlich die generierten Optionstexte für die Suche verwendet. |

### CoreTypeSelectManager\<T>
Dieser Manager generiert seine Liste an Optionen selbst, basierend auf Angaben zum `CoreType`, dem Schuljahr und den Schulformen. Hierfür muss dem Konstruktor eine `CoreTypeSelectManagerConfig<T>` übergeben werden, die alle Grundeinstellungen vornimmt. Die Übergabe einer Config ist optional. Wird keine übergeben, dann wird in dem Select eine leere Liste für die Optionen angezeigt.

#### CoreTypeSelectManagerConfig\<T>
Diese Config erweitert die `BaseSelectManagerConfig`, implementiert aber nicht den Parameter `options`, da diese selbst berechnet werden. Für diese Berechnung ist Mindestens die Angabe von `clazz` und `schuljahr` erforderlich. Ohne diese bleibt die Optionenliste leer. Alle Parameter der Config sind Optional. Wenn eine leere Config an den `SelectManager` übergeben wird, dann erzeugt dieser für die Optionen eine leere Liste.
| Parameter | Typ             | Definition                                               |
|-----------|------------------|----------------------------------------------------------|
| clazz   | `Class<T>` |  Die Klasse des CoreTypes           |
| schuljahr     | `number \| null`           | Das Schuljahr, nach dem gefiltert wird. Bei null erhält die Komponente nur eine leere Liste an Optionen.      |
| schulformen | `Schulform \| Schulform[] \| null` | Die Schulformen, nach denen gefiltert wird. Sie beinhaltet alle Daten, die in mindestens einer der Schulformen vorkommen. Bei `null` werden alle Daten unabhägig der Schulform geladen |
| selectionDisplayText | `"kuerzel" \| "text" \| "kuerzelText" \| ((option: U) => string)` | Gibt an, wie der Selektionstext angezeigt werden soll. `kuerzel`, `text` und `kuerzelText` sind defaults, die verwendet werden können.  |
| optionDisplayText | `"kuerzel" \| "text" \| "kuerzelText" \| ((option: U) => string)` | Gibt an, wie der Optionentext in Dropdown angezeigt werden soll. `kuerzel`, `text` und `kuerzelText` sind defaults, die verwendet werden können. |

## Filter
Optionen im einem UiSelect können gefiltert werden. Damit mehrere Filter kombiniert werden können, existiert das Interface `SelectFilter`, auf dem basierend die Filter definiert werden. Diese Filter können anschließend an den `SelectManager` übergeben werden. \
Bei Änderungen an einem Filter wie beispielsweise an darin befindlichen Attributen, muss `manager.updateFilteredOptions (filter?: SelectFilter<T>, remove: boolean = false)` aufgerufen werden, da der Manager diese Änderungen andernfalls nicht registriert. \
Die angezeigten Optionen im Dropdown des Selects sind ausschließlich Optionen, die für alle angewendeten Filter valide sind.

### SearchSelectFilter
Ein spezieller Filter, der für die Filterung nach Suchbegriffen verwendet wird. Wird im UiSelect die prop `searchable = true` gesetzt, dann wird dieser Filter automatisch verwendet. 
Es ist außerdem eine Tiefensuche möglich. Dabei wird der Suchbegriff nicht nur mit dem Optiontext verglichen, sondern auch mit den Inhalten von angegebenene Attributen.
Diese Suche wird aktiviert, indem auf dem `SelectManager` `setDeepSearchAttributes(['attributeName'])` aufgerufen wird. Dabei werden alle Attributnamen übergeben, 
die zusätzlich durchsucht werden sollen. Alternativ klnnen die Attribute auch direkt im `SearchSelectFilter` gesetzt werden, falls dieser selbst an den SelectManager
übergeben wird.

**Konstruktor**
| Parameter | Typ             | Definition                                               |
|-----------|------------------|----------------------------------------------------------|
| key   | `string` |  Ein eindeutiger Key zur Identifikation des Filters. Der Key muss nur innerhalb des SelectManagers eindeutig sein.          |
| search   | `string` | Der Suchbegriff, nach dem gefiltert werden soll. Es werden nur exakte Übereinstimmungen gefiltert (Groß- und Kleinschreibung irrelevant). Wildcards sind nicht erlaubt.          |
| getText   | `(option: T) => string` |  Eine Funktion, die verwendet wird, um den Text der Optionen zu generieren. Dieser Text wird zum Vergleich mit dem Suchbegriff verwendet          |
| deepSearchAttributes | `string[]` | Attribute der Option, die bei einer Suche ebenfalls durchsucht werden sollen, auch wenn sie nicht im Optionentext im Dropdown stehen. |

### FachSelectFilter
Ein spezieller Filter, der Optionen vom Typ `Fach` nach `Fachgruppen` filtert. Es können mehrere Fachgruppen angegeben werden. Der Filter gibt dann alle Fächer zurück, die mit min. einer Fachgruppe übereinstimmen.

**Konstruktor**
| Parameter | Typ             | Definition                                               |
|-----------|------------------|----------------------------------------------------------|
| key   | `string` |  Ein eindeutiger Key zur Identifikation des Filters. Der Key muss nur innerhalb des SelectManagers eindeutig sein.          |
| fachgruppen   | `List<Fachgruppe>` |  Eine Liste der Fachgruppen, nach denen gefiltert wird.          |
| schuljahr   | `number` | Das dafür verwendet Schuljahr.          |