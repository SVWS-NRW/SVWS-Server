<template>
	<svws-ui-content-card title="Belegprüfungsergebnisse">
		<div class="print:hidden">
			<s-laufbahnplanung-belegpruefungsart v-model="art" />
			<s-laufbahnplanung-fehler :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-informationen :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-fachkombinationen :abiturmanager="abiturmanager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen" />
			<div class="am:px-6 py-2 lg:px-8">
				<svws-ui-text-input v-model="inputBeratungsdatum" type="date" placeholder="Beratungsdatum" />
			</div>
			<div class="am:px-6 py-2 lg:px-8">
				<svws-ui-textarea-input placeholder="Kommentar" resizeable="vertical" />
			</div>
			<s-laufbahnplanung-sprachpruefungen v-if="sprachendaten" :sprachendaten="sprachendaten" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { List, GostBelegpruefungErgebnisFehler, GostJahrgangFachkombination, AbiturdatenManager, GostFaecherManager, GostBelegpruefungsArt, Sprachendaten } from "@svws-nrw/svws-core-ts";

	const props = defineProps<{
		abiturmanager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
		fehlerliste: List<GostBelegpruefungErgebnisFehler>;
		belegpruefungsart: GostBelegpruefungsArt;
	}>();

	const emit = defineEmits<{
		(e: 'update:belegpruefungsart', value: GostBelegpruefungsArt): void,
	}>();

	const art: WritableComputedRef<GostBelegpruefungsArt> = computed({
		get: () => props.belegpruefungsart,
		set: (value) => emit('update:belegpruefungsart', value)
	});

	// TODO Beratungsdatum (siehe Komponente oben)
	const inputBeratungsdatum: WritableComputedRef<string> = computed({
		get: () => "",
		set: (value) => void value
	});

	// TODO Kommentar (siehe Komponente oben)
	const sprachendaten: ComputedRef<Sprachendaten | null> = computed(() => props.abiturmanager.getSprachendaten());

</script>
