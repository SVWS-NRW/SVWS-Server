<template>
	<div class="page page-flex-row">
		<div class="flex flex-row h-full w-full overflow-hidden">
			<div class="grow w-full h-full overflow-hidden">
				<enm-klassenleitung-uebersicht ref="gridRef" :enm-manager :patch-bemerkungen :patch-lernabschnitt :columns-visible :set-columns-visible :floskel-editor-visible :focus-floskel-editor :auswahl />
			</div>
			<enm-floskeleditor ref="gridRefFlosekeleditor" v-if="show" v-model="show" :patch="doPatchBemerkungen" :erlaubte-hauptgruppe :enm-manager :auswahl="auswahlZelle" :lerngruppen-auswahl="auswahl" :on-update :initial-row />
		</div>
	</div>
</template>
<script setup lang="ts">

	import { nextTick, ref, shallowRef, useTemplateRef } from 'vue';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';
	import type { ENMLeistungBemerkungen } from '../../../../core/src/core/data/enm/ENMLeistungBemerkungen';
	import type { BemerkungenHauptgruppe } from './EnmManager';
	import type { ENMKlasse } from '../../../../core/src/core/data/enm/ENMKlasse';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import type { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';

	type AuswahlZelle = { klasse: ENMKlasse | null, schueler: ENMSchueler | null, leistung: ENMLeistung | null };

	const props = defineProps<EnmKlassenleitungProps>();

	const gridRef = useTemplateRef('gridRef');
	const auswahlZelle = shallowRef<AuswahlZelle>({ klasse: null, schueler: null, leistung: null });
	const show = ref(false);
	const initialRow = ref<number | null>(null);

	function focusGrid() {
		if (gridRef.value !== null)
			gridRef.value.gridManager.doFocus(true);
	}

	function onUpdate(row: number | null, focus: boolean) {
		if ((gridRef.value === null) || (row === null))
			return;
		gridRef.value.gridManager.focusRowLast = row;
		const { b: schueler, a: klasse } = gridRef.value.gridManager.daten.get(row);
		auswahlZelle.value = { klasse, schueler, leistung: null };
		if (focus)
			focusGrid();
	}
	const erlaubteHauptgruppe = shallowRef<BemerkungenHauptgruppe>('ZB');

	async function focusFloskelEditor(hauptgruppe: BemerkungenHauptgruppe | null, schueler: ENMSchueler | null, klasse: ENMKlasse | null, row: number | null, doFocus: boolean) {
		if (hauptgruppe !== null)
			erlaubteHauptgruppe.value = hauptgruppe;
		auswahlZelle.value = { klasse, schueler, leistung: null };
		initialRow.value = row;
		if (doFocus) {
			show.value = true;
			await nextTick(() => (document.getElementsByClassName("floskel-input")[0] as HTMLElement).focus());
		}
	}

	async function doPatchBemerkungen(bemerkung: string | null) {
		if ((auswahlZelle.value.schueler === null) || (auswahlZelle.value.klasse === null))
			return;
		const patch = <Partial<ENMLeistungBemerkungen>>{};
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
