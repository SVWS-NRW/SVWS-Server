<template>
	<svws-ui-content-card title="Belegprüfungsergebnisse">
		<template #actions>
			<s-laufbahnplanung-belegpruefungsart v-model="art" />
		</template>
		<div class="print:hidden -mt-4">
			<s-laufbahnplanung-fehler :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-informationen :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-fachkombinationen :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen" />
			<s-laufbahnplanung-sprachpruefungen v-if="sprachendaten" :sprachendaten="sprachendaten" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, GostBelegpruefungErgebnisFehler, GostJahrgangFachkombination, AbiturdatenManager, GostFaecherManager,
		GostBelegpruefungsArt, Sprachendaten } from "@svws-nrw/svws-core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
		fehlerliste: List<GostBelegpruefungErgebnisFehler>;
		gostBelegpruefungsArt: GostBelegpruefungsArt;
	}>();

	const emit = defineEmits<{
		(e: 'update:gost-belegpruefungs-art', value: GostBelegpruefungsArt): void,
	}>();

	const art: WritableComputedRef<GostBelegpruefungsArt> = computed({
		get: () => props.gostBelegpruefungsArt,
		set: (value) => emit('update:gost-belegpruefungs-art', value)
	});

	const sprachendaten: ComputedRef<Sprachendaten | null> = computed(() => props.abiturdatenManager.getSprachendaten());

</script>
