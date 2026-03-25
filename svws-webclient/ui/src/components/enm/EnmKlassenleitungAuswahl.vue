<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Klassen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<div class="w-full px-2">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" removable />
			</div>
			<svws-ui-table :items="rowsFiltered" :model-value="auswahlMehrfach()" @update:model-value="setMehrfachauswahl"
				:clickable="!enmManager().mapLerngruppenAuswahl.isEmpty()" :clicked="auswahlEinzel()" @update:clicked="setEinzelauswahl"
				:columns :filter-open="false" selectable count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled multi-select-focus-enabled />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onBeforeMount, ref } from 'vue';
	import type { ENMv1Klasse } from '../../../../core/src/core/data/enm/v1/ENMv1Klasse';
	import { useRegionSwitch } from '../../ui/composables/useRegionSwitch';
	import type { EnmKlassenleitungAuswahlProps } from './EnmKlassenleitungAuswahlProps';
	import { ArrayList } from '../../../../core/src/java/util/ArrayList';

	const props = defineProps<EnmKlassenleitungAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref<string>("");

	const columns = [{ key: "kuerzelAnzeige", label: "Klasse" }];

	onBeforeMount(() => props.setAuswahlEinzel(getFirst()));

	const rowsFiltered = computed<Iterable<ENMv1Klasse>>(() => {
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		if (searchValueLowerCase === "") {
			return props.enmManager().listKlassenKlassenlehrer;
		}
		const list = new ArrayList<ENMv1Klasse>();
		for (const e of props.enmManager().listKlassenKlassenlehrer) {
			if (((e.kuerzel !== null) && e.kuerzel.toLocaleLowerCase().includes(searchValueLowerCase))
				|| (e.kuerzelAnzeige !== null && e.kuerzelAnzeige.toLocaleLowerCase().includes(searchValueLowerCase))) {
				list.add(e);
			}
		}
		return list;
	});

	function getFirst(): ENMv1Klasse | null {
		const list = props.enmManager().listKlassenKlassenlehrer;
		if (list.isEmpty()) {
			return null;
		}
		return list.getFirst();
	}

	function setMehrfachauswahl(items: Array<ENMv1Klasse>) {
		if (items.length === 0) {
			const first = (props.auswahlMehrfach().length === 0) ? getFirst() : props.auswahlMehrfach()[0];
			props.setAuswahlMehrfach(items);
			props.setAuswahlEinzel(first);
		} else {
			props.setAuswahlMehrfach(items);
			props.setAuswahlEinzel(null);
		}
	}

	function setEinzelauswahl(item: ENMv1Klasse) {
		props.setAuswahlEinzel(item);
		props.setAuswahlMehrfach([]);
	}

</script>
