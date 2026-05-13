## [Name des Obervalidators]

**Spezifikation:** [JSON-Link] 

**Java-Code:** [JavaCode-Link]

### Standardvalidatoren

#### Allgemein

- [ ] Required
- [ ] Unique
  * Geprüfte Menge:
- [ ] Trailing/Leading Whitespaces
- [ ] Whitespaces

#### Strings

- [ ] min. Zeichenlänge
  * min:
- [ ] max. Zeichenlänge
  * max:
- [ ] Email-Format
  * Format:
- [ ] Telefonnummer-Format
  * Format:
- [ ] Straße
  * Kriterien:

#### Datum

- [ ] frühstes Datum
  * Datum:
- [ ] spätestes Datum
  * Datum:

#### Number

- [ ] min. Zahl
  * min:
- [ ] max. Zahl
  * max:
- [ ] nur ganze Zahlen
- [ ] max. Nachkommastellen
  * max:

#### Multi-Selects

- [ ] min. Anzahl an Selektionen
  * min:
- [ ] max. Anzahl an Selektionen
  * max:

---

## Hinweis zu SVWS-Client-Standardvalidatoren

Im SVWS-Client werden für einige typische Fälle Standardvalidatoren verwendet. Um zu vermeiden, dass die Validierung bereits von den ASD-Validatoren abgedeckt und vom SVWS-Client dann nochmals validiert wird, werden von IT-NRW die Infos benötigt, welche der Standardvalidierungen bereits selbst vorgenommen werden. Eine Liste der Standardvalidatoren, was sie validieren und welche Zusatzinfos von IT-NRW zu deren eigenen Abdeckung benötigt werden, ist aus der folgenden Liste zu entnehmen.

<details>
<summary>Hinweise zu einzelnen Standardvalidatoren</summary>

### Allgemein

- **Required**: Prüft, ob das Pflichtfeld befüllt ist.
- **Unique**: Prüft, ob der Inhalt des Felds in einer vorgegebenen Menge schon existiert.\
  → Benötigte Infos: Die Menge, auf die geprüft wird.
- **Trailing/Leading Spaces**: Prüft, ob nicht erlaubte führende/nachgestellte Leerzeichen vorhanden sind.
- **Whitespaces**: Prüft, ob nicht erlaubte Leerzeichen vorhanden sind. Prüft auch Leading/Trailing Spaces.

### Strings

- **Min. Zeichenlänge**: Prüft, ob die minimale Anzahl an Zeichen eingegeben wurde. Bei 1 wird stattdessen der Required-Validator verwendet.\
  → Benötigte Infos: Die minimale Zeichenlänge, die erforderlich ist.
- **Max. Zeichenlänge**: Prüft, ob die maximale Anzahl an Zeichen überschritten wurde.\
  → Benötigte Infos: Die maximale Zeichenlänge, die erlaubt ist.
- **Email-Format**: Prüft, ob die eingegebene Email einem gültigen Format entspricht.\
  → Benötigte Infos: Das geprüfte Format.
- **Telefonnummer-Format**: Prüft, ob die eingegebene Telefonnummer einem gültigen Format entspricht.\
  → Benötigte Infos: Das geprüfte Format.
- **Straße**: Prüft, ob die Teilangaben (Straßenname, Hausnummer, Hausnummerzusatz) die maximale Zeichenlänge überschreiten.\
  → Benötigte Infos: Geprüfte Zeichenlängen.

### Datum

- **Frühstes Datum**: Prüft, ob das eingegebene Datum vor dem frühsten erlaubten Datum liegt.\
  → Benötigte Infos: Das frühste zulässige Datum.
- **Spätestes Datum**: Prüft, ob das eingegebene Datum nach dem spätesten erlaubten Datum liegt.\
  → Benötigte Infos: Das späteste zulässige Datum.

### Number

- **Min. Zahl**: Prüft, ob die eingegebene Zahl mindestens dem erlaubten Wert entspricht.\
  → Benötigte Infos: Die definierte Untergrenze der einzugebenden Zahl.
- **Max. Zahl**: Prüft, ob die eingegebene Zahl maximal dem erlaubten Wert entspricht.\
  → Benötigte Infos: Die definierte Obergrenze der einzugebenden Zahl.
- **Nur ganze Zahlen**: Prüft, ob bei nur erlaubten ganzen Zahlen keine Kommazahl eingegeben wurde.
- **Max. Nachkommastellen**: Prüft, ob die maximale Anzahl an Nachkommastellen überschritten wurde.\
  → Benötigte Infos: Maximal erlaubte Anzahl an Nachkommastellen.

### Multi-Selects

- **Min. Anzahl an Selektionen**: Prüft, ob die Anzahl der selektierten Einträge mindestens der vorgegebenen Anzahl entspricht.\
  → Benötigte Infos: Die minimale Anzahl an Selektionen.
- **Max. Anzahl an Selektionen**: Prüft, ob die Anzahl der selektierten Einträge maximal der vorgegebenen Anzahl entspricht.\
  → Benötigte Infos: Die maximale Anzahl an Selektionen.

</details>




/status "Ready For Refinement"

/label Tickettyp::Story

/label init

/label ASD::*