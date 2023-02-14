<template>
	<div class="flex flex-row gap-4">
		<div class="flex-none">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturmanager="abiturmanager" :faechermanager="faechermanager" :fachkombinationen="fachkombinationen"
				:jahrgangsdaten="jahrgangsdaten" :item="schueler" :set-wahl="setWahl" :get-pdf-wahlbogen="getPdfWahlbogen" />
		</div>
		<div class="flex-auto">
			<s-laufbahnplanung-card-status v-if="visible" :abiturmanager="abiturmanager" :faechermanager="faechermanager" :fachkombinationen="fachkombinationen"
				:fehlerliste="belegpruefungsergebnis.fehlercodes" :belegpruefungsart="props.belegpruefungsart" @update:belegpruefungsart="setBelegpruefungsart" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { AbiturdatenManager, GostBelegpruefungErgebnis, GostBelegpruefungsArt, GostFaecherManager, GostJahrgangFachkombination, GostJahrgangsdaten,
		GostSchuelerFachwahl, List, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		setBelegpruefungsart: (value: GostBelegpruefungsArt) => Promise<void>;
		getPdfWahlbogen: () => Promise<Blob>;
		schueler: SchuelerListeEintrag | undefined,
		jahrgangsdaten: GostJahrgangsdaten;
		belegpruefungsart: GostBelegpruefungsArt;
		belegpruefungsergebnis: GostBelegpruefungErgebnis;
		abiturmanager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		fachkombinationen: List<GostJahrgangFachkombination>;
	}>();

	const visible: ComputedRef<boolean> = computed(() => (props.schueler?.id !== undefined) && (props.schueler?.abiturjahrgang !== undefined));

</script>
