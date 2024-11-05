---
group: 'design-system'
title: 'Changelog'
iconColor: '#ccc'
id: 'changelog'
icon: 'carbon:bookmark'
---

# Changelog

Chronologische Auflistung von Änderungen an den Komponenten Fix/Feature/Breaking Change:
## ab 1.0.1
* [Design Tokens](/story/farben) werden eingeführt. Deprecated Farbdefinitionen sollen nicht mehr verwendet werden. In den Docs sind zu dem Thema alle wichtigen Details nachzulesen und es gibt eine Übersicht aller verfügbaren Variablen.  
Beispiel: `bg-white dark:bg-black text-black dark:text-white hover:text-primary dark:hover:text-primary-dark` → `bg-ui text-ui hover:text-ui-hover`
* Dark Mode wird ab jetzt automatisch über die semantischen Design Tokens umgesetzt, alle `dark:bg-color` classes entfallen.
## ab 0.9.3
* Die Tabelle bekommt ein neues Prop `unselectable`. Hier wird als Set eine Liste aller Items übergeben, die sich nicht auswählen lassen, z.B. Vorgaben.
* ebenfalls gibt es neu `allowArrowKeySelection`. Das erlaubt die Auswahl der Items per Tastatur.
* Die Checkbox stellt nun per `defineExposed` auch den `content`und das `input` zur Verfügung, analog zum TextInput.
## ab 0.9.2
* Das Text-Input unterstützt keinen Typ number mehr und verlangt als Input den Typ string | null. Für Zahleneingabefelder ist als Ersatz der Number-Input zu nehmen.
## ab 0.8.16
* Das Multiselect bekommt die Prop `autofocus`. Wird das übergeben, dann öffnet sich automatisch das Auswahlmenü. Wird z.B. bei Stundenplan-Unterichte verwendet
* Das MS sendet auch ein `blur`-Event, damit man beim Verlassen z.B. auch das MS wieder entfernen kann. Ebenso bei den Unterrichten zu finden.
## ab 0.8.11
* Die Table-Komponente kennt nun auch zwei weitere Slots: `row` und `rowSelectable` und `rowCustom`. Dies gibt nun die jeweiligen Rows zurück und sollte statt `body` verwendet werden.
	** `row` ist bereits in ein `tr` gewrappt und liefert nur die `td`.
	** `rowSelectable` hat zusätzlich als erstes `td` eine Checkbox für die Zeilenauswahl.
	** `rowCustom` hat kein `tr` und muss vollständig implementiert werden.
* Man kann der Tabelle ein Set `hiddenColumns` üergeben mit den Keys der Spalten, um festzulegen, welche Spalten standardmäßig ausgeblendet sind. Kann auch als `v-model:hiddenColumns` verwendet werden, um z.b. die Daten anderweitig zu speichern.
* Das Select kann nun ein Vorgabe-Item hervorheben. Wird z.B. bei dem aktuellen Schulabschnitt genutzt. `:highlightItem`
## ab 0.8.9
* Die Checkbox hat das Attribut `color = 'success' | 'error' | 'warning';` dazubekommen.
## ab 0.7.8
* Die Checkbox verwendet nun das Attribut `indeterminate`, um den Zustzand von `undefined` oder `null`, unabhängig vom verwendeten `v-model` bzw. `model-value` anzuzeigen. `model-value` muss nun ein Boolean-Wert sein. Dazu wurde ein `readonly` Attribut hinzugefügt, das Eingaben verhindert.
* Die Tabelle hat zwei neue Attribute bekommen: `sortByAndOrder?: {key: string|null; order: boolean|null}` und `sortByMulti?: Map<string, (boolean | null)>`. `sortByAndOrder` gibt an, welcher key in der Tabelle sortiert wird aktuell und ob es `true`-> aufsteigend, `false`: absteigend oder `null` -> nicht sortiert wird. `sortByMulti` übergibt diese Information als Map, wenn nach mehreren Keys sortiert wird. Damit können mehrere Felder gleichzeitig sortiert werden. Allerdings muss dafür die Sortierung extern vorgenommen werden und `items` entsprechend der Sortierung aktualisiert werden. Dazu gibt es ein Event, was über den Stand der aktuellen Auswahl informiert: `@update:sort-by-and-order`. Je nach `order` können entsprechende Filter dann zu- oder abgeschaltet werden. Die beiden Props `sortBy` und `sortingOrder` wurden entfernt.
* Es wurde eine neue Input-Komponente `SvwsUiInputNumber` hinzugefügt. Diese übernimmt die Aufgabe des Text-Inputs für den Type `number` und stellt nun typ-sicher Zahlen oder null zur Verfügung. In zukünfigen Versionen soll der type `number` bei dem Text-Input entfernt werden.
## ab 0.7.3
* Modal wurde überarbeitet und stellt nicht mehr die Funktionen openModal, closeModal und isOpen zur Verfügung. Das Öffnen erfolgt nun über die Property show, mit der eine Funktion übergeben wird, welche eine vue-Ref auf einen boolean zurückgibt
* Das MultiSelect kann nur noch die Variante mit Tags übernehmen, ohne Tags wird mit Select verwendet. Da sich bis auf das `tags`-Prop nichts ändert, kann mit Suchen und Ersetzen diese Komponente relativ problemlos ausgetauscht werden.
## ab 0.7
* DataTable ist nun auf generics umgestellt, die Items bestimmen den Typ.
* Multiselect, TextInput und TextArea geben nun auch den Wert bei @blur/@change raus. Darüber hinaus verwalten sie nun den internen State.
## ab 0.6.18:
* TextInput: Fix, wenn `type="number"` ist, dann wird beim Klicken der rauf/runter-Pfeile kein `emit` ausgelöst, weil `document.ativeElement` nicht das Input-Feld ist. Es muss erst durch ein `focus()` gesetzt werden.
* Multiselect kann bei autocomplete nun auch per Tab das erste Element auswählen.
* emits werden bei TextInput und TextArea nur noch bei `blur`-Events ausgegeben.
* Drag und Drop wird entfernt