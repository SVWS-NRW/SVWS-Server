<template>
	<div class="page page-flex-row">
		<div class="flex flex-row h-full w-full overflow-hidden" @dragover="dragOver">
			<div class="grow w-full h-full overflow-hidden">
				<enm-klassenleitung-uebersicht :manager :patch-bemerkungen :patch-lernabschnitt :columns-visible :set-columns-visible @hauptgruppe="erlaubteHauptgruppe = $event" />
			</div>
			<template v-if="floskelEditorVisible">
				<div class="h-full content-center text-center cursor-col-resize min-w-8 max-w-8 lg:min-w-12 lg:max-w-12 bg-ui hover:bg-ui-hover" draggable="true" @dragstart="dragStart">
					<span class="icon i-ri-arrow-left-s-line" />
					<span class="icon i-ri-arrow-right-s-line" />
				</div>
				<div class="h-full overflow-hidden" :style="{ 'min-width': posDivider + 'rem', 'max-width': posDivider + 'rem' }">
					<enm-floskeleditor :manager :patch="doPatchBemerkungen" :erlaubte-hauptgruppe />
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watchEffect } from 'vue';
	import type { BemerkungenHauptgruppe } from './EnmManager';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';

	const props = defineProps<EnmKlassenleitungProps>();

	const erlaubteHauptgruppe = ref<BemerkungenHauptgruppe>('ZB');

	const floskelEditorVisible = computed(() => {
		const cols = props.columnsVisible();
		return (cols.get('AUE') ?? false)
			|| (cols.get('ASV') ?? false)
			|| (cols.get('ZB') ?? false);
	})

	watchEffect(() => {
		if (props.manager.klassenAuswahlGetSchueler().contains(props.manager.auswahlSchueler))
			return;
		props.manager.auswahlSchuelerNaechster();
	})

	async function doPatchBemerkungen(bemerkung: string|null) {
		const schueler = props.manager.auswahlSchueler ?? null;
		if (schueler === null)
			return;
		switch (erlaubteHauptgruppe.value) {
			case 'ASV':
				schueler.bemerkungen.ASV = bemerkung;
				break;
			case 'AUE':
				schueler.bemerkungen.AUE = bemerkung;
				break;
			case 'ZB':
				schueler.bemerkungen.ZB = bemerkung;
				break;
			default:
				return;
		}
		await props.patchBemerkungen(schueler.id, schueler.bemerkungen);
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
			width = 0;
		// Setze die Position des Dividers nur neu, wenn er stark genug von der alten Position abweicht (mehr als 1 rem) -> Performance
		if (Math.abs(width - posDivider.value) > 1)
			posDivider.value = width;
	}

	function dragStart(event: DragEvent) {
		// erstelle ein unsichtbares Image
		const img = document.createElement('img');
		img.src = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';
		// setze dieses unsichtbare Image als DragImage
		event.dataTransfer?.setDragImage(img, 0, 0);
	}

</script>
