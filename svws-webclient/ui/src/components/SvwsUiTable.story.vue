<template>
	<Story title="Table" id="svws-ui-table" icon="ri:table-line" auto-props-disabled>
		<Variant title="Default">
			<div class="px-3">
				<svws-ui-content-card>
					<svws-ui-table v-model="selectedRows" :items="data" :columns="cols" :clickable="state.clickable" :disable-header="state.disableHeader" :disable-footer="state.disableFooter" :selectable="state.selectable" :count="state.count" v-model:clicked="clickedRow" :filtered="docsMultiselectFilterA?.length > 0 || docsMultiselectFilterB?.length > 0"
						:toggle-columns="state.toggleColumns" :filter-reset="filterReset" :type="state.typeGrid ? 'grid' : 'table'">
						<template #search v-if="state.docsWithSearch">
							<svws-ui-text-input type="search" placeholder="Suche" v-model="search" />
						</template>
						<template #filter v-if="state.docsWithFilter">
							<svws-ui-checkbox type="toggle" v-model="state.docsToggleValue">
								Toggle
							</svws-ui-checkbox>
						</template>
						<template #filterAdvanced v-if="state.docsWithFilterAdvanced">
							<svws-ui-select :item-text="(item) => item" v-model="docsMultiselectFilterA" :items="['Option A', 'Option B']" title="Abschluss" removable />
							<svws-ui-select :item-text="(item) => item" v-model="docsMultiselectFilterB" :items="['Option A', 'Option B']" title="Beispiel-Filter" removable />
						</template>
						<template #header(customIcon)>
							<svws-ui-tooltip>
								<i-ri-lightbulb-line class="text-base" />
								<template #content>
									Icon statt Text
								</template>
							</svws-ui-tooltip>
						</template>
						<template #cell(fach)="{value}">
							<span v-if="value" class="svws-ui-badge" :style="`--background-color: hsl(${(value.length * 42)},90%,80%)`">{{ value }}</span>
						</template>
						<template #cell(name)="{value}">
							<span class="line-clamp-1 break-all">{{ value }}</span>
						</template>
						<template #cell(email)="{value}">
							<span class="line-clamp-1 break-all">{{ value }}</span>
						</template>
					</svws-ui-table>
				</svws-ui-content-card>
			</div>
			<template #source>
				{{ `
<svws-ui-table :items="[]" :columns="[{key: 'key', label: 'Label'}]">
  <template #header>
    <div class="svws-ui-tr" role="row">
      <div class="svws-ui-td" role="columnheader">Column</div>
    </div>
  </template>
  <template #body>
    <div class="svws-ui-tr" role="row">
      <div class="svws-ui-td" role="cell">Cell</div>
    </div>
  </template>
  <template #actions>
    <svws-ui-button>Button</svws-ui-button>
  </template>
</svws-ui-table>
` }}
			</template>
			<template #controls>
				<HstCheckbox v-model="state.docsWithSearch" title="Suche" />
				<HstCheckbox v-model="state.docsWithFilter" title="Einfacher Filter" />
				<HstCheckbox v-model="state.docsWithFilterAdvanced" title="Advanced Filter" />
				<div class="h-3" />
				<hr>
				<HstCheckbox v-model="state.clickable" title="clickable" />
				<HstCheckbox v-model="state.selectable" title="selectable" />
				<HstCheckbox v-model="state.toggleColumns" title="toggle-columns" />
				<HstCheckbox v-model="state.typeGrid" title="type='grid'" />
				<HstCheckbox v-model="state.disableHeader" title="disable-header" />
				<HstCheckbox v-model="state.disableFooter" title="disable-footer" />
				<HstCheckbox v-model="state.count" title="count" />
			</template>
		</Variant>
		<Variant title="Inputs">
			<div class="px-3">
				<svws-ui-content-card>
					<svws-ui-table v-model="selectedRows" :items="data" :columns="cols2" clickable>
						<template #cell(email)="{ rowIndex }">
							<svws-ui-text-input :model-value="1" @keyup.down="next(rowIndex)" @keyup.up="previous(rowIndex)" @keyup.enter="next(rowIndex)" :ref="(el) => itemRefs.set(rowIndex, el)" />
						</template>
					</svws-ui-table>
				</svws-ui-content-card>
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import type { Ref} from "vue";
	import { ref, reactive } from "vue";
	import type { DataTableColumn } from "../types";

	const itemRefs = ref(new Map());

	const state = reactive({
		disableHeader: false,
		clickable: true,
		allowUnclick: true,
		clicked: undefined,
		selectable: true,
		sortBy: undefined,
		sortingOrder: undefined,
		disableFooter: false,
		count: true,
		noData: undefined,
		noDataText: "Keine Einträge gefunden",
		type: 'table',
		hasBackground: false,
		toggleColumns: true,
		docsWithSearch: true,
		docsWithFilter: false,
		docsWithFilterAdvanced: true,
		docsToggleValue: true,
		typeGrid: false,
	});

	const cols = ref<DataTableColumn[]>([
		{key: "name", label: "Name", sortable: true, span: 1,},
		{key: "fach", label: "Fach", span: 0.5},
		{key: "email", label: "E-Mail", toggle: true},
		{key: "customIcon", label: "Icon", tooltip: "Icon statt Text", sortable: true, span: 0.25},
		{key: "test", label: "Column", sortable: true},
		{key: "itemID", label: "ID", tooltip: "Identifikation", fixedWidth: 4, align: "right", toggle: true},
	]);

	const cols2 = ref([
		{key: "name", label: "Name", sortable: true, span: 1,},
		{key: "fach", label: "Fach", span: 0.5},
		{key: "email", label: "Note"},
	]);

	const data = ref([
		{id: 0,name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch'},
		{id: 1,name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "Neu", itemID: '99', fach: 'Englisch'},
		{id: 2,name: "Andere Person", email: "person@example.com", customIcon: "2008", test: "Platzhalter", itemID: '42', fach: ''},
		{id: 3,name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik'},
		{id: 4,name: "Erster Schüler", email: "schueler@example.com", customIcon: "1999", test: "Data", itemID: '1', fach: ''},
		{id: 5,name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch'},
		{id: 6,name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "Neu", itemID: '99', fach: 'Englisch'},
		{id: 7,name: "Andere Person", email: "person@example.com", customIcon: "2008", test: "Platzhalter", itemID: '42', fach: ''},
		{id: 8,name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik'},
		{id: 9,name: "Erster Schüler", email: "schueler@example.com", customIcon: "1999", test: "", itemID: '1', fach: ''},
		{id: 10,name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch'},
		{id: 11,name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "", itemID: '99', fach: 'Englisch'},
		{id: 12,name: "Andere Person", email: "person@example.com", customIcon: "2008", test: "Platzhalter", itemID: '42', fach: ''},
		{id: 13,name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik'},
		{id: 14,name: "Erster Schüler", email: "schueler@example.com", customIcon: "1999", test: "Data", itemID: '1', fach: ''},
		{id: 15,name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch'},
		{id: 16,name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "Neu", itemID: '99', fach: 'Englisch'},
		{id: 17,name: "Andere Person", email: "person@example.com", customIcon: "2008", test: "", itemID: '42', fach: ''},
		{id: 18,name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik'},
		{id: 19,name: "Erster Schüler", email: "schueler@example.com", customIcon: "1999", test: "Data", itemID: '1', fach: ''},
	]);

	const selectedRows = ref([]);
	const docsMultiselectFilterA = ref();
	const docsMultiselectFilterB = ref();
	const clickedRow = ref(undefined);
	const search: Ref<string> = ref("");

	const filterReset = () => {
		docsMultiselectFilterA.value = undefined;
		docsMultiselectFilterB.value = undefined;
	}

	function next(id: number) {
		const el = itemRefs.value.get(id + 1);
		if (el)
			el.input.focus();
	}

	function previous(id: number) {
		const el = itemRefs.value.get(id - 1);
		if (el)
			el.input.focus();
	}

</script>

<docs lang="md">
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
</docs>
