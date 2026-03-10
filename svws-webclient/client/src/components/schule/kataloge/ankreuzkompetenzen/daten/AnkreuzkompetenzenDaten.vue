<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-text-input placeholder="Kompetenzbeschreibung" class="contentFocusField"
					:model-value="manager().daten().floskelText"
					@change="patchFloskelText"
					:valid="floskelTextIsValid" :min-len="1" :max-len="255" :readonly="!hatKompetenzUpdate" required />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-if="istASVRef"
						placeholder="Fach"
						model-value="ASV"
						readonly />
					<ui-select v-else
						label="Fach"
						:manager="faecherSelectManager"
						v-model="selectedFach" :removable="false" required />
					<svws-ui-checkbox class="my-auto"
						v-model="istASV"
						:readonly="!hatKompetenzUpdate">
						ASV
					</svws-ui-checkbox>
					<ui-select label="Schulgliederung"
						v-model="selectedSchulgliederung"
						:manager="schulgliederungSelectManager"
						:readonly="!hatKompetenzUpdate" />
					<ui-select label="Abschnitt"
						v-model="selectedAbschnitt"
						:manager="abschnittSelectManager"
						:readonly="!hatKompetenzUpdate" :removable="false" required />
					<svws-ui-checkbox v-model="istAktiv"
						:readonly="!hatKompetenzUpdate">
						Aktiv
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zugeordnete Jahrgänge">
			<svws-ui-table :columns
				:items="manager().getJahrgaengeByAuswahl()"
				v-model="jahrgaengeToBeDeleted"
				:selectable="hatKompetenzUpdate" count scroll>
				<template #actions v-if="hatKompetenzUpdate">
					<div class="inline-flex gap-4">
						<svws-ui-button title="Jahrgang löschen" type="trash"
							@click="deleteSelectedJahrgaenge"
							:disabled="jahrgaengeToBeDeleted.length === 0" />
						<svws-ui-button title="Jahrgang hinzufügen" type="icon"
							@click="openModal"
							:disabled="addableJahrgaenge.length === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-modal :show="modalIsOpen"
			@update:show="closeModal"
			:auto-close="false">
			<template #modalTitle>Jahrgänge hinzufügen</template>
			<template #modalContent>
				<svws-ui-table class="max-h-[400px]"
					:items="addableJahrgaenge" :columns
					v-model="jahrgaengeToBeAdded"
					selectable scroll>
					<template #actions v-if="hatKompetenzUpdate">
						<div class="inline-flex gap-4">
							<div class="mt-7 flex flex-row gap-4 justify end">
								<svws-ui-button type="secondary"
									@click="closeModal">
									Abbrechen
								</svws-ui-button>
								<svws-ui-button @click="addJahrgaenge"
									:disabled="jahrgaengeToBeAdded.length === 0">
									Speichern
								</svws-ui-button>
							</div>
						</div>
					</template>
				</svws-ui-table>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import type { AnkreuzkompetenzenDatenProps } from "~/components/schule/kataloge/ankreuzkompetenzen/daten/AnkreuzkompetenzenDatenProps";
	import { computed, ref } from "vue";
	import type { FachDaten, JahrgangsDaten, SchulgliederungKatalogEintrag } from "@core";
	import { ArrayList, BenutzerKompetenz, Schulgliederung, Arrays } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager, type DataTableColumn, SelectManager } from "@ui";
	import { AnkreuzkompetenzAbschnitt } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzAbschnitt";

	const manager = () => props.manager();
	const props = defineProps<AnkreuzkompetenzenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	type AbschnittOption = { id: AnkreuzkompetenzAbschnitt; text: string };

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Jahrgang" },
	];

	const istASVRef = ref(manager().daten().istASV);
	const istASV = computed<boolean>({
		get: () => istASVRef.value,
		set: (istASV: boolean) => void patchASV(istASV),
	});

	const faecher = computed(() => [...manager().faecherById.values()]);
	const faecherSelectManager = new SelectManager({
		options: faecher,
		optionDisplayText: f => f.bezeichnung,
		selectionDisplayText: f => f.bezeichnung,
	});

	const selectedFach = computed<FachDaten | null>({
		get: () => manager().faecherById.get(manager().daten().idFach ?? -1) ?? null,
		set: (fach: FachDaten | null) => {
			if (fach !== null) {
				void props.patch({ idFach: fach.id, istASV: istASV.value });
			}
		},
	});

	const schulgliederungSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedSchulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, manager().daten().schulgliederung ?? ""),
		set: (schulgliederung: SchulgliederungKatalogEintrag | null) => void props.patch({ schulgliederung: schulgliederung?.schluessel ?? null }),
	});

	const abschnittOptionen: AbschnittOption[] = [
		{ id: AnkreuzkompetenzAbschnitt.HJ1, text: "1. HJ" },
		{ id: AnkreuzkompetenzAbschnitt.HJ2, text: "2. HJ" },
		{ id: AnkreuzkompetenzAbschnitt.BEIDE, text: "Beide" },
	];

	const abschnittSelectManager = new SelectManager({
		options: abschnittOptionen,
		optionDisplayText: a => a.text,
		selectionDisplayText: a => a.text,
	});

	const selectedAbschnitt = computed<AbschnittOption | null>({
		get: () => abschnittOptionen.find(a => a.id === manager().daten().abschnitt as AnkreuzkompetenzAbschnitt) ?? null,
		set: (abschnitt: AbschnittOption | null) => abschnitt?.id !== undefined && void props.patch({ abschnitt: abschnitt.id }),
	});

	const istAktiv = computed<boolean>({
		get: () => manager().auswahl().istAktiv,
		set: (istAktiv: boolean) => void props.patch({ istAktiv }),
	});

	const istSichtbar = computed<boolean>({
		get: () => manager().auswahl().istSichtbar,
		set: (istSichtbar: boolean) => void props.patch({ istSichtbar }),
	});

	// --- patch ---
	async function patchFloskelText(floskelText: string | null) {
		if (floskelTextIsValid(floskelText)) {
			await props.patch({ floskelText: floskelText.trim() });
		}
	}

	async function patchASV(istASV: boolean) {
		istASVRef.value = istASV;
		if (istASV) {
			await props.patch({ istASV, idFach: null });
		}
	}

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung });
		}
	}

	// --- validate ---
	function floskelTextIsValid(floskelText: string | null): floskelText is string {
		return mandatoryInputIsValid(floskelText, 255)
			&& isUniqueInList(floskelText, manager().liste.list(), "floskelText", "id", manager().auswahlID() ?? undefined);
	}

	function sortierungIsValid(sortierung: number | null): sortierung is number {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	// --- Jahrgangszuordnungen ---
	const jahrgaengeToBeDeleted = ref<JahrgangsDaten[]>([]);
	const jahrgaengeToBeAdded = ref<JahrgangsDaten[]>([]);
	const addableJahrgaenge = computed(() => manager().getAddableJahrgaenge());
	const jahrgaengezuordnungenByIdJahrgaenge = computed(() => manager().getJahrgaengezuordnungenByIdJahrgang());

	async function addJahrgaenge() {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		if (jahrgaengeToBeAdded.value.length === 0) {
			closeModal();
			return;
		}

		const idsJahrgaenge = Arrays.asList(jahrgaengeToBeAdded.value.map(jahrgang => jahrgang.id));
		await props.addJahrgaengezuordnungen(manager().daten().id, idsJahrgaenge);

		isLoading.value = false;
		closeModal();
	}

	async function deleteSelectedJahrgaenge() {
		if (jahrgaengeToBeDeleted.value.length === 0) {
			return;
		}

		const ids = new ArrayList<number>();
		for (const jahrgang of jahrgaengeToBeDeleted.value) {
			const zuordnung = jahrgaengezuordnungenByIdJahrgaenge.value.get(jahrgang.id);
			if (zuordnung !== null) {
				ids.add(zuordnung.id);
			}
		}

		await props.deleteJahrgaengezuordnungen(ids);
		jahrgaengeToBeDeleted.value = [];
	}

	// --- Modal ---
	const isLoading = ref<boolean>(false);
	const modalIsOpen = ref<boolean>(false);

	function closeModal() {
		jahrgaengeToBeAdded.value = [];
		modalIsOpen.value = false;
	}

	function openModal() {
		jahrgaengeToBeAdded.value = [];
		modalIsOpen.value = true;
	}
</script>
