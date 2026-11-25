<template>
	<ui-table-grid class="max-h-72! max-w-full" :class="{ '[&_tbody>tr]:cursor-pointer': !readonly }"
		:manager="() => gridManager"
		name="Teilstandorte">
		<template #header>
			<template v-for="col of gridManager.cols.values()" :key="col.name">
				<th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">
					<svws-ui-checkbox :model-value="bulkAllChecked"
						:indeterminate="bulkIntermediate"
						:disabled="gridManager.daten.size() === 0"
						aria-label="Alle Teilstandorte auswählen"
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
				<svws-ui-checkbox :model-value="selectedTeilstandorte.includes(row)"
					@update:model-value="toggleBulkSelection(row, $event)" />
			</td>
			<td :ref="row.adrMerkmal === scrollTargetId ? setScrollTargetElement : undefined" class="flex items-start justify-center" @click="openPatchModal(row)">
				<span class="inline-flex items-center justify-center gap-1">
					<svws-ui-tooltip v-if="selectedTeilstandorte.includes(row) && row.referenziertInAnderenTabellen">
						<span class="icon icon-ui-danger i-ri-alert-line" />
						<template #content>
							Dieser Teilstandort ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.
						</template>
					</svws-ui-tooltip>
					{{ row.adrMerkmal }}
				</span>
			</td>
			<td v-for="attribute of VALUE_ATTRIBUTES" :key="attribute" class="flex items-start justify-center" @click="openPatchModal(row)">
				<span v-if="isEmpty(row[attribute])" class="text-ui-secondary">—</span>
				<template v-else>{{ row[attribute] }}</template>
			</td>
			<td v-if="!readonly" class="flex items-start justify-center" @click.stop>
				<ui-table-actions :actions="getRowActions(row)" />
			</td>
		</template>
		<template v-if="!readonly" #footer>
			<template v-for="col of gridManager.cols.values()" :key="col.name">
				<td v-if="col.kuerzel === 'RowActions'" class="flex items-start justify-center">
					<ui-table-actions :actions="footerActions" always-visible />
				</td>
				<td v-else />
			</template>
		</template>
	</ui-table-grid>
</template>

<script lang="ts" setup>

	import type { ComponentPublicInstance } from "vue";
	import { computed, nextTick, ref, shallowRef, watch } from "vue";

	import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
	import type { List } from "@core/java/util/List";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { TableAction } from "@ui/ui/controls/tablegrid/UiTableActions.vue";

	const props = defineProps<{
		readonly: boolean;
		getListTeilstandorte: () => List<Teilstandort>;
	}>();

	const emit = defineEmits<{
		'openPatchModal': [item: Teilstandort];
		'deleteTeilstandorte': [items: Teilstandort[]];
		'addModal': [];
	}>();

	/** Die Attribute der Teilstandorte, die in der Reihenfolge der Spalten nach dem Merkmal angezeigt werden */
	const VALUE_ATTRIBUTES: ReadonlyArray<keyof Teilstandort> = ["strassenname", "hausNr", "hausNrZusatz", "plz", "ort", "kuerzel", "bemerkung"];

	watch(() => props.getListTeilstandorte().size(), async (newSize, oldSize) => {
		if (newSize > oldSize) {
			await scrollToLastElement();
		}
	});

	const selectedTeilstandorte = shallowRef<Teilstandort[]>([]);
	const scrollTargetId = ref<string | null>(null);
	const scrollTargetElement = ref<HTMLElement | null>(null);

	const gridManager = computed(() => new GridManager<string, Teilstandort, List<Teilstandort>>({
		daten: computed(() => props.getListTeilstandorte()),
		getRowKey: teilstandort => teilstandort.adrMerkmal ?? "",
		columns: [
			...(props.readonly ? [] : [{ kuerzel: "Auswahl", name: "Auswahl", width: "2rem", hideable: false }]),
			{ kuerzel: "Merkmal", name: "Merkmal", width: "minmax(5rem, 0.75fr)" },
			{ kuerzel: "Straße", name: "Straße", width: "minmax(8rem, 1fr)" },
			{ kuerzel: "Hausnr.", name: "Hausnummer", width: "minmax(4rem, 0.5fr)" },
			{ kuerzel: "Zusatz", name: "Zusatz", width: "minmax(5rem, 0.75fr)" },
			{ kuerzel: "PLZ", name: "PLZ", width: "minmax(4rem, 0.65fr)" },
			{ kuerzel: "Ort", name: "Ort", width: "minmax(6rem, 1fr)" },
			{ kuerzel: "Kürzel", name: "Kürzel", width: "minmax(5rem, 0.75fr)" },
			{ kuerzel: "Bemerkung", name: "Bemerkung", width: "minmax(8rem, 2fr)" },
			...(props.readonly ? [] : [{ kuerzel: "RowActions", name: "Zeilenaktionen", width: "4rem", hideable: false }]),
		],
	}));

	// Gleicht die Selektion mit der aktuellen Liste ab (gelöschte Einträge entfernen, geänderte Objekte ersetzen)
	watch(() => props.getListTeilstandorte(), (list) => {
		const adrMerkmale = new Set(selectedTeilstandorte.value.map(t => t.adrMerkmal));
		selectedTeilstandorte.value = [...list].filter(t => adrMerkmale.has(t.adrMerkmal));
	}, { deep: true });

	function getRowActions(teilstandort: Teilstandort): TableAction[] {
		return [
			{
				label: "Teilstandort bearbeiten",
				iconClasses: "i-ri-edit-2-line",
				action: () => emit("openPatchModal", teilstandort),
			},
			{
				label: "Teilstandort löschen",
				trash: true,
				action: () => emit("deleteTeilstandorte", [teilstandort]),
				disabled: teilstandort.referenziertInAnderenTabellen,
			},
		];
	}

	const allSelectedDeletable = computed<boolean>(() =>
		selectedTeilstandorte.value.every(teilstandort => !teilstandort.referenziertInAnderenTabellen)
	);

	const bulkAllChecked = computed(() =>
		(selectedTeilstandorte.value.length === gridManager.value.daten.size())
		&& (selectedTeilstandorte.value.length > 0)
	);

	const bulkIntermediate = computed(() =>
		(selectedTeilstandorte.value.length > 0)
		&& (selectedTeilstandorte.value.length < gridManager.value.daten.size())
	);

	const footerActions = computed<TableAction[]>(() => [
		{
			label: "Teilstandort hinzufügen",
			iconClasses: "i-ri-add-line",
			action: () => emit("addModal"),
		}, {
			label: "Ausgewählte Teilstandorte löschen",
			trash: true,
			action: () => emit("deleteTeilstandorte", selectedTeilstandorte.value),
			disabled: (selectedTeilstandorte.value.length === 0) || !allSelectedDeletable.value,
		},
	]);

	function isEmpty(value: unknown): boolean {
		return (value === null) || (value === undefined) || (value === "");
	}

	function openPatchModal(teilstandort: Teilstandort): void {
		if (!props.readonly) {
			emit("openPatchModal", teilstandort);
		}
	}

	function setScrollTargetElement(element: Element | ComponentPublicInstance | null): void {
		if (element instanceof HTMLElement) {
			scrollTargetElement.value = element;
		} else {
			scrollTargetElement.value = null;
		}
	}

	function toggleBulkAll(value: boolean): void {
		selectedTeilstandorte.value = value ? [...gridManager.value.daten] : [];
	}

	function toggleBulkSelection(teilstandort: Teilstandort, value: boolean): void {
		selectedTeilstandorte.value = value
			? [...selectedTeilstandorte.value, teilstandort]
			: selectedTeilstandorte.value.filter(t => t.adrMerkmal !== teilstandort.adrMerkmal);
	}

	async function scrollToLastElement(): Promise<void> {
		const lastTeilstandort = props.getListTeilstandorte().getLast();
		scrollTargetId.value = lastTeilstandort.adrMerkmal;
		await nextTick();
		scrollTargetElement.value?.scrollIntoView({ block: "nearest" });
		scrollTargetId.value = null;
	}

</script>
