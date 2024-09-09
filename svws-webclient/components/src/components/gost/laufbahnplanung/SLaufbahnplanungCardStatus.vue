<template>
	<svws-ui-content-card title="Belegprüfungsergebnisse">
		<template #actions>
			<s-laufbahnplanung-belegpruefungsart v-model="art" />
		</template>
		<div class="print:hidden">
			<s-laufbahnplanung-fehler :fehlerliste="fehlerliste" :belegpruefungs-art="() => abiturdatenManager().getPruefungsArt()" />
			<s-laufbahnplanung-informationen :fehlerliste="fehlerliste" />
			<s-laufbahnplanung-sprachpruefungen v-if="sprachendaten" :schuljahr="schuljahr" :sprachendaten="() => abiturdatenManager().getSprachendaten()" />
			<s-laufbahnplanung-fachkombinationen :abiturdaten-manager="abiturdatenManager" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { GostBelegpruefungErgebnisFehler } from "../../../../../core/src/core/abschluss/gost/GostBelegpruefungErgebnisFehler";
	import type { AbiturdatenManager } from "../../../../../core/src/core/abschluss/gost/AbiturdatenManager";
	import type { List } from "../../../../../core/src/java/util/List";
	import type { Sprachendaten } from "../../../../../core/src/core/data/schueler/Sprachendaten";

	const props = defineProps<{
		abiturdatenManager: () => AbiturdatenManager;
		fehlerliste: () => List<GostBelegpruefungErgebnisFehler>;
		gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	}>();

	const schuljahr = computed<number>(() => props.abiturdatenManager().getSchuljahr());

	const emit = defineEmits<{
		(e: 'update:gost-belegpruefungs-art', value: 'ef1'|'gesamt'|'auto'): void,
	}>();

	const art: WritableComputedRef<'ef1'|'gesamt'|'auto'> = computed({
		get: () => props.gostBelegpruefungsArt(),
		set: (value) => emit('update:gost-belegpruefungs-art', value)
	});

	const sprachendaten: ComputedRef<Sprachendaten | null> = computed(() => props.abiturdatenManager().getSprachendaten());

</script>
