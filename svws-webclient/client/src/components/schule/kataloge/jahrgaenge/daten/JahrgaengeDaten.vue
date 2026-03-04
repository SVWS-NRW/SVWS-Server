<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="data.proxy.kuerzel"
						:validation="() => data.getFehler('kuerzel')"
						@commit="data.patch"
						skip-default-validation
						:max-len="20" required :readonly />
					<svws-ui-text-input placeholder="Bezeichnung"
						v-model="data.proxy.bezeichnung"
						:validation="() => data.getFehler('bezeichnung')"
						@commit="data.patch"
						skip-default-validation
						:max-len="100" required :readonly />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						v-model="data.proxy.kurzbezeichnung"
						:validation="() => data.getFehler('kurzbezeichnung')"
						@commit="data.patch"
						skip-default-validation
						:max-len="2" :readonly />
					<ui-select label="Folgejahrgang"
						v-model="data.folgejahrgang.value"
						:manager="folgeJahrgangManager"
						skip-default-validation
						:readonly />
					<ui-select label="Schulgliederung ASD-Kürzel"
						v-model="data.schulgliederung.value"
						:manager="schulgliederungKuerzelSelectManager"
						skip-default-validation
						searchable statistics :readonly />
					<ui-select label="Schulgliederung ASD-Text"
						v-model="data.schulgliederung.value"
						:manager="schulgliederungTextSelectManager"
						skip-default-validation
						searchable statistics :readonly />
					<ui-select label="Jahrgang ASD-Kürzel"
						:manager="jahrgangKuerzelSelectManager"
						v-model="data.statistikJahrgang.value"
						:validation="() => data.getFehler('kuerzelStatistik')"
						skip-default-validation
						searchable statistics :readonly required :removable="false" />
					<ui-select label="Jahrgang ASD-Text"
						:manager="jahrgangTextSelectManager"
						v-model="data.statistikJahrgang.value"
						:validation="() => data.getFehler('kuerzelStatistik')"
						skip-default-validation
						searchable statistics :readonly required :removable="false" />
					<svws-ui-input-number placeholder="Anzahl der Restabschnitte"
						v-model="data.proxy.anzahlRestabschnitte"
						:validation="() => data.getFehler('anzahlRestabschnitte')"
						@commit="data.patch"
						skip-default-validation
						:min="0" :max="40" :readonly />
					<ui-select label="Bildungsstufe"
						:manager="bildungsstufeSelectManager"
						v-model="data.bildungsstufe.value"
						skip-default-validation
						:readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.proxy.sortierung"
						:validation="() => data.getFehler('sortierung')"
						@commit="data.patch"
						skip-default-validation
						:min="0" :max="32000" :readonly :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.proxy.istSichtbar"
						:validation="() => data.getFehler('istSichtbar')"
						@commit="data.patch"
						skip-default-validation
						:readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { Schulgliederung, Jahrgaenge, BenutzerKompetenz, Bildungsstufe } from "@core";
	import type { JahrgangsDaten } from "@core";
	import type { JahrgaengeDatenProps } from "./JahrgaengeDatenProps";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { JahrgaengeModelProxy } from "~/components/schule/kataloge/jahrgaenge/modelproxy/JahrgaengeModelProxy";

	const props = defineProps<JahrgaengeDatenProps>();
	const data = new JahrgaengeModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), props.schuljahr, props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const availableFolgejahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().liste.list()].filter(j => j.id !== props.manager().daten().id));

	const folgeJahrgangManager = new SelectManager({
		options: availableFolgejahrgaenge,
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

</script>
