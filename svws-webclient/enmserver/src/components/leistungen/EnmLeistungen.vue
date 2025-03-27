<template>
	<div class="page page-flex-row">
		<div class="flex flex-row h-full w-full overflow-hidden" @dragover="dragOver">
			<div class="grow w-full h-full overflow-hidden">
				<enm-leistungen-uebersicht :manager :patch-leistung :columns-visible :set-columns-visible :floskel-editor-visible :set-floskel-editor-visible />
			</div>
			<template v-if="columnsVisible().get('Bemerkung')">
				<div class="h-full content-center text-center cursor-col-resize min-w-8 max-w-8 lg:min-w-12 lg:max-w-12 bg-ui hover:bg-ui-hover" draggable="true" @dragstart="dragStart" @dragend="dragEnd">
					<span class="icon i-ri-arrow-left-s-line" />
					<span class="icon i-ri-arrow-right-s-line" />
				</div>
				<div class="h-full overflow-hidden" :style="{ 'min-width': floskelEditorVisible ? posDivider + 'rem' : '4rem', 'max-width': floskelEditorVisible ? posDivider + 'rem' : '4rem' }">
					<enm-floskeleditor :manager :patch="doPatchLeistung" erlaubte-hauptgruppe="FACH" :floskel-editor-visible :set-floskel-editor-visible />
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, watchEffect } from 'vue';
	import type { EnmLeistungenProps } from './EnmLeistungenProps';

	const props = defineProps<EnmLeistungenProps>();

	// erstelle ein unsichtbares Image
	const img = document.createElement('img');
	img.src = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';

	watchEffect(() => {
		if (props.manager.lerngruppenAuswahlGetLeistungOrNull(props.manager.auswahlLeistung.leistung?.id ?? null) !== null)
			return;
		props.manager.auswahlLeistungNaechste();
	})

	async function doPatchLeistung(fachbezogeneBemerkungen: string|null) {
		if (props.manager.auswahlLeistung.leistung === null)
			return;
		const id = props.manager.auswahlLeistung.leistung.id
		const patch = { id, fachbezogeneBemerkungen };
		const success = await props.patchLeistung(patch);
		if (success)
			Object.assign(props.manager.auswahlLeistung.leistung, patch);
		props.manager.update();
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
