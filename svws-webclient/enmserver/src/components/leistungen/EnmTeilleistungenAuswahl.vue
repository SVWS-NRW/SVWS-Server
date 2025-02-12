<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lerngruppen</span>
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :items="manager.lerngruppenAuswahlliste" clickable @update:clicked="item => manager.filterLerngruppen = [ item ]" :clicked="manager.filterLerngruppen[0]"
				:columns :filter-open="false" count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { onMounted } from 'vue';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';
	import { useRegionSwitch } from "~/components/useRegionSwitchEnm";

	const props = defineProps<EnmTeilleistungenProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	onMounted(() => {
		if (props.manager.filterLerngruppen.length > 0)
			props.manager.filterLerngruppen = [ props.manager.filterLerngruppen[0] ];
		else if (!props.manager.lerngruppenAuswahlliste.isEmpty())
			props.manager.filterLerngruppen = [ props.manager.lerngruppenAuswahlliste.getFirst() ];
		else
			props.manager.filterLerngruppen = [];
	})

	const columns = [
		{ key: "bezeichnung", label: "Lerngruppe" },
		{ key: "klassen", label: "Klasse(n)" },
	];

</script>
