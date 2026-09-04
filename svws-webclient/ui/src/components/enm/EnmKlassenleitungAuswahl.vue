<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Klassen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<div class="w-full px-2">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
			</div>
			<svws-ui-table :items="rowsFiltered" :model-value="auswahlMehrfach()" @update:model-value="setMehrfachauswahl"
				:clickable="!enmManager().mapLerngruppenAuswahl.isEmpty()" :clicked="auswahlEinzel()" @update:clicked="setEinzelauswahl"
				:columns :filter-open="false" selectable count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled multi-select-focus-enabled />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import { ArrayList } from '@core/java/util/ArrayList';
	import { useRegionSwitch } from '@ui/ui/composables/useRegionSwitch';
	import { computed, onBeforeMount, ref } from 'vue';
	import type { EnmKlassenleitungAuswahlProps } from './EnmKlassenleitungAuswahlProps';

	const props = defineProps<EnmKlassenleitungAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref<string>("");

	const columns = [{ key: "kuerzelAnzeige", label: "Klasse" }];

	onBeforeMount(() => props.setAuswahlEinzel(getFirst()));

	const rowsFiltered = computed<Iterable<ENMv2Klasse>>(() => {
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		if (searchValueLowerCase === "") {
			return props.enmManager().listKlassenKlassenlehrer;
		}
		const list = new ArrayList<ENMv2Klasse>();
		for (const e of props.enmManager().listKlassenKlassenlehrer) {
			if (((e.kuerzel !== null) && e.kuerzel.toLocaleLowerCase().includes(searchValueLowerCase))
				|| (e.kuerzelAnzeige !== null && e.kuerzelAnzeige.toLocaleLowerCase().includes(searchValueLowerCase))) {
				list.add(e);
			}
		}
		return list;
	});

	function getFirst(): ENMv2Klasse | null {
		const list = props.enmManager().listKlassenKlassenlehrer;
		if (list.isEmpty()) {
			return null;
		}
		return list.getFirst();
	}

	function setMehrfachauswahl(items: Array<ENMv2Klasse>) {
		if (items.length === 0) {
			const first = (props.auswahlMehrfach().length === 0) ? getFirst() : props.auswahlMehrfach()[0];
			props.setAuswahlMehrfach(items);
			props.setAuswahlEinzel(first);
		} else {
			props.setAuswahlMehrfach(items);
			props.setAuswahlEinzel(null);
		}
	}

	function setEinzelauswahl(item: ENMv2Klasse) {
		props.setAuswahlEinzel(item);
		props.setAuswahlMehrfach([]);
	}

</script>
