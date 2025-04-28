## UiCard

Mit UiCard lassen sich Informationen in einer Card organisieren, die optional auch collapsible ist. Die Card gliedert sich in zwei Bereiche: Der Header, welcher immer sichtbar ist und der Body, der nur sichtbar ist, wenn die Card geöffnet ist. 

### Anpassungen
Die Card kann durch ihre Props und durch Slots angepasst werden:
<details>
<summary>Props</summary>

**compact**
- **Typ**: `boolean`   
- **Default**: `false`   
- Zeigt die Komponente in kompakter Darstellung mit geringeren Abständen und kleineren Icons.

**fehler**
- **Typ**: `ValidatorFehlerart`   
- **Default**: `ValidatorFehlerart.UNGENUTZT`   
- Färbt die Komponente abhängig von ihrer Fehlerart ein

#### **Collapse Optionen**
**isOpen**
- **Typ**: `boolean`   
- **Default**: `undefined`  
- Gibt an, ob die Komponente geöffnet ist. Der Zustand kann damit von außen manipuliert werden, ohne auf den Header klicken zu müssen/können. Dadurch bleibt die Card bei `collapsible = false` durch Manipulation von außen klappbar. Wenn `undefined`, hängt der initiale Zustand von `collapsible` ab.

**collapsible**
- **Typ**: `boolean`   
- **Default**: `true`  
- Wenn `true`, dann ist die Komponente klappbar und die Collapse-Icons werden gerendert.

**Initialer Zustand der Card abhängig von `collapsible` und `isOpen`**
- `isOpen = true` und `collapsible = true`: Card wird geöffnet dargestellt und kann durch Klick auf den Header geschlossen werden. Es werden entsprechende collapse-Icons dargestellt.
- `isOpen = true` und `collapsible = false`: Die Card wird geöffnet dargestellt, kann aber nicht durch Klick auf den Header geschlossen werden, sondern nur noch von außen über die Prop `isOpen`. Es werden keine collapse-Icons gerendert.
- `isOpen = false` und `collapsible = true`: Card wird geschlossen dargestellt und kann durch Klick auf den Header geöffnet werden. Es werden entsprechende collapse-Icons dargestellt.
- `isOpen = false` und `collapsible = false`: Die Card wird geschlossen dargestellt, kann aber nicht durch Klick auf den Header geöffnet werden, sondern nur noch von außen über die Prop `isOpen`. Es werden keine collapse-Icons gerendert.
- `isOpen = undefined` und `collapsible = true`: Die Card wird geschlossen dargestellt. Es werden entsprechende collapse-Icons dargestellt.
- `isOpen = undefined` und `collapsible = false`: Die Card wird geöffnet dargestellt. Es werden keine collapse-Icons gerendert.
- `collapsible = undefined`: Ist `collapsible` nicht gesetzt, dann wird es per Default auf `true` gesetzt und die Card verhält sich wie oben beschrieben.

**collapseIconPosition**
- **Typ**: `'left'`, `'right'`  
- **Default**: `'right'`  
- Position des Collapse-Icons im Header. Wird nicht gerendert, wenn `collapsible = false` gesetzt ist.

**collapseIconOpened**
- **Typ**: `string`   
- **Default**: `'i-ri-arrow-up-s-line'`
- Icon für den geöffneten Zustand. Wird nicht gerendert, wenn `collapsible = false` gesetzt ist.

**collapseIconClosed**
- **Typ**: `string`   
- **Default**: `'i-ri-arrow-down-s-line'`
- Icon für den geschlossenen Zustand. Wird nicht gerendert, wenn `collapsible = false` gesetzt ist.

#### **Header Optionen**
**icon**
- **Typ**: `string`   
- **Default**: `undefined`  
- Icon, das im Header angezeigt wird. Wenn `undefined`, wird kein Icon gerendert.

**title**
- **Typ**: `string`   
- **Default**: `undefined`  
- Haupttitel des Headers. Wenn `undefined`, wird kein Titel angezeigt.

**subtitle**
- **Typ**: `string`   
- **Default**: `undefined`  
- Untertitel des Headers. Wenn `undefined`, wird kein Untertitel angezeigt.

**info**
- **Typ**: `string`   
- **Default**: `undefined`  
- Zusätzliche Informationen im Header auf der rechten Seite. Wenn `undefined`, wird kein Infotext angezeigt.

#### **Body Optionen**
**content**
- **Typ**: `string`   
- **Default**: `undefined`  
- Hauptinhalt des Bodys. Ein Body wird auch ohne Inhalt noch gerendert.

**showDivider**
- **Typ**: `boolean`   
- **Default**: `false`   
- Zeigt einen Trennstrich zwischen dem Content und dem Footer.

**footer**
- **Typ**: `string`   
- **Default**: `undefined`  
- Inhalt des Footers. Wenn `undefined`, wird kein Footer angezeigt.

#### **Button Optionen**
**buttonMode**
- **Typ**: `'text'`, `'icon'`  
- **Default**: `'text'`  
- Darstellung der Buttons entweder als Textbuttons oder als Icons.

**buttonContainer**
- **Typ**: `'content'`, `'footer'`  
- **Default**: `'content'`  
- Platzierung der Buttons.

**buttonPosition**
- **Typ**: `'left'`, `'right'`  
- **Default**: `'right'`  
- Position der Buttons innerhalb des Containers.

**buttonOrientation**
- **Typ**: `'vertical'`, `'horizontal'`  
- **Default**: `'horizontal'`  
- Ausrichtung der Buttons.

#### **Button Funktionen**
**onEdit**
- **Typ**: `() => void  `
- **Default**: `undefined`  
- Wird ausgeführt, wenn der Bearbeiten-Button geklickt wird. Sobald diese Funktion definiert ist, wird auch ein Bearbeiten-Button gerendert.

**onSave**
- **Typ**: `() => void ` 
- **Default**: `undefined`  
- Wird ausgeführt, wenn der Speichern-Button geklickt wird. Sobald diese Funktion definiert ist, wird auch ein Speichern-Button gerendert.

**onDelete**
- **Typ**: `() => void  `
- **Default**: `undefined`  
- Wird ausgeführt, wenn der Löschen-Button geklickt wird. Sobald diese Funktion definiert ist, wird auch ein Löschen-Button gerendert.

**onCancel**
- **Typ**: `() => void ` 
- **Default**: `undefined`  
- Wird ausgeführt, wenn der Abbrechen-Button geklickt wird. Sobald diese Funktion definiert ist, wird auch ein Abbrechen-Button gerendert.

#### **Button Deaktivierung**
**editButtonDisabled**
- **Typ**: `boolean`   
- **Default**: `false`   
- Deaktiviert den Bearbeiten-Button, sodass dieser erscheint aber nicht klickbar ist.

**saveButtonDisabled**
- **Typ**: `boolean`   
- **Default**: `false`   
- Deaktiviert den Speichern-Button, sodass dieser erscheint aber nicht klickbar ist.

**deleteButtonDisabled**
- **Typ**: `boolean`   
- **Default**: `false`   
- Deaktiviert den Löschen-Button, sodass dieser erscheint aber nicht klickbar ist.

**cancelButtonDisabled**
- **Typ**: `boolean`   
- **Default**: `false`   
- Deaktiviert den Abbrechen-Button, sodass dieser erscheint aber nicht klickbar ist.

#### **Button Deaktivierung Begründung**
**editButtonDisabledReason**
- **Typ**: `string`   
- **Default**: `undefined`  
- Grund für die Deaktivierung des Bearbeiten-Buttons. Dieser wird als Tooltip angezeigt.

**saveButtonDisabledReason**
- **Typ**: `string`   
- **Default**: `undefined`  
- Grund für die Deaktivierung des Speichern-Buttons. Dieser wird als Tooltip angezeigt.

**deleteButtonDisabledReason**
- **Typ**: `string`   
- **Default**: `undefined`  
- Grund für die Deaktivierung des Löschen-Buttons. Dieser wird als Tooltip angezeigt.

**cancelButtonDisabledReason**
- **Typ**: `string`   
- **Default**: `undefined`  
- Grund für die Deaktivierung des Abbrechen-Buttons. Dieser wird als Tooltip angezeigt.
  
</details>

<details>
<summary>Slots</summary>

#### **Header Section**

**collapseLeft**  
  - **Position**: Links im Header  
  - **Verwendung**: Dient zur Darstellung der Collapse-Icons auf der linken Seite des Headers. Wenn nicht gesetzt, wird die Standarddarstellung verwendet.

**icon**  
  - **Position**: Links vom Titel
  - **Verwendung**: Dient zur Darstellung eines benutzerdefinierten Icons im Header.

**title**  
  - **Position**: Im Titelbereich des Headers.
  - **Verwendung**: Dient zur Darstellung eines benutzerdefinierten Titels.

**subtitle**  
  - **Position**: Im Titelbereich des Headers unter dem `title`.
  - **Verwendung**: Dient zur Darstellung eines benutzerdefinierten Untertitels.

**info**  
  - **Position**: Rechts im Header
  - **Verwendung**: Dient zur Darstellung zusätzlicher Informationen im Header.

**collapseRight**  
  - **Position**: Rechts im Header nach `info`
  - **Verwendung**: Dient zur Darstellung der Collapse-Icons auf der rechten Seite des Headers.

#### **Body Section**

**buttonContentLeft**  
  - **Position**: Links im Content-Bereich
  - **Verwendung**: Dient zur Darstellung der 4 vordefinierten Buttons auf der linken Seite des Bodys.

**default**  
	Der default Slot der Komponente.
  - **Position**: In der Mitte des Content-Bereichs
  - **Verwendung**: Dient zur Darstellung des benutzerdefinierten Inhalts im Body.

**buttonContentRight**  
  - **Position**: Rechts im Content-Bereich
  - **Verwendung**: Dient zur Darstellung der 4 vordefinierten Buttons auf der rechten Seite des Bodys.

**footerDivider**  
  - **Position**: Zwischen Content und Footer
  - **Verwendung**: Dient zur Darstellung eines Trennstrichs, um Content und Footer im Body visuell zu trennen.

**footer**  
  - **Position**: Unten im Body
  - **Verwendung**: Dient zur Darstellung des benutzerdefinierten Footers.

**buttonFooterLeft**  
  - **Position**: Links im Footer-Bereich  
  - **Verwendung**: Dient zur Darstellung der 4 vordefinierten Buttons auf der linken Seite des Footers.

**buttonFooterRight**  
  - **Position**: Rechts im Footer-Bereich  
  - **Verwendung**: Dient zur Darstellung der 4 vordefinierten Buttons auf der rechten Seite des Footers.

</details>

### Events
Die Card emittet beim Öffnen und Schließen ein Event, auf das entsprechend reagiert werden kann.

`update:isOpen` 
  - `true`: Wird emittet, *bevor* die Card geöffnet wird.
  - `false`: Wird emittet, *nachdem* die Card geschlossen ist.
  