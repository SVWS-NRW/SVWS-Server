---
group: 'design-system'
title: 'Changelog'
iconColor: '#ccc'
id: 'changelog'
icon: 'carbon:bookmark'
---

# Changelog

Chronologische Auflistung von Änderungen an den Komponenten Fix/Feature/Breaking Change:
## ab 0.8.11
* Die Table-Komponente kennt nun auch zwei weitere Slots: `row` und `rowSelectable` und `rowCustom`. Dies gibt nun die jeweiligen Rows zurück und sollte statt `body` verwendet werden.
	** `row` ist bereits in ein `tr` gewrappt und liefert nur die `td`.
	** `rowSelectable` hat zusätzlich als erstes `td` eine Checkbox für die Zeilenauswahl.
	** `rowCustom` hat kein `tr` und muss vollständig implementiert werden.
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