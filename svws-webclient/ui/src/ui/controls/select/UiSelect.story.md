## UiSelect
UiSelect ist eien Combobox, die zur Auswahl von vorgegebenen Option verwendet werden kann. Sie kann mit einfachen Datentypen wie Strings oder Numbers umgehen, aber auch mit komplexeren wie CoreTypes. Hierfür werden sogenannte `SelectManager` verwendet, von denen einige einfache bereits zur Verfügung stehen. Für komplexere Fälle können eigene SelectManager erstellt werden, die von den vorhandenen ableiten.

<details>
<summary>Inhalt</summary>

- [Props](#props)
- [Tastaturbedienung](#tastaturbedienung)
- [SelectManager](#selectmanager)
  - [SimpleSelectManager](#simpleselectmanager)
  - [CoreTypeSelectManager](#coretypeselectmanager)
- [Filter](#filter)
  - [SearchSelectFilter](#searchselectfilter)
  - [FachSelectFilter](#fachselectfilter)

</details>

### Props
Folgende Props können gesetzt werden, um die Komponente zu konfigurieren.

**label**
- **Typ**: `string`   
- **Default**: `""`   
- Das Label der Komponente

**selectManager**
- **Typ**: `BaseSelectManager<T>` (T ist der Typ der Optionen zB. `string`)
- Der `SelectManager`, der für die Logik der Komponente verantwortlich ist. Er stellt unter anderem die Optionen des Dropdowns zur Verfügung, speichert die aktuelle Selektion, definiert, ob die Komponente Multi-Selektionen zulässt etc. Für weitere Infos siehe [SelectManager](#selectmanager)

**searchable**
- **Typ**: `boolean`   
- **Default**: `false`   
- Definiert, ob die Optionen des Dropdowns durch Suchbegriffe gefiltert werden können.

**removable**
- **Typ**: `boolean`   
- **Default**: `true`   
- Definiert, ob ein Button zum Löschen der gesamten Selektion angezeigt werden soll

**disabled**
- **Typ**: `boolean`   
- **Default**: `false`   
- Definiert, ob die Komponente disabled ist

**statistics**
- **Typ**: `boolean`   
- **Default**: `false`   
- Definiert, ob die Komponente für die Statistik relevant ist

**minOptions**
- **Typ**: `number`   
- **Default**: `undefined`   
- Nur Multiselektion: Definiert, wie viele Optionen mindestens angegeben werden müssen. Wenn die Zahl größer ist als `maxOptions`, dann werden die Werte intern vertauscht. Negative Werte werden ignoriert.

**maxOptions**
- **Typ**: `number`   
- **Default**: `undefined`   
- Nur Multiselektion: Definiert, wie viele Optionen maximal angegeben werden dürfen. Wenn die Zahl kleiner ist als `minOptions`, dann werden die Werte intern vertauscht. Negative Werte werden ignoriert.

### Tastaturbedienung
Die Komponente kann mit der Tastatur bedient werden. Wichtig bei der Navigation durch das Dropdown ist, dass dabei nur der visuelle Fokus gesteuert wird. Der DOM-Fokus bleibt auf dem Input.

<details>
  <summary>Geschlossenes Dropdown</summary>

- **Alt + ↓ (Arrow Down):** Öffnet das Dropdown  
- **↓ (Arrow Down):** Öffnet das Dropdown und setzt den visuellen Fokus auf das erste Element  
- **↑ (Arrow Up):** Öffnet das Dropdown und setzt den Fokus auf das letzte Element  
- **Enter oder Leertaste:** Öffnet das Dropdown
- **Tab:** Fokussiert das nächste Element (nicht im Dropdown)
- **Home:** Öffnet das Dropdown und setzt den Fokus auf das erste Element  
- **End:** Öffnet das Dropdown und setzt den Fokus auf das letzte Element  
- **Escape:**
  - `searchable = false`: Nichts passiert 
  - `searchable = true`: Setzt den Suchtext zurück
- **Druckbare Zeichen:**
  - `searchable = false`: Öffnet das Dropdown und startet eine Suche nach passenden Einträgen. Wird dabei mehrfach derselbe Buchstabe gedrückt, dann wird immer der nächste Eintrag mit dem passenden Anfangsbuchstaben visuell fokussiert. Werden unterschiedliche Buchstaben innerhalb einer kurzen Zeit eingegebn, dann wird nach Einträgen gesucht, die mit dem gesamten eingegebenen Suchbegriff beginnen. Die Liste wird dabie *nicht* gefiltert wie bei `searchable = true`, sondern lediglich passende Einträge fokussiert
  - `searchable = true`: Öffnet das Dropdown und filtert die Optionen nach dem eingegebenen Suchbegriff. Nur einfache Suchen sind möglich, also keine Wildcards oder Ähnliches. Übereinstimmende Textstücke werden in den Optionen hervorgehoben.

</details>

<details>
  <summary>Geöffnetes Dropdown</summary>

- **Enter oder Leertaste:** 
  - Ein Element ist fokussiert: Wählt das Element aus (Falls `multi = false`: Schließt das Dropdown)
  - Kein Element ist fokussiert: Schließt das Dropdown
- **Tab:** Wählt das markierte Element aus und schließt das Dropdown  
- **Escape:** Schließt das Dropdown  
- **↓ (Arrow Down):** Springt zum nächsten Element in der Liste. Ist bereits das Letzte visuell fokussiert, dann springt es zum Letzten.
- **↑ (Arrow Up):** Springt zum vorherigen Element in der Liste. Ist bereits das Erste visuell fokussiert, dann springt es zum Letzten.
- **Alt + ↑ (Arrow Up):** Wählt das markierte Element
  - Falls `multi = false`: Schließt das Dropdown
- **→ (Arrow Right), ← (Arrow Left):** 
  - `searchable = false`: Nichts passiert
  - `searchable = true`: Entfernt den visuellen Fokus im Dropdown
- **Home:** 
  - `searchable = false`: Springt zum ersten Element in der Liste
  - `searchable = true`: Bewegt den Cursor im Suchtext an die erste Stelle
- **End:**
  - `searchable = false`: Springt zum letzten Element in der Liste
  - `searchable = true`: Bewegt den Cursor im Suchtext an die letzte Stelle
- **Page Up:** Springt 10 Elemente nach oben  
- **Page Down:** Springt 10 Elemente nach unten
- **Druckbare Zeichen:**
  - `searchable = false`: Startet eine Suche nach passenden Einträgen. Wird dabei mehrfach derselbe Buchstabe gedrückt, dann wird immer der nächste Eintrag mit dem passenden Anfangsbuchstaben visuell fokussiert. Werden unterschiedliche Buchstaben innerhalb einer kurzen Zeit eingegebn, dann wird nach Einträgen gesucht, die mit dem gesamten eingegebenen Suchbegriff beginnen. Die Liste wird dabie *nicht* gefiltert wie bei `searchable = true`, sondern lediglich passende Einträge fokussiert
  - `searchable = true`: Filtert die Optionen nach dem eingegebenen Suchbegriff. Nur einfache Suchen sind möglich, also keine Wildcards oder Ähnliches. Übereinstimmende Textstücke werden in den Optionen hervorgehoben. Außerdem wird der visuelle Fokus aus der Liste entfernt.
- **Escape:**
  - `searchable = false`: Das Dropdown schließt sich
  - `searchable = true`: Setzt den Suchtext zurück und das Dropdown schließt sich
</details>

## SelectManager
Der SelectManager beinhaltet die Logik der Komponente basierend auf dem Datentyp der Optionen in Dropdown. Er kümmert sich um folgende Punkte:
- speichert alle Optionen des Dropdowns
- speichert die aktuelle Selektion
- speichert aktive Filter und wendet diese an
- gibt an, ob es sich um eine Multi- oder Singleselektion Komponente handelt
- Gibt den Textinhalt für die Optionen im Dropdown zurück

SelectManager leiten alle von der abstrakten Klasse `BaseSelectManager` ab. Es stehen bereits einige einfache SelectManager zur Verfügung, die für simple Anwendungsfälle ausreichen. Für komplexere Fälle müssen eigene Manager geschrieben werden.

**Konstruktor**
- **multi (boolean)**: Gibt an, ob es sich um eine Multiselektion handelt
- **list (Iterable)**: Alle Optionen, die das Dropdown beinhaltet
- **selected (Iterable, optional)**: Definiert die bereits selektierten Einträge
- 
### SimpleSelectManager
Dieser Manager kann mit einfachen Optionen-Datentypen bestehend aus Numbers oder Strings umgehen. Er kann die Einträge der Optionen nach Suchtexten filtern. 

**Konstruktor**
- **multi (boolean)**: Gibt an, ob es sich um eine Multiselektion handelt
- **list (Iterable)**: Alle Optionen, die das Dropdown beinhaltet
- **selected (Iterable, optional)**: Definiert die bereits selektierten Einträge

### CoreTypeSelectManager
Dieser Manager akzeptiert einfach CoreTypes als Optionen und definiert, wie diese in der Liste angezeigt werden. Er filtert die Einträge nach Schuljahr und Schulform und kann zusätzlich noch nach Suchtexten filtern

**Konstruktor**
- **multi (boolean)**: Gibt an, ob es sich um eine Multiselektion handelt
- **clazz (class)**: Die Klasse des CoreTypes
- **schuljahr (number)**: Das Schuljahr, nach dem gefiltert wid
- **schulform (Schulform)**: Die Schulform, nach der gefiltert wird
- **selectionDisplayText (string | Funktion)**: Gibt an, wie die Selektion dargestellt werden soll. Es kann zwischen `text`, `kuerzel` und `kuerzelText` gewählt werden, falls vordefinierte Einstellungen gewünscht sind. Wenn eigene Definitionen verwendet werden sollen, dann können diese zum Beispiel wie folgt angegeben werden: ``(option) => `${option.id}: ${option.text}` ``
- **optionDisplayText (string | Funktion)**: Gibt an, wie die Optionen in der Dropdownliste dargestellt werden sollen. Es kann zwischen `text`, `kuerzel` und `kuezerlText` gewählt werden, falls vordefinierte Einstellungen gewünscht sind. Wenn eigene Definitionen verwendet werden sollen, dann können diese zum Beispiel wie folgt angegeben werden: ``(option) => `${option.id}: ${option.text}` ``

## Filter
Optionen im einem UiSelect können gefiltert werden. Damit mehrere Filter kombiniert werden können, existiert das Interface `SelectFilter`, auf dem basierend die Filter definiert werden. Diese Filter können anschließend an den SelectManager übergeben werden. \
Bei Änderungen an einem Filter wie beispielsweise an darin befindlichen Attributen, muss ein `selectManager.updateFilter([betroffener Filter])` aufgerufen werden, da der selectManager diese Änderungen andernfalls nicht registriert. \
Die angezeigten Optionen im Dropdown des Selects sind ausschließlich Optionen, die für alle angewendeten Filter valide sind.

### SearchSelectFilter
Ein spezieller Filter, der für die Filterung nach Suchbegriffen verwendet wird. Wird im UiSelect die prop `searchable = true` gesetzt, dann wird dieser Filter automatisch verwendet. Er kann aber auch mit anderen Suchbegriffen kombiniert werden.

**Konstruktor**
- **key (string)**: Ein eindeutiger Key zur Identifikation des Filters. Der Key muss nur innerhalb des SelectManagers eindeutig sein.
- **search (string)**: Der Suchbegriff, nach dem gefiltert werden soll. Es werden nur exakte Übereinstimmungen gefiltert (Groß- und Kleinschreibung irrelevant). Wildcards sind nicht erlaubt.
- **getText ((option: T) => string))**: Eine Funktion, die verwendet wird, um den Text der Optionen zu generieren. Dieser Text wird zum Vergleich mit dem Suchbegriff verwendet

### FachSelectFilter
Ein spezieller Filter, der Optionen vom Typ `Fach` nach `Fachgruppen` filtert. Es können mehrere Fachgruppen angegeben werden. Der Filter gibt dann alle Fächer zurück, die mit min. einer Fachgruppe übereinstimmen.

**Konstruktor**
- **key (string)**: Ein eindeutiger Key zur Identifikation des Filters. Der Key muss nur innerhalb des SelectManagers eindeutig sein.
- **fachgruppen (List<Fachgruppe>)**: Eine Liste der Fachgruppen, nach denen gefiltert wird.
- **schuljahr (number)**: Das dafür verwendet Schuljahr.