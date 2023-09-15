<template>
	<svws-ui-content-card title="Belegprüfungsergebnisse">
		<template #actions>
			<s-laufbahnplanung-belegpruefungsart v-model="art" />
		</template>
		<div class="print:hidden">
			<s-laufbahnplanung-fehler :fehlerliste="fehlerliste" :belegpruefungs-art="() => abiturdatenManager().getPruefungsArt()" />
			<s-laufbahnplanung-informationen :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-fachkombinationen :abiturdaten-manager="abiturdatenManager" />
			<s-laufbahnplanung-sprachpruefungen v-if="sprachendaten" :sprachendaten="() => abiturdatenManager().getSprachendaten()" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, GostBelegpruefungErgebnisFehler, AbiturdatenManager, Sprachendaten } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
		fehlerliste: () => List<GostBelegpruefungErgebnisFehler>;
		gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	}>();

	const emit = defineEmits<{
		(e: 'update:gost-belegpruefungs-art', value: 'ef1'|'gesamt'|'auto'): void,
	}>();

	const art: WritableComputedRef<'ef1'|'gesamt'|'auto'> = computed({
		get: () => props.gostBelegpruefungsArt(),
		set: (value) => emit('update:gost-belegpruefungs-art', value)
	});

	const sprachendaten: ComputedRef<Sprachendaten | null> = computed(() => props.abiturdatenManager().getSprachendaten());

</script>
