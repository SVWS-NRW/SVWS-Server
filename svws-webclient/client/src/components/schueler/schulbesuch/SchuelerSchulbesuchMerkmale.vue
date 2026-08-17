<template>
	<svws-ui-content-card title="Besondere Merkmale für die Statistik">
		<svws-ui-table v-model="selectedEntries"
			:items="entries"
			@update:clicked="v => patchEntry(v)"
			:no-data="entries.length === 0"
			clickable :columns :selectable="true">
			<template #cell(merkmal)="{ rowData }">
				<span>{{ bezeichnungMerkmal(rowData) }}</span>
			</template>
			<template #cell(datumVon)="{ rowData }">
				<span>{{ formatToLocalDate(rowData.datumVon) }}</span>
			</template>
			<template #cell(datumBis)="{ rowData }">
				<span>{{ formatToLocalDate(rowData.datumBis) }}</span>
			</template>
			<template #actions v-if="updateKompetenz">
				<svws-ui-button type="trash"
					@click="deleteEntries"
					:disabled="(selectedEntries.length === 0) || !updateKompetenz" />
				<svws-ui-button title="Merkmal hinzufügen" type="icon"
					@click="addEntry" :disabled="!updateKompetenz">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</template>
		</svws-ui-table>
		<!-- Modal zum Erzeugen und Patchen eines Eintrags der besonderen Merkmale für die Statistik -->
		<svws-ui-modal :show="modalIsShown"
			@update:show="closeModal">
			<template #modalTitle>Merkmal hinzufügen</template>
			<template #modalContent>
				<ui-select label="Merkmal" class="pb-4"
					v-model="model.merkmal.value"
					:manager="merkmalManager"
					:removable="false" statistics />
				<svws-ui-input-wrapper :grid="2" style="text-align: left">
					<svws-ui-text-input placeholder="Von" type="date"
						v-model="model.proxy.datumVon" />
					<svws-ui-text-input placeholder="Bis" type="date"
						v-model="model.proxy.datumBis" />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify end">
					<svws-ui-button type="secondary"
						@click="closeModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="sendRequest"
						:disabled="!model.proxy.idMerkmal || !updateKompetenz">
						Speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">


	import { computed, ref } from "vue";
	import type { Merkmal, List } from "@core";
	import { ArrayList, SchuelerSchulbesuchMerkmal } from "@core";
	import type { DataTableColumn, SchuelerSchulbesuchManager } from "@ui";
	import { SelectManager } from "@ui";
	import { formatToLocalDate } from "~/utils/date";
	import { SchuelerSchulbesuchMerkmaleModelProxy } from "./modelProxy/SchuelerSchulbesuchMerkmaleModelProxy";

	const props = defineProps<{
		manager: () => SchuelerSchulbesuchManager;
		getMerkmale: () => List<SchuelerSchulbesuchMerkmal>;
		addMerkmal: (data: Partial<SchuelerSchulbesuchMerkmal>) => Promise<void>;
		patchMerkmal: (data: Partial<SchuelerSchulbesuchMerkmal>) => Promise<void>;
		deleteMerkmale: (idsEintraege: List<number>) => Promise<void>;
		updateKompetenz: boolean;
	}>();

	const selectedEntries = ref<SchuelerSchulbesuchMerkmal[]>([]);
	let model = new SchuelerSchulbesuchMerkmaleModelProxy(() => new SchuelerSchulbesuchMerkmal(), () => props.manager());
	const entries = computed(() => [...props.getMerkmale()]);
	const merkmale = computed(() => props.manager().merkmaleById.values());

	const merkmalManager = new SelectManager<Merkmal>({
		options: merkmale,
		optionDisplayText: m => m.bezeichnung ?? '-',
		selectionDisplayText: m => m.bezeichnung ?? '-',
	});

	// --- requests ---

	function addEntry() {
		resetData();
		model.proxy.idSchueler = props.manager().idSchueler;
		setMode(Mode.ADD);
		openModal();
	}

	function patchEntry(entry: SchuelerSchulbesuchMerkmal) {
		resetData();
		setMode(Mode.PATCH);
		const { id, idMerkmal, datumVon, datumBis } = entry;
		Object.assign(model.proxy, { id, idMerkmal, datumVon, datumBis });
		openModal();
	}

	async function deleteEntries() {
		if (selectedEntries.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedEntries.value) {
			ids.add(s.id);
		}
		await props.deleteMerkmale(ids);
		selectedEntries.value = [];
	}

	async function sendRequest() {
		if (currentMode.value === Mode.ADD) {
			await props.addMerkmal(model.proxy);
		}
		if (currentMode.value === Mode.PATCH) {
			await props.patchMerkmal(model.proxy);
		}
		enterDefaultMode();
	}

	// --- modal ---

	const modalIsShown = ref<boolean>(false);

	function openModal() {
		modalIsShown.value = true;
	}

	function closeModal() {
		resetData();
		setMode(Mode.DEFAULT);
		modalIsShown.value = false;
	}

	// --- table ---

	function bezeichnungMerkmal(merkmal: SchuelerSchulbesuchMerkmal) {
		return props.manager().merkmaleById.get(merkmal.idMerkmal ?? -1)?.bezeichnung ?? " - ";
	}

	const columns: DataTableColumn[] = [
		{ key: "merkmal", label: "Merkmal", statistic: true },
		{ key: "datumVon", label: "Von" },
		{ key: "datumBis", label: "Bis" },
	];

	// --- mode ---

	enum Mode { ADD, PATCH, DEFAULT }

	const currentMode = ref<Mode>(Mode.DEFAULT);

	function setMode(newMode: Mode) {
		currentMode.value = newMode;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetData();
		closeModal();
	}

	// --- util ---

	function resetData() {
		model = new SchuelerSchulbesuchMerkmaleModelProxy(() => new SchuelerSchulbesuchMerkmal(), () => props.manager());
	}

</script>

