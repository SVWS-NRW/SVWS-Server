<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@change="model.patch"
						:max-len="30" required />
					<svws-ui-input-number placeholder="Entfernung zur Schule"
						v-model="model.proxy.entfernungSchule"
						@change="model.patch"
						:min="0" />
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

	import type { HaltestellenDatenProps } from "~/components/schule/kataloge/haltestellen/daten/HaltestellenDatenProps";
	import { HaltestellenModelProxy } from "~/components/schule/kataloge/haltestellen/modelproxy/HaltestellenModelProxy";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<HaltestellenDatenProps>();
	const benutzerState = useBenutzerState();

	const model = new HaltestellenModelProxy(() => props.manager().daten(), props.manager, props.patch);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzAdd.value);


</script>
