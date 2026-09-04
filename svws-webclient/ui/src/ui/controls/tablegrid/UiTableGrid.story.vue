<template>
	<Story title="UiTableGrid" id="ui-table-grid" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}" :source="sourceCode">
		<Variant title="Default" id="Default">
			<ui-table-grid name="Schüler" :manager="() => gridManager">
				<template #header>
					<template v-for="col of gridManager.cols.values()" :key="col.name">
						<th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">
							<svws-ui-checkbox :model-value="(auswahl.length === gridManager.daten.size()) && (auswahl.length > 0)"
								:indeterminate="(auswahl.length > 0) && (auswahl.length < gridManager.daten.size())"
								@update:model-value="toggleAll" />
						</th>
						<th v-else-if="col.kuerzel === 'RowActions'" />
						<th v-else class="flex items-start justify-center">
							{{ col.kuerzel }}
						</th>
					</template>
				</template>
				<template #default="{ row }">
					<td class="flex items-center justify-center">
						<svws-ui-checkbox :model-value="auswahl.includes(row)" @update:model-value="(value: boolean) => toggleSelection(row, value)" />
					</td>
					<td class="flex items-start justify-center">
						{{ row.vorname }}
					</td>
					<td class="flex items-start justify-center">
						{{ row.nachname }}
					</td>
					<td class="flex items-start justify-center">
						{{ row.birthYear }}
					</td>
					<td v-if="countActiveActions > 0">
						<ui-table-actions :actions="rowActions" :items="row" />
					</td>
				</template>
				<template v-if="state.showBulk" #footer>
					<td v-if="countActiveActions > 0" class="col-span-full">
						<div class="w-full flex items-center justify-end py-1">
							<ui-table-actions :actions="bulkActions" always-visible :items="auswahl" />
						</div>
					</td>
				</template>
			</ui-table-grid>
			<template #controls>
				<div class="text-headline-sm">
					Actions
				</div>
				<HstCheckbox v-model="state.showBulk" title="Bulk Actions" />
				<HstCheckbox v-model="state.edit" title="Hinzufügen" />
				<HstCheckbox v-model="state.add" title="Bearbeiten" />
				<HstCheckbox v-model="state.delete" title="Löschen" />
			</template>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { GridManager } from "./GridManager";
	import { computed, reactive, ref } from "vue";
	import UiTableActions from "./UiTableActions.vue";
	import type { TableActions } from "./UiTableActions.vue";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";

	const state = reactive({
		add: false,
		delete: false,
		edit: false,
		showBulk: false,
		allChecked: false,
	});

	type Schueler = { id: number, vorname: string, nachname: string, birthYear: number };

	const schuelerArray = ref([
		{ id: 1, vorname: "Lena", nachname: "Müller", birthYear: 2005 },
		{ id: 2, vorname: "Lukas", nachname: "Stark", birthYear: 2004 },
		{ id: 3, vorname: "Anton", nachname: "Meier", birthYear: 2003 },
		{ id: 4, vorname: "Hannah", nachname: "Strauch", birthYear: 2003 },
	]);

	const auswahl = ref<Schueler[]>([]);

	function toggleSelection(schueler: Schueler, value: boolean): void {
		if (value) {
			auswahl.value.push(schueler);
		} else {
			const idx = auswahl.value.indexOf(schueler);
			if (idx !== -1) {
				auswahl.value.splice(idx, 1);
			}
		}
	}

	function toggleAll(value: boolean): void {
		auswahl.value = value ? [...gridManager.value.daten] : [];
	}

	const schueler = new ArrayList<Schueler>();
	for (const item of schuelerArray.value) {
		schueler.add(item);
	}

	const countActiveActions = computed(() =>
		[state.add, state.edit, state.delete].filter(Boolean).length
	);

	const gridManager = computed(() => new GridManager<string, Schueler, List<Schueler>>({
		daten: computed(() => schueler),
		getRowKey: row => `ID_${row.id}`,
		columns: [
			{ kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Vorname", name: "Vorname", width: "1fr" },
			{ kuerzel: "Nachname", name: "Nachname", width: '1fr' },
			{ kuerzel: "Geburtsjahr", name: "Geburtsjahr", width: '1fr' },
			...(countActiveActions.value > 0
				? [{ kuerzel: "RowActions", name: "Zeilenaktionen", width: (countActiveActions.value * 2) + 'em' }]
				: []),
		],
	}));

	const rowActions = computed(() => {
		const actions: TableActions<Schueler>[] = [];

		if (state.edit) {
			actions.push({ label: "Hinzufügen", iconClasses: "i-ri-add-line", action: (item: Schueler) => alert(`Hinzufügen: ${item.vorname} ${item.nachname}`) });
		}

		if (state.add) {
			actions.push({ label: "Bearbeiten", iconClasses: "i-ri-edit-2-line", action: (item: Schueler) => alert(`Bearbeiten: ${item.vorname} ${item.nachname}`) });
		}

		if (state.delete) {
			actions.push({ label: "Trash", trash: true, action: (item: Schueler) => alert(`Löschen: ${item.vorname} ${item.nachname}`) });
		}

		return actions;
	});

	const bulkActions = computed(() => {
		const actions: TableActions<Schueler[]>[] = [];

		if (state.edit) {
			actions.push(
				{
					label: "Hinzufügen",
					iconClasses: "i-ri-add-line",
					action: () => alert("Hinzufügen:\n" + auswahl.value.map(s => `${s.vorname} ${s.nachname}`).join("\n")),
					disabled: auswahl.value.length === 0,
				}
			);
		}

		if (state.add) {
			actions.push(
				{
					label: "Bearbeiten",
					iconClasses: "i-ri-edit-2-line",
					action: (items: Schueler[]) => alert("Bearbeiten:\n" + items.map(s => `${s.vorname} ${s.nachname}`).join("\n")),
					disabled: auswahl.value.length === 0,
				}
			);
		}

		if (state.delete) {
			actions.push(
				{
					label: "Löschen",
					trash: true,
					action: (items: Schueler[]) => alert("Löschen:\n" + items.map(s => `${s.vorname} ${s.nachname}`).join("\n")),
					disabled: auswahl.value.length === 0,
				}
			);
		}

		return actions;
	});

	const sourceCode = computed(() => {
		const rowActionButtons = [
			state.add ? `<svws-ui-button v-if="state.add" type="icon" @click="...">\n\t\t\t\t\t\t<span class="icon i-ri-add-line" />\n\t\t\t\t\t</svws-ui-button>` : "",
			state.delete ? `<svws-ui-button v-if="state.delete" type="trash" @click="..." />` : "",
			state.edit ? `<svws-ui-button v-if="state.default" type="icon" @click="..." >\n\t\t\t\t\t\t<span class="icon i-ri-check-line" />\n\t\t\t\t\t</svws-ui-button>` : "",
		].filter(Boolean);
		const bulkActionButtons = [
			state.add ? `<svws-ui-button v-if="state.primary" type="icon" @click="..." :disabled >\n\t\t\t\t\t<span class="icon i-ri-add-line" />\n\t\t\t\t</svws-ui-button>` : "",
			state.edit ? `<svws-ui-button v-if="state.default" type="icon" @click="..." :disabled >\n\t\t\t\t\t<span class="icon i-ri-check-line" />\n\t\t\t\t</svws-ui-button>` : "",
			state.delete ? `<svws-ui-button v-if="state.trash" type="trash" @click="..." :disabled />` : "",
		].filter(Boolean);

		const rowActionsBlock = countActiveActions.value > 0 ? [
			`		<ui-row-actions v-if="countActiveActions > 0">`,
			`			<template #default>`,
			`				<div class="flex items-center justify-end">`,
			...rowActionButtons.map(b => `					${b}`),
			`				</div>`,
			`			</template>`,
			`		</ui-row-actions>`,
		].join("\n") : "";

		const bulkActionsBlock = (countActiveActions.value > 0) && state.showBulk ? [
			`    <template #footer>`,
			`        <td class="col-span-full">`,
			`            <div class="w-full flex items-center justify-end py-1">`,
			...bulkActionButtons.map(b => `                ${b}`),
			`            </div>`,
			`        </td>`,
			`    </template>`,
		].join("\n") : "";

		return [
			`<ui-table-grid name="Schüler" :manager="() => gridManager">`,
			`    <template #header>`,
			`        <template v-for="col of gridManager.cols.values()" :key="col.name">`,
			`            <th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">`,
			`                <svws-ui-checkbox v-model="state.allChecked" @update:model-value="toggleChecked" />`,
			`            </th>`,
			`            <th v-else-if="col.kuerzel === 'RowActions'" />`,
			`            <th v-else class="flex items-start justify-center">`,
			`                {{ col.kuerzel }}`,
			`            </th>`,
			`        </template>`,
			`    </template>`,
			`    <template #default="{ row }">`,
			`        <td class="flex items-center justify-center">`,
			`            <svws-ui-checkbox v-model="row.checked" />`,
			`        </td>`,
			`        <td class="flex items-start justify-center">`,
			`            {{ row.vorname }}`,
			`        </td>`,
			`        <td class="flex items-start justify-center">`,
			`            {{ row.nachname }}`,
			`        </td>`,
			`        <td class="flex items-start justify-center">`,
			`            {{ row.birthYear }}`,
			`        </td>`,
			rowActionsBlock,
			`    </template>`,
			bulkActionsBlock,
			`</ui-table-grid>`,
		].filter(Boolean).join("\n");
	});

</script>
