<template>
	<div class="page page-flex-row">
		<div class="flex flex-row h-full w-full overflow-hidden" @dragover="dragOver">
			<div class="grow w-full h-full overflow-hidden">
				<enm-klassenleitung-uebersicht ref="gridRef" :enm-manager :patch-bemerkungen :patch-lernabschnitt :columns-visible :set-columns-visible :floskel-editor-visible :focus-floskel-editor :auswahl />
			</div>
			<template v-if="floskelColumnsVisible">
				<div class="h-full content-center text-center cursor-col-resize min-w-8 max-w-8 lg:min-w-12 lg:max-w-12 bg-ui hover:bg-ui-hover" draggable="true" @dragstart="dragStart" @dragend="dragEnd">
					<span class="icon i-ri-arrow-left-s-line" />
					<span class="icon i-ri-arrow-right-s-line" />
				</div>
				<div class="h-full overflow-hidden" :style="{ 'min-width': floskelEditorVisible ? posDivider + 'rem' : '4rem', 'max-width': floskelEditorVisible ? posDivider + 'rem' : '4rem' }">
					<enm-floskeleditor :enm-manager :patch="doPatchBemerkungen" :erlaubte-hauptgruppe :floskel-editor-visible :set-floskel-editor-visible :auswahl="() => auswahlZelle" />
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, triggerRef } from 'vue';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';
	import type { ENMLeistungBemerkungen } from '../../../../core/src/core/data/enm/ENMLeistungBemerkungen';
	import type { BemerkungenHauptgruppe } from './EnmManager';
	import type { ENMKlasse } from '../../../../core/src/core/data/enm/ENMKlasse';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import type { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';

	const props = defineProps<EnmKlassenleitungProps>();

	// erstelle ein unsichtbares Image
	const img = document.createElement('img');
	img.src = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';

	const auswahlZelle = shallowRef<{ klasse: ENMKlasse | null, schueler: ENMSchueler | null, leistung: ENMLeistung | null }>({ klasse: null, schueler: null, leistung: null });
	const erlaubteHauptgruppe = shallowRef<BemerkungenHauptgruppe>('ZB');

	const gridRef = shallowRef(null);
	// eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
	const focusGrid = () => { if (gridRef.value !== null) (gridRef.value as { focusGrid: () => void }).focusGrid(); };

	async function focusFloskelEditor(hauptgruppe: BemerkungenHauptgruppe | null, schueler: ENMSchueler | null, klasse: ENMKlasse | null, doFocus: boolean) {
		if (hauptgruppe !== null)
			erlaubteHauptgruppe.value = hauptgruppe;
		if (doFocus) {
			await props.setFloskelEditorVisible(true).then(() => {
				auswahlZelle.value.schueler = schueler;
				auswahlZelle.value.klasse = klasse;
				triggerRef(auswahlZelle);
				(document.getElementsByClassName("floskel-input")[0] as HTMLElement).focus();
			});
			return;
		}
		auswahlZelle.value.schueler = schueler;
		auswahlZelle.value.klasse = klasse;
		triggerRef(auswahlZelle);
	}

	const floskelColumnsVisible = computed(() => {
		const cols = props.columnsVisible();
		return (cols.get('AUE') ?? false)
			|| (cols.get('ASV') ?? false)
			|| (cols.get('ZB') ?? false);
	})

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
		focusGrid();
	}

	// Default-Breite von 49 rem für den Floskel-Editor
	const posDivider = ref<number>(45);

	function dragOver(event: DragEvent) {
		// Bestimme die Anzahl der Pixel pro 1 rem
		const pxPerRem = parseFloat(getComputedStyle(document.documentElement).fontSize);
		// Bestimme den Abstand vom rechten Rand des Content-Bereich in Pixeln
		const posFromRight = (event.currentTarget as Element).getBoundingClientRect().right - event.pageX;
		// Bestimme die neue Breite des Floskel-Editors in rem
		let width = posFromRight / pxPerRem;
		// Erlaube dabei eine maximale Breits von 60 rem
		if (width > 60)
			width = 60;
		// Kleinere Werte als 10 rem blenden den Floskel-Editor komplett aus
		if (width < 10)
			width = 4;
		// Setze die Position des Dividers nur neu, wenn er stark genug von der alten Position abweicht (mehr als 1 rem) -> Performance
		if (Math.abs(width - posDivider.value) > 1)
			posDivider.value = width;
	}

	async function dragStart(event: DragEvent) {
		if (!props.floskelEditorVisible)
			await props.setFloskelEditorVisible(true);
		// setze dieses unsichtbare Image als DragImage
		event.dataTransfer?.setDragImage(img, 0, 0);
	}

	async function dragEnd(event: DragEvent) {
		if (posDivider.value === 4)
			await props.setFloskelEditorVisible(false);
	}

</script>
