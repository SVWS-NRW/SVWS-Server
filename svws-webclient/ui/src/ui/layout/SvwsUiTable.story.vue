<template>
	<Story title="Table" id="svws-ui-table" icon="ri:table-line" :layout="{ type: 'single', iframe: false }" auto-props-disabled>
		<template #docs>
			<SvwsUiTableStory />
		</template>
		<Variant title="Default" id="default">
			<div class="px-3">
				<svws-ui-content-card>
					<svws-ui-table v-model="selectedRows" v-model:clicked="clickedRow" v-model:hidden-columns="state.hiddenColumns" :items="data"
						:columns="cols" :row-actions :clickable="state.clickable" :selectable="state.selectable"
						:disable-header="state.disableHeader" :disable-footer="state.disableFooter" :count="state.count"
						:filtered="docsMultiselectFilterA?.length > 0 || docsMultiselectFilterB?.length > 0"
						:toggle-columns="state.toggleColumns" :filter-reset :type="state.typeGrid ? 'grid' : 'table'" :lock-selectable="state.lockSelectable">
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
						<template #cell(rowActions)="{ rowData }">
							<ui-table-actions :actions="rowActions" :items="rowData" />
						</template>
						<template #actions v-if="state.showBulk">
							<ui-table-actions :actions="bulkActions" :items="[]" always-visible />
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
				<div class="text-headline-sm">
					Actions
				</div>
				<HstCheckbox v-model="state.showBulk" title="Bulk Actions" />
				<HstCheckbox v-model="state.add" title="Hinzufügen" />
				<HstCheckbox v-model="state.delete" title="Löschen" />
				<HstCheckbox v-model="state.accept" title="Bestätigen" />
				<HstCheckbox v-model="state.details" title="Details" />
			</template>
		</Variant>
		<Variant title="Inputs" id="inputs">
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
		<Variant title="Sortierung" id="sortierung">
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
	import type { TableActions } from "../controls/tablegrid/UiTableActions.vue";
	import type { DataTableColumn, SortByAndOrder } from "@ui/types.js";

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
		add: false,
		delete: false,
		accept: false,
		details: false,
		showBulk: false,
	});

	const rowActions = computed<TableActions<DataType>[]>(() => {
		const actions: TableActions<DataType>[] = [];

		if (state.add) {
			actions.push({ label: "Hinzufügen", iconClasses: "i-ri-add-line", action: (item: DataType) => alert(`Hinzufügen: ${item.name}`), disabled: true });
		}

		if (state.delete) {
			actions.push({ label: "Löschen", action: (item: DataType) => alert(`Löschen: ${item.name}`), trash: true });
		}

		if (state.accept) {
			actions.push({ label: "Bestätigen", iconClasses: "i-ri-check-line", action: (item: DataType) => alert(`Bestätigen: ${item.name}`) });
		}

		if (state.details) {
			actions.push({ label: "Details", iconClasses: "i-ri-eye-line", action: (item: DataType) => alert(`Default: ${item.name}`) });
		}

		return actions;
	});

	const bulkActions = computed<TableActions<DataType[]>[]>(() => {
		const actions: TableActions<DataType[]>[] = [];

		if (state.add) {
			actions.push({ label: "Hinzufügen", iconClasses: "i-ri-add-line", action: () => alert(`Hinzufügen: Bulk`), disabled: true });
		}

		if (state.delete) {
			actions.push({ label: "Löschen", action: () => alert(`Löschen: Bulk`), trash: true });
		}

		if (state.accept) {
			actions.push({ label: "Bestätigen", iconClasses: "i-ri-check-line", action: () => alert(`Bestätigen: Bulk`) });
		}

		if (state.details) {
			actions.push({ label: "Details", iconClasses: "i-ri-eye-line", action: () => alert(`Default: Bulk`) });
		}

		return actions;
	});

	const cols = computed((): DataTableColumn[] => [
		{ key: "name", label: "Name", sortable: true, span: 1, toggleInvisible: true },
		{ key: "name", label: "Name", sortable: true, span: 1, toggleInvisible: true },
		{ key: "fach", label: "Fach", span: 0.5, toggle: true },
		{ key: "email", label: "E-Mail", toggle: true },
		{ key: "customIcon", label: "Icon", tooltip: "Icon statt Text", sortable: true, span: 0.25 },
		{ key: "test", label: "Column", sortable: true },
		{ key: "itemID", label: "ID", tooltip: "Identifikation", fixedWidth: 4, align: "right", toggle: true },
		...(rowActions.value.length > 0
			? [{ key: "rowActions", label: "", tooltip: "Aktionen", fixedWidth: (rowActions.value.length * 2.3), align: "right" }] as DataTableColumn[]
			: []),
	]);

	type DataType = { id: number, name: string, email: string, customIcon: string, test: string, itemID: string, fach: string };
	const data = ref<DataType[]>([
		{ id: 0, name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch' },
		{ id: 1, name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "Neu", itemID: '99', fach: 'Englisch' },
		{ id: 2, name: "Andere Person 6", email: "person6@example.com", customIcon: "2008", test: "Platzhalter", itemID: '42', fach: '' },
		{ id: 3, name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik' },
		{ id: 4, name: "Erster Schüler", email: "schueler4example.com", customIcon: "1999", test: "Data", itemID: '1', fach: '' },
		{ id: 5, name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch' },
		{ id: 6, name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "Neu", itemID: '99', fach: 'Englisch' },
		{ id: 7, name: "Andere Person", email: "person@example.com", customIcon: "2008", test: "Platzhalter", itemID: '42', fach: '' },
		{ id: 8, name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik' },
		{ id: 9, name: "Erster Schüler mit Bart", email: "schueler9@example.com", customIcon: "1999", test: "", itemID: '1', fach: 'Französisch' },
		{ id: 10, name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch' },
		{ id: 11, name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "", itemID: '99', fach: 'Englisch' },
		{ id: 12, name: "Andere Person", email: "person@example.com", customIcon: "2008", test: "Platzhalter", itemID: '42', fach: '' },
		{ id: 13, name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik' },
		{ id: 14, name: "Erster Schüler", email: "schueler@example.com", customIcon: "1999", test: "Data", itemID: '1', fach: '' },
		{ id: 15, name: "Testlisa Testschülerin", email: "lisa@example.com", customIcon: "2023", test: "Zum Testen", itemID: '3', fach: 'Deutsch' },
		{ id: 16, name: "Generischer Name", email: "name@example.com", customIcon: "2022", test: "Neu", itemID: '99', fach: 'Englisch' },
		{ id: 17, name: "Andere Person", email: "person@example.com", customIcon: "2008", test: "", itemID: '42', fach: '' },
		{ id: 18, name: "Nico Beispiel", email: "nico@example.com", customIcon: "2022", test: "Beispiel", itemID: '0', fach: 'Musik' },
		{ id: 19, name: "Erster Schüler", email: "schueler@example.com", customIcon: "1999", test: "Data", itemID: '1', fach: '' },
	]);

	const selectedRows = ref([]);
	const docsMultiselectFilterA = ref<any>();
	const docsMultiselectFilterB = ref<any>();
	const clickedRow = ref<any>(undefined);
	const search = ref("");

	const filterReset = () => {
		docsMultiselectFilterA.value = undefined;
		docsMultiselectFilterB.value = undefined;
	};

	function next(id: number) {
		const el = itemRefs.value.get(id + 1);
		if ((el !== null) && (el !== undefined)) {
			el.input.focus();
		}
	}

	function previous(id: number) {
		const el = itemRefs.value.get(id - 1);
		if ((el !== null) && (el !== undefined)) {
			el.input.focus();
		}
	}

	// Funktionen für die Sortierung	// Sortiere nur Name und Fach
	const cols2 = ref([
		{ key: "name", label: "Name", sortable: true, span: 1 },
		{ key: "fach", label: "Fach", sortable: true, span: 0.5 },
		{ key: "email", label: "Note" },
	]);
	const sortByAndOrder = ref<SortByAndOrder | undefined>();

	const dataSorted = computed(() => {
		const temp = sortByAndOrder.value;
		if (temp === undefined) {
			return data.value;
		}
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
		});
		return temp.order === true ? arr : arr.reverse();
	});
</script>
