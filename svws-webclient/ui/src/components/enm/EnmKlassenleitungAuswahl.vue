<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Klassen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :items="enmManager().listKlassenKlassenlehrer" :model-value="auswahlMehrfach()" @update:model-value="setMehrfachauswahl"
				:clickable="!enmManager().mapLerngruppenAuswahl.isEmpty()" :clicked="auswahlEinzel()" @update:clicked="setEinzelauswahl"
				:columns :filter-open="false" selectable count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled multi-select-focus-enabled />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onBeforeMount } from 'vue';
	import type { ENMKlasse } from '../../../../core/src/core/data/enm/ENMKlasse';
	import { useRegionSwitch } from '../../ui/useRegionSwitch';
	import type { EnmKlassenleitungAuswahlProps } from './EnmKlassenleitungAuswahlProps';

	const props = defineProps<EnmKlassenleitungAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns = [ { key: "kuerzelAnzeige", label: "Klasse" } ];

	onBeforeMount(() => props.setAuswahlEinzel(getFirst()));

	function getFirst() : ENMKlasse | null {
		const list = props.enmManager().listKlassenKlassenlehrer;
		if (list.isEmpty())
			return null;
		return list.getFirst();
	}

	function setMehrfachauswahl(items: Array<ENMKlasse>) {
		if (items.length === 0) {
			const first = (props.auswahlMehrfach().length === 0) ? getFirst() : props.auswahlMehrfach()[0];
			props.setAuswahlMehrfach(items);
			props.setAuswahlEinzel(first);
		} else {
			props.setAuswahlMehrfach(items);
			props.setAuswahlEinzel(null);
		}
	}

	function setEinzelauswahl(item: ENMKlasse) {
		props.setAuswahlEinzel(item);
		props.setAuswahlMehrfach([]);
	}

</script>
