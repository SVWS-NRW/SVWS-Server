<template>
	<div class="flex gap-x-8 2xl:gap-x-16">
		<div class="flex-grow">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen"
											:gost-jahrgangsdaten="gostJahrgangsdaten" :item="schueler" :set-wahl="setWahl" :get-pdf-wahlbogen="getPdfWahlbogen" />
		</div>
		<div class="w-1/3 4xl:w-2/5">
			<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen"
										   :fehlerliste="gostBelegpruefungErgebnis.fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
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
