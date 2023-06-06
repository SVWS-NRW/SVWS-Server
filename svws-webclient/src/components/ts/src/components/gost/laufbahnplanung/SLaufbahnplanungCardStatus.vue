<template>
	<svws-ui-content-card title="Belegprüfungsergebnisse">
		<template #actions>
			<s-laufbahnplanung-belegpruefungsart v-model="art" />
		</template>
		<div class="print:hidden -mt-4">
			<s-laufbahnplanung-fehler :fehlerliste="fehlerliste" :belegpruefungs-art="pruefungsart" />
			<s-laufbahnplanung-informationen :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-fachkombinationen :abiturdaten-manager="abiturdatenManager" :faechermanager="faechermanager" :map-fachkombinationen="mapFachkombinationen" />
			<s-laufbahnplanung-sprachpruefungen v-if="sprachendaten" :sprachendaten="sprachendaten" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, GostBelegpruefungErgebnisFehler, GostJahrgangFachkombination, AbiturdatenManager, GostFaecherManager, Sprachendaten } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		abiturdatenManager: AbiturdatenManager;
		faechermanager: GostFaecherManager;
		mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
		fehlerliste: List<GostBelegpruefungErgebnisFehler>;
		gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	}>();

	const emit = defineEmits<{
		(e: 'update:gost-belegpruefungs-art', value: 'ef1'|'gesamt'|'auto'): void,
	}>();

	const art: WritableComputedRef<'ef1'|'gesamt'|'auto'> = computed({
		get: () => props.gostBelegpruefungsArt(),
		set: (value) => emit('update:gost-belegpruefungs-art', value)
	});

	const pruefungsart = computed(() => {
		return props.abiturdatenManager.getPruefungsArt().kuerzel as 'gesamt'|'ef1';
	})

	const sprachendaten: ComputedRef<Sprachendaten | null> = computed(() => props.abiturdatenManager.getSprachendaten());

</script>
