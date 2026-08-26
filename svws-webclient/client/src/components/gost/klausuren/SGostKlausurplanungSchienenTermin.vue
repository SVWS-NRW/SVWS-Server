<template>
	<s-gost-klausurplanung-termin-card card-class="svws-klausurplanung-schienen-termin max-w-120"
		:termin="termin()"
		:selected="terminSelected"
		:warning="!stundenplanVorhanden"
		:drop-state
		:drop-allowed="isDropZone()"
		:conflict-count
		:title-placeholder="titlePlaceholderText"
		:goto-kalenderdatum
		:goto-raumzeit-termin
		@drop="onDrop($event, termin())">
		<s-gost-klausurplanung-termin :termin="termin()"
			:draggable
			:on-drag
			:show-kursschiene="true"
			:klausur-css-classes>
			<template #nachschreiberSummary>
				<button v-if="nachschreiberAnzahl > 0" type="button" class="mt-2 inline-flex w-fit items-center gap-1 rounded-md px-1.5 py-0.5 text-sm font-bold underline-offset-2 hover:underline"
					:class="{
						'border border-ui-danger/40 bg-ui-danger/10 text-ui-danger': nachschreiberKonflikt,
						'text-ui-75': !nachschreiberKonflikt,
					}"
					@click.stop="gotoNachschreiber(termin().abiturjahrgang, GostHalbjahr.fromIDorException(termin().halbjahr))">
					+ {{ nachschreiberAnzahl }} Nachschreiber
					<span class="icon i-ri-link icon-ui" />
				</button>
			</template>
			<template #loeschen>
				<svws-ui-button type="trash" v-if="termin !== undefined" :disabled="!hatKompetenzUpdate" size="small" @click="state.loescheKlausurtermine(Arrays.asList([termin()]));$event.stopPropagation()" />
			</template>
		</s-gost-klausurplanung-termin>
	</s-gost-klausurplanung-termin-card>
</template>

<script setup lang="ts">
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { GostHalbjahr as GostHalbjahrType, GostKlausurtermin } from "@core";
	import { Arrays, BenutzerKompetenz, GostHalbjahr, GostKursklausur, GostSchuelerklausurtermin } from "@core";
	import { useBenutzerState, useGostKlausurplanungState } from "@ui";
	import { computed, type HTMLAttributes } from 'vue';
	import { klausurplanungDropState } from "./SGostKlausurplanungDragUtils";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";

	const props = defineProps<{
		termin: () => GostKlausurtermin;
		klausurCssClasses: (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => HTMLAttributes["class"];
		dragData: GostKlausurplanungDragData;
		onDrag: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		onDrop: (event: DragEvent | undefined, zone: GostKlausurplanungDropZone) => Promise<void>;
		draggable: (data: GostKlausurplanungDragData) => boolean;
		terminSelected?: boolean;
		showSchuelerklausuren?: boolean;
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoNachschreiber: (abiturjahr: number, halbjahr: GostHalbjahrType) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahrType, value: number) => Promise<void>;
	}>();

	const benutzerState = useBenutzerState();
	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));
	const stundenplanVorhanden = computed<boolean>(() => (props.termin().datum === null) || (state.manager.stundenplanManagerGetByTerminOrNull(props.termin()) !== null));
	const titlePlaceholderText = computed<string>(() => {
		const bezeichnung = props.termin().bezeichnung;
		if ((bezeichnung !== null) && (bezeichnung.trim().length > 0)) {
			return "Klausurtermin";
		}
		return state.manager.kursklausurGetMengeByTermin(props.termin()).size() > 0 ? presenter.terminTitelShort(props.termin()) : "Neuer Termin";
	});
	const kursklausurDrop = computed(() => {
		const data = props.dragData;
		if (!(data instanceof GostKursklausur)) {
			return undefined;
		}
		const termin = props.termin();
		const canDrop = state.manager.kursklausurPasstInTermin(termin, data);
		const conflictCount = canDrop ? state.manager.konflikteAnzahlZuTerminGetByTerminAndKursklausur(termin, data) : 0;
		return { canDrop, conflictCount };
	});

	const conflictCount = computed(() => {
		if (kursklausurDrop.value !== undefined) {
			return kursklausurDrop.value.conflictCount;
		}
		const terminKonflikte = konflikteTermin();
		return Math.max(terminKonflikte, 0);
	});

	function isDropZone(): boolean {
		if (kursklausurDrop.value !== undefined) {
			return kursklausurDrop.value.canDrop;
		}
		if (props.dragData instanceof GostSchuelerklausurtermin) {
			return true;
		}
		return false;
	}

	const dropState = computed(() => klausurplanungDropState({
		hasDragData: props.dragData !== undefined,
		canDrop: isDropZone(),
		hasConflict: conflictCount.value > 0,
	}));

	const konflikteTermin = () => state.manager.konflikteAnzahlGetByTermin(props.termin());
	const nachschreiber = computed<GostSchuelerklausurtermin[]>(() => [...state.manager.schuelerklausurterminAktuellNtGetMengeByTermin(props.termin())]);
	const nachschreiberAnzahl = computed<number>(() => nachschreiber.value.length);
	const nachschreiberKonflikt = computed<boolean>(() => {
		if (nachschreiber.value.length === 0) {
			return false;
		}
		const kursklausuren = [...state.manager.kursklausurGetMengeByTermin(props.termin())];
		if ((props.dragData instanceof GostKursklausur) && state.manager.kursklausurPasstInTermin(props.termin(), props.dragData) && (props.dragData.idTermin !== props.termin().id)) {
			kursklausuren.push(props.dragData);
		}
		for (const kursklausur of kursklausuren) {
			for (const schuelerklausurtermin of nachschreiber.value) {
				if (state.manager.konfliktZuKursklausurBySchuelerklausur(schuelerklausurtermin, kursklausur)) {
					return true;
				}
			}
		}
		return false;
	});

</script>
