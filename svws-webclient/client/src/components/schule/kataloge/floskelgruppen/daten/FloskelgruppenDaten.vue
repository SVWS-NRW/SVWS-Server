<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
					v-model="model.proxy.kuerzel"
					:validation="() => model.getFehler('kuerzel')"
					@commit="model.patch"
					:max-len="10" required :readonly />
				<svws-ui-text-input placeholder="Bezeichnung"
					v-model="model.proxy.bezeichnung"
					:validation="() => model.getFehler('bezeichnung')"
					@commit="model.patch"
					:max-len="50" required :readonly />
				<ui-select label="Floskelgruppenart"
					v-model="model.selectedFloskelgruppenart.value"
					:validation="() => model.getFehler('idFloskelgruppenart')"
					:manager="floskelgruppenartManager"
					:removable="false" searchable required :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, Floskelgruppenart } from "@core";
	import { computed } from "vue";
	import { CoreTypeSelectManager, useSchuleState } from "@ui";
	import type { FloskelgruppenDatenProps } from "./FloskelgruppenDatenProps";
	import { FloskelgruppeModelProxy } from "~/components/schule/kataloge/floskelgruppen/modelproxy/FloskelgruppeModelProxy";

	const props = defineProps<FloskelgruppenDatenProps>();
	const schuleState = useSchuleState();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const model = new FloskelgruppeModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), schuleState.abschnitt.schuljahr, props.patch);
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);

	const floskelgruppenartManager = new CoreTypeSelectManager({
		clazz: Floskelgruppenart.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	</script>
