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
			<svws-ui-table :items="klassenGefiltert" :model-value="auswahlMehrfach()" @update:model-value="setMehrfachauswahl"
				clickable :clicked="auswahlEinzel()" @update:clicked="setEinzelauswahl"
				:columns :filter-open="false" selectable count scroll-into-view scroll allow-arrow-key-selection :focus-help-visible :focus-switching-enabled multi-select-focus-enabled />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from 'vue';

	import { ENMLehrerFunktion } from '@core/core/data/enm/ENMLehrerFunktion';
	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import { useRegionSwitch } from '@ui/ui/composables/useRegionSwitch';

	import type { EnmKlassenleitungAuswahlProps } from './EnmKlassenleitungAuswahlProps';
	import type { EnmManager } from './EnmManager';

	const props = defineProps<EnmKlassenleitungAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const search = ref('');
	const columns = [{ key: 'kuerzelAnzeige', label: 'Klasse' }];

	const klassenKlassenleitung = computed(() => {
		const result: ENMv2Klasse[] = [];
		const manager = props.enmManager();
		for (const klasse of props.enmManager().listKlassenKlassenlehrer) {
			if (istKlasseZugaenglich(klasse, manager)) {
				result.push(klasse);
			}
		}
		return result;
	});

	const klassenGefiltert = computed(() => {
		const begriff = search.value.toLocaleLowerCase();
		if (begriff === '') {
			return klassenKlassenleitung.value;
		}
		return klassenKlassenleitung.value.filter(klasse =>
			(klasse.kuerzel ?? '').toLocaleLowerCase().includes(begriff)
			|| (klasse.kuerzelAnzeige ?? '').toLocaleLowerCase().includes(begriff));
	});

	watch(klassenKlassenleitung, (klassen) => {
		const aktuelleAuswahl = props.auswahlEinzel();
		if ((aktuelleAuswahl !== null) && klassen.some(klasse => klasse.id === aktuelleAuswahl.id)) {
			return;
		}
		const mehrfachauswahl = props.auswahlMehrfach();
		if ((mehrfachauswahl.length > 0) && mehrfachauswahl.every(auswahl => klassen.some(klasse => klasse.id === auswahl.id))) {
			return;
		}
		props.setAuswahlMehrfach([]);
		props.setAuswahlEinzel(klassen[0] ?? null);
	}, { immediate: true });

	function istKlasseZugaenglich(klasse: ENMv2Klasse, manager: EnmManager): boolean {
		if (manager.lehrerFunktion === ENMLehrerFunktion.KLASSENLEITUNG) {
			return manager.klasseIstKlassenleitung(klasse.id);
		}
		if (manager.lehrerFunktion === ENMLehrerFunktion.ABTEILUNGSLEITUNG) {
			return manager.klasseIstAbteilungsleitung(klasse.id);
		}
		return false;
	}

	function setMehrfachauswahl(klassen: ENMv2Klasse[]) {
		if (klassen.length === 0) {
			props.setAuswahlMehrfach([]);
			props.setAuswahlEinzel(klassenKlassenleitung.value[0] ?? null);
			return;
		}
		props.setAuswahlMehrfach(klassen);
		props.setAuswahlEinzel(null);
	}

	function setEinzelauswahl(klasse: ENMv2Klasse) {
		props.setAuswahlEinzel(klasse);
		props.setAuswahlMehrfach([]);
	}

</script>
