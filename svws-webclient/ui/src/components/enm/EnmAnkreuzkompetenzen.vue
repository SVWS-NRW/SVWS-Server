<template>
	<div class="page page-flex-row">
		<enm-ankreuzkompetenzen-uebersicht ref="gridRef" :enm-manager :patch-leistung :patch-ankreuzkompetenz :focus-floskel-editor :auswahl />
		<enm-floskeleditor ref="gridRefFlosekeleditor" v-if="show" v-model="show" :patch="doPatchLeistung" erlaubte-hauptgruppe="FACH" :enm-manager
			:auswahl="auswahlZelle" :lerngruppen-auswahl="auswahl" :on-update :initial-row disable-schueler-grid />
	</div>
</template>

<script setup lang="ts">

	import { nextTick, ref, shallowRef, useTemplateRef } from 'vue';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';
	import type { ENMKlasse } from '../../../../core/src';
	import type { EnmAnkreuzkompetenzenProps } from './EnmAnkreuzkompetenzenProps';

	type AuswahlZelle = { klasse: ENMKlasse | null, schueler: ENMSchueler | null, leistung: ENMLeistung | null };

	const props = defineProps<EnmAnkreuzkompetenzenProps>();

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
		const { b: schueler } = gridRef.value.gridManagerSchueler.daten.get(row);
		const { kompetenz: leistung } = gridRef.value.gridManager.daten.getFirst();
		if (leistung instanceof ENMLeistung) {
			auswahlZelle.value = { klasse: null, schueler, leistung };
		} else {
			auswahlZelle.value = { klasse: null, schueler, leistung: null };
		}
		if (focus) {
			focusGrid();
		}
	}

	async function focusFloskelEditor(schueler: ENMSchueler | null, leistung: ENMLeistung | null, row: number | null, doFocus: boolean) {
		auswahlZelle.value = { klasse: null, schueler, leistung };
		initialRow.value = row;
		if (doFocus) {
			show.value = true;
			await nextTick(() => (document.getElementsByClassName("floskel-input")[0] as HTMLElement).focus());
		}
	}

	async function doPatchLeistung(fachbezogeneBemerkungen: string | null) {
		if ((auswahlZelle.value.schueler === null) || (auswahlZelle.value.leistung === null)) {
			return;
		}
		await props.patchLeistung(auswahlZelle.value.leistung, { fachbezogeneBemerkungen });
		const { schueler, leistung, klasse } = auswahlZelle.value;
		leistung.fachbezogeneBemerkungen = fachbezogeneBemerkungen;
		auswahlZelle.value = { klasse, schueler, leistung };
	}

</script>
