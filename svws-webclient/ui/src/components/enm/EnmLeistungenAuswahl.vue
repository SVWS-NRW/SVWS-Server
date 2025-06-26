<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Lerngruppen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :items="enmManager().mapLerngruppenAuswahl.values()" :model-value="auswahlMehrfach()" @update:model-value="setMehrfachauswahl"
				:clickable="!enmManager().mapLerngruppenAuswahl.isEmpty()" :clicked="auswahlEinzel()" @update:clicked="setEinzelauswahl"
				:columns :filter-open="false" selectable count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled multi-select-focus-enabled />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onBeforeMount } from 'vue';
	import { useRegionSwitch } from '../../ui/useRegionSwitch';
	import type { EnmLeistungenAuswahlProps } from './EnmLeistungenAuswahlProps';
	import type { EnmLerngruppenAuswahlEintrag } from './EnmManager';

	const props = defineProps<EnmLeistungenAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns = [
		{ key: "bezeichnung", label: "Lerngruppe" },
		{ key: "klassen", label: "Klasse(n)" },
	];

	onBeforeMount(() => props.setAuswahlEinzel(getFirst()));

	function getFirst() : EnmLerngruppenAuswahlEintrag | null {
		const map = props.enmManager().mapLerngruppenAuswahl.values();
		if (map.isEmpty())
			return null;
		return map.iterator().next();
	}

	function setMehrfachauswahl(items: Array<EnmLerngruppenAuswahlEintrag>) {
		if (items.length === 0) {
			const first = (props.auswahlMehrfach().length === 0) ? getFirst() : props.auswahlMehrfach()[0];
			props.setAuswahlMehrfach(items);
			props.setAuswahlEinzel(first);
		} else {
			props.setAuswahlMehrfach(items);
			props.setAuswahlEinzel(null);
		}
	}

	function setEinzelauswahl(item: EnmLerngruppenAuswahlEintrag) {
		props.setAuswahlEinzel(item);
		props.setAuswahlMehrfach([]);
	}

</script>
