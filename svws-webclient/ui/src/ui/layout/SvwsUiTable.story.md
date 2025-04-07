## .svws-ui-table
`<div/>` mit `role="table"`

Im `<template #filter />` können einzelne Buttons oder Toggles eingesetzt werden.

Bei mehreren und komplexeren Filter-Optionen sollte `<template #filterAdvanced />` verwendet werden.
Sobald Elemente in diesem Slot sind, erscheint automatisch ein "Filter"-Button über der Tabelle, der die Filter-Optionen öffnet/versteckt.

Wenn ein Filter aktiv ist, wird der Button mit einem kleinen Punkt markiert (`:filtered="customIsFiltered"` Attribut muss gesetzt werden) und kann über einen zweiten Button direkt daneben einfach zurückgesetzt werden (`:filter-reset="customFilterResetFunction"`).

Spalten können manuell vom User ein- und ausgeblendet werden, wenn mindestens eine Spalte in der `columns=[{...}]` Definition das Attribut `toggle: true` hat. `toggle-columns` auf der Tabelle aktiviert den Button im Filter-Bereich.

Die Suche ist ein einfaches `<svws-ui-text-input type="search" />` Element, das im `<template #search />` eingefügt werden kann. Die Funktion zum Filtern muss manuell implementiert werden.

| Modifier Classes | Attribute | Beschreibung |
| --- | --- | --- |
| `svws-clickable` | `clickable` (bool) | Zeilen sind anklickbar (z. B. für die Schüler-Auswahl) |
| `svws-selectable` | `selectable` (bool) | Zeilen sind mit Checkbox auswählbar |
| `svws-has-selection` | | Wenn mindestens eine Zeile über eine Checkbox ausgewählt wurde: alle nicht ausgewählten Zeilen verblassen leicht |
| `svws-sortable` | `sortBy` (string\|undefined) | Tabellen-Spalten sind sortierbar |
| `svws-no-data` | `noData` (bool) | Zeigt einen Fallback Text an, reduziert Opacity bei thead und tfoot |
| `svws-type-grid` | `type="grid"` (string) | Jede Zelle einzeln abgetrennt ("Excel"-Style). Sollte nur für einzelne, komplexe Tabellen eingesetzt werden – in den meisten Fällen wird die vertikale Trennung nicht benötigt. |
| `svws-type-navigation` | `type="navigation"` (string) | Style zur Verwendung als Navigation (ohne Border, Zeilen sehen aus wie Buttons bei Hover und Active Status) |
| `svws-has-background` | `hasBackground` (bool) | Stärkerer Kontrast für Borders und Input-Elemente bei der Verwendung von vollflächigen Hintergrundfarben in den Zeilen |
| `overflow-[visible\|auto]` | `scroll` (bool) | Damit thead und tfoot sticky relativ zum Content-Bereich angezeigt werden, wird `overflow-visible` verwendet (Standard: scroll=false). In seltenen Fällen soll die Tabelle einzeln gescrollt werden – dafür kann `scroll` als Attribut gesetzt werden. |

---

## .svws-ui-tr
Table Row: `<div/>` mit `role="row"`

| Modifier Classes | Attribute | Beschreibung |
| --- | --- | --- |
| `svws-disabled` | `column.disabled` (bool) | Style für deaktivierte Zeile |
| `svws-disabled-soft` | | Visuell nur kleine Unterschiede zu einer normalen Zeile |

---

## .svws-ui-td
Table Cell: `<div/>` mit `role="cell"` (oder `role="columnheader"` im thead)

Eine extra CSS Class für `th` ist nicht notwendig, da die Cell immer in `svws-ui-thead` untergeordnet sein sollte und darüber mit CSS angepasst wird.

| Modifier Classes | Attribute | Beschreibung |
| --- | --- | --- |
| `svws-column-key--[...]` | `column.key` (string) | Key der Spalte |
| `svws-align-[left\|center\|right]` | `column.align` (string) | Textausrichtung (Standard: Linksbündig) |
| `svws-disabled` | `column.disabled` (bool) | Style für Cell, wenn diese oder die gesamte Spalte deaktiviert ist |
| `svws-disabled-soft` | | Visuell nur kleine Unterschiede zu einer normalen Cell |
| `svws-no-value` | | Wird automatisch gesetzt, wenn die Cell leer ist |
| `svws-no-padding` | | Kein Padding in der jeweiligen Cell, damit z. B. Input Felder die gesamte Fläche einnehmen können |

Untergeordnete Elemente von `svws-ui-td` können mit `svws-no-padding` die gesamte Fläche der Cell nutzen.

Text wird im tbody automatisch umgebrochen und zeigt immer den gesamten Inhalt an. Um Text bei overflow mit "…" abzukürzen, kann der Inhalt in ein `<span class="line-clamp-1 break-all">...</span>` gesetzt werden.
_"line-clamp-1" schneidet den Text ggf. je nach verfügbarer Breite ab. Mit "break-all" wird auch innerhalb einzelner Worte gekürzt. Im thead wird automatisch ohne Umbruch gekürzt, wenn das `label` Attribut für die Spalte verwendet wird._

---

## .svws-ui-thead
Table Header: `<div/>` mit `role="rowgroup"`

---

## .svws-ui-tbody
Table Body: `<div/>` mit `role="rowgroup"`

---

## .svws-ui-tfoot
Table Footer: `<div/>` mit `role="rowgroup"`