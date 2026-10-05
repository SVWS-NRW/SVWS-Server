<template>
	<Story title="UiTableGrid" id="ui-table-grid" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}" :source="sourceCode">
		<Variant title="Default" id="Default">
			<ui-table-grid name="Schüler" :manager="() => gridManager">
				<template #header>
					<template v-for="col of gridManager.cols.values()" :key="col.name">
						<th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">
							<svws-ui-checkbox :model-value="(auswahl.length === gridManager.daten.length) && (auswahl.length > 0)"
								:indeterminate="(auswahl.length > 0) && (auswahl.length < gridManager.daten.length)"
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
						<ui-table-actions :actions="getRowActions(row)" :items="row" />
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

	import type { ComponentPublicInstance } from "vue";
	import { computed, reactive, ref, useTemplateRef } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";

	import { Note } from "@core/asd/types/Note";

	import { GridManager } from "./GridManager";
	import type { TableAction } from "./UiTableActions.vue";
	import UiTableActions from "./UiTableActions.vue";

	const state = reactive({
		add: false,
		delete: false,
		edit: false,
		showBulk: false,
		allChecked: false,
	});

	type Schueler = { id: number, vorname: string, nachname: string, birthYear: number, note: string | null };

	const schuelerArray2 = ref([
		{ id: 1, vorname: "Lena", nachname: "Müller", birthYear: 2005, note: Note.AUSREICHEND.getNoteKuerzel(2026) },
		{ id: 2, vorname: "Lukas", nachname: "Stark", birthYear: 2004, note: Note.BEFRIEDIGEND.getNoteKuerzel(2026) },
		{ id: 3, vorname: "Anton", nachname: "Meier", birthYear: 2003, note: Note.SEHR_GUT_MINUS.getNoteKuerzel(2026) },
		{ id: 4, vorname: "Hannah", nachname: "Strauch", birthYear: 2003, note: Note.BEFRIEDIGEND_MINUS.getNoteKuerzel(2026) },
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

	const countActiveActions = computed(() =>
		[state.add, state.edit, state.delete].filter(Boolean).length
	);

	const gridManager = computed(() => new GridManager<string, Schueler, Array<Schueler>>({
		daten: computed(() => schuelerArray2.value),
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

	const gridManagerNoten = computed(() => new GridManager<string, Schueler, Array<Schueler>>({
		daten: computed(() => schuelerArray2.value),
		getRowKey: row => `ID_${row.id}`,
		columns: [
			{ kuerzel: "Vorname", name: "Vorname", width: "1fr" },
			{ kuerzel: "Nachname", name: "Nachname", width: '1fr' },
			{ kuerzel: "Note", name: "Note", width: '1fr' },
		],
	}));

	function inputNote(note: string | null, index: number) {
		const key = 'Note_' + index;
		const setter = (value: string | null) => console.log(value);
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManagerNoten.value.applyInputNote(key, 3, index, element, setter, 2026);
			if (input !== null) {
				gridManagerNoten.value.update(key, note);
			}
		};
	}

	function getRowActions(schueler: Schueler): TableAction[] {
		const actions: TableAction[] = [];

		if (state.edit) {
			actions.push({
				label: "Hinzufügen",
				iconClasses: "i-ri-add-line",
				action: () => alert(`Hinzufügen: ${schueler.vorname} ${schueler.nachname}`),
			});
		}

		if (state.add) {
			actions.push({
				label: "Bearbeiten",
				iconClasses: "i-ri-edit-2-line",
				action: () => alert(`Bearbeiten: ${schueler.vorname} ${schueler.nachname}`),
			});
		}

		if (state.delete) {
			actions.push({
				label: "Trash",
				trash: true,
				action: () => alert(`Löschen: ${schueler.vorname} ${schueler.nachname}`),
			});
		}

		return actions;
	};

	const bulkActions = computed<TableAction[]>(() => {
		const actions: TableAction[] = [];

		if (state.edit) {
			actions.push({
				label: "Hinzufügen",
				iconClasses: "i-ri-add-line",
				action: () => {
					alert(
						"Hinzufügen:\n"
							+ auswahl.value
								.map(schueler => `${schueler.vorname} ${schueler.nachname}`)
								.join("\n")
					);
				},
				disabled: auswahl.value.length === 0,
			});
		}

		if (state.add) {
			actions.push({
				label: "Bearbeiten",
				iconClasses: "i-ri-edit-2-line",
				action: () => {
					alert(
						"Bearbeiten:\n"
							+ auswahl.value
								.map(schueler => `${schueler.vorname} ${schueler.nachname}`)
								.join("\n")
					);
				},
				disabled: auswahl.value.length === 0,
			});
		}

		if (state.delete) {
			actions.push({
				label: "Löschen",
				trash: true,
				action: () => {
					alert(
						"Löschen:\n"
							+ auswahl.value
								.map(schueler => `${schueler.vorname} ${schueler.nachname}`)
								.join("\n")
					);
				},
				disabled: auswahl.value.length === 0,
			});
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
