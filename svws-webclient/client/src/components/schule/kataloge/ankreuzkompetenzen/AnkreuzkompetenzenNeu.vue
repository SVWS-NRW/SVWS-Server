<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-text-input placeholder="Kompetenzbeschreibung" class="contentFocusField"
					v-model="data.floskelText"
					:valid="() => fieldIsValid('floskelText')"
					:min-len="1" :max-len="255" :disabled="!hatKompetenzAdd" required />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-if="istASV" placeholder="Fach"
						model-value="ASV"
						readonly />
					<ui-select v-else label="Fach"
						:manager="faecherManager"
						v-model="selectedFach"
						:disabled="!hatKompetenzAdd" required />
					<svws-ui-checkbox class="my-auto"
						v-model="istASV"
						:disabled="!hatKompetenzAdd">
						ASV
					</svws-ui-checkbox>
					<ui-select label="Gliederung"
						v-model="selectedSchulgliederung"
						:manager="schulgliederungSelectManager" :disabled="!hatKompetenzAdd" />
					<ui-select label="Abschnitt"
						v-model="selectedAbschnitt"
						:manager="abschnittSelectManager"
						:removable="false" :disabled="!hatKompetenzAdd" required />
					<svws-ui-checkbox v-model="data.istAktiv"
						:disabled="!hatKompetenzAdd">
						Aktiv
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')"
						:min="0" :max="32000" :disabled="!hatKompetenzAdd" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar"
						:disabled="!hatKompetenzAdd">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="add" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Jahrgänge zuordnen">
			<svws-ui-table :columns
				:items="manager().jahrgaengeById.values()"
				v-model="jahrgaengeToBeAdded"
				:selectable="hatKompetenzAdd" count scroll />
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { AnkreuzkompetenzenNeuProps } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenNeuProps";
	import type { FachDaten, JahrgangsDaten, List, SchulgliederungKatalogEintrag } from "@core";
	import { Ankreuzkompetenz, Schulgliederung, BenutzerKompetenz, Arrays } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";
	import type { DataTableColumn } from "@ui";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { AnkreuzkompetenzAbschnitt } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzAbschnitt";

	const props = defineProps<AnkreuzkompetenzenNeuProps>();
	const data = ref<Ankreuzkompetenz>(Object.assign(new Ankreuzkompetenz(), { istSichtbar: true, sortierung: 32000 }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const jahrgaengeToBeAdded = ref<JahrgangsDaten[]>([]);
	const jahrgaengeIdsToBeAdded = computed<List<number>>(() => Arrays.asList(jahrgaengeToBeAdded.value.map(jahrgang => jahrgang.id)));
	type AbschnittOption = { id: AnkreuzkompetenzAbschnitt; text: string };

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Jahrgang" },
	];

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Ankreuzkompetenz));
	});

	const fieldIsValid = (field: keyof Ankreuzkompetenz): boolean => {
		switch (field) {
			case 'floskelText':
				return floskelTextIsValid(data.value.floskelText);
			case 'idFach':
				return data.value.istASV || (data.value.idFach !== null);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	const faecher = computed(() => [...props.manager().faecherById.values()]);
	const faecherManager = new SelectManager({
		options: faecher,
		optionDisplayText: f => f.bezeichnung,
		selectionDisplayText: f => f.bezeichnung,
	});

	const selectedFach = computed<FachDaten | null>({
		get: () => props.manager().faecherById.get(data.value.idFach ?? -1) ?? null,
		set: (fach: FachDaten | null) => data.value.idFach = fach?.id ?? null,
	});

	const schulgliederungSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedSchulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, data.value.schulgliederung ?? ""),
		set: (schulgliederung: SchulgliederungKatalogEintrag | null) => data.value.schulgliederung = schulgliederung?.schluessel ?? null,
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
		get: () => abschnittOptionen.find(a => a.id === data.value.abschnitt as AnkreuzkompetenzAbschnitt) ?? null,
		set: (abschnitt: AbschnittOption | null) => data.value.abschnitt = abschnitt?.id ?? 0,
	});

	const istASV = computed<boolean>({
		get: () => data.value.istASV,
		set: (istASV: boolean) => {
			data.value.istASV = istASV;
			if (istASV) {
				data.value.idFach = null;
			}
		},
	});

	function floskelTextIsValid(floskelText: string | null): boolean {
		return mandatoryInputIsValid(floskelText, 255)
			&& isUniqueInList(floskelText, props.manager().liste.list(), "floskelText", "id", props.manager().auswahlID() ?? undefined);
	}

	function sortierungIsValid(sortierung: number): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	// --- util ---
	async function add() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, jahrgaengezuordnung, referenziertInAnderenTabellen, ...partialData } = data.value;
		const ankreuzkompetenz = await props.addAnkreuzkompetenz(partialData, jahrgaengeIdsToBeAdded.value);

		await props.gotoDefaultView(ankreuzkompetenz.id);

		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
