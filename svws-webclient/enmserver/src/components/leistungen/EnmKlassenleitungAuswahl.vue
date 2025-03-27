<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Klassen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :items="manager.klassenOfKlassenlehrer" clickable @update:clicked="item => manager.filterKlassen = [ item ]" :clicked="manager.filterKlassen[0]"
				:columns :filter-open="false" count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onMounted } from 'vue';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';
	import { useRegionSwitch } from "~/components/useRegionSwitchEnm";

	const props = defineProps<EnmKlassenleitungProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

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
