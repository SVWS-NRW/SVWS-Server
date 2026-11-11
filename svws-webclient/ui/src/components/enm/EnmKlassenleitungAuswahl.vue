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

	import { computed, ref, watch } from 'vue';

	import { ENMLehrerFunktion } from '@core/core/data/enm/ENMLehrerFunktion';
	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import { ArrayList } from '@core/java/util/ArrayList';
	import { useRegionSwitch } from '@ui/ui/composables/useRegionSwitch';

	import type { EnmKlassenleitungAuswahlProps } from './EnmKlassenleitungAuswahlProps';

	const props = defineProps<EnmKlassenleitungAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref<string>("");

	const columns = [{ key: "kuerzelAnzeige", label: "Klasse" }];

	const klassenAuswahl = computed(() => {
		const manager = props.enmManager();
		const klassen: ENMv2Klasse[] = [];
		for (const klasse of manager.listKlassenKlassenlehrer) {
			if ((manager.lehrerFunktion !== ENMLehrerFunktion.KLASSENLEITUNG) || manager.klasseIstKlassenleitung(klasse.id)) {
				klassen.push(klasse);
			}
		}
		return klassen;
	});

	watch(klassenAuswahl, (klassen) => {
		const ids = new Set(klassen.map(klasse => klasse.id));
		const auswahlEinzel = props.auswahlEinzel();
		const auswahlMehrfach = props.auswahlMehrfach();
		const einzelIstZulaessig = (auswahlEinzel !== null) && ids.has(auswahlEinzel.id);
		const mehrfachIstZulaessig = auswahlMehrfach.every(klasse => ids.has(klasse.id));
		if (einzelIstZulaessig || ((auswahlMehrfach.length > 0) && mehrfachIstZulaessig)) {
			return;
		}
		props.setAuswahlMehrfach([]);
		props.setAuswahlEinzel(klassen[0] ?? null);
	}, { immediate: true });

	const rowsFiltered = computed<Iterable<ENMv2Klasse>>(() => {
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		if (searchValueLowerCase === "") {
			return klassenAuswahl.value;
		}
		const list = new ArrayList<ENMv2Klasse>();
		for (const e of klassenAuswahl.value) {
			if (((e.kuerzel !== null) && e.kuerzel.toLocaleLowerCase().includes(searchValueLowerCase))
				|| (e.kuerzelAnzeige !== null && e.kuerzelAnzeige.toLocaleLowerCase().includes(searchValueLowerCase))) {
				list.add(e);
			}
		}
		return list;
	});

	function getFirst(): ENMv2Klasse | null {
		return klassenAuswahl.value[0] ?? null;
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
