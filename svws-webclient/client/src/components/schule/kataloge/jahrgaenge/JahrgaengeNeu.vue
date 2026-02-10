<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="data.kuerzel"
						:valid="() => fieldIsValid('kuerzel')" :min-len="1" :max-len="20" :disabled="!hatKompetenzAdd" required />
					<svws-ui-text-input placeholder="Bezeichnung"
						v-model="data.bezeichnung"
						:valid="() => fieldIsValid('bezeichnung')" :min-len="1" :max-len="100" :disabled="!hatKompetenzAdd" required />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						v-model="data.kurzbezeichnung"
						:valid="() => fieldIsValid('kurzbezeichnung')" :max-len="2" :disabled="!hatKompetenzAdd" />
					<ui-select label="Folgejahrgang"
						v-model="selectedFolgejahrgang"
						:manager="folgeJahrgangManager"
						:disabled="!hatKompetenzAdd" />
					<ui-select label="Schulgliederung ASD-Kürzel"
						v-model="selectedSchulgliederung"
						:manager="schulgliederungKuerzelSelectManager"
						searchable statistics :disabled="!hatKompetenzAdd" />
					<ui-select label="Schulgliederung ASD-Text"
						v-model="selectedSchulgliederung"
						:manager="schulgliederungTextSelectManager"
						searchable statistics :disabled="!hatKompetenzAdd" />
					<ui-select label="Jahrgang ASD-Kürzel"
						:manager="jahrgangKuerzelSelectManager"
						v-model="selectedStatistikJahrgang"
						:valid="fieldIsValid('kuerzelStatistik')" searchable statistics :disabled="!hatKompetenzAdd" required :removable="false" />
					<ui-select label="Jahrgang ASD-Text"
						:manager="jahrgangTextSelectManager"
						v-model="selectedStatistikJahrgang"
						statistics searchable :disabled="!hatKompetenzAdd" required :removable="false" />
					<svws-ui-input-number placeholder="Anzahl der Restabschnitte"
						v-model="data.anzahlRestabschnitte"
						:valid="() => fieldIsValid('anzahlRestabschnitte')" :min="0" :max="40" :disabled="!hatKompetenzAdd" />
					<ui-select label="Bildungsstufe"
						:manager="bildungsstufeSelectManager"
						v-model="selectedBildungsstufe"
						:readonly="!hatKompetenzAdd" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="!hatKompetenzAdd" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled="!hatKompetenzAdd">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>

			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addJahrgangsdaten" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { JahrgaengeNeuProps } from "./JahrgaengeNeuProps";
	import { computed, ref, watch } from "vue";
	import { Bildungsstufe, BenutzerKompetenz, Jahrgaenge, JahrgangsDaten, Schulgliederung } from "@core";
	import type { BildungsstufeKatalogEintrag, JahrgaengeKatalogEintrag, SchulgliederungKatalogEintrag } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<JahrgaengeNeuProps>();
	const data = ref<JahrgangsDaten>(Object.assign(new JahrgangsDaten(), { istSichtbar: true, sortierung: 32000, anzahlRestabschnitte: 0 }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const jahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().liste.list()]);
	const folgeJahrgangManager = new SelectManager({
		options: jahrgaenge,
		optionDisplayText: v => v.bezeichnung ?? "",
		selectionDisplayText: v => v.bezeichnung ?? "",
	});

	const schulgliederungTextSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulgliederungKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const jahrgangTextSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const jahrgangKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const bildungsstufeSelectManager = new CoreTypeSelectManager({
		clazz: Bildungsstufe.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedFolgejahrgang = computed<JahrgangsDaten | null>({
		get: () => props.manager().liste.get(data.value.idFolgejahrgang ?? -1),
		set: (value: JahrgangsDaten | null) => data.value.idFolgejahrgang = value?.id ?? null,
	});

	const selectedSchulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, data.value.kuerzelSchulgliederung ?? ""),
		set: (value: SchulgliederungKatalogEintrag | null) => data.value.kuerzelSchulgliederung = value?.schluessel ?? null,
	});

	const selectedStatistikJahrgang = computed<JahrgaengeKatalogEintrag | null>({
		get: () => Jahrgaenge.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, data.value.kuerzelStatistik ?? ""),
		set: (value: JahrgaengeKatalogEintrag | null) => data.value.kuerzelStatistik = value?.schluessel ?? null,
	});

	const selectedBildungsstufe = computed<BildungsstufeKatalogEintrag | null>({
		get: () => Bildungsstufe.data().getEintragByID(data.value.idBildungsstufe ?? -1),
		set: (value: BildungsstufeKatalogEintrag | null) => data.value.idBildungsstufe = value?.id ?? null,
	});

	function kuerzelIsValid(kuerzel: string | null): boolean {
		return mandatoryInputIsValid(kuerzel, 20)
			&& isUniqueInList(kuerzel, props.manager().liste.list(), "kuerzel");
	}

	function bezeichnungIsValid(bezeichnung: string | null): boolean {
		return mandatoryInputIsValid(bezeichnung, 100)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "bezeichnung");
	}

	function kuerzelStatistikIsValid(kuerzelStatistik: string | null): boolean {
		if (kuerzelStatistik === null) {
			return false;
		}

		const result = Jahrgaenge.data().getWertByKuerzel(kuerzelStatistik);
		return (result !== null);
	}

	function anzahlRestabschnitteIsValid(anzahlRestabschnitte: number | null): boolean {
		return !numberHasDecimals(anzahlRestabschnitte)
			&& numberIsValid(anzahlRestabschnitte, false, 0, 40);
	}

	function sortierungIsValid(sortierung: number): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof JahrgangsDaten));
	});

	const fieldIsValid = (field: keyof JahrgangsDaten): boolean => {
		switch (field) {
			case 'kuerzel':
				return kuerzelIsValid(data.value.kuerzel);
			case 'bezeichnung':
				return bezeichnungIsValid(data.value.bezeichnung);
			case 'kurzbezeichnung':
				return optionalInputIsValid(data.value.kurzbezeichnung, 2);
			case 'kuerzelStatistik':
				return kuerzelStatistikIsValid(data.value.kuerzelStatistik);
			case 'anzahlRestabschnitte':
				return anzahlRestabschnitteIsValid(data.value.anzahlRestabschnitte);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	// --- util ---
	async function addJahrgangsdaten() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
