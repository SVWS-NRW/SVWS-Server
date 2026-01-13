# UiSelectMulti
UiSelectMulti ist eine Combobox, die zur Selektion mehrerer Optionen aus mehreren vorgegebenen verwendet werden kann. Sie kann mit einfachen Datentypen wie Strings oder Numbers umgehen, aber auch mit komplexeren wie CoreTypes. Hierfür werden Manager verwendet, von denen einige einfache bereits zur Verfügung stehen. Für komplexere Fälle können eigene Manager erstellt werden, die von den vorhandenen ableiten.

<details>
<summary>Inhalt</summary>

- [Props](#props)
- [Fokusklassen](#fokusklassen)
- [Tastaturbedienung](#tastaturbedienung)
  - [Geschlossenes Dropdown](#geschlossenes-dropdown)
  - [Geöffnetes Dropdown](#geöffnetes-dropdown)
- [Manager](#manager)
  - [BaseSelectManager](#baseselectmanager)
  - [BaseSelectManagerConfig](#baseselectmanagerconfig)
  - [SelectManager](#selectmanager-1)
  - [SelectManagerConfig](#selectmanagerconfig)
  - [CoreTypeSelectManager](#coretypeselectmanager)
  - [CoreTypeSelectManagerConfig](#coretypeselectmanagerconfig)
- [Filter](#filter)
  - [FachSelectFilter](#fachselectfilter)
    - [Konstruktor](#konstruktor)

</details>

## Props
Folgende Props können gesetzt werden, um die Komponente zu konfigurieren.

| Prop                 | Typ                                                             | Default                                                | Definition                                                                                                                                           |
|----------------------|-----------------------------------------------------------------|--------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| modelValue           | `Iterable<T>` \| `null`                                         | `null`                                                 | Enthält die aktuelle Selektion.                                                                                                                      |
| label                | `string`                                                        | `""`                                                   | Das Label der Komponente                                                                                                                             |
| manager              | `BaseSelectManager<T>`                                          | `SelectManager<T>` mit Defaultwerten und ohne Optionen | Der Manager, der für die Logik der Komponente verantwortlich ist. [Mehr](#manager)                                                                   |
| searchable           | `boolean`                                                       | `false`                                                | Definiert, ob die Optionen des Dropdowns durch Suchbegriffe gefiltert werden können                                                                  |
| deepSearchAttributes | `string[]`                                                      | `[]`                                                   | Definiert Attribute in den Optionen, die bei der Suche zusätzlich berücksichtigt werden sollen, auch wenn diese nicht im Optionentext auftauchen.    |
| required             | `boolean`                                                       | `false`                                                | Definiert, ob das Select ein Pflichtfeld ist.                                                                                                        |
| removable            | `boolean`                                                       | `true`                                                 | Definiert, ob eine Selektion gelöscht werden kann. Das Löschen ist dann nur für einzelne Elemente möglich, außer es ist das letzte in der Selektion. |
| nullable             | `boolean`                                                       | `true`                                                 | Definiert, ob für das model `null` oder `undefined` übergeben werden darf.                                                                           |
| disabled             | `boolean`                                                       | `false`                                                | Definiert, ob die Komponente deaktiviert ist                                                                                                         |
| readonly             | `boolean`                                                       | `false`                                                | Definiert, ob die Komponente schreibgeschützt ist                                                                                                    |
| statistics           | `boolean`                                                       | `false`                                                | Definiert, ob die Komponente für die Statistik relevant ist                                                                                          |
| headless             | `boolean`                                                       | `false`                                                | Definiert, ob das Select headless (ohne Rahmen und Hintergrund) dargestellt wird, z. B. für Tabellen                                                 |                            |
| minOptions           | `number`                                                        | `undefined`                                            | Definiert die minimale Anzahl an auszuwählenden Optionen. Wird mit `maxOptions` abgeglichen                                                          |
| maxOptions           | `number`                                                        | `undefined`                                            | Definiert die maximale Anzahl an auswählbaren Optionen. Wird mit `minOptions` abgeglichen                                                            |
| validator            | `() => Validator`                                               | `undefined`                                            | Ein Statistik-Validator, der auf Fehler für die Statistik prüft.                                                                                     |
| doValidate           | `(validator: Validator, value: Iterable<T> \| null) => boolean` | `(validator: Validator) : boolean => validator.run()`  | Die Funktion, die zur Validierung ausgeführt wird. `value` beinhaltet die aktuelle Selektion, die validiert wird.                                    |




## Fokusklassen
Wie bei anderen Inputs auch kann am UiSelectMulti eine Fokusklasse gesetzt werden, entweder `contentFocusField` oder `subNavigationFocusField`. Diese Zuordnung erfolgt über `<ui-select-multi [...] class="contentFocusField"` bzw. `<ui-select [...] class="subNavigationFocusField"`. Je nachdem ob es sich bei dem Select um ein searchable Select handelt, wird die Fokusklasse dann intern an die Combobox (searchable = false) oder an das SearchInput (searchable = true) weitergeleitet und gesetzt. Bei `contentFocusField` ist das Select über `Alt` + `8` ansteuerbar.

## Tastaturbedienung
Die Komponente kann mit der Tastatur bedient werden. Wichtig bei der Navigation durch das Dropdown ist, dass dabei nur der visuelle Fokus gesteuert wird. Der DOM-Fokus bleibt auf dem Input.

<details>
  <summary>Geschlossenes Dropdown</summary>

| Eingabe           | Aktion                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
|-------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Enter / Leertaste | Öffnet das Dropdown                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| ↓                 | Öffnet das Dropdown und hebt die erste Option hervor                                                                                                                                                                                                                                                                                                                                                                                                                               |
| Alt + ↓           | Öffnet das Dropdown                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| ↑                 | Öffnet das Dropdown und hebt die letzte Option hervor                                                                                                                                                                                                                                                                                                                                                                                                                              |
| Alt + ↑           | Öffnet das Dropdown                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| Tab               | Fokussiert das nächste Element im DOM (nicht im Dropdown)                                                                                                                                                                                                                                                                                                                                                                                                                          |
| Home              | Öffnet das Dropdown und bei `searchable = false` setzt den Fokus auf die erste Option                                                                                                                                                                                                                                                                                                                                                                                              |
| End               | Öffnet das Dropdown und bei `searchable = false` setzt den Fokus auf die letzte Option                                                                                                                                                                                                                                                                                                                                                                                             |
| Escape            | `searchable = true`: Setzt den Suchtext zurück                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| Druckbare Zeichen | `searchable = false`: Öffnet das Dropdown und startet eine Suche nach passenden Einträgen. Mehrfach derselbe Buchstabe: nächster passender Eintrag. Unterschiedliche Buchstaben kurz nacheinander: Suche nach Begriff mit diesen Anfangsbuchstaben. Liste wird nicht gefiltert, sondern passende Einträge fokussiert. <br> `searchable = true`:  Öffnet das Dropdown und filtert die Optionen nach dem eingegebenen Suchbegriff. Übereinstimmende Textstücke werden hervorgehoben. |


</details>
<details>
  <summary>Geöffnetes Dropdown</summary>

| Eingabe           | Aktion                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Enter             | Selektiert die fokussierte Option (falls eine fokussiert ist)                                                                                                                                                                                                                                                                                                                                                                                                      |
| Leertaste         | `searchable = false`: Selektiert die fokussierte Option (falls eine fokussiert ist)                                                                                                                                                                                                                                                                                                                                                                                |
| Escape            | `searchable = false`: Schließt das Dropdown <br> `searchable = true`: Schließt das Dropdown und löscht den Suchtext                                                                                                                                                                                                                                                                                                                                                |
| ↓                 | Fokussiert die nächste Option in der Liste. Ist bereits die Letzte fokussiert, dann wird die erste Option fokussiert.                                                                                                                                                                                                                                                                                                                                              |
| Alt + ↓           | Wählt die fokussierte Option. Schließt das Dropdown                                                                                                                                                                                                                                                                                                                                                                                                                |
| ↑                 | Fokussiert die vorherige Option in der Liste. Ist bereits die Erste fokussiert, dann wird die letzte Option fokussiert.                                                                                                                                                                                                                                                                                                                                            |
| Alt + ↑           | Wählt die fokussierte Option. Schließt das Dropdown                                                                                                                                                                                                                                                                                                                                                                                                                |
| Tab               | Schließt das Dropdown und fokussiert das nächste Element im DOM (nicht im Dropdown)                                                                                                                                                                                                                                                                                                                                                                                |
| Home              | `searchable = false`: Fokussiert die erste Option in der Liste <br> `searchable = true`: Bewegt den Cursor im Suchtext an die erste Stelle                                                                                                                                                                                                                                                                                                                         |
| End               | `searchable = false`: Fokussiert die letzte Option in der Liste <br> `searchable = true`: Bewegt den Cursor im Suchtext an die letzte Stelle                                                                                                                                                                                                                                                                                                                       |
| PageUp            | `searchable = false`: Setzt den Fokus 10 Optionen nach oben <br> `searchable = true`: Bewegt den Cursor im Suchtext an die erste Stelle                                                                                                                                                                                                                                                                                                                            |
| PageDown          | `searchable = false`: Setzt den Fokus 10 Optionen nach unten <br> `searchable = true`: Bewegt den Cursor im Suchtext an die letzte Stelle                                                                                                                                                                                                                                                                                                                          |
| Druckbare Zeichen | `searchable = false`: Startet eine Suche nach passenden Einträgen. Mehrfach derselbe Buchstabe: nächster passender Eintrag. Unterschiedliche Buchstaben kurz nacheinander: Suche nach Begriff mit diesen Anfangsbuchstaben. Liste wird nicht gefiltert, sondern passende Einträge fokussiert. <br> `searchable = true`: Filtert die Optionen nach dem eingegebenen Suchbegriff. Übereinstimmende Textstücke werden hervorgehoben. Fokus im Dropdown wird entfernt. |


</details>

## Manager
Manager übernehmen die Logik des Selects. Sie kümmern sich um folgende Punkte:
- speichert alle (gefilterten und ungefilterten) Optionen des Dropdowns
- speichert aktive Filter und wendet diese an
- sortiert die Optionen
- Gibt den Textinhalt für die Optionen und die Selektion im Dropdown zurück
- reagiert auf Änderungen aus der Config, falls die Attribute darin als Ref angegeben wurden

### BaseManager\<T>
`BaseSelectManager<T>` ist die abstrakte Basisklasse für alle Selects. Um einen Manager zu erzeugen, muss dem Konstruktor eine `BaseSelectManagerConfig<T>` übergeben werden, die alle Grundeinstellungen vornimmt. Die Übergabe einer Config ist optional. Wird keine übergeben, dann wird in dem Select eine leere Liste für die Optionen angezeigt.

#### BaseSelectManagerConfig\<T>
Alle Parameter der Config sind optional und können entweder direkt angegeben werden oder in einem Ref. Werden sie in einem Ref angegeben, dann reagierte der Manager auf Änderungen dieses Refs und aktualisiert sich automatisch.

| Parameter | Typ                                                       | Definition                                                                                                                             |
|-----------|-----------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| options   | `MaybeRef<Iterable<T>>`                                   | Alle Optionen, die das Dropdown beinhaltet. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben.     |
| sort      | `MaybeRef<Comparator<T>\|((a: T, b: T) => number)\|null>` | Gibt eine Sortierfunktion an. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben.                   |
| filters   | `MaybeRef<Iterable<SelectFilter<T>>>`                     | Setzt die aktiven Filter für die Optionenliste. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben. |

### SelectManager\<T>
Der `SelectManager<T>` ist ein einfacher, allgemeiner Manager für Selects, der für die verschiedensten Datentypen wie `string`, `number` oder auch Custom-Objekte verwendet werden kann. Er beinhaltet die Logik der Komponente basierend auf dem Datentyp der Optionen in Dropdown.

Für das Erzeugen eines SelectManagers wird dem Konstruktor eine `SelectManagerConfig\<T>` übergeben. Wird das nicht getan, dann wird ein Select ohne Optionen erzeugt.

#### SelectManagerConfig\<T>
Leitet von der Config `BaseSelectManagerConfig\<T>` ab und erweitert sie. Alle Parameter der Config sind optional und können entweder direkt angegeben werden oder in einem Ref. Werden sie in einem Ref angegeben, dann reagierte der Manager auf Änderungen dieses Refs und aktualisiert sich automatisch.

| Parameter            | Typ                                 | Definition                                                                                                                                              |
|----------------------|-------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| selectionDisplayText | `MaybeRef<((option: U) => string)>` | Gibt an, wie der Selektionstext angezeigt werden soll. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben.           |
| optionDisplayText    | `MaybeRef<((option: U) => string)>` | Gibt an, wie der Optionentext in Dropdown angezeigt werden soll. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben. |


### CoreTypeSelectManager\<T>
Dieser Manager generiert seine Liste an Optionen selbst, basierend auf Angaben zum `CoreType`, dem Schuljahr und den Schulformen. Hierfür muss dem Konstruktor eine `CoreTypeSelectManagerConfig<T>` übergeben werden, die alle Grundeinstellungen vornimmt. Die Übergabe einer Config ist optional. Wird keine übergeben, dann wird in dem Select eine leere Liste für die Optionen angezeigt.

### CoreTypeSelectManagerConfig\<T>
Diese Config erweitert die `BaseSelectManagerConfig<T>`, implementiert aber nicht den Parameter `options`, da diese selbst berechnet werden. Die Optionen der Liste sind dabei vom Typ `CoreTypeData`. Für diese Berechnung ist mindestens die Angabe von `clazz` und `schuljahr` erforderlich. Ohne diese bleibt die Optionenliste leer. Alle Parameter der Config sind optional und können entweder direkt angegeben werden oder in einem Ref. Werden sie in einem Ref angegeben, dann reagierte der Manager auf Änderungen dieses Refs und aktualisiert sich automatisch.

| Parameter            | Typ                                                                         | Definition                                                                                                                                                                                                                                                                     |
|----------------------|-----------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| clazz                | `MaybeRef<Class<T>>`                                                        | Die Klasse des CoreTypes. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben.                                                                                                                                                               |
| schuljahr            | `MaybeRef<number \| null>`                                                  | Das Schuljahr, nach dem gefiltert wird. Bei null erhält die Komponente nur eine leere Liste an Optionen. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben.                                                                                |
| schulformen          | `MaybeRef<Schulform \| Schulform[] \| null>`                                | Die Schulformen, nach denen gefiltert wird. Sie beinhaltet alle Daten, die in mindestens einer der Schulformen vorkommen. Bei `null` werden alle Daten unabhägig der Schulform geladen. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben. |
| selectionDisplayText | `MaybeRef<"kuerzel" \| "text" \| "kuerzelText" \| ((option: U) => string)>` | Gibt an, wie der Selektionstext angezeigt werden soll. `kuerzel`, `text` und `kuerzelText` sind defaults, die verwendet werden können. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben.                                                  |
| optionDisplayText    | `MaybeRef<"kuerzel" \| "text" \| "kuerzelText" \| ((option: U) => string)>` | Gibt an, wie der Optionentext in Dropdown angezeigt werden soll. `kuerzel`, `text` und `kuerzelText` sind defaults, die verwendet werden können. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben.                                        |

## Filter
Optionen in einem UiSelect können gefiltert werden. Damit mehrere Filter kombiniert werden können, existiert das Interface `SelectFilter`, auf dem basierend die Filter definiert werden. Diese Filter können anschließend an den `Manager` übergeben werden. \
Veränderbare Attribute eines Filters sollten als `ShallowRef` implementiert werden, damit auf deren Änderungen reagiert werden kann. Andernfalls muss bei Änderungen an einem Filter `manager.updateFilteredOptions (filter?: SelectFilter<T>, remove: boolean = false)` aufgerufen werden, da der Manager diese Änderungen andernfalls nicht registriert. \
Die angezeigten Optionen im Dropdown des Selects sind ausschließlich Optionen, die für alle angewendeten Filter valide sind.

### FachSelectFilter
Ein spezieller Filter, der Optionen vom Typ `Fach` nach `Fachgruppen` filtert. Es können mehrere Fachgruppen angegeben werden. Der Filter gibt dann alle Fächer zurück, die mit min. einer Fachgruppe übereinstimmen.
#### Konstruktor
| Parameter   | Typ                          | Definition                                                                                                                                    |
|-------------|------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| key         | `string`                     | Ein eindeutiger Key zur Identifikation des Filters. Der Key muss nur innerhalb des Managers eindeutig sein.                                   |
| fachgruppen | `MaybeRef<List<Fachgruppe>>` | Eine Liste der Fachgruppen, nach denen gefiltert wird. <br/>Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben. |
