<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Klassen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :items="manager.listKlassenKlassenlehrer" clickable @update:clicked="klasse => auswahlmanager.filter = klasse" :clicked="auswahlmanager.filter"
				:columns :filter-open="false" count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';
	import { useRegionSwitch } from "~/components/useRegionSwitchEnm";

	const props = defineProps<EnmKlassenleitungProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns = [ { key: "kuerzelAnzeige", label: "Klasse" } ];

</script>
