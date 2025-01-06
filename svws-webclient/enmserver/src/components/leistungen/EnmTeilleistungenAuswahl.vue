<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lerngruppen</span>
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :items="manager.lerngruppenAuswahlliste" :model-value="manager.filterLerngruppen" @update:model-value="items => manager.filterLerngruppen = items"
				:columns :filter-open="false" selectable count scroll-into-view scroll allow-arrow-key-selection />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';

	const props = defineProps<EnmTeilleistungenProps>();

	const columns = [
		{ key: "bezeichnung", label: "Lerngruppe" },
		{ key: "klassen", label: "Klasse(n)" },
	];

</script>
