<template>
	<ui-table-grid class="max-h-72! max-w-full [&_tbody>tr]:cursor-pointer"
		:manager="() => gridManager"
		name="Weitere Telefonnummern">
		<template #header>
			<template v-for="col of gridManager.cols.values()" :key="col.name">
				<th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">
					<svws-ui-checkbox :model-value="bulkAllChecked"
						:indeterminate="bulkIntermediate"
						:disabled="gridManager.daten.size() === 0"
						aria-label="Alle Telefonnummern auswählen"
						@update:model-value="toggleBulkAll" />
				</th>
				<th v-else-if="col.kuerzel === 'RowActions'" />
				<th v-else class="flex items-start justify-center">
					{{ col.kuerzel }}
				</th>
			</template>
		</template>
		<template #default="{ row }">
			<td v-if="!readonly" class="flex items-start justify-center" @click.stop>
				<svws-ui-checkbox :model-value="selectedTelefonnummern.includes(row)"
					@update:model-value="toggleBulkSelection(row, $event)" />
			</td>
			<td :ref="row.id === scrollTargetId ? setScrollTargetElement : undefined" class="flex items-start justify-center" @click="emit('openPatchModal', row)">
				{{ getBezeichnungTelefonart(row.idTelefonArt) }}
			</td>
			<td class="flex items-start justify-center" @click="emit('openPatchModal', row)">{{ row.telefonnummer }}</td>
			<td class="flex items-start justify-center" @click="emit('openPatchModal', row)">{{ row.bemerkung }}</td>
			<td class="flex items-center justify-center" @click="emit('openPatchModal', row)">{{ row.istGesperrt ? 'ja' : 'nein' }}</td>
			<td v-if="!readonly" class="flex items-start justify-center" @click.stop>
				<ui-table-actions :actions="getRowActions(row)" />
			</td>
		</template>
		<template v-if="!readonly" #footer>
			<td class="col-span-full">
				<div class="w-full flex items-center justify-end py-1">
					<ui-table-actions :actions="footerActions" always-visible />
				</div>
			</td>
		</template>
	</ui-table-grid>
</template>
<script lang="ts" setup>

	import type { ComponentPublicInstance } from "vue";
	import { computed, nextTick, ref, shallowRef, watch } from "vue";

	import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
	import type { List } from "@core/java/util/List";
	import { useTelefonartenState } from "@ui/states/kataloge/TelefonartenState";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { TableAction } from "@ui/ui/controls/tablegrid/UiTableActions.vue";

	import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

	const props = defineProps<{
		readonly: boolean;
	}>();

	const emit = defineEmits<{
		'openPatchModal': [item: SchuelerTelefon];
		'deleteTelefonnummern': [items: SchuelerTelefon[]];
		'addModal': [];
	}>();

	const schuelerAuswahlState = useSchuelerAuswahlState();

	watch(() => schuelerAuswahlState.listTelefoneintraege.size(), async (neueAnzahl, alteAnzahl) => {
		if (neueAnzahl > alteAnzahl) {
			await scrollToLastElement();
		}
	});

	const telefonartenState = useTelefonartenState();
	const selectedTelefonnummern = shallowRef<SchuelerTelefon[]>([]);
	const scrollTargetId = ref<number | null>(null);
	const scrollTargetElement = ref<HTMLElement | null>(null);

	const gridManager = computed(() => new GridManager<number, SchuelerTelefon, List<SchuelerTelefon>>({
		daten: computed(() => schuelerAuswahlState.listTelefoneintraege),
		getRowKey: telefonnummer => telefonnummer.id,
		columns: [
			...(props.readonly ? [] : [{ kuerzel: "Auswahl", name: "Auswahl", width: "2rem", hideable: false }]),
			{ kuerzel: "Telefonart", name: "Telefonart", width: "minmax(8rem, 1fr)" },
			{ kuerzel: "Telefonnummer", name: "Telefonnummer", width: "minmax(10rem, 1fr)" },
			{ kuerzel: "Bemerkung", name: "Bemerkung", width: "minmax(10rem, 2fr)" },
			{ kuerzel: "Gesperrt", name: "Gesperrt", width: "4rem" },
			...(props.readonly ? [] : [{ kuerzel: "RowActions", name: "Zeilenaktionen", width: "4rem", hideable: false }]),
		],
	}));

	// Gleicht die Selektion mit der aktuellen Liste ab (gelöschte Einträge entfernen, geänderte Objekte ersetzen)
	watch(() => schuelerAuswahlState.listTelefoneintraege, (liste) => {
		const ids = new Set(selectedTelefonnummern.value.map(t => t.id));
		selectedTelefonnummern.value = [...liste].filter(t => ids.has(t.id));
	}, { deep: true });

	function getRowActions(telefonnummer: SchuelerTelefon): TableAction[] {
		return [
			{
				label: "Telefonnummer bearbeiten",
				iconClasses: "i-ri-edit-2-line",
				action: () => emit("openPatchModal", telefonnummer),
			},
			{
				label: "Telefonnummer löschen",
				trash: true,
				action: () => emit("deleteTelefonnummern", [telefonnummer]),
			},
		];
	}

	const bulkAllChecked = computed(() =>
		(selectedTelefonnummern.value.length === gridManager.value.daten.size())
		&& (selectedTelefonnummern.value.length > 0)
	);

	const bulkIntermediate = computed(() =>
		(selectedTelefonnummern.value.length > 0)
		&& (selectedTelefonnummern.value.length < gridManager.value.daten.size())
	);

	const footerActions = computed<TableAction[]>(() => [
		{
			label: "Telefonnummer hinzufügen",
			iconClasses: "i-ri-add-line",
			action: () => emit("addModal"),
		}, {
			label: "Ausgewählte Telefonnummern löschen",
			trash: true,
			action: () => emit("deleteTelefonnummern", selectedTelefonnummern.value),
			disabled: selectedTelefonnummern.value.length === 0,
		},
	]);

	function getBezeichnungTelefonart(idTelefonArt: number): string {
		return telefonartenState.telefonarten.byId.get(idTelefonArt)?.bezeichnung ?? "";
	}

	function setScrollTargetElement(element: Element | ComponentPublicInstance | null): void {
		if (element instanceof HTMLElement) {
			scrollTargetElement.value = element;
		} else {
			scrollTargetElement.value = null;
		}
	}
	function toggleBulkAll(value: boolean): void {
		selectedTelefonnummern.value = value ? [...gridManager.value.daten] : [];
	}

	function toggleBulkSelection(telefonnummer: SchuelerTelefon, value: boolean): void {
		selectedTelefonnummern.value = value
			? [...selectedTelefonnummern.value, telefonnummer]
			: selectedTelefonnummern.value.filter(t => t.id !== telefonnummer.id);
	}

	async function scrollToLastElement(): Promise<void> {
		const letzteTelefonnummer = schuelerAuswahlState.listTelefoneintraege.getLast();
		scrollTargetId.value = letzteTelefonnummer.id;
		await nextTick();
		scrollTargetElement.value?.scrollIntoView({ block: "nearest" });
		scrollTargetId.value = null;
	}

</script>
