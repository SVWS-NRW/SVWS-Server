<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						v-model="data.proxy.bezeichnung"
						:validation="() => data.getFehler('bezeichnung')"
						@change="data.patch"
						:max-len="50" :readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.proxy.sortierung"
						:validation="() => data.getFehler('sortierung')"
						@change="data.patch"
						:min="0"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.proxy.istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import { useBenutzerState } from '@ui/states/BenutzerState';
	import type { BetriebsartenDatenProps } from './BetriebsartenDatenProps';
	import { computed } from "vue";
	import { BetriebsartenModelProxy } from "~/components/schule/kataloge/betriebsarten/modelproxy/BetriebsartenModelProxy";

	const props = defineProps<BetriebsartenDatenProps>();
	const benutzerState = useBenutzerState();

	const data = new BetriebsartenModelProxy(() => props.manager().auswahl(), () => props.manager().liste.list(), props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

</script>
