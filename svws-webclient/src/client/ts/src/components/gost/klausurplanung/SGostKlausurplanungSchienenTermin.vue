<template>
	<div class="flex flex-col border border-blue-900 border-solid w-72 shrink-0">
		<svws-ui-drop-data @drop="setKlausurToTermin" class="h-full">
			<div class="flex flex-row-reverse">
				<svws-ui-badge class="-m-2 z-10"
					v-if="(termin === null || dragKlausur === undefined || dragKlausur === null || termin?.quartal === dragKlausur?.quartal) && (konflikteTerminDragKlausur > 0 || konflikteTermin > 0)"
					type="error"
					size="big">
					<span class="text-base">{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin }}</span>
				</svws-ui-badge>
			</div>
			<!--<svws-ui-text-input v-if="termin !== null" placeholder="Terminbezeichnung" :model-value="termin.bezeichnung" @update:model-value="patchKlausurtermin !== undefined ? patchKlausurtermin({ bezeichnung: String($event) }, termin!.id) : null" />-->
			<s-gost-klausurplanung-termin-common :kursklausurmanager="kursklausurmanager"
				:quartal="quartal"
				:termin="termin"
				:alle-termine="alleTermine"
				:map-lehrer="mapLehrer"
				:kursmanager="kursmanager"
				:klausur-draggable="true"
				@drag-start-klausur="dragStartKlausur"
				@drag-end-klausur="dragEndKlausur"
				:klausur-css-classes="klausurCssClasses" />
		</svws-ui-drop-data>
	</div>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostKlausurtermin, GostFaecherManager, LehrerListeEintrag, SchuelerListeEintrag, List, KursManager} from "@core";
	import { computed } from 'vue';

	const props = defineProps<{
		termin: GostKlausurtermin | null;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		kursmanager: KursManager;
		setTerminToKursklausur: (idTermin: number | null, klausur: GostKursklausur) => Promise<boolean>;
		loescheKlausurtermin?: (termin: GostKlausurtermin) => Promise<boolean>;
		patchKlausurtermin?: (termin: Partial<GostKlausurtermin>, id: number) => Promise<boolean>;
		quartal?: number;
		alleTermine: List<GostKlausurtermin>;
		dragKlausur?: GostKursklausur | null;
	}>();

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
				konfliktfreiZuFremdtermin = props.kursklausurmanager().gibKonfliktTerminKursklausurIds(termin.id, klausur.id).isEmpty();
			if (konfliktfreiZuFremdtermin)
				break;
		}
		const konfliktZuEigenemTermin = props.termin === undefined || props.termin === null || klausur === null ? false : props.kursklausurmanager().gibKonfliktTerminInternKursklausur(props.termin, klausur).size() > 0;
		return {
			"bg-success": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			"bg-error": konfliktZuEigenemTermin,
		}
	};

	const setKlausurToTermin = async (pKlausur: GostKursklausur) => {
		const klausur = props.kursklausurmanager().gibKursklausur(pKlausur.id)!;
		const terminNeu = props.termin !== null ? props.termin.id : null;
		if (props.termin !== null && props.termin.quartal !== klausur.quartal)
			return;
		const result = await props.setTerminToKursklausur(terminNeu, klausur);
	}

	const konflikteTerminDragKlausur = computed(() =>
		props.termin !== null && props.dragKlausur !== null ? props.kursklausurmanager().gibKonfliktTerminKursklausurIds(props.termin.id, props.dragKlausur!.id).size() : -1
	);

	const konflikteTermin = computed(() =>
		props.termin !== null ? props.kursklausurmanager().gibAnzahlKonflikteZuTermin(props.termin.id) : 0
	);

	const loescheTermin = async() => {
		if (props.loescheKlausurtermin !== undefined && props.termin !== null)
			await props.loescheKlausurtermin(props.termin);
	};



</script>