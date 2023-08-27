<template>
	<div class="flex flex-col border border-blue-900 border-solid w-72 shrink-0">
		<svws-ui-drop-data @drop="setKlausurToTermin" class="h-full">
			<div class="">
				<svws-ui-button v-if="loescheKlausurtermine !== undefined && termin !== null" class="float-right" type="danger" size="small" @click="loescheKlausurtermine(Arrays.asList([termin]))"><i-ri-delete-bin-line /></svws-ui-button>
				<svws-ui-badge class="-m-2 z-10 float-right"
					v-if="(termin?.quartal === dragKlausur?.quartal) && (konflikteTerminDragKlausur > 0 || konflikteTermin > 0)"
					type="error"
					size="big">
					<span class="text-base">{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin }}</span>
				</svws-ui-badge>
			</div>
			<!--<svws-ui-text-input v-if="termin !== null" placeholder="Terminbezeichnung" :model-value="termin.bezeichnung" @update:model-value="patchKlausurtermin !== undefined ? patchKlausurtermin({ bezeichnung: String($event) }, termin!.id) : null" />-->
			<s-gost-klausurplanung-termin v-if="termin !== null" :kursklausurmanager="kursklausurmanager"
				:jahrgangsdaten="jahrgangsdaten"
				:toggle-details="false"
				:show-details="showDetails"
				:quartal="quartal"
				:termin="termin"
				:map-lehrer="mapLehrer"
				:kursmanager="kursmanager"
				:klausur-draggable="true"
				@drag-start-klausur="dragStartKlausur"
				@drag-end-klausur="dragEndKlausur"
				:klausur-css-classes="klausurCssClasses" />
			<table v-else class="w-full">
				<thead />
				<tbody>
					<svws-ui-drag-data v-for="klausur in klausurenOhneTermin()"
						:key="klausur.id"
						:data="klausur"
						:draggable="true"
						@drag-start="dragStartKlausur($event, klausur)"
						@drag-end="dragEndKlausur($event)"
						tag="tr"
						:class="klausurCssClasses === undefined ? '' : klausurCssClasses(klausur)">
						<td>{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</td>
						<td>{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</td>
						<td class="text-center">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</td>
						<td class="text-center">{{ klausur.dauer }}</td>
						<td>&nbsp;</td>
						<td />
					</svws-ui-drag-data>
				</tbody>
			</table>
		</svws-ui-drop-data>
	</div>
</template>

<script setup lang="ts">

	import type { GostJahrgangsdaten} from "@core";
	import { type GostKursklausurManager, type GostKursklausur, type GostKlausurtermin, type GostFaecherManager, type LehrerListeEintrag, type SchuelerListeEintrag, type List, type KursManager, Arrays} from "@core";
	import { computed, ref } from 'vue';

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten;
		termin: GostKlausurtermin | null;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		kursmanager: KursManager;
		setTerminToKursklausur: (idTermin: number | null, klausur: GostKursklausur) => Promise<boolean>;
		loescheKlausurtermine?: (termine: List<GostKlausurtermin>) => Promise<boolean>;
		patchKlausurtermin?: (id: number, termin: Partial<GostKlausurtermin>) => Promise<boolean>;
		quartal?: number;
		alleTermine: List<GostKlausurtermin>;
		dragKlausur?: GostKursklausur | null;
	}>();

	const showDetails = ref(true);

	const klausurenOhneTermin = () => props.quartal === undefined || props.quartal <= 0 ? props.kursklausurmanager().kursklausurOhneTerminGetMenge() : props.kursklausurmanager().kursklausurOhneTerminGetMengeByQuartal(props.quartal);

	const emit = defineEmits<{
		(e: 'dragStartKlausur', data: DragEvent, klausur: GostKursklausur): void;
		(e: 'dragEndKlausur', data: DragEvent): void;
	}>()

	function dragStartKlausur(e: DragEvent, klausur: GostKursklausur) {
		emit("dragStartKlausur", e, klausur);
	}

	function dragEndKlausur(e: DragEvent) {
		emit("dragEndKlausur", e);
	}

	const klausurCssClasses = (klausur: GostKursklausur) => {
		let konfliktfreiZuFremdtermin = false;
		for (const termin of props.alleTermine) {
			if (termin.id !== klausur.idTermin && termin.quartal === klausur.quartal)
				konfliktfreiZuFremdtermin = props.kursklausurmanager().konfliktSchueleridsGetMengeByTerminidAndKursklausurid(termin.id, klausur.id).isEmpty();
			if (konfliktfreiZuFremdtermin)
				break;
		}
		const konfliktZuEigenemTermin = props.termin === undefined || props.termin === null || klausur === null ? false : props.kursklausurmanager().konfliktTermininternSchueleridsGetMengeByTerminAndKursklausur(props.termin, klausur).size() > 0;
		return {
			"bg-success": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			"bg-error": konfliktZuEigenemTermin,
		}
	};

	const setKlausurToTermin = async (pKlausur: GostKursklausur) => {
		const klausur = props.kursklausurmanager().kursklausurGetByIdOrException(pKlausur.id);
		const terminNeu = props.termin !== null ? props.termin.id : null;
		if (props.termin !== null && props.termin.quartal !== klausur.quartal)
			return;
		const result = await props.setTerminToKursklausur(terminNeu, klausur);
	}

	const konflikteTerminDragKlausur = computed(() =>
		props.termin !== null && props.dragKlausur !== null ? props.kursklausurmanager().konfliktSchueleridsGetMengeByTerminidAndKursklausurid(props.termin.id, props.dragKlausur!.id).size() : -1
	);

	const konflikteTermin = computed(() =>
		props.termin !== null ? props.kursklausurmanager().konflikteAnzahlGetByTerminid(props.termin.id) : 0
	);

</script>