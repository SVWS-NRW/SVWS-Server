<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Status"
					:model-value="statusNeuaufnahme?.text"
					readonly />
				<ui-select label="Schuljahresabschnitt"
					v-model="selectedSchuljahresabschnitt"
					:manager="schuljahresabschnittManager"
					required searchable />
				<ui-select label="Jahrgang"
					v-model="selectedJahrgang"
					:manager="jahrgangManager"
					:disabled="(data.idSchuljahresabschnitt <= 0)"
					required searchable />
				<ui-select label="Klasse"
					v-model="selectedKlasse"
					:manager="klassenManager"
					:disabled="(data.idJahrgang === null)" />
				<svws-ui-text-input placeholder="Anmeldedatum" type="date"
					v-model="selectedAnmeldedatum"
					:valid="() => fieldIsValid('anmeldedatum')"
					:max-date="today" />
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date"
					v-model="selectedAufnahmedatum"
					:valid="() => fieldIsValid('aufnahmedatum')"
					:min-date="data.anmeldedatum || today" />
				<ui-select label="Einschulungsart" v-if="schulenMitPrimaerstufe"
					v-model="einschulungsart"
					:manager="einschulungsartManager"
					:removable="true" searchable />
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-if="schulenMitBKoderSK"
					v-model="data.beginnBildungsgang"
					:valid="() => fieldIsValid('beginnBildungsgang')"
					:min-date="data.aufnahmedatum || today" />
				<svws-ui-input-number placeholder="Dauer Bildungsgang" v-if="schulenMitBKoderSK"
					v-model="data.dauerBildungsgang" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Nachname"
					v-model="data.nachname"
					:valid="() => fieldIsValid('nachname')"
					:min-len="1" :max-len="120" required />
				<svws-ui-text-input placeholder="Rufname"
					v-model="data.vorname"
					:valid="() => fieldIsValid('vorname')"
					:min-len="1" :max-len="80" required />
				<svws-ui-text-input placeholder="Alle Vornamen"
					v-model="data.alleVornamen"
					:valid="() => fieldIsValid('alleVornamen')"
					:max-len="255" />
				<div />
				<svws-ui-text-input placeholder="Geburtsdatum" type="date"
					v-model="data.geburtsdatum"
					:valid="() => fieldIsValid('geburtsdatum')"
					:min-date="minGeburtsdatum" :max-date="maxGeburtsdatum"
					required />
				<ui-select label="Geschlecht"
					v-model="selectedGeschlecht"
					:manager="geschlechtManager"
					:removable="false" required searchable />
				<ui-select label="Religion"
					v-model="selectedReligion"
					:manager="religionManager"
					:removable="false" required searchable />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addSchueler" :disabled="(!formIsValid) || (!hatKompetenzUpdate)">
					Anlegen
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
	<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/neuanlage/SchuelerNeuProps";
	import type { EinschulungsartKatalogEintrag, JahrgangsDaten, KlassenDaten, List, ReligionEintrag, Schuljahresabschnitt } from "@core";
	import { BenutzerKompetenz, Geschlecht, SchuelerNeu, SchuelerStatus, Schulform } from "@core";
	import { computed, ref, watch } from "vue";
	import { SelectManager } from "@ui";
	import { mandatoryInputIsValid, optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SchuelerNeuProps>();

	const manager = () => props.manager();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const isLoading = ref<boolean>(false);
	const schulenMitBKoderSK = computed(() => (props.schulform === Schulform.BK) || (props.schulform === Schulform.SK));
	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V];
		return erlaubteSchulformen.includes(props.schulform);
	});
	const schuljahr = manager().aktuellerAbschnitt.schuljahr;
	const statusNeuaufnahme = SchuelerStatus.NEUAUFNAHME.daten(schuljahr);
	const formIsValid = computed(() => Object.keys(data.value).every((field) => fieldIsValid(field as keyof SchuelerNeu)));

	const data = ref<SchuelerNeu>(Object.assign(new SchuelerNeu(), { status: statusNeuaufnahme?.id ?? -1 }));

	const abschnitteFiltered = computed(() => manager().schuljahresabschnitteFilteredById.values());
	const jahrgaenge = computed(() => Array.from(manager().jahrgaengeById.values()));
	const klassenByIdAbschnitt = computed<Map<number, List<KlassenDaten>>>(() => manager().klassenByIdAbschnitt);
	const klassen = computed(() => klassenByIdAbschnitt.value.get(data.value.idSchuljahresabschnitt) ?? []);
	const einschulungsarten = computed(() => manager().einschulungsartenById.values());
	const religionen = computed(() => manager().religionenById.values());

	const today = new Date().toISOString().split("T")[0];
	const minGeburtsdatum = new Date(new Date().setFullYear(new Date().getFullYear() - 50)).toISOString().split("T")[0];
	const maxGeburtsdatum = new Date(new Date().setFullYear(new Date().getFullYear() - 4)).toISOString().split("T")[0];

	const selectedSchuljahresabschnitt = computed<Schuljahresabschnitt | null>({
		get: () => manager().schuljahresabschnitteFilteredById.get(data.value.idSchuljahresabschnitt) ?? null,
		set: (value: Schuljahresabschnitt | null) => {
			data.value.idSchuljahresabschnitt = value?.id ?? -1;
			data.value.idJahrgang = null;
		},
	});

	const selectedJahrgang = computed<JahrgangsDaten | null>({
		get: () => jahrgaenge.value.find(i => i.id === data.value.idJahrgang) ?? null,
		set: (value: JahrgangsDaten | null) => {
			data.value.idJahrgang = value?.id ?? null;
			data.value.idKlasse = null;
		},
	});

	const selectedKlasse = computed<KlassenDaten | null>({
		get: () => manager().getKlassenByIdFuerAbschnitt(data.value.idSchuljahresabschnitt).get(data.value.idKlasse ?? -1) ?? null,
		set: (value: KlassenDaten | null) => data.value.idKlasse = value?.id ?? -1,
	});

	const einschulungsart = computed<EinschulungsartKatalogEintrag | null>({
		get: () => manager().einschulungsartenById.get(data.value.idGrundschuleEinschulungsart ?? -1) ?? null,
		set: (value: EinschulungsartKatalogEintrag | null) => data.value.idGrundschuleEinschulungsart = value?.id ?? null,
	});

	const selectedAnmeldedatum = computed<string | null>({
		get: () => data.value.anmeldedatum,
		set: (value: string | null) => {
			data.value.anmeldedatum = value;
			data.value.aufnahmedatum = null;
		},
	});

	const selectedAufnahmedatum = computed<string | null>({
		get: () => data.value.aufnahmedatum,
		set: (value: string | null) => {
			data.value.aufnahmedatum = value;
			data.value.beginnBildungsgang = null;
		},
	});

	const selectedGeschlecht = computed({
		get: () => Geschlecht.fromValue(data.value.geschlecht) ?? null,
		set: (value) => data.value.geschlecht = value?.id ?? -1,
	});

	const selectedReligion = computed<ReligionEintrag | null>({
		get: () => manager().religionenById.get(data.value.idReligion ?? -1) ?? null,
		set: (value: ReligionEintrag | null) => data.value.idReligion = value?.id ?? -1,
	});

	// --- manager ---

	const schuljahresabschnittManager = new SelectManager({
		options: abschnitteFiltered,
		optionDisplayText: schuljahresabschnittText,
		selectionDisplayText: schuljahresabschnittText,
	});

	const jahrgangManager = new SelectManager({
		options: jahrgaenge,
		optionDisplayText: j => j.bezeichnung ?? '',
		selectionDisplayText: j => j.bezeichnung ?? '',
	});

	const klassenManager = new SelectManager({
		options: klassen,
		optionDisplayText: k => k.kuerzel ?? "",
		selectionDisplayText: k => k.kuerzel ?? "",
	});

	const einschulungsartManager = new SelectManager({
		options: einschulungsarten,
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	const geschlechtManager = new SelectManager({
		options: Geschlecht.values(),
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	const religionManager = new SelectManager({
		options: religionen,
		optionDisplayText: r => r.bezeichnung,
		selectionDisplayText: r => r.bezeichnung,
	});

	// --- validate ---

	function geburtsdatumIsValid() {
		return (data.value.geburtsdatum !== null)
			&& (data.value.geburtsdatum >= minGeburtsdatum)
			&& (data.value.geburtsdatum <= maxGeburtsdatum);
	}

	function anmeldedatumIsValid() {
		if (data.value.anmeldedatum === null) {
			return true;
		}
		return data.value.anmeldedatum <= today;
	}

	function aufnahmedatumIsValid() {
		const { aufnahmedatum, anmeldedatum } = data.value;
		if (aufnahmedatum === null) {
			return true;
		}
		return aufnahmedatum >= (anmeldedatum ?? today);
	}

	function beginnBildungsgangIsValid() {
		const { beginnBildungsgang, aufnahmedatum } = data.value;
		if (beginnBildungsgang === null) {
			return true;
		}
		return beginnBildungsgang >= (aufnahmedatum ?? today);
	}

	const fieldIsValid = (field: keyof SchuelerNeu | null) => {
		switch (field) {
			case 'idSchuljahresabschnitt':
				return (data.value.idSchuljahresabschnitt > 0);
			case 'idJahrgang':
				return (data.value.idJahrgang !== null);
			case 'nachname':
				return mandatoryInputIsValid(data.value.nachname, 120);
			case 'vorname':
				return mandatoryInputIsValid(data.value.vorname, 80);
			case 'alleVornamen':
				return optionalInputIsValid(data.value.alleVornamen, 255);
			case 'geschlecht':
				return (Geschlecht.fromValue(data.value.geschlecht) !== null);
			case 'idReligion':
				return (data.value.idReligion !== null);
			case 'geburtsdatum':
				return geburtsdatumIsValid();
			case 'anmeldedatum':
				return anmeldedatumIsValid();
			case 'aufnahmedatum':
				return aufnahmedatumIsValid();
			case 'beginnBildungsgang':
				return beginnBildungsgangIsValid();
			default:
				return true;
		}
	};

	// --- util ---

	function schuljahresabschnittText(value: Schuljahresabschnitt) {
		return value.schuljahr > 0 ? `${value.schuljahr}/${(value.schuljahr + 1) % 100}.${value.abschnitt}` : "Abschnitt";
	}

	async function addSchueler() {
		if (isLoading.value) {
			return;
		}
		isLoading.value = true;
		props.checkpoint.active = false;
		await props.add(data.value);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.gotoDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
