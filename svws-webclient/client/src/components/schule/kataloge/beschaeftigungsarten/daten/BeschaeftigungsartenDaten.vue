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
						:readonly :min="0" required />
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

	import type { BeschaeftigungsartenDatenProps } from "~/components/schule/kataloge/beschaeftigungsarten/daten/BeschaeftigungsartenDatenProps";
	import { computed } from "vue";
	import { BeschaeftigungsartModelProxy } from "~/components/schule/kataloge/beschaeftigungsarten/modelproxy/BeschaeftigungsartModelProxy";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<BeschaeftigungsartenDatenProps>();
	const benutzerState = useBenutzerState();

	const data = new BeschaeftigungsartModelProxy(() => props.manager().auswahl(), () => props.manager().liste.list(), props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);


</script>
