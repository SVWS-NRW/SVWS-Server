<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@change="model.patch"
						:max-len="20" required :readonly />
					<svws-ui-text-input placeholder="Bezeichnung"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@change="model.patch"
						:max-len="100" required :readonly />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						v-model="model.proxy.kurzbezeichnung"
						:validation="() => model.getFehler('kurzbezeichnung')"
						@change="model.patch"
						:max-len="2" :readonly />
					<ui-select label="Folgejahrgang"
						v-model="model.folgejahrgang.value"
						:manager="folgeJahrgangManager"
						:readonly />
					<ui-select label="Schulgliederung ASD-Kürzel"
						v-model="model.schulgliederung.value"
						:manager="schulgliederungKuerzelSelectManager"
						searchable statistics :readonly />
					<ui-select label="Schulgliederung ASD-Text"
						v-model="model.schulgliederung.value"
						:manager="schulgliederungTextSelectManager"
						searchable statistics :readonly />
					<ui-select label="Jahrgang ASD-Kürzel"
						:manager="jahrgangKuerzelSelectManager"
						v-model="model.asdJahrgang.value"
						:validation="() => model.getFehler('idJahrgang')"
						searchable statistics :readonly required :removable="false" />
					<ui-select label="Jahrgang ASD-Text"
						:manager="jahrgangTextSelectManager"
						v-model="model.asdJahrgang.value"
						:validation="() => model.getFehler('idJahrgang')"
						searchable statistics :readonly required :removable="false" />
					<svws-ui-input-number placeholder="Anzahl der Restabschnitte"
						v-model="model.proxy.anzahlRestabschnitte"
						:validation="() => model.getFehler('anzahlRestabschnitte')"
						@change="model.patch"
						:min="0" :max="40" :readonly />
					<ui-select label="Bildungsstufe"
						:manager="bildungsstufeSelectManager"
						v-model="model.bildungsstufe.value"
						:readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0" :max="32000"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { JahrgaengeDatenProps } from "./JahrgaengeDatenProps";
	import { JahrgangModelProxy } from "~/components/schule/kataloge/jahrgaenge/modelproxy/JahrgangModelProxy";
	import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
	import { Bildungsstufe } from "@core/asd/types/schule/Bildungsstufe";
	import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	const props = defineProps<JahrgaengeDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const model = new JahrgangModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), schuleState.abschnitt.schuljahr, props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const availableFolgejahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().liste.list()].filter(j => j.id !== props.manager().daten().id));

	const folgeJahrgangManager = new SelectManager({
		options: availableFolgejahrgaenge,
		optionDisplayText: v => v.bezeichnung ?? "",
		selectionDisplayText: v => v.bezeichnung ?? "",
	});

	const schulgliederungTextSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulgliederungKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const jahrgangTextSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const jahrgangKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const bildungsstufeSelectManager = new CoreTypeSelectManager({
		clazz: Bildungsstufe.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

</script>
