<template>
	<div class="page page-flex-row">
		<div class="flex flex-row h-full w-full overflow-hidden" @dragover="dragOver">
			<div class="grow w-full h-full overflow-hidden">
				<enm-leistungen-uebersicht ref="gridRef" :enm-manager :patch-leistung :columns-visible :set-columns-visible :floskel-editor-visible :focus-floskel-editor :auswahl />
			</div>
			<template v-if="columnsVisible().get('Bemerkung')">
				<div class="h-full content-center text-center cursor-col-resize min-w-8 max-w-8 lg:min-w-12 lg:max-w-12 bg-ui hover:bg-ui-hover" draggable="true" @dragstart="dragStart" @dragend="dragEnd">
					<span class="icon i-ri-arrow-left-s-line" />
					<span class="icon i-ri-arrow-right-s-line" />
				</div>
				<div class="h-full overflow-hidden" :style="{ 'min-width': floskelEditorVisible ? posDivider + 'rem' : '4rem', 'max-width': floskelEditorVisible ? posDivider + 'rem' : '4rem' }">
					<enm-floskeleditor :enm-manager :patch="doPatchLeistung" erlaubte-hauptgruppe="FACH" :floskel-editor-visible :set-floskel-editor-visible :auswahl="() => auswahlZelle" />
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, shallowRef, triggerRef } from 'vue';
	import type { EnmLeistungenProps } from './EnmLeistungenProps';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import type { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';
	import type { ENMKlasse } from '../../../../core/src';

	const props = defineProps<EnmLeistungenProps>();

	// erstelle ein unsichtbares Image
	const img = document.createElement('img');
	img.src = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';

	const auswahlZelle = shallowRef<{ klasse: ENMKlasse | null, schueler: ENMSchueler | null, leistung: ENMLeistung | null }>({ klasse: null, schueler: null, leistung: null });

	const gridRef = shallowRef(null);
	// eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
	const focusGrid = () => { if (gridRef.value !== null) (gridRef.value as { focusGrid: () => void }).focusGrid(); };

	async function focusFloskelEditor(schueler: ENMSchueler | null, leistung: ENMLeistung | null, doFocus: boolean) {
		if (doFocus) {
			await props.setFloskelEditorVisible(true).then(() => {
				auswahlZelle.value.schueler = schueler;
				auswahlZelle.value.leistung = leistung;
				triggerRef(auswahlZelle);
				(document.getElementsByClassName("floskel-input")[0] as HTMLElement).focus();
			});
			return;
		}
		auswahlZelle.value.schueler = schueler;
		auswahlZelle.value.leistung = leistung;
		triggerRef(auswahlZelle);
	}

	async function doPatchLeistung(fachbezogeneBemerkungen: string|null) {
		if ((auswahlZelle.value.schueler === null) || (auswahlZelle.value.leistung === null))
			return;
		await props.patchLeistung(auswahlZelle.value.leistung, { fachbezogeneBemerkungen });
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
