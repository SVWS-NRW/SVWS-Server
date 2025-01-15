<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Klassen</span>
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :items="manager.klassenOfKlassenlehrer" clickable @update:clicked="item => manager.filterKlassen = [ item ]" :clicked="manager.filterKlassen[0]"
				:columns :filter-open="false" count scroll-into-view scroll allow-arrow-key-selection />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { onMounted } from 'vue';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';

	const props = defineProps<EnmKlassenleitungProps>();

	onMounted(() => {
		if (props.manager.filterKlassen.length > 0)
			props.manager.filterKlassen = [ props.manager.filterKlassen[0] ];
		else if (!props.manager.lerngruppenAuswahlliste.isEmpty())
			props.manager.filterKlassen = [ props.manager.klassenOfKlassenlehrer.getFirst() ];
		else
			props.manager.filterKlassen = [];
	});

	const columns = [
		{ key: "kuerzelAnzeige", label: "Klasse" },
	];

</script>
