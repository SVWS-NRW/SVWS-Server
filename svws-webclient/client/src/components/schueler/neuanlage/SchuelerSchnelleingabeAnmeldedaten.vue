<template>
	<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
		<svws-ui-input-wrapper :grid="4">
			<ui-select label="Status"
				v-model="status"
				:manager="statusManager"
				:removable="false" searchable />
			<svws-ui-text-input placeholder="Schuljahresabschnitt"
				:model-value="schuljahresabschnitt"
				readonly required />
			<svws-ui-text-input placeholder="Jahrgang"
				:model-value="jahrgang"
				readonly required />
			<ui-select label="Klasse"
				v-model="klasse"
				:manager="klassenManager"
				searchable />
			<svws-ui-spacing />
			<ui-select label="Einschulungsart" v-if="schulenMitPrimaerstufe"
				v-model="einschulungsart"
				:manager="einschulungsartManager"
				:removable="false" />
			<svws-ui-text-input placeholder="Anmeldedatum" type="date"
				:model-value="manager().stammdaten.anmeldedatum"
				@change="patchAnmeldedatum"
				:valid="anmeldedatumIsValid"
				:readonly :max-date="today" />
			<svws-ui-text-input placeholder="Aufnahmedatum" type="date"
				:model-value="manager().stammdaten.aufnahmedatum"
				@change="patchAufnahmedatum"
				:valid="datumIsValid"
				:readonly :min-date="manager().stammdaten.anmeldedatum || today" />
			<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-if="schulenMitBKoderSK"
				:model-value="manager().stammdaten.beginnBildungsgang"
				@change="patchBeginnBildungsgang"
				:valid="datumIsValid"
				:readonly :min-date="manager().stammdaten.aufnahmedatum || today" />
			<svws-ui-input-number placeholder="Dauer Bildungsgang" v-if="schulenMitBKoderSK"
				:model-value="manager().stammdaten.dauerBildungsgang"
				@change="dauerBildungsgang => patchSchueler({ dauerBildungsgang }, manager().stammdaten.id)"
				:readonly />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchnelleingabeManager } from "@ui";
	import type { EinschulungsartKatalogEintrag, KlassenDaten, SchuelerSchulbesuchsdaten, SchuelerStammdaten, SchuelerStatusKatalogEintrag,
		SchuelerLernabschnittsdaten } from "@core";
	import { SchuelerStatus, Schulform } from "@core";
	import { computed } from "vue";
	import { CoreTypeSelectManager, SelectManager, useAbschnittState, useSchuleState } from "@ui";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		patchSchueler: (patchObject: Partial<SchuelerStammdaten>, id: number) => Promise<void>;
		patchSchulbesuchsdaten: (data: Partial<SchuelerSchulbesuchsdaten>, idSchueler: number) => Promise<void>;
		patchLernabschnittsdaten: (data: Partial<SchuelerLernabschnittsdaten>, idEintrag: number) => Promise<void>;
		readonly: boolean;
		schulenMitPrimaerstufe: boolean;
	}>();
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();

	const manager = () => props.manager();
	const schulenMitBKoderSK = computed(() => (schuleState.schulform === Schulform.BK) || (schuleState.schulform === Schulform.SK));
	const jahrgaenge = computed(() => Array.from(props.manager().jahrgaengeById.values()));
	const schuljahresabschnitte = computed(() => Array.from(props.manager().schuljahresabschnitte));
	const einschulungsarten = computed(() => props.manager().einschulungsartenById.values());

	const klassen = computed(() => {
		return [...props.manager().klassenAktuell]
			.filter(k => k.idJahrgang === manager().lernabschnittsdaten.jahrgangID);
	});

	const status = computed<SchuelerStatusKatalogEintrag | null>({
		get: () => SchuelerStatus.data().getWertByKuerzel('' + props.manager().stammdaten.status)?.daten(abschnittState.auswahl.schuljahr) ?? null,
		set: (value: SchuelerStatusKatalogEintrag | null) => {
			props.manager().stammdaten.status = value?.id ?? -1;
			void props.patchSchueler({ status: value?.id }, manager().stammdaten.id);
		},
	});

	const schuljahresabschnitt = computed<string>(() => {
		const abschnitt = schuljahresabschnitte.value.find(i => i.id === manager().lernabschnittsdaten.schuljahresabschnitt) ?? null;
		if (abschnitt === null) {
			return '';
		}
		return `${abschnitt.schuljahr}/${(abschnitt.schuljahr + 1) % 100}.${abschnitt.abschnitt}`;
	});

	const jahrgang = computed<string | null>(
		() => jahrgaenge.value.find(i => i.id === (manager().lernabschnittsdaten.jahrgangID))?.kuerzel ?? null);

	const klasse = computed<KlassenDaten | null>({
		get: () => klassen.value.find(i => i.id === (manager().lernabschnittsdaten.klassenID)) ?? null,
		set: (value: KlassenDaten | null) => {
			void props.patchLernabschnittsdaten({ klassenID: value?.id ?? null }, manager().lernabschnittsdaten.id);
			manager().lernabschnittsdaten.klassenID = value?.id ?? -1;
		},
	});

	const einschulungsart = computed({
		get: () => props.manager().einschulungsartenById.get(props.manager().schulbesuchsdaten.grundschuleEinschulungsartID ?? -1) ?? null,
		set: (value: EinschulungsartKatalogEintrag) => {
			props.manager().schulbesuchsdaten.grundschuleEinschulungsartID = value.id;
			void props.patchSchulbesuchsdaten({ grundschuleEinschulungsartID: value.id }, manager().stammdaten.id);
		},
	});

	const statusManager = new CoreTypeSelectManager({
		clazz: SchuelerStatus.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text" });

	const klassenManager = new SelectManager({
		options: klassen,
		optionDisplayText: i => i.kuerzel ?? '',
		selectionDisplayText: i => i.kuerzel ?? '',
	});

	const einschulungsartManager = new SelectManager({
		options: einschulungsarten,
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	// --- validation ---

	const today = new Date().toISOString().split("T")[0];

	async function patchAnmeldedatum(anmeldedatum: string | null) {
		if (anmeldedatumIsValid(anmeldedatum)) {
			await props.patchSchueler({ anmeldedatum }, manager().stammdaten.id);
		}
	}

	async function patchAufnahmedatum(aufnahmedatum: string | null) {
		if (datumIsValid(aufnahmedatum)) {
			await props.patchSchueler({ aufnahmedatum }, manager().stammdaten.id);
		}
	}

	async function patchBeginnBildungsgang(beginnBildungsgang: string | null) {
		if (datumIsValid(beginnBildungsgang)) {
			await props.patchSchueler({ beginnBildungsgang }, manager().stammdaten.id);
		}
	}

	function anmeldedatumIsValid(value: string | null) {
		if (value === null) {
			return true;
		}
		return value <= today;
	}

	function datumIsValid(value: string | null) {
		if (value === null) {
			return true;
		}
		return value >= (props.manager().stammdaten.aufnahmedatum ?? today);
	}

</script>
