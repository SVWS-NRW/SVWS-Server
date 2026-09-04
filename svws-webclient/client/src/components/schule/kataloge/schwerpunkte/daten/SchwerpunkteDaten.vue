<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@change="model.patch"
						required :max-len="50" :readonly />
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
	import type { SchwerpunkteDatenProps } from './SchwerpunkteDatenProps';
	import { SchwerpunkteModelProxy } from "~/components/schule/kataloge/schwerpunkte/modelproxy/SchwerpunkteModelProxy";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<SchwerpunkteDatenProps>();
	const benutzerState = useBenutzerState();

	const model = new SchwerpunkteModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => {
		return benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	});
	const readonly = computed(() => !hatKompetenzUpdate.value);
</script>
