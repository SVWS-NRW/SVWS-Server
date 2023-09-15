<template>
	<div class="flex flex-col border border-blue-900 border-solid w-72 shrink-0" @drop="onDrop(termin())" @dragover="checkDropZone($event)">
		<div class="flex flex-row">
			<svws-ui-text-input :placeholder="termin().bezeichnung === null ? terminTitel() : 'Terminbezeichnung'" :model-value="termin().bezeichnung" @blur="bezeichnung => patchKlausurtermin(termin().id, {bezeichnung})" />
			<svws-ui-button v-if="loescheKlausurtermine !== undefined && termin !== undefined" class="float-right" type="danger" size="small" @click="loescheKlausurtermine(Arrays.asList([termin()]))"><i-ri-delete-bin-line /></svws-ui-button>
			<svws-ui-button class="float-right" size="small" @click="changeTerminQuartal" :disabled="quartalsWechselMoeglich()"><span class="flex row" v-if="termin().quartal > 0"><i-ri-lock-line />{{ termin().quartal }}</span><i-ri-lock-unlock-line v-else /></svws-ui-button>
			<svws-ui-badge class="-m-2 z-10 float-right"
				v-if="(dragData() === undefined || dragData() instanceof GostKursklausur && termin().quartal === dragData()!.quartal) && (konflikteTerminDragKlausur > 0 || konflikteTermin > 0)"
				type="error"
				size="big">
				<span class="text-base">&nbsp;{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin }}&nbsp;</span>
			</svws-ui-badge>
		</div>
		<s-gost-klausurplanung-termin :kursklausurmanager="kursklausurmanager"
			:jahrgangsdaten="jahrgangsdaten"
			:toggle-details="false"
			:show-details="showDetails"
			:quartal="quartal"
			:termin="termin()"
			:map-lehrer="mapLehrer"
			:kursmanager="kursmanager"
			:klausur-draggable="true"
			:on-drag="onDrag"
			:klausur-css-classes="klausurCssClasses" />
	</div>
</template>

<script setup lang="ts">

	import type { GostJahrgangsdaten} from "@core";
	import { type GostKursklausurManager, GostKursklausur, type GostKlausurtermin, type GostFaecherManager, type LehrerListeEintrag, type SchuelerListeEintrag, type List, type KursManager, Arrays} from "@core";
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten;
		termin: () => GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		kursmanager: KursManager;
		loescheKlausurtermine?: (termine: List<GostKlausurtermin>) => Promise<void>;
		patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
		quartal?: number;
		klausurCssClasses: (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => void;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
	}>();

	const showDetails = ref(true);

	const terminTitel = () => props.jahrgangsdaten.jahrgang + " - " + props.termin().halbjahr % 2 + ". Hj - " + props.termin().quartal + ". Quartal";

	function isDropZone() : boolean {
		if ((props.dragData() !== undefined) && (props.dragData() instanceof GostKursklausur))
			if (props.dragData()!.quartal === props.termin().quartal || props.termin().quartal === 0)
				return true;
		return false;
	}

	const quartalsWechselMoeglich = () => props.termin().quartal === 0 && props.kursklausurmanager().quartalGetByTerminid(props.termin().id) === -1;

	async function changeTerminQuartal() {
		if (props.termin().quartal === 0)
			if (props.kursklausurmanager().quartalGetByTerminid(props.termin().id) > 0)
				await props.patchKlausurtermin!(props.termin().id, {quartal: props.kursklausurmanager().quartalGetByTerminid(props.termin().id)});
			else
				return; // TODO Fehlermeldung, Klausuren mit unterschiedlichen Quartale enthalten
		else if (props.termin().quartal > 0 && props.kursklausurmanager().kursklausurGetMengeByTerminid(props.termin().id).size() > 0)
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

	const konflikteTermin = computed(() =>
		props.kursklausurmanager().konflikteAnzahlGetByTerminid(props.termin().id)
	);

</script>