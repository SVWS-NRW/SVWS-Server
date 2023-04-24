<template>
	<svws-ui-content-card class="col-span-full">
		<svws-ui-toggle v-model="filterFehler" /> Nur Ergebnisse mit Fehlern anzeigen
		<svws-ui-data-table :items="laufbahnplanungsergebnisse" :no-data="false" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { List } from '@svws-nrw/svws-core';
	import type { WritableComputedRef} from 'vue';
	import type { Config } from '~/components/Config';
	import { computed } from 'vue';

	const props = defineProps<{
		laufbahnplanungsergebnisse: List<any>;
		config: Config;
	}>();

	const filterFehler: WritableComputedRef<boolean> = computed({
		get: () => props.config.getValue('gost.laufbahnplanung.filterFehler') === 'true',
		set: (value) =>	void props.config.setValue('gost.laufbahnplanung.filterFehler', value === true ? 'true':'false')
	});
</script>
