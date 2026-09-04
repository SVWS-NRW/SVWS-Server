<template>
	<div class="flex flex-col gap-4">
		<div class="flex flex-row justify-between">
			<div class="text-headline-md">Belegprüfungsergebnisse</div>
			<div class="flex flex-row gap-2">
				<svws-ui-radio-option v-model="art" value="ef1" name="ef1" label="EF.1" />
				<svws-ui-radio-option v-model="art" value="gesamt" name="gesamt" label="Gesamt" />
				<svws-ui-radio-option v-model="art" value="auto" name="gesamt" label="Automatisch" />
			</div>
		</div>
		<div v-if="gostLaufbahnplanungState.abiturdatenManager.getBiligualenBildungsgang() !== null" class="mb-4">
			<span class="font-bold">Hinweis:</span> Der Schüler befindet sich aktuell im Bilingualen Zweig ({{ gostLaufbahnplanungState.abiturdatenManager.getBiligualenBildungsgang() }})
		</div>
		<s-laufbahnplanung-fehler :fehlerliste="() => gostLaufbahnplanungState.gostBelegpruefungErgebnis.fehlercodes"
			:pruefungs-art="gostLaufbahnplanungState.abiturdatenManager.getPruefungsArt" />
		<s-laufbahnplanung-informationen :fehlerliste="() => gostLaufbahnplanungState.gostBelegpruefungErgebnis.fehlercodes" />
		<s-laufbahnplanung-sprachpruefungen v-if="sprachendaten" />
		<s-laufbahnplanung-fachkombinationen />
	</div>
</template>

<script setup lang="ts">

	import type { Sprachendaten } from "@core/asd/data/schueler/Sprachendaten";
	import { useGostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";
	import { computed } from "vue";

	const gostLaufbahnplanungState = useGostLaufbahnplanungState();

	const art = computed<'ef1' | 'gesamt' | 'auto'>({
		get: () => gostLaufbahnplanungState.gostBelegpruefungsArt,
		set: (value) => void gostLaufbahnplanungState.setGostBelegpruefungsArt(value),
	});

	const sprachendaten = computed<Sprachendaten | null>(() => gostLaufbahnplanungState.abiturdatenManager.getSprachendaten());

</script>
