<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						:model-value="model.proxy.bezeichnung"
						readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :max="32000"
						:readonly="!hatKompetenzUpdate"
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :disabled="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { ErzieherartenDatenProps } from "~/components/schule/kataloge/erzieherarten/daten/ErzieherartenDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { ErzieherartenModelProxy } from "../modelproxy/ErzieherartenModelProxy";

	const props = defineProps<ErzieherartenDatenProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const model = new ErzieherartenModelProxy(() => props.manager().daten(), () => props.manager(), props.patch);

</script>
