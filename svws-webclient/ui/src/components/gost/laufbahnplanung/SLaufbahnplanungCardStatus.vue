<template>
	<div class="flex flex-col gap-2">
		<div class="flex flex-row justify-between">
			<div class="text-headline-md">Belegprüfungsergebnisse</div>
			<div class="flex flex-row gap-2">
				<svws-ui-radio-option v-model="art" value="ef1" name="ef1" label="EF.1" />
				<svws-ui-radio-option v-model="art" value="gesamt" name="gesamt" label="Gesamt" />
				<svws-ui-radio-option v-model="art" value="auto" name="gesamt" label="Automatisch" />
			</div>
		</div>
		<div class="print:!hidden">
			<div v-if="abiturdatenManager().getBiligualenBildungsgang() !== null" class="mb-4">
				<span class="font-bold">Hinweis:</span> Der Schüler befindet sich aktuell im Bilingualen Zweig ({{ abiturdatenManager().getBiligualenBildungsgang() }})
			</div>
			<s-laufbahnplanung-fehler :fehlerliste :belegpruefungs-art="() => abiturdatenManager().getPruefungsArt()" />
			<s-laufbahnplanung-informationen :fehlerliste />
			<s-laufbahnplanung-sprachpruefungen v-if="sprachendaten" :schuljahr :sprachendaten="() => abiturdatenManager().getSprachendaten()" />
			<s-laufbahnplanung-fachkombinationen :abiturdaten-manager />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { AbiturdatenManager } from "../../../../../core/src/core/abschluss/gost/AbiturdatenManager";
	import type { GostBelegpruefungErgebnisFehler } from "../../../../../core/src/core/abschluss/gost/GostBelegpruefungErgebnisFehler";
	import type { List } from "../../../../../core/src/java/util/List";
	import type { Sprachendaten } from "../../../../../core/src/asd/data/schueler/Sprachendaten";

	const props = defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
		fehlerliste: () => List<GostBelegpruefungErgebnisFehler>;
		gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
		setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	}>();

	const schuljahr = computed<number>(() => props.abiturdatenManager().getSchuljahr());

	const art = computed<'ef1'|'gesamt'|'auto'>({
		get: () => props.gostBelegpruefungsArt(),
		set: (value) => void props.setGostBelegpruefungsArt(value),
	});

	const sprachendaten = computed<Sprachendaten | null>(() => props.abiturdatenManager().getSprachendaten());

</script>
