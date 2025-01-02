## SvwsUiCard

Mit SvwsUiCard lassen sich Informationen in einer Card organisieren, die optional auch collapsible ist. Die Card gliedert sich in zwei Bereiche: Der Header, welcher immer sichtbar ist und der Body, der nur sichtbar ist, wenn die Card geöffnet ist. \
Die Card kann durch ihre Props und durch Slots angepasst werden:
<details>
<summary>Props</summary>

**compact**
- **Typ**: boolean  
- **Default**: false  
- Zeigt die Komponente in kompakter Darstellung mit geringeren Abständen und kleineren Icons.

#### **Collapse Optionen**
**isOpen**
- **Typ**: boolean  
- **Default**: undefined  
- Gibt an, ob die Komponente geöffnet ist. Der Zustand kann von außen manipuliert werden. Wenn undefined, hängt der Zustand von `collapsible` ab.

**collapsible**
- **Typ**: boolean  
- **Default**: true  
- Macht die Komponente klappbar. Der initiale Zustand wird von `isOpen` oder `collapsible` bestimmt.

**collapseIconPosition**
- **Typ**: left, right  
- **Default**: right  
- Position des Collapse-Icons im Header. Keine Auswirkung, wenn `collapsible` = false.

**collapseIconOpened**
- **Typ**: string  
- **Default**: 'i-ri-arrow-up-s-line'  
- Icon für den geöffneten Zustand. Keine Auswirkung, wenn `collapsible` = false.

**collapseIconClosed**
- **Typ**: string  
- **Default**: 'i-ri-arrow-down-s-line'  
- Icon für den geschlossenen Zustand. Keine Auswirkung, wenn `collapsible` = false.

#### **Header Optionen**
**icon**
- **Typ**: string  
- **Default**: undefined  
- Icon, das im Header angezeigt wird. Wenn undefined, wird kein Icon gerendert.

**title**
- **Typ**: string  
- **Default**: undefined  
- Haupttitel des Headers. Wenn undefined, wird kein Titel angezeigt.

**subtitle**
- **Typ**: string  
- **Default**: undefined  
- Untertitel des Headers. Wenn undefined, wird kein Untertitel angezeigt.

**info**
- **Typ**: string  
- **Default**: undefined  
- Zusätzliche Informationen im Header auf der rechten Seite. Wenn undefined, wird kein Infotext angezeigt.

#### **Body Optionen**
**content**
- **Typ**: string  
- **Default**: undefined  
- Hauptinhalt des Bodys. Ein Body wird auch ohne Inhalt noch gerendert.

**showDivider**
- **Typ**: boolean  
- **Default**: false  
- Zeigt einen Trennstrich zwischen dem Content und dem Footer.

**footer**
- **Typ**: string  
- **Default**: undefined  
- Inhalt des Footers. Wenn undefined, wird kein Footer angezeigt.

#### **Button Optionen**
**buttonMode**
- **Typ**: text, icon  
- **Default**: text  
- Darstellung der Buttons entweder als Textbuttons oder als Icons.

**buttonContainer**
- **Typ**: content, footer  
- **Default**: content  
- Platzierung der Buttons.

**buttonPosition**
- **Typ**: left, right  
- **Default**: right  
- Position der Buttons innerhalb des Containers.

**buttonOrientation**
- **Typ**: vertical, horizontal  
- **Default**: horizontal  
- Ausrichtung der Buttons.

#### **Button Funktionen**
**onEdit**
- **Typ**: () => void  
- **Default**: undefined  
- Wird ausgeführt, wenn der Bearbeiten-Button geklickt wird.

**onSave**
- **Typ**: () => void  
- **Default**: undefined  
- Wird ausgeführt, wenn der Speichern-Button geklickt wird.

**onDelete**
- **Typ**: () => void  
- **Default**: undefined  
- Wird ausgeführt, wenn der Löschen-Button geklickt wird.

**onCancel**
- **Typ**: () => void  
- **Default**: undefined  
- Wird ausgeführt, wenn der Abbrechen-Button geklickt wird.

#### **Button Deaktivierung**
**editButtonDisabled**
- **Typ**: boolean  
- **Default**: false  
- Deaktiviert den Bearbeiten-Button.

**saveButtonDisabled**
- **Typ**: boolean  
- **Default**: false  
- Deaktiviert den Speichern-Button.

**deleteButtonDisabled**
- **Typ**: boolean  
- **Default**: false  
- Deaktiviert den Löschen-Button.

**cancelButtonDisabled**
- **Typ**: boolean  
- **Default**: false  
- Deaktiviert den Abbrechen-Button.

#### **Button Deaktivierung Begründung**
**editButtonDisabledReason**
- **Typ**: string  
- **Default**: undefined  
- Grund für die Deaktivierung des Bearbeiten-Buttons. Dieser wird als Tooltip angezeigt.

**saveButtonDisabledReason**
- **Typ**: string  
- **Default**: undefined  
- Grund für die Deaktivierung des Speichern-Buttons. Dieser wird als Tooltip angezeigt.

**deleteButtonDisabledReason**
- **Typ**: string  
- **Default**: undefined  
- Grund für die Deaktivierung des Löschen-Buttons. Dieser wird als Tooltip angezeigt.

**cancelButtonDisabledReason**
- **Typ**: string  
- **Default**: undefined  
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
  - **Verwendung**: Dient zur Darstellung von den 4 vordefinierten Buttons auf der linken Seite des Bodys.

**content**  
  - **Position**: In der Mitte des Content-Bereichs
  - **Verwendung**: Dient zur Darstellung des benutzerdefinierten Inhalts im Body.

**buttonContentRight**  
  - **Position**: Rechts im Content-Bereich
  - **Verwendung**: Dient zur Darstellung von den 4 vordefinierten Buttons auf der rechten Seite des Bodys.

**footerDivider**  
  - **Position**: Zwischen Content und Footer
  - **Verwendung**: Dient zur Darstellung eines Trennstrichs, um Content und Footer im Body visuell zu trennen.

**footer**  
  - **Position**: Unten im Body
  - **Verwendung**: Dient zur Darstellung des benutzerdefinierten Footers.

**buttonFooterLeft**  
  - **Position**: Links im Footer-Bereich  
  - **Verwendung**: Dient zur Darstellung von den 4 vordefinierten Buttons auf der linken Seite des Footers.

**buttonFooterRight**  
  - **Position**: Rechts im Footer-Bereich  
  - **Verwendung**: Dient zur Darstellung von den 4 vordefinierten Buttons auf der rechten Seite des Footers.