<template>
	<svws-ui-content-card class="col-span-full">
		<svws-ui-toggle v-model="filterFehler" /> Nur Ergebnisse mit Fehlern anzeigen
		<s-laufbahnplanung-belegpruefungsart v-model="art" />
		<svws-ui-data-table :items="listBelegpruefungsErgebnisse" :no-data="false">
		
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostBelegpruefungsErgebnisse, List } from '@svws-nrw/svws-core';
	import { GostBelegpruefungsArt } from '@svws-nrw/svws-core';
	import type { WritableComputedRef } from 'vue';
	import { computed, shallowRef } from 'vue';
	import type { Config } from '~/components/Config';

	const props = defineProps<{
		listBelegpruefungsErgebnisse: List<GostBelegpruefungsErgebnisse>;
		config: Config;
		belegpruefungsart: GostBelegpruefungsArt;
		setBelegpruefungsart: (belegpruefungsart: GostBelegpruefungsArt) => Promise<void>;
	}>();

	const art = shallowRef(GostBelegpruefungsArt.GESAMT);

	const filterFehler: WritableComputedRef<boolean> = computed({
		get: () => props.config.getValue('gost.laufbahnplanung.filterFehler') === 'true',
		set: (value) =>	void props.config.setValue('gost.laufbahnplanung.filterFehler', value === true ? 'true':'false')
	});
</script>
