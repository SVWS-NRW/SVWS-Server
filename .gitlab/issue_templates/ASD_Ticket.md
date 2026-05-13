<table>
  <tr>
    <th>Obervalidator</th>
    <th>Spezifikation</th>
    <th>Java-Code</th>
    <th colspan="3">Standardvalidatoren</th>
  </tr>
  <tr>
    <td></td>
    <td></td>
    <td></td>
    <td>Kategorie</td>
    <td>Validierung</td>
    <td>Implementierung</td>
  </tr>

  <!-- Allgemein -->
  <tr>
    <td rowspan="17">[Name]</td>
    <td rowspan="17">JSON</td>
    <td rowspan="17">JavaCode</td>
    <td rowspan="4"><strong>Allgemein</strong></td>
    <td><input type="checkbox"> Required</td>
    <td>/</td>
  </tr>
  <tr>
    <td><input type="checkbox"> Unique</td>
    <td>Geprüfte Menge: </td>
  </tr>
  <tr>
    <td><input type="checkbox"> Trailing/Leading Whitespaces</td>
    <td>/</td>
  </tr>
  <tr>
    <td><input type="checkbox"> Whitespaces</td>
    <td>/</td>
  </tr>

  <!-- Strings -->
  <tr>
    <td rowspan="5"><strong>Strings</strong></td>
    <td><input type="checkbox"> min. Zeichenlänge</td>
    <td>min: </td>
  </tr>
  <tr>
    <td><input type="checkbox"> max. Zeichenlänge</td>
    <td>max: </td>
  </tr>
  <tr>
    <td><input type="checkbox"> Email-Format</td>
    <td>Format: </td>
  </tr>
  <tr>
    <td><input type="checkbox"> Telefonnummer-Format</td>
    <td>Format: </td>
  </tr>
  <tr>
    <td><input type="checkbox"> Straße</td>
    <td>Kriterien:</td>
  </tr>

  <!-- Datum -->
  <tr>
    <td rowspan="2"><strong>Datum</strong></td>
    <td><input type="checkbox"> frühstes Datum</td>
    <td>Datum: </td>
  </tr>
  <tr>
    <td><input type="checkbox"> spätestes Datum</td>
    <td>Datum:</td>
  </tr>

  <!-- Number -->
  <tr>
    <td rowspan="4"><strong>Number</strong></td>
    <td><input type="checkbox"> min. Zahl</td>
    <td>min:</td>
  </tr>
  <tr>
    <td><input type="checkbox"> max. Zahl</td>
    <td>max:</td>
  </tr>
  <tr>
    <td><input type="checkbox"> nur ganze Zahlen</td>
    <td>/</td>
  </tr>
  <tr>
    <td><input type="checkbox"> max. Nachkommastellen</td>
    <td>max:</td>
  </tr>

  <!-- Multi-Selects -->
  <tr>
    <td rowspan="2"><strong>Multi-Selects</strong></td>
    <td><input type="checkbox"> min. Anzahl an Selektionen</td>
    <td>min:</td>
  </tr>
  <tr>
    <td><input type="checkbox"> max. Anzahl an Selektionen</td>
    <td>max:</td>
  </tr>
</table>

## Standardvalidatoren
Im SVWS-Client werden für einige typische Fälle Standardvalidatoren verwendet.
Um zu vermeiden, dass die Validierung bereits von den ASD-Validatoren abgedeckt und vom SVWS-Client
dann nochmals validiert wird, werden von IT-NRW die Infos benötigt, welche der Standardvalidierungen
bereits selbst vorgenommen werden. Eine Liste der Standardvalidatoren, was sie validieren und welche
Zusatzinfos von IT-NRW zu deren eigenen Abdeckung benötigt werden, ist aus der folgenden Liste
zu entnehmen.
<details>
<summary>Hinweise zur Implementierungen</summary>
<table>
  <tr>
	<th>Validierung</th>
	<th>Anmerkungen</th>
	<th>Benötigte Infos bei Implementierung durch IT-NRW</th>
  </tr>
  <tr>
	<td colspan="3" style="text-align: center"><strong>Allgemein</strong></td>
  </tr>
  <tr>
    <td>Required</td>
    <td>Prüft, ob das Pflichtfeld befüllt ist.</td>
    <td>Keine</td>
  </tr>
  <tr>
    <td>Unique</td>
    <td>Prüft, ob der Inhalt des Felds in einer vorgegebenen Menge schon existiert</td>
    <td>Die Menge, auf die geprüft wird.</td>
  </tr>
  <tr>
    <td>Trailing/Leading Spaces</td>
    <td>Prüft, ob nicht erlaubte führende/nachgestellte Leerzeichen vorhanden sind.</td>
    <td>Keine</td>
  </tr>
  <tr>
    <td>Whitespaces</td>
    <td>Prüft, ob nicht erlaubte Leerzeichen vorhanden sind. Prüft auch Leading/Trailing Spaces.</td>
    <td>Keine</td>
  </tr>
  <tr>
	<td colspan="3" style="text-align: center"><strong>Strings</strong></td>
  </tr>
  <tr>
    <td>Min. Zeichenlänge</td>
    <td>Prüft, ob die minimale Anzahl an Zeichen eingegeben wurde. Bei 1 wird stattdessen der Required-Validator verwendet.</td>
    <td>Die minimale Zeichenlänge, die erforderlich ist.</td>
  </tr>
  <tr>
    <td>Max. Zeichenlänge</td>
    <td>Prüft, ob die maximale Anzahl an Zeichen überschritten wurde.</td>
    <td>Die maximale Zeichenlänge, die erlaubt ist.</td>
  </tr>
  <tr>
    <td>Email-Format</td>
    <td>Prüft, ob die eingegebene Email einem gültigen Format entspricht.</td>
    <td>Das geprüfte Format.</td>
  </tr>
  <tr>
    <td>Telefonnummer-Format</td>
    <td>Prüft, ob die eingegebene Telefonnummer einem gültigen Format entspricht.</td>
    <td>Das geprüfte Format.</td>
  </tr>
  <tr>
    <td>Straße</td>
    <td>Prüft, ob die Teilangeben (Straßenname, Hausnummer, Hausnummerzusatz) die maximale Zeichenlänge überschreiten.</td>
    <td>Geprüfte Zeichenlängen</td>
  </tr>
  <tr>
	<td colspan="3" style="text-align: center"><strong>Datum</strong></td>
  </tr>
  <tr>
    <td>Frühstes Datum</td>
    <td>Prüft, ob das eingegebene Datum vor dem frühsten erlaubten Datum liegt.</td>
    <td>Das frühste zulässige Datum.</td>
  </tr>
  <tr>
    <td>Spätestes Datum</td>
    <td>Prüft, ob das eingegebene Datum nach dem spätesten erlaubten Datum liegt.</td>
    <td>Das späteste zulässige Datum.</td>
  </tr>
 <tr>
	<td colspan="3" style="text-align: center"><strong>Number</strong></td>
  </tr>
  <tr>
    <td>Min. Zahl</td>
    <td>Prüft, ob die eingegebene Zahl mindestens dem erlaubten Wert entspricht.</td>
    <td>Die definierte Untergrenze der einzugebenden Zahl.</td>
  </tr>
  <tr>
    <td>Max. Zahl</td>
    <td>Prüft, ob die eingegebene Zahl maximal dem erlaubten Wert entspricht.</td>
    <td>Die definierte Obergrenze der einzugebenden Zahl.</td>
  </tr>
  <tr>
    <td>Nur ganze Zahlen</td>
    <td>Prüft, ob bei nur erlaubten ganzen Zahlen keine Kommazahl eingegeben wurde.</td>
    <td>Keine</td>
  </tr>
  <tr>
    <td>Max. Nachkommastellen</td>
    <td>Prüft, ob die maximale Anzahl an Nachkommastellen überschritten wurde.</td>
    <td>Maximal erlaubte Anzahl an Nachkommastellen</td>
  </tr>
 <tr>
	<td colspan="3" style="text-align: center"><strong>Multi-Selects</strong></td>
  </tr>
  <tr>
    <td>Min. Anzahl an Selektionen</td>
    <td>Prüft, ob die Anzahl der selektierten Einträge mindestens der vorgegebenen Anzahl entspricht.</td>
    <td>Die minimale Anzahl an Selektionen</td>
  </tr>
  <tr>
    <td>Max. Anzahl an Selektionen</td>
    <td>Prüft, ob die Anzahl der selektierten Einträge maximal der vorgegebenen Anzahl entspricht.</td>
    <td>Die maximale Anzahl an Selektionen</td>
  </tr>
</table>


</details>

/status "Ready For Refinement"
/label ~"Tickettyp::Story"
/label ~"init"
/label ~"ASD::*"


