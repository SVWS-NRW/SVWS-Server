<template>
	<div class="flex gap-x-8 2xl:gap-x-16">
		<div class="flex-grow">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen"
				:gost-jahrgangsdaten="gostJahrgangsdaten" :item="schueler" :set-wahl="setWahl" :get-pdf-wahlbogen="getPdfWahlbogen" :export-laufbahnplanung="exportLaufbahnplanung" :import-laufbahnplanung="importLaufbahnplanung" />
		</div>
		<div class="w-1/3 4xl:w-2/5">
			<div class="flex flex-col gap-3 sticky -top-8 pt-8">
				<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen"
					:fehlerliste="gostBelegpruefungErgebnis.fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
				<s-laufbahnplanung-card-beratung v-if="visible" :gost-laufbahn-beratungsdaten="gostLaufbahnBeratungsdaten" :patch-beratungsdaten="patchBeratungsdaten" :map-lehrer="mapLehrer" />
			</div>
		</div>
	</div>
</template>

<style lang="postcss">
	.bg--stripes {
		@apply text-black/50 text-sm cursor-not-allowed;
		background-image: url("/images/table-cell--stripes.svg");
		background-size: 12px;
		background-attachment: fixed;
	}
</style>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { SchuelerLaufbahnplanungProps } from "./SSchuelerLaufbahnplanungProps";

	const props = defineProps<SchuelerLaufbahnplanungProps>();

	const visible: ComputedRef<boolean> = computed(() => props.schueler?.abiturjahrgang !== undefined);

</script>
