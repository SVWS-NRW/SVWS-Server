<template>
	<svws-ui-content-card title="Alle bisher besuchten Schulen" class="col-span-full">
		<svws-ui-table v-model="selectedEntries"
			:items="entries"
			@update:clicked="v => patchEntry(v)"
			:no-data="entries.length === 0"
			clickable :columns :selectable="true">
			<template #cell(schulform)="{ rowData }">
				<span>{{ bezeichnungSchulformTable(rowData) }}</span>
			</template>
			<template #cell(schulname)="{ rowData }">
				<span>{{ bezeichnungSchulname(rowData) }}</span>
			</template>
			<template #cell(datumVon)="{ rowData }">
				<span>{{ formatToLocalDate(rowData.datumVon) }}</span>
			</template>
			<template #cell(datumBis)="{ rowData }">
				<span>{{ formatToLocalDate(rowData.datumBis) }}</span>
			</template>
			<template #cell(jahrgangVon)="{ rowData }">
				<span>{{ rowData.jahrgangVon }}</span>
			</template>
			<template #cell(jahrgangBis)="{ rowData }">
				<span>{{ rowData.jahrgangBis }}</span>
			</template>
			<template #cell(schulgliederung)="{ rowData }">
				<span>{{ bezeichnungSchulgliederung(rowData) }}</span>
			</template>
			<template #cell(entlassart)="{ rowData }">
				<span>{{ bezeichnungEntlassgrund(rowData) }}</span>
			</template>
			<template #actions v-if="updateKompetenz">
				<svws-ui-button type="trash"
					@click="deleteEntries"
					:disabled="(selectedEntries.length === 0) || !updateKompetenz" />
				<svws-ui-button title="Schuleintrag hinzufügen" type="icon"
					@click="addEntry"
					:disabled="!updateKompetenz">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</template>
		</svws-ui-table>
		<!-- Modal zum Erzeugen und Patchen eines Eintrags der bisher besuchten Schulen -->
		<svws-ui-modal :show="modalIsShown"
			@update:show="closeModal">
			<template #modalTitle>Schule hinzufügen</template>
			<template #modalContent>
				<svws-ui-input-wrapper :grid="2" style="text-align: left">
					<ui-select label="Schule" class="col-span-full"
						v-model="model.schule.value"
						:manager="schulenManager"
						:removable="false" />
					<svws-ui-text-input span="full" placeholder="Adresse"
						:model-value="model.adresseSchule.value"
						readonly />
					<svws-ui-text-input placeholder="Schulnummer"
						:model-value="model.schule.value?.schulnummerStatistik ?? '-'"
						readonly statistics />
					<svws-ui-text-input placeholder="Schulform"
						:model-value="bezeichnungSchulformModal"
						readonly />
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Start des Schulbesuchs" type="date"
						v-model="model.proxy.datumVon" />
					<svws-ui-text-input placeholder="Ende des Schulbesuchs" type="date"
						v-model="model.proxy.datumBis" />
					<ui-select label="Jahrgang von"
						v-model="model.jahrgangVon.value"
						:manager="jahrgangVonManager"
						:disabled="!model.schule.value || !model.proxy.datumVon" />
					<ui-select label="Jahrgang bis"
						v-model="model.jahrgangBis.value"
						:manager="jahrgangBisManager"
						:disabled="!model.schule.value || !model.proxy.datumBis" />
					<ui-select label="Schulgliederung" class="col-span-full"
						v-model="model.schulgliederung.value"
						:manager="schulgliederungenManager"
						:disabled="(!model.proxy.datumBis || !model.schule.value)" />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary"
						@click="closeModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="sendRequest"
						:disabled="(!model.schule.value) || !updateKompetenz">
						Speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">


	import { computed, ref, watch } from "vue";
	import { formatToLocalDate } from "~/utils/date";
	import { SchuelerSchulbesuchSchuleModelProxy } from "./modelProxy/SchuelerSchulbesuchSchuleModelProxy";
	import { SchuelerSchulbesuchSchule } from "@core/asd/data/schueler/SchuelerSchulbesuchSchule";
	import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
	import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import type { DataTableColumn } from "@ui/types";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { SchuelerSchulbesuchManager } from "@ui/ui/manager/schueler/SchuelerSchulbesuchManager";

	const props = defineProps<{
		manager: () => SchuelerSchulbesuchManager;
		getBisherigeSchulen: () => List<SchuelerSchulbesuchSchule>;
		addBisherigeSchule: (data: Partial<SchuelerSchulbesuchSchule>) => Promise<void>;
		patchBisherigeSchule: (data: Partial<SchuelerSchulbesuchSchule>) => Promise<void>;
		deleteBisherigeSchulen: (idsEintraege: List<number>) => Promise<void>;
		updateKompetenz: boolean;
	}>();

	const selectedEntries = ref<SchuelerSchulbesuchSchule[]>([]);
	let model = new SchuelerSchulbesuchSchuleModelProxy(() => new SchuelerSchulbesuchSchule(), () => props.manager());
	const entries = computed(() => [...props.getBisherigeSchulen()]);
	const schulen = computed(() => props.manager().schulenById.values());

	const bezeichnungSchulformModal = computed<string>(() => Schulform.data().getEintragByID(model.schule.value?.idSchulform ?? -1)?.text ?? '');
	const schuljahrDatumVon = computed<number>(() => (model.proxy.datumVon === null) ? -1 : Number(model.proxy.datumVon.substring(0, 4)));
	const schuljahrDatumBis = computed<number>(() => (model.proxy.datumBis === null) ? -1 : Number(model.proxy.datumBis.substring(0, 4)));
	const schulformSelectedSchule = computed<Schulform | null>(() => Schulform.data().getWertByIDOrNull(model.schule.value?.idSchulform ?? -1));

	const schulenManager = new SelectManager<SchulEintrag>({
		options: schulen,
		optionDisplayText: s => s.name,
		selectionDisplayText: s => s.name,
	});

	const jahrgangVonManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: schuljahrDatumVon,
		schulformen: schulformSelectedSchule,
		selectionDisplayText: "text",
		optionDisplayText: "text",
	});

	const jahrgangBisManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: schuljahrDatumBis,
		schulformen: schulformSelectedSchule,
		selectionDisplayText: "text",
		optionDisplayText: "text",
	});

	const schulgliederungenFilter = {
		key: "isNotUsed",
		apply: (options: List<SchulgliederungKatalogEintrag>) => {
			const filtered = new ArrayList<SchulgliederungKatalogEintrag>();
			const kuerzelSchulform = Schulform.data().getEintragByID(model.schule.value?.idSchulform ?? -1)?.kuerzel ?? '';
			for (const option of options) {
				if (option.schulformen.contains(kuerzelSchulform)) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const schulgliederungenManager = new CoreTypeSelectManager({
		filters: [schulgliederungenFilter],
		clazz: Schulgliederung.class,
		schuljahr: schuljahrDatumBis,
		schulformen: model.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	watch(() => model.schule.value, async () => {
		schulgliederungenManager.updateFilteredOptions();
	}, { immediate: true });

	// --- requests ---

	function addEntry() {
		resetData();
		model.proxy.idSchueler = props.manager().idSchueler;
		setMode(Mode.ADD);
		openModal();
	}

	function patchEntry(entry: SchuelerSchulbesuchSchule) {
		resetData();
		setMode(Mode.PATCH);
		const { id, idSchule, datumVon, datumBis, schluesselSchulgliederung, jahrgangVon, jahrgangBis } = entry;
		Object.assign(model.proxy, { id, idSchule, datumVon, datumBis, schluesselSchulgliederung, jahrgangVon, jahrgangBis });
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
		await props.deleteBisherigeSchulen(ids);
		selectedEntries.value = [];
	}

	async function sendRequest() {
		if (currentMode.value === Mode.ADD) {
			await props.addBisherigeSchule(model.proxy);
		}
		if (currentMode.value === Mode.PATCH) {
			await props.patchBisherigeSchule(model.proxy);
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

	function bezeichnungSchulformTable(schule: SchuelerSchulbesuchSchule): string {
		const idSchulform = props.manager().schulenById.get(schule.idSchule ?? -1)?.idSchulform ?? -1;
		return Schulform.data().getEintragByID(idSchulform)?.kuerzel ?? '-';
	}

	function bezeichnungSchulname(schule: SchuelerSchulbesuchSchule): string {
		return props.manager().schulenById.get(schule.idSchule ?? -1)?.name ?? '-';
	}

	function bezeichnungSchulgliederung(schule: SchuelerSchulbesuchSchule): string {
		return Schulgliederung.data().getWertBySchluessel(schule.schluesselSchulgliederung ?? '')?.daten(props.manager().schuljahr)?.text ?? '-';
	}

	function bezeichnungEntlassgrund(schule: SchuelerSchulbesuchSchule): string {
		return props.manager().entlassgruendeById.get(schule.idEntlassgrund ?? -1)?.bezeichnung ?? '-';
	}

	const columns: DataTableColumn[] = [
		{ key: "schulform", label: "Schulform", span: 0.2, align: "center" },
		{ key: "schulname", label: "Schulname" },
		{ key: "datumVon", label: "Aufnahme-Datum", span: 0.25, align: "center" },
		{ key: "datumBis", label: "Entlass-Datum", span: 0.25, align: "center" },
		{ key: "jahrgangVon", label: "Jahrgang Von", span: 0.2, align: "center" },
		{ key: "jahrgangBis", label: "Jahrgang Bis", span: 0.2, align: "center" },
		{ key: "schulgliederung", label: "Schulgliederung", align: "center" },
		{ key: "entlassart", label: "Entlassart", span: 0.2, align: "center" },
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
		model = new SchuelerSchulbesuchSchuleModelProxy(() => new SchuelerSchulbesuchSchule(), () => props.manager());
	}

</script>

