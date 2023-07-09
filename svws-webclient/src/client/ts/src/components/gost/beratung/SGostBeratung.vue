<template>
	<div v-if="jahrgangsdaten !== undefined" class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<div class="flex-grow">
			<s-laufbahnplanung-card-planung :abiturdaten-manager="abiturdatenManager"
				:gost-jahrgangsdaten="jahrgangsdaten()" :item="schueler" :set-wahl="setWahl" />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem]">
			<div class="flex flex-col gap-16">
				<s-card-gost-text-beratungsbogen :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
				<s-card-gost-text-mailversand :jahrgangsdaten="jahrgangsdaten" :patch-jahrgangsdaten="patchJahrgangsdaten" />
				<s-card-gost-beratungslehrer v-if="istAbiturjahrgang" :jahrgangsdaten="jahrgangsdaten" />
				<s-laufbahnplanung-card-status :abiturdaten-manager="abiturdatenManager"
					:fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { GostBeratungProps } from "./SGostBeratungProps";
	import { SchuelerListeEintrag } from "@core";

	const props = defineProps<GostBeratungProps>();

	const istAbiturjahrgang: ComputedRef<boolean> = computed(() => (props.jahrgangsdaten().abiturjahr > 0));

	const schueler: ComputedRef<SchuelerListeEintrag> = computed(() => {
		const e = new SchuelerListeEintrag();
		e.vorname = "Vorlage";
		e.nachname = props.jahrgangsdaten().jahrgang || "undefined";
		e.id = props.jahrgangsdaten().abiturjahr;
		return e;
	});

</script>
