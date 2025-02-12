<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lerngruppen</span>
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :items="manager.lerngruppenAuswahlliste" :model-value="manager.filterLerngruppen" @update:model-value="items => manager.filterLerngruppen = items"
				:columns :filter-open="false" selectable count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { EnmLeistungenProps } from './EnmLeistungenProps';
	import { useRegionSwitch } from "~/components/useRegionSwitchEnm";

	const props = defineProps<EnmLeistungenProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns = [
		{ key: "bezeichnung", label: "Lerngruppe" },
		{ key: "klassen", label: "Klasse(n)" },
	];

</script>
