<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung"
					:readonly="!hatKompetenzUpdate" :max-len="30" :min-len="1" @change="v => patch({ bezeichnung: v ?? undefined })" />
				<svws-ui-input-number placeholder="Sortierung" :model-value="manager().daten().sortierung" :readonly="!hatKompetenzUpdate"
					@change="value => patch({ sortierung: value === null ? 32000 : value })" />
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="manager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })"
					:readonly="!hatKompetenzUpdate">
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { EntlassgruendeDatenProps } from "~/components/schule/kataloge/entlassgruende/daten/SEntlassgruendeDatenProps";
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";

	const props = defineProps<EntlassgruendeDatenProps>()
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));

</script>
