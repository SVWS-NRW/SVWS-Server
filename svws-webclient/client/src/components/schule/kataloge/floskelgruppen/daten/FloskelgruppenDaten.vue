<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
					v-model="model.proxy.kuerzel"
					:validation="() => model.getFehler('kuerzel')"
					@change="model.patch"
					:max-len="10" required :readonly />
				<svws-ui-text-input placeholder="Bezeichnung"
					v-model="model.proxy.bezeichnung"
					:validation="() => model.getFehler('bezeichnung')"
					@change="model.patch"
					:max-len="50" required :readonly />
				<ui-select label="Floskelgruppenart"
					v-model="model.selectedFloskelgruppenart.value"
					:validation="() => model.getFehler('idFloskelgruppenart')"
					:manager="floskelgruppenartManager"
					:removable="false" required :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FloskelgruppenDatenProps } from "./FloskelgruppenDatenProps";
	import { FloskelgruppeModelProxy } from "~/components/schule/kataloge/floskelgruppen/modelproxy/FloskelgruppeModelProxy";
	import { Floskelgruppenart } from "@core/asd/types/schule/Floskelgruppenart";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

	const props = defineProps<FloskelgruppenDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
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
