<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Lerngruppen</h1>
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

	import { computed, onBeforeMount, ref } from 'vue';
	import type { EnmLeistungenAuswahlProps } from './EnmLeistungenAuswahlProps';
	import type { EnmLerngruppenAuswahlEintrag } from './EnmManager';
	import { ArrayList } from '@core/java/util/ArrayList';
	import { useRegionSwitch } from '@ui/ui/composables/useRegionSwitch';

	const props = defineProps<EnmLeistungenAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref<string>("");

	const columns = [
		{ key: "bezeichnung", label: "Lerngruppe" },
		{ key: "klassen", label: "Klasse(n)" },
	];

	onBeforeMount(() => {
		if ((props.auswahlEinzel() === null) && (props.auswahlMehrfach().length === 0)) {
			props.setAuswahlEinzel(getFirst());
		}
	});

	const rowsFiltered = computed<Iterable<EnmLerngruppenAuswahlEintrag>>(() => {
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		if (searchValueLowerCase === "") {
			return props.enmManager().mapLerngruppenAuswahl.values();
		}
		const list = new ArrayList<EnmLerngruppenAuswahlEintrag>();
		for (const e of props.enmManager().mapLerngruppenAuswahl.values()) {
			if (e.bezeichnung.toLocaleLowerCase().includes(searchValueLowerCase) || e.klassen.toLocaleLowerCase().includes(searchValueLowerCase)) {
				list.add(e);
			}
		}
		return list;
	});

	function getFirst(): EnmLerngruppenAuswahlEintrag | null {
		const map = props.enmManager().mapLerngruppenAuswahl.values();
		if (map.isEmpty()) {
			return null;
		}
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
