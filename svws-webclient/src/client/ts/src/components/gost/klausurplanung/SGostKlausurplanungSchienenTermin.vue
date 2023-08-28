<template>
	<div class="flex flex-col border border-blue-900 border-solid w-72 shrink-0" @drop="onDrop(termin)" @dragover="checkDropZone($event)">
		<div class="">
			<svws-ui-button v-if="loescheKlausurtermine !== undefined && termin !== undefined" class="float-right" type="danger" size="small" @click="loescheKlausurtermine(Arrays.asList([termin]))"><i-ri-delete-bin-line /></svws-ui-button>
			<svws-ui-badge class="-m-2 z-10 float-right"
				v-if="(dragData() === undefined || dragData() instanceof GostKursklausur && termin?.quartal === dragData()!.quartal) && (konflikteTerminDragKlausur > 0 || konflikteTermin > 0)"
				type="error"
				size="big">
				<span class="text-base">&nbsp;{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin }}&nbsp;</span>
			</svws-ui-badge>
		</div>
		<!--<svws-ui-text-input v-if="termin !== null" placeholder="Terminbezeichnung" :model-value="termin.bezeichnung" @update:model-value="patchKlausurtermin !== undefined ? patchKlausurtermin({ bezeichnung: String($event) }, termin!.id) : null" />-->
		<s-gost-klausurplanung-termin v-if="termin !== undefined" :kursklausurmanager="kursklausurmanager"
			:jahrgangsdaten="jahrgangsdaten"
			:toggle-details="false"
			:show-details="showDetails"
			:quartal="quartal"
			:termin="termin"
			:map-lehrer="mapLehrer"
			:kursmanager="kursmanager"
			:klausur-draggable="true"
			:on-drag="onDrag"
			:klausur-css-classes="klausurCssClasses" />
		<table v-else class="w-full">
			<thead />
			<tbody>
				<tr v-for="klausur in klausurenOhneTermin()"
					:key="klausur.id"
					:data="klausur"
					:draggable="true"
					@dragstart="onDrag(klausur)"
					@dragend="onDrag(undefined)"
					:class="klausurCssClasses === undefined ? '' : klausurCssClasses(klausur)">
					<td>{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</td>
					<td>{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</td>
					<td class="text-center">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</td>
					<td class="text-center">{{ klausur.dauer }}</td>
					<td>&nbsp;</td>
					<td />
				</tr>
			</tbody>
		</table>
	</div>
</template>

<script setup lang="ts">

	import type { GostJahrgangsdaten} from "@core";
	import { type GostKursklausurManager, GostKursklausur, type GostKlausurtermin, type GostFaecherManager, type LehrerListeEintrag, type SchuelerListeEintrag, type List, type KursManager, Arrays} from "@core";
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten;
		termin: GostKlausurtermin | undefined;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		kursmanager: KursManager;
		loescheKlausurtermine?: (termine: List<GostKlausurtermin>) => Promise<boolean>;
		patchKlausurtermin?: (id: number, termin: Partial<GostKlausurtermin>) => Promise<boolean>;
		quartal?: number;
		alleTermine: List<GostKlausurtermin>;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
	}>();

	const showDetails = ref(true);

	const klausurenOhneTermin = () => props.quartal === undefined || props.quartal <= 0 ? props.kursklausurmanager().kursklausurOhneTerminGetMenge() : props.kursklausurmanager().kursklausurOhneTerminGetMengeByQuartal(props.quartal);

	const klausurCssClasses = (klausur: GostKursklausur) => {
		let konfliktfreiZuFremdtermin = false;
		for (const termin of props.alleTermine) {
			if (termin.id !== klausur.idTermin && termin.quartal === klausur.quartal)
				konfliktfreiZuFremdtermin = props.kursklausurmanager().konfliktSchueleridsGetMengeByTerminidAndKursklausurid(termin.id, klausur.id).isEmpty();
			if (konfliktfreiZuFremdtermin)
				break;
		}
		const konfliktZuEigenemTermin = props.termin === undefined || klausur === null ? false : props.kursklausurmanager().konfliktTermininternSchueleridsGetMengeByTerminAndKursklausur(props.termin, klausur).size() > 0;
		return {
			"bg-success": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			"bg-error": konfliktZuEigenemTermin,
		}
	};

	function isDropZone() : boolean {
		if ((props.dragData() !== undefined) && (props.dragData() instanceof GostKursklausur))
			return true;
		return false;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	const konflikteTerminDragKlausur = computed(() =>
		props.termin !== undefined && props.dragData() !== undefined ? props.kursklausurmanager().konfliktSchueleridsGetMengeByTerminidAndKursklausurid(props.termin.id, props.dragData()!.id).size() : -1
	);

	const konflikteTermin = computed(() =>
		props.termin !== undefined ? props.kursklausurmanager().konflikteAnzahlGetByTerminid(props.termin.id) : 0
	);

</script>