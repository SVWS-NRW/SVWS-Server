<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="PLZ" class="contentFocusField"
						v-model="model.proxy.plz"
						:validation="() => model.getFehler('plz')"
						@commit="model.patch"
						:max-len="10" :disabled="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Ortsname"
						v-model="model.proxy.ortsname"
						:validation="() => model.getFehler('ortsname')"
						@commit="model.patch"
						:max-len="50" :disabled="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Kreis"
						v-model="model.proxy.kreis"
						:validation="() => model.getFehler('kreis')"
						@commit="model.patch"
						:max-len="3" :disabled="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Land"
						v-model="model.proxy.kuerzelBundesland"
						:validation="() => model.getFehler('kuerzelBundesland')"
						@commit="model.patch"
						:max-len="2" :disabled="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@commit="model.patch"
						:min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" />
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
	import { BenutzerKompetenz } from "@core";
	import { OrtModelProxy } from "~/components/schule/kataloge/orte/modelproxy/OrtModelProxy";

	const props = defineProps<OrteDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const model = new OrtModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), props.patch);

</script>

