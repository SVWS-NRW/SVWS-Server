<template>
	<Story title="Table" id="svws-ui-table" icon="ri:table-line" :layout="{ type: 'single', iframe: false }" auto-props-disabled>
		<template #docs>
			<SvwsUiTableStory />
		</template>
		<Variant title="Default">
			<div class="px-3">
				<svws-ui-content-card>
					<svws-ui-table v-model="selectedRows" :items="data" :columns="cols" :clickable="state.clickable" :disable-header="state.disableHeader" :disable-footer="state.disableFooter" :selectable="state.selectable" :count="state.count" v-model:clicked="clickedRow" :filtered="docsMultiselectFilterA?.length > 0 || docsMultiselectFilterB?.length > 0"
						:toggle-columns="state.toggleColumns" :filter-reset="filterReset" :type="state.typeGrid ? 'grid' : 'table'" v-model:hidden-columns="state.hiddenColumns" :lock-selectable="state.lockSelectable">
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
								<span class="icon i-ri-lightbulb-line text-base" />
								<template #content>
									Icon statt Text
								</template>
							</svws-ui-tooltip>
						</template>
						<template #cell(fach)="{value}">
							<span v-if="value" class="svws-ui-badge" :style="`background-color: hsl(${(value.length * 42)},90%,80%)`">{{ value }}</span>
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
				<HstCheckbox v-model="state.lockSelectable" title="disable-selection-checkboxes" />
				<HstCheckbox v-model="state.count" title="count" />
			</template>
		</Variant>
		<Variant title="Inputs">
			<div class="px-3">
				<svws-ui-content-card>
					<svws-ui-table v-model="selectedRows" :items="data" :columns="cols2" clickable>
						<template #cell(email)="{ rowIndex }">
							<svws-ui-text-input :model-value="'1'" @keyup.down="next(rowIndex)" @keyup.up="previous(rowIndex)" @keyup.enter="next(rowIndex)" :ref="(el) => itemRefs.set(rowIndex, el)" />
						</template>
					</svws-ui-table>
				</svws-ui-content-card>
			</div>
		</Variant>
		<Variant title="Sortierung">
			<div class="px-3">
				<svws-ui-content-card>
					<svws-ui-table v-model="selectedRows" :items="dataSorted" :columns="cols2" clickable v-model:sort-by-and-order="sortByAndOrder" />
				</svws-ui-content-card>
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { ref, reactive, computed } from "vue";
	import type { DataTableColumn, SortByAndOrder } from "../../types";

	const itemRefs = ref(new Map());
	const hiddenColumns = ref<Set<string>>(new Set<string>());
	hiddenColumns.value.add('fach');
	hiddenColumns.value.add('email');

	const state = reactive({
		disableHeader: false,
		clickable: true,
		// allowUnclick: true,
		clicked: undefined,
		selectable: true,
		sortBy: undefined,
		sortingOrder: undefined,
		disableFooter: false,
		lockSelectable: false,
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
		hiddenColumns,
	});

	const cols = ref<DataTableColumn[]>([
		{key: "name", label: "Name", sortable: true, span: 1, toggleInvisible: true},
		{key: "fach", label: "Fach", span: 0.5, toggle: true},
		{key: "email", label: "E-Mail", toggle: true},
		{key: "customIcon", label: "Icon", tooltip: "Icon statt Text", sortable: true, span: 0.25},
		{key: "test", label: "Column", sortable: true},
		{key: "itemID", label: "ID", tooltip: "Identifikation", fixedWidth: 4, align: "right", toggle: true},
	]);

	const data = ref([
		{id: 0,name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch'},
		{id: 1,name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "Neu", itemID: '99', fach: 'Englisch'},
		{id: 2,name: "Andere Person 6", email: "person6@example.com", customIcon: "2008", test: "Platzhalter", itemID: '42', fach: ''},
		{id: 3,name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik'},
		{id: 4,name: "Erster Schüler", email: "schueler4example.com", customIcon: "1999", test: "Data", itemID: '1', fach: ''},
		{id: 5,name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch'},
		{id: 6,name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "Neu", itemID: '99', fach: 'Englisch'},
		{id: 7,name: "Andere Person", email: "person@example.com", customIcon: "2008", test: "Platzhalter", itemID: '42', fach: ''},
		{id: 8,name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik'},
		{id: 9,name: "Erster Schüler mit Bart", email: "schueler9@example.com", customIcon: "1999", test: "", itemID: '1', fach: 'Französisch'},
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
	const search = ref("");

	const filterReset = () => {
		docsMultiselectFilterA.value = undefined;
		docsMultiselectFilterB.value = undefined;
	}

	function next(id: number) {
		const el = itemRefs.value.get(id + 1);
		if ((el !== null) && (el !== undefined))
			el.input.focus();
	}

	function previous(id: number) {
		const el = itemRefs.value.get(id - 1);
		if ((el !== null) && (el !== undefined))
			el.input.focus();
	}

	// Funktionen für die Sortierung	// Sortiere nur Name und Fach
	const cols2 = ref([
		{key: "name", label: "Name", sortable: true, span: 1},
		{key: "fach", label: "Fach", sortable: true, span: 0.5},
		{key: "email", label: "Note"},
	]);
	const sortByAndOrder = ref<SortByAndOrder | undefined>()

	const dataSorted = computed(() => {
		const temp = sortByAndOrder.value;
		if (temp === undefined)
			return data.value;
		const arr = [...data.value];
		arr.sort((a, b) => {
			switch (temp.key) {
				case 'name':
					return a.name.localeCompare(b.name, "de-DE");
				case 'fach':
					return a.fach.localeCompare(b.fach, "de-DE");
				default:
					return 0;
			}
		})
		return temp.order === true ? arr : arr.reverse();
	})
</script>
