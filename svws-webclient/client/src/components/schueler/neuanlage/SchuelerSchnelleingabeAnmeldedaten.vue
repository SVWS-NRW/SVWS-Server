<template>
	<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
		<svws-ui-input-wrapper :grid="4">
			<ui-select label="Status"
				v-model="status"
				:manager="statusManager"
				:removable="false" />
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
				:readonly />
			<svws-ui-text-input placeholder="Aufnahmedatum" type="date"
				:model-value="manager().stammdaten.aufnahmedatum"
				@change="patchAufnahmedatum"
				:readonly />
			<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-if="schulenMitBKoderSK"
				:model-value="manager().stammdaten.beginnBildungsgang"
				@change="patchBeginnBildungsgang"
				:readonly />
			<svws-ui-input-number placeholder="Dauer Bildungsgang" v-if="schulenMitBKoderSK"
				:model-value="manager().stammdaten.dauerBildungsgang"
				@change="dauerBildungsgang => patchSchueler({ dauerBildungsgang }, manager().stammdaten.id)"
				:readonly />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
	import type { EinschulungsartKatalogEintrag } from "@core/asd/data/schueler/EinschulungsartKatalogEintrag";
	import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
	import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
	import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
	import type { SchuelerStatusKatalogEintrag } from "@core/asd/data/schueler/SchuelerStatusKatalogEintrag";
	import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";
	import { computed } from "vue";

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
		get: () => props.manager().einschulungsartenById.get(props.manager().schulbesuchsdaten.idEinschulungsartGrundschule ?? -1) ?? null,
		set: (value: EinschulungsartKatalogEintrag) => {
			props.manager().schulbesuchsdaten.idEinschulungsartGrundschule = value.id;
			void props.patchSchulbesuchsdaten({ idEinschulungsartGrundschule: value.id }, manager().stammdaten.id);
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

	async function patchAnmeldedatum(anmeldedatum: string | null) {
		await props.patchSchueler({ anmeldedatum }, manager().stammdaten.id);
	}

	async function patchAufnahmedatum(aufnahmedatum: string | null) {
		await props.patchSchueler({ aufnahmedatum }, manager().stammdaten.id);
	}

	async function patchBeginnBildungsgang(beginnBildungsgang: string | null) {
		await props.patchSchueler({ beginnBildungsgang }, manager().stammdaten.id);
	}

</script>
