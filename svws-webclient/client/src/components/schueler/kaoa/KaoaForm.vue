<template>
	<div class="w-full flex flex-col gap-2">
		<div class="overflow-hidden flex flex-col gap-2">
			<ui-select label="Schuljahresabschnitt" class="mt-2"
				v-model="selectedSchuljahresabschnitt"
				:manager="schuljahresabschnittManager"
				:readonly searchable :removable="false" />
			<ui-select v-if="data.idSchuljahresabschnitt !== -1"
				class="pl-8" label="Kategorie"
				v-model="selectedKategorie"
				:manager="kategorieManager"
				:readonly searchable />
			<ui-select v-if="data.idKategorie !== -1"
				class="pl-16" label="Merkmal"
				v-model="selectedMerkmal"
				:manager="merkmalManager"
				:readonly searchable />
			<ui-select v-if="data.idMerkmal !== -1"
				class="pl-24" label="Zusatzmerkmal"
				v-model="selectedZusatzmerkmal"
				:manager="zusatzmerkmalManager"
				:readonly searchable />
			<ui-select v-if="showEbene4"
				class="pl-32" label="KAoAEbene4"
				v-model="selectedEbene4"
				:manager="ebene4Manager"
				:readonly searchable />
			<ui-select v-if="showAnschlussoption"
				class="pl-32" label="KAoAAnschlussoption"
				v-model="selectedAnschlussoption"
				:manager="anschlussoptionManager"
				:readonly searchable />
			<ui-select v-if="showBerufsfeld"
				class="pl-32" label="KAoABerufsfeld"
				v-model="selectedBerufsfeld"
				:manager="berufsfeldmanager"
				:readonly searchable />
			<div class="pl-32">
				<svws-ui-text-input v-if="showFreitext"
					placeholder="Bemerkung"
					v-model="data.bemerkung"
					:valid="(v) => optionalInputIsValid(v, 255)"
					:max-len="255" :readonly :rows="6" />
			</div>
			<slot />
		</div>
	</div>
</template>
<script setup lang="ts">
	import type { JahrgaengeKatalogEintrag, KAOAAnschlussoptionenKatalogEintrag, KAOABerufsfeldKatalogEintrag, KAOAEbene4KatalogEintrag,
		KAOAKategorieKatalogEintrag, KAOAMerkmalKatalogEintrag, KAOAZusatzmerkmalKatalogEintrag, SchuelerKAoADaten, SchuelerListeEintrag,
		Schuljahresabschnitt } from "@core";
	import type { SchuelerKAoAManager } from "@ui";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { Jahrgaenge, KAOAAnschlussoptionen, KAOABerufsfeld, KAOAEbene4, KAOAKategorie, KAOAMerkmal, KAOAZusatzmerkmal } from "@core";
	import { computed, watch } from "vue";
	import { optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<{
		data: SchuelerKAoADaten;
		manager: () => SchuelerKAoAManager;
		auswahl: () => SchuelerListeEintrag;
		readonly: boolean;
	}>();

	const schuljahresabschnitteFiltered = computed(() => props.manager().schuljahresabschnitteFiltered);
	const schuljahr = computed<number>(() => selectedSchuljahresabschnitt.value?.schuljahr ?? -1);
	const jahrgang = computed<JahrgaengeKatalogEintrag | null>(() => {
		const kuerzelJahrgang = props.manager().lernabschnitteBySchuljahr.get(schuljahr.value)?.jahrgang ?? '';
		return Jahrgaenge.data().getWertByKuerzel(kuerzelJahrgang)?.daten(schuljahr.value) ?? null;
	});
	const zusatzmerkmal = computed<KAOAZusatzmerkmalKatalogEintrag | null>(() => KAOAZusatzmerkmal.data().getEintragByID(props.data.idZusatzmerkmal) ?? null);
	const showEbene4 = computed<boolean>(() => zusatzmerkmal.value?.optionsart === 'SBO_EBENE_4');
	const showAnschlussoption = computed<boolean>(() => zusatzmerkmal.value?.optionsart === 'ANSCHLUSSOPTION');
	const showBerufsfeld = computed<boolean>(() => zusatzmerkmal.value?.optionsart === 'BERUFSFELD');
	const showFreitext = computed<boolean>(() => (zusatzmerkmal.value?.optionsart === 'FREITEXT') || (zusatzmerkmal.value?.optionsart === 'FREITEXT_BERUF'));

	const selectedSchuljahresabschnitt = computed<Schuljahresabschnitt | null>({
		get: () => {
			const idSchuljahresabschnitt = (props.data.idSchuljahresabschnitt === -1) ? props.auswahl().idSchuljahresabschnitt : props.data.idSchuljahresabschnitt;
			return props.manager().schuljahresabschnitteById.get(idSchuljahresabschnitt) ?? null;
		},
		set: (v: Schuljahresabschnitt | null) => updateModel(1, v?.id ?? -1),
	});

	const selectedKategorie = computed<KAOAKategorieKatalogEintrag | null>({
		get: () => KAOAKategorie.data().getEintragByID(props.data.idKategorie),
		set: (v: KAOAKategorieKatalogEintrag | null) => updateModel(2, v?.id ?? -1),
	});

	const selectedMerkmal = computed<KAOAMerkmalKatalogEintrag | null>({
		get: () => KAOAMerkmal.data().getEintragByID(props.data.idMerkmal),
		set: (v: KAOAMerkmalKatalogEintrag | null) => updateModel(3, v?.id ?? -1),
	});

	const selectedZusatzmerkmal = computed<KAOAZusatzmerkmalKatalogEintrag | null>({
		get: () => KAOAZusatzmerkmal.data().getEintragByID(props.data.idZusatzmerkmal),
		set: (v: KAOAZusatzmerkmalKatalogEintrag | null) => updateModel(4, v?.id ?? -1),
	});

	const selectedEbene4 = computed<KAOAEbene4KatalogEintrag | null>({
		get: () => KAOAEbene4.data().getEintragByID(props.data.idEbene4 ?? -1),
		set: (v: KAOAEbene4KatalogEintrag | null) => props.data.idEbene4 = v?.id ?? null,
	});

	const selectedAnschlussoption = computed<KAOAAnschlussoptionenKatalogEintrag | null>({
		get: () => KAOAAnschlussoptionen.data().getEintragByID(props.data.idAnschlussoption ?? -1),
		set: (v: KAOAAnschlussoptionenKatalogEintrag | null) => props.data.idAnschlussoption = v?.id ?? null,
	});

	const selectedBerufsfeld = computed<KAOABerufsfeldKatalogEintrag | null>({
		get: () => KAOABerufsfeld.data().getEintragByID(props.data.idBerufsfeld ?? -1),
		set: (v: KAOABerufsfeldKatalogEintrag | null) => props.data.idBerufsfeld = v?.id ?? null,
	});

	const schuljahresabschnittManager = new SelectManager({
		options: schuljahresabschnitteFiltered,
		optionDisplayText: v => schuljahresabschnittText(v),
		selectionDisplayText: v => schuljahresabschnittText(v),
	});

	const kategorieManager = new CoreTypeSelectManager({
		filters: [{ key: 'vorhandene', apply: () => KAOAKategorie.getEintraegeBySchuljahrAndIdJahrgang(schuljahr.value, props.data.idJahrgang) }],
		clazz: KAOAKategorie.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const merkmalManager = new CoreTypeSelectManager({
		filters: [{ key: 'vorhandene', apply: () => KAOAMerkmal.getEintraegeBySchuljahrAndIdKategorie(schuljahr.value, props.data.idKategorie) }],
		clazz: KAOAMerkmal.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const zusatzmerkmalManager = new CoreTypeSelectManager({
		filters: [{ key: 'vorhandene', apply: () => KAOAZusatzmerkmal.getEintraegeBySchuljahrAndIdMerkmal(schuljahr.value, props.data.idMerkmal) }],
		clazz: KAOAZusatzmerkmal.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const ebene4Manager = new CoreTypeSelectManager({
		filters: [{ key: 'vorhandene', apply: () => KAOAEbene4.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr.value, zusatzmerkmal.value?.id ?? -1) }],
		clazz: KAOAEbene4.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const anschlussoptionManager = new CoreTypeSelectManager({
		filters: [{
			key: 'vorhandene',
			apply: () => KAOAAnschlussoptionen.getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr.value, zusatzmerkmal.value?.id ?? -1),
		}],
		clazz: KAOAAnschlussoptionen.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const berufsfeldmanager = new CoreTypeSelectManager({
		filters: [{ key: 'vorhandene', apply: () => KAOABerufsfeld.getEintraegeBySchuljahr(schuljahr.value) }],
		clazz: KAOABerufsfeld.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	// setzt die selektierten Felder abhängig vom Ziellevel zurück
	function updateModel(targetLevel: number, value: number) {
		if (targetLevel <= 1) {
			props.data.idSchuljahresabschnitt = value;
			props.data.idJahrgang = jahrgang.value?.id ?? -1;
		}
		if (targetLevel <= 2) {
			props.data.idKategorie = targetLevel === 2 ? value : -1;
		}
		if (targetLevel <= 3) {
			props.data.idMerkmal = targetLevel === 3 ? value : -1;
		}
		if (targetLevel <= 4) {
			props.data.idZusatzmerkmal = targetLevel === 4 ? value : -1;
		}
		if (targetLevel >= 1) {
			props.data.idEbene4 = null;
			props.data.idAnschlussoption = null;
			props.data.idBerufsfeld = null;
			props.data.bemerkung = null;
		}
	}

	function schuljahresabschnittText(value: Schuljahresabschnitt) {
		return value.schuljahr > 0 ? `${value.schuljahr}/${(value.schuljahr + 1) % 100}.${value.abschnitt}` : "Abschnitt";
	}

	watch(() => props.data, async () => {
		kategorieManager.updateFilteredOptions();
		merkmalManager.updateFilteredOptions();
		zusatzmerkmalManager.updateFilteredOptions();
		ebene4Manager.updateFilteredOptions();
		anschlussoptionManager.updateFilteredOptions();
		berufsfeldmanager.updateFilteredOptions();
	}, { immediate: true, deep: true });

</script>

