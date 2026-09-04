<template>
	<svws-ui-content-card title="Vermerke anlegen" class="col-span-full">
		<svws-ui-table v-model="selectedVermerke"
			:items="getVermerke()"
			@update:clicked="patch"
			:no-data="getVermerke().size() === 0"
			clickable :columns :selectable="true">
			<template #cell(idVermerkart)="{ value }">
				{{ bezeichnung(value) }}
			</template>
			<template #actions>
				<div class="inline-flex gap-4">
					<svws-ui-button type="trash"
						@click="deleteEntries"
						:disabled="(selectedVermerke.length === 0) || (!updateKompetenz)" />
					<svws-ui-button title="Vermerk hinzufügen" type="icon"
						@click="add"
						:disabled="!updateKompetenz">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-table>
		<svws-ui-modal :show="modalIsShown" @update:show="closeModal">
			<template #modalTitle>Vermerk hinzufügen</template>
			<template #modalContent>
				<ui-select label="Vermerkart"
					v-model="selectedVermerkart"
					:manager="vermerkartenManager"
					:removable="false" />
				<svws-ui-textarea-input placeholder="Bemerkung" class="col-span-full"
					v-model="vermerk.bemerkung"
					:autoresize="true" />
				<svws-ui-notification type="warning" v-if="manager().vermerkartenById.size === 0">
					Die Liste der Vermerkarten ist leer. Es sollte mindestens eine Vermerkart unter Schule/Kataloge angelegt werden, damit zusätzliche Vermerke
					eine gültige Zuordnung haben.
				</svws-ui-notification>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary"
						@click="closeModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="sendRequest"
						:disabled="(selectedVermerkart === null) || (manager().vermerkartenById.size === 0) || (vermerk.bemerkung === '') || (!updateKompetenz)">
						Speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>
<script setup lang="ts">

	import { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
	import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import type { DataTableColumn } from "@ui/types";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";
	import { computed, ref } from "vue";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		getVermerke: () => List<SchuelerVermerke>;
		addVermerk: (data: Partial<SchuelerVermerke>) => Promise<void>;
		patchVermerk: (data: Partial<SchuelerVermerke>, idEintrag: number) => Promise<void>;
		deleteVermerke: (idsEintraege: List<number>) => Promise<void>;
		updateKompetenz: boolean;
	}>();

	const manager = () => props.manager();
	const selectedVermerke = ref<SchuelerVermerke[]>([]);
	const vermerk = ref<SchuelerVermerke>(new SchuelerVermerke());
	const vermerkarten = computed(() => manager().vermerkartenById.values());
	const selectedVermerkart = computed<VermerkartEintrag | undefined>({
		get: () => manager().vermerkartenById.get(vermerk.value.idVermerkart ?? -1),
		set: (value) => vermerk.value.idVermerkart = value?.id ?? null,
	});

	const vermerkartenManager = new SelectManager({
		options: vermerkarten,
		optionDisplayText: i => i.bezeichnung ?? "",
		selectionDisplayText: i => i.bezeichnung ?? "",
	});

	// --- requests ---

	function add() {
		resetData();
		setMode(Mode.ADD);
		openModal();
	}

	async function sendRequest() {
		const { id, datum, angelegtVon, geaendertVon, ...partialDataWithoutId } = vermerk.value;
		partialDataWithoutId.idSchueler = manager().stammdaten.id;
		if (currentMode.value === Mode.ADD) {
			await props.addVermerk(partialDataWithoutId);
		}
		if (currentMode.value === Mode.PATCH) {
			await props.patchVermerk(partialDataWithoutId, vermerk.value.id);
		}
		enterDefaultMode();
	}

	function patch(vermerkEintrag: SchuelerVermerke) {
		resetData();
		setMode(Mode.PATCH);
		vermerk.value.id = vermerkEintrag.id;
		vermerk.value.idVermerkart = vermerkEintrag.idVermerkart;
		vermerk.value.bemerkung = vermerkEintrag.bemerkung;
		openModal();
	}

	async function deleteEntries() {
		if (selectedVermerke.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedVermerke.value) {
			ids.add(s.id);
		}
		await props.deleteVermerke(ids);
		selectedVermerke.value = [];
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

	// --- mode ---

	enum Mode { ADD, PATCH, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);

	function setMode(newMode: Mode) {
		currentMode.value = newMode;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		closeModal();
		resetData();
	}

	// --- util ---

	function bezeichnung(idVermerkArt: number): string {
		return manager().vermerkartenById.get(idVermerkArt)?.bezeichnung ?? "";
	}

	function resetData() {
		const defaultVermerk = new SchuelerVermerke();
		const ersteVermerkArt = manager().vermerkartenById.values().next().value;
		defaultVermerk.idVermerkart = ersteVermerkArt?.id ?? 0;
		defaultVermerk.bemerkung = '';
		vermerk.value = defaultVermerk;
	}

	const columns: DataTableColumn[] = [
		{ key: "idVermerkart", label: "Vermerkart" },
		{ key: "bemerkung", label: "Bemerkung", span: 2 },
		{ key: "angelegtVon", label: "Angelegt von" },
		{ key: "geaendertVon", label: "Geändert von" },
		{ key: "datum", label: "Erstellt am", span: 1, align: "right" },
	];

</script>
