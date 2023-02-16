<template>
	<div class="flex flex-row gap-4">
		<div class="flex-none">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen"
				:gost-jahrgangsdaten="gostJahrgangsdaten" :item="schueler" :set-wahl="setWahl" :get-pdf-wahlbogen="getPdfWahlbogen" />
		</div>
		<div class="flex-auto">
			<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen"
				:fehlerliste="gostBelegpruefungErgebnis.fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { AbiturdatenManager, GostBelegpruefungErgebnis, GostBelegpruefungsArt, GostFaecherManager, GostJahrgangFachkombination, GostJahrgangsdaten,
		GostSchuelerFachwahl, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		setGostBelegpruefungsArt: (value: GostBelegpruefungsArt) => Promise<void>;
		getPdfWahlbogen: () => Promise<Blob>;
		schueler: SchuelerListeEintrag | undefined,
		gostJahrgangsdaten: GostJahrgangsdaten;
		gostBelegpruefungsArt: GostBelegpruefungsArt;
		gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
	}>();

	const visible: ComputedRef<boolean> = computed(() => (props.schueler?.id !== undefined) && (props.schueler?.abiturjahrgang !== undefined));

</script>
