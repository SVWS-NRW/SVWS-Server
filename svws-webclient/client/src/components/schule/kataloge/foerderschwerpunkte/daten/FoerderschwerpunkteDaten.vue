<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Förderschwerpunkt ASD-Kürzel"
						v-model="model.foerderschwerpunkt.value"
						:validation="() => model.getFehler('kuerzelStatistik')"
						:manager="foerderschwerpunktKuerzelManager"
						searchable required :removable="false" statistics />
					<ui-select label="Förderschwerpunkt ASD-Text" class="contentFocusField"
						v-model="model.foerderschwerpunkt.value"
						:validation="() => model.getFehler('kuerzelStatistik')"
						:manager="foerderschwerpunktTextManager"
						searchable :removable="false" statistics required />
					<svws-ui-text-input placeholder="Interne Bezeichnung" span="2"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@change="model.patch"
						:max-len="50" required />
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
					<svws-ui-checkbox v-model="model.proxy.istSichtbar">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunkteDatenProps } from "~/components/schule/kataloge/foerderschwerpunkte/daten/FoerderschwerpunkteDatenProps";
	import { FoerderschwerpunkteModelProxy } from "~/components/schule/kataloge/foerderschwerpunkte/modelproxy/FoerderschwerpunkteModelProxy";
	import { computed } from "vue";
	import { Foerderschwerpunkt } from "@core/asd/types/schule/Foerderschwerpunkt";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

	const props = defineProps<FoerderschwerpunkteDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const model = new FoerderschwerpunkteModelProxy(() => props.manager().daten(), props.manager, schuleState.abschnitt.schuljahr, props.patch);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzAdd.value);

	const foerderschwerpunktKuerzelManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const foerderschwerpunktTextManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

</script>
