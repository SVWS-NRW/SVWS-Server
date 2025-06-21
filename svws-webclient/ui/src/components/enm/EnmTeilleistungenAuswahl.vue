<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Lerngruppen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :items="manager.listLerngruppenAuswahlliste" clickable @update:clicked="item => auswahlmanager.filter = item" :clicked="auswahlmanager.filter"
				:columns :filter-open="false" count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { useRegionSwitch } from '../../ui/useRegionSwitch';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';

	const props = defineProps<EnmTeilleistungenProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns = [
		{ key: "bezeichnung", label: "Lerngruppe" },
		{ key: "klassen", label: "Klasse(n)" },
	];

</script>
