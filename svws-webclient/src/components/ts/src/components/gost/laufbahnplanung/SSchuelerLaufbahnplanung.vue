<template>
	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<div class="flex-grow">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen"
				:gost-jahrgangsdaten="gostJahrgangsdaten" :item="schueler" :set-wahl="setWahl" :get-pdf-wahlbogen="getPdfWahlbogen" :export-laufbahnplanung="exportLaufbahnplanung"
				:import-laufbahnplanung="importLaufbahnplanung" :zwischenspeicher="zwischenspeicher" :save-laufbahnplanung="saveLaufbahnplanung" :restore-laufbahnplanung="restoreLaufbahnplanung" />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem]">
			<div class="flex flex-col gap-16">
				<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen"
					:fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
				<s-laufbahnplanung-card-beratung v-if="visible" :gost-laufbahn-beratungsdaten="gostLaufbahnBeratungsdaten" :patch-beratungsdaten="patchBeratungsdaten" :map-lehrer="mapLehrer" :id="id" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { SchuelerLaufbahnplanungProps } from "./SSchuelerLaufbahnplanungProps";

	const props = defineProps<SchuelerLaufbahnplanungProps>();

	const visible: ComputedRef<boolean> = computed(() => props.schueler.abiturjahrgang !== undefined);

</script>
