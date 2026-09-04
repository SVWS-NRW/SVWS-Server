<template>
	<div class="page page-flex-row">
		<enm-klassenleitung-uebersicht ref="gridRef" :enm-manager :patch-bemerkungen :patch-lernabschnitt :columns-visible :set-columns-visible :focus-floskel-editor :auswahl />
		<enm-floskeleditor ref="gridRefFlosekeleditor" v-if="show" v-model="show" :patch="doPatchBemerkungen" :erlaubte-hauptgruppe :enm-manager :auswahl="auswahlZelle" :lerngruppen-auswahl="auswahl" :on-update :initial-row />
	</div>
</template>
<script setup lang="ts">

	import { nextTick, ref, shallowRef, useTemplateRef } from 'vue';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';
	import type { BemerkungenHauptgruppe } from './EnmManager';
	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import type { ENMv2Leistung } from '@core/core/data/enm/v2/ENMv2Leistung';
	import type { ENMv2LeistungBemerkungen } from '@core/core/data/enm/v2/ENMv2LeistungBemerkungen';
	import type { ENMv2Schueler } from '@core/core/data/enm/v2/ENMv2Schueler';

	type AuswahlZelle = { klasse: ENMv2Klasse | null, schueler: ENMv2Schueler | null, leistung: ENMv2Leistung | null };

	const props = defineProps<EnmKlassenleitungProps>();

	const gridRef = useTemplateRef('gridRef');
	const auswahlZelle = shallowRef<AuswahlZelle>({ klasse: null, schueler: null, leistung: null });
	const show = ref(false);
	const initialRow = ref<number | null>(null);

	function focusGrid() {
		if (gridRef.value !== null) {
			gridRef.value.gridManager.doFocus(true);
		}
	}

	function onUpdate(row: number | null, focus: boolean) {
		if ((gridRef.value === null) || (row === null)) {
			return;
		}
		gridRef.value.gridManager.focusRowLast = row;
		const { b: schueler, a: klasse } = gridRef.value.gridManager.daten.get(row);
		auswahlZelle.value = { klasse, schueler, leistung: null };
		if (focus) {
			focusGrid();
		}
	}
	const erlaubteHauptgruppe = shallowRef<BemerkungenHauptgruppe>('ZB');

	async function focusFloskelEditor(hauptgruppe: BemerkungenHauptgruppe | null, schueler: ENMv2Schueler | null, klasse: ENMv2Klasse | null, row: number | null, doFocus: boolean) {
		if (hauptgruppe !== null) {
			erlaubteHauptgruppe.value = hauptgruppe;
		}
		auswahlZelle.value = { klasse, schueler, leistung: null };
		initialRow.value = row;
		if (doFocus) {
			show.value = true;
			await nextTick(() => (document.getElementsByClassName("floskel-input")[0] as HTMLElement).focus());
		}
	}

	async function doPatchBemerkungen(bemerkung: string | null) {
		if ((auswahlZelle.value.schueler === null) || (auswahlZelle.value.klasse === null)) {
			return;
		}
		const patch = <Partial<ENMv2LeistungBemerkungen>>{};
		switch (erlaubteHauptgruppe.value) {
			case 'ASV':
				patch.ASV = bemerkung;
				break;
			case 'AUE':
				patch.AUE = bemerkung;
				break;
			case 'ZB':
				patch.ZB = bemerkung;
				break;
			default:
				return;
		}
		await props.patchBemerkungen(auswahlZelle.value.schueler.id, auswahlZelle.value.schueler.bemerkungen, patch);
		const { schueler, leistung, klasse } = auswahlZelle.value;
		Object.assign(schueler.bemerkungen, patch);
		auswahlZelle.value = { klasse, schueler, leistung };
	}


</script>
