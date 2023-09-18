<template>
	<div class="flex flex-col border border-blue-900 border-solid w-96 shrink-0" @drop="onDrop(termin())" @dragover="checkDropZone($event)">
		<div class="flex flex-row">
			<svws-ui-text-input :placeholder="termin().bezeichnung === null ? terminTitel() : 'Terminbezeichnung'" :model-value="termin().bezeichnung" @blur="bezeichnung => patchKlausurtermin(termin().id, {bezeichnung})" />
			<svws-ui-button v-if="loescheKlausurtermine !== undefined && termin !== undefined" class="float-right" type="danger" size="small" @click="loescheKlausurtermine(Arrays.asList([termin()]))"><i-ri-delete-bin-line /></svws-ui-button>
			<svws-ui-button class="float-right" size="small" @click="terminQuartalWechseln" :disabled="terminQuartalsWechselMoeglich()"><span class="flex row" v-if="termin().quartal > 0"><i-ri-lock-line />{{ termin().quartal }}</span><i-ri-lock-unlock-line v-else /></svws-ui-button>
			<svws-ui-badge class="-m-2 z-10 float-right"
				v-if="(dragData() === undefined || dragData() instanceof GostKursklausur && (termin().quartal === dragData()!.quartal) || termin().quartal === 0) && (konflikteTerminDragKlausur > 0 || konflikteTermin() > 0)"
				type="error"
				size="big">
				<span class="text-base">&nbsp;{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin() }}&nbsp;</span>
			</svws-ui-badge>
		</div>
		<s-gost-klausurplanung-termin :termin="termin()"
			:kursklausurmanager="kursklausurmanager"
			:map-lehrer="mapLehrer"
			:kursmanager="kursmanager"
			:on-drag-klausur="onDrag"
			:klausur-css-classes="klausurCssClasses">
			<template #title><template /></template>
		</s-gost-klausurplanung-termin>
	</div>
</template>

<script setup lang="ts">

	import { type GostKursklausurManager, GostKursklausur, type GostKlausurtermin, type LehrerListeEintrag, type List, type KursManager, Arrays} from "@core";
	import { computed } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";

	const props = defineProps<{
		termin: () => GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		loescheKlausurtermine?: (termine: List<GostKlausurtermin>) => Promise<void>;
		patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
		klausurCssClasses: (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => void;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
	}>();

	const klausuren = () => props.kursklausurmanager().kursklausurGetMengeByTerminid(props.termin().id);
	const terminTitel = () => kurzBezeichnungen;
	const kurzBezeichnungen = [...klausuren()].map(k => k.kursKurzbezeichnung).join(", ");

	function isDropZone() : boolean {
		if ((props.dragData() !== undefined) && (props.dragData() instanceof GostKursklausur))
			if (props.dragData()!.quartal === props.termin().quartal || props.termin().quartal === 0)
				return true;
		return false;
	}

	const terminQuartalsWechselMoeglich = () => props.termin().quartal === 0 && props.kursklausurmanager().quartalGetByTerminid(props.termin().id) === -1;

	async function terminQuartalWechseln() {
		if (props.termin().quartal === 0)
			if (props.kursklausurmanager().quartalGetByTerminid(props.termin().id) > 0)
				await props.patchKlausurtermin!(props.termin().id, {quartal: props.kursklausurmanager().quartalGetByTerminid(props.termin().id)});
			else
				return; // TODO Fehlermeldung, Klausuren mit unterschiedlichen Quartale enthalten
		else if (props.termin().quartal > 0 && klausuren().size() > 0)
			await props.patchKlausurtermin!(props.termin().id, {quartal: 0});
		else
			await props.patchKlausurtermin!(props.termin().id, {quartal: (props.termin().quartal + 1) % 3});
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	const konflikteTerminDragKlausur = computed(() =>
		props.dragData() !== undefined ? props.kursklausurmanager().konfliktSchueleridsGetMengeByTerminidAndKursklausurid(props.termin().id, props.dragData()!.id).size() : -1
	);

	const konflikteTermin = () => props.kursklausurmanager().konflikteAnzahlGetByTerminid(props.termin().id);

</script>