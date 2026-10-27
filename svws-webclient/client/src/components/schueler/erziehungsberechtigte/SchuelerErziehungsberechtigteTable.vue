<template>
	<ui-table-grid class="max-w-full [&_tbody>tr]:cursor-pointer [&_tbody>tr.bg-ui-selected:hover]:bg-ui-selected"
		:manager="() => gridManager"
		name="Erziehungsberechtigte">
		<template #header>
			<template v-for="col of gridManager.cols.values()" :key="col.name">
				<th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">
					<svws-ui-checkbox :model-value="bulkAllChecked"
						:indeterminate="bulkIntermediate"
						:disabled="sortedData.length === 0"
						aria-label="Alle Erziehungsberechtigten auswählen"
						@update:model-value="toggleBulkAll" />
				</th>
				<th v-else-if="col.kuerzel === 'RowActions'" />
				<th v-else-if="col.kuerzel === 'Anschreiben'" class="flex items-center justify-center">
					<svws-ui-tooltip>
						<span class="icon i-ri-mail-send-line" />
						<template #content>{{ col.name }}</template>
					</svws-ui-tooltip>
				</th>
				<th v-else-if="col.kuerzel === '2. Person'" class="flex items-center justify-center">
					<svws-ui-tooltip>
						<span>{{ col.kuerzel }}</span>
						<template #content>{{ col.name }}</template>
					</svws-ui-tooltip>
				</th>
				<th v-else class="flex items-start justify-center">
					{{ col.kuerzel }}
				</th>
			</template>
		</template>
		<template #default="{ row }">
			<td v-if="hatKompetenzUpdate" class="flex items-start justify-center" @click.stop>
				<svws-ui-checkbox :model-value="isSelected(row)"
					@update:model-value="toggleBulkSelection(row, $event)" />
			</td>
			<td class="flex items-start justify-center">{{ (row.idErzieherArt === null) ? '' : (erzieherartenState.erzieherarten.byId.get(row.idErzieherArt)?.bezeichnung ?? '') }}</td>
			<td class="flex items-start justify-center">{{ row.vorname }} {{ row.nachname }}</td>
			<td class="flex items-start justify-center">{{ row.eMail ? row.eMail : '—' }}</td>
			<td class="flex items-start justify-center">{{ adresse(row) }}</td>
			<td class="flex items-center justify-center">{{ row.erhaeltAnschreiben ? '✓' : '×' }}</td>
			<td class="flex items-center justify-center" @click.stop>
				<svws-ui-button v-if="hatKompetenzUpdate && isSuffix1(row.id) && !hasSuffix2(row.id)" class="h-6! px-2! py-0! text-sm" @click="emit('openModalForPos2', row)"> + </svws-ui-button>
			</td>
			<td v-if="hatKompetenzUpdate" class="flex justify-center" @click.stop>
				<ui-table-actions class="pr-3" :actions="getRowActions(row)" />
			</td>
		</template>
		<template v-if="hatKompetenzUpdate" #footer>
			<td class="col-span-full">
				<div class="w-full flex items-center justify-end py-1">
					<ui-table-actions class="pr-3" :actions="footerActions" always-visible />
				</div>
			</td>
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import { computed, shallowRef, watch } from "vue";

	import type { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
	import { AdressenUtils } from "@core/core/utils/AdressenUtils";
	import type { List } from "@core/java/util/List";
	import { useErzieherartenState } from "@ui/states/kataloge/ErzieherartenState";
	import { useOrteState } from "@ui/states/kataloge/OrteState";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { TableAction } from "@ui/ui/controls/tablegrid/UiTableActions.vue";

	const props = defineProps<{
		data: () => List<ErzieherStammdaten>;
		hatKompetenzUpdate: boolean;
		erzieher: ErzieherStammdaten | undefined;
	}>();

	const emit = defineEmits<{
		'update:erzieher': [value: ErzieherStammdaten | undefined];
		'openModalForPos2': [item: ErzieherStammdaten];
		'deleteErzieher': [items: ErzieherStammdaten[]];
		'addModal': [];
	}>();

	const orteState = useOrteState();
	const erzieherartenState = useErzieherartenState();
	const selectedErz = shallowRef<ErzieherStammdaten[]>([]);

	const suffix2Ids = computed(() => new Set(sortedData.value.map(e => e.id)));
	const sortedData = computed<ErzieherStammdaten[]>(() => [...props.data()].sort((a, b) => {
		const ersteErzId = Math.floor(a.id / 10);
		const zweiteErzId = Math.floor(b.id / 10);
		if (ersteErzId !== zweiteErzId) {
			return ersteErzId - zweiteErzId;
		}
		return a.id - b.id;
	}));


	watch(sortedData, (liste) => {
		const ids = new Set(selectedErz.value.map(e => e.id));
		selectedErz.value = liste.filter(e => ids.has(e.id));
	});

	const gridManager = new GridManager<number, ErzieherStammdaten, ErzieherStammdaten[]>({
		daten: sortedData,
		getRowKey: erzieher => erzieher.id,
		allowEmptyRowSelection: true,
		columns: [
			...(props.hatKompetenzUpdate ? [{ kuerzel: "Auswahl", name: "Auswahl", width: "2rem", hideable: false }] : []),
			{ kuerzel: "Art", name: "Art", width: "minmax(8rem, 2fr)" },
			{ kuerzel: "Name", name: "Name", width: "minmax(10rem, 2fr)" },
			{ kuerzel: "E-Mail", name: "E-Mail", width: "minmax(10rem, 2fr)" },
			{ kuerzel: "Adresse", name: "Adresse", width: "minmax(14rem, 3fr)" },
			{ kuerzel: "Anschreiben", name: "Erhält Anschreiben", width: "4rem" },
			{ kuerzel: "2. Person", name: "Weiteres Elternteil hinzufügen", width: "6rem" },
			...(props.hatKompetenzUpdate ? [{ kuerzel: "RowActions", name: "Zeilenaktionen", width: "4rem", hideable: false }] : []),
		],
	});

	// Zeilenauswahl im Grid -> aktuell angezeigter Erziehungsberechtigter
	watch(() => gridManager.focusRow, (row) => {
		if (row === null) {
			return;
		}
		const neu = sortedData.value[row] as ErzieherStammdaten | undefined;
		if ((neu !== undefined) && (neu.id !== props.erzieher?.id)) {
			emit('update:erzieher', neu);
		}
	});

	// Aktuell angezeigter Erziehungsberechtigter -> Zeilenauswahl im Grid
	watch([() => props.erzieher, sortedData], () => {
		if (props.erzieher === undefined) {
			return;
		}
		const id = props.erzieher.id;
		const index = sortedData.value.findIndex(e => e.id === id);
		if ((index >= 0) && (gridManager.focusRow !== index)) {
			gridManager.doFocusRowIfNotFocussed(index);
		}
	}, { immediate: true });

	const bulkAllChecked = computed(() => (selectedErz.value.length > 0) && (selectedErz.value.length === sortedData.value.length));

	const bulkIntermediate = computed(() => (selectedErz.value.length > 0) && (selectedErz.value.length < sortedData.value.length));

	const footerActions = computed<TableAction[]>(() => [
		{
			label: "Erziehungsberechtigten hinzufügen",
			iconClasses: "i-ri-add-line",
			action: () => emit("addModal"),
		}, {
			label: "Ausgewählte Erziehungsberechtigte löschen",
			trash: true,
			action: () => emit("deleteErzieher", selectedErz.value),
			disabled: selectedErz.value.length === 0,
		},
	]);

	function getRowActions(erzieher: ErzieherStammdaten): TableAction[] {
		return [{
			label: "Erziehungsberechtigten löschen",
			trash: true,
			action: () => emit("deleteErzieher", [erzieher]),
		}];
	}

	function adresse(erzieher: ErzieherStammdaten): string {
		const strasse = AdressenUtils.combineStrasse(erzieher.strassenname ?? "", erzieher.hausnummer ?? "", erzieher.hausnummerZusatz ?? "") ?? "";
		const ort = (erzieher.wohnortID === null) ? undefined : orteState.orte.byId.get(erzieher.wohnortID);
		return (ort === undefined) ? strasse : `${strasse}, ${ort.plz ?? ''} ${ort.ortsname}`;
	}

	function isSuffix1(id: number): boolean {
		return id % 10 === 1;
	}

	function hasSuffix2(id: number): boolean {
		return suffix2Ids.value.has(id + 1);
	}

	function isSelected(erzieher: ErzieherStammdaten): boolean {
		return selectedErz.value.some(e => e.id === erzieher.id);
	}

	function toggleBulkAll(value: boolean): void {
		selectedErz.value = value ? [...sortedData.value] : [];
	}

	function toggleBulkSelection(erzieher: ErzieherStammdaten, value: boolean): void {
		selectedErz.value = value
			? [...selectedErz.value, erzieher]
			: selectedErz.value.filter(e => e.id !== erzieher.id);
	}

</script>
