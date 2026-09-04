<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="PLZ" class="contentFocusField"
						v-model="model.proxy.plz"
						:validation="() => model.getFehler('plz')"
						@change="model.patch"
						:max-len="10" :disabled="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Ortsname"
						v-model="model.proxy.ortsname"
						:validation="() => model.getFehler('ortsname')"
						@change="model.patch"
						:max-len="50" :disabled="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Kreis"
						v-model="model.proxy.kreis"
						:validation="() => model.getFehler('kreis')"
						@change="model.patch"
						:max-len="3" :disabled="!hatKompetenzUpdate" />
					<ui-select label="Land"
						:manager="laenderManager"
						v-model="model.bundesland.value"
						:disabled="!hatKompetenzUpdate" />
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
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { OrteDatenProps } from "~/components/schule/kataloge/orte/daten/OrteDatenProps";
	import { computed } from "vue";
	import { OrtModelProxy } from "~/components/schule/kataloge/orte/modelproxy/OrtModelProxy";
	import { Laender } from "@core/asd/types/schule/Laender";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

	const props = defineProps<OrteDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const model = new OrtModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), props.patch);
	const schuljahr = schuleState.schuljahr;

	const laenderManager = new CoreTypeSelectManager({
		clazz: Laender.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

</script>

