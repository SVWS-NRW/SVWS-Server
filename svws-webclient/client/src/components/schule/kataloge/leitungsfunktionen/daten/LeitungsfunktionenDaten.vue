<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-text-input placeholder="Bezeichnung" span="2" class="contentFocusField"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@commit="model.patch"
						:max-len="50" required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@commit="model.patch"
						:min="0" :max="32000" :removable="false" />
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

	import type { LeitungsfunktionenDatenProps } from "./LeitungsfunktionenDatenProps";
	import { LeitungsfunktionenModelProxy } from "~/components/schule/kataloge/leitungsfunktionen/modelproxy/LeitungsfunktionenModelProxy";

	const props = defineProps<LeitungsfunktionenDatenProps>();
	const model = new LeitungsfunktionenModelProxy(() => props.manager().daten(), props.manager, props.patch);

</script>
