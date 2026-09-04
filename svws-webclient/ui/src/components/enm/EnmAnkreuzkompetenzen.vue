<template>
	<div class="page page-flex-row">
		<enm-ankreuzkompetenzen-uebersicht ref="gridRef" :enm-manager :patch-leistung :patch-ankreuzkompetenz :focus-floskel-editor :auswahl />
		<enm-floskeleditor ref="gridRefFlosekeleditor" v-if="show" v-model="show" :patch :erlaubte-hauptgruppe :enm-manager
			:auswahl="auswahlZelle" :lerngruppen-auswahl="auswahl" :on-update :initial-row disable-schueler-grid />
	</div>
</template>

<script setup lang="ts">

	import { nextTick, ref, shallowRef, useTemplateRef } from 'vue';
	import type { EnmAnkreuzkompetenzenProps } from './EnmAnkreuzkompetenzenProps';
	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import { ENMv2Leistung } from '@core/core/data/enm/v2/ENMv2Leistung';
	import type { ENMv2Schueler } from '@core/core/data/enm/v2/ENMv2Schueler';

	type AuswahlZelle = { klasse: ENMv2Klasse | null, schueler: ENMv2Schueler | null, leistung: ENMv2Leistung | null };

	const props = defineProps<EnmAnkreuzkompetenzenProps>();

	const gridRef = useTemplateRef('gridRef');
	const auswahlZelle = shallowRef<AuswahlZelle>({ klasse: null, schueler: null, leistung: null });
	const erlaubteHauptgruppe = ref<'FACH' | 'ASV'>('FACH');
	const patch = ref(doPatchLeistung);
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
		if (leistung instanceof ENMv2Leistung) {
			auswahlZelle.value = { klasse: null, schueler, leistung };
		} else {
			auswahlZelle.value = { klasse: null, schueler, leistung: null };
		}
		if (focus) {
			focusGrid();
		}
	}

	async function focusFloskelEditor(schueler: ENMv2Schueler | null, leistung: ENMv2Leistung | null, row: number | null, doFocus: boolean) {
		auswahlZelle.value = { klasse: null, schueler, leistung };
		if (auswahlZelle.value.leistung === null) {
			erlaubteHauptgruppe.value = 'ASV';
			patch.value = doPatchBemerkungen;
		} else {
			erlaubteHauptgruppe.value = 'FACH';
			patch.value = doPatchLeistung;
		}
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

	async function doPatchBemerkungen(ASV: string | null) {
		if (auswahlZelle.value.schueler === null) {
			return;
		}
		const patch = { ASV };
		await props.patchBemerkungen(auswahlZelle.value.schueler.id, auswahlZelle.value.schueler.bemerkungen, patch);
		const { schueler, leistung, klasse } = auswahlZelle.value;
		Object.assign(schueler.bemerkungen, patch);
		auswahlZelle.value = { klasse, schueler, leistung };
	}

</script>
