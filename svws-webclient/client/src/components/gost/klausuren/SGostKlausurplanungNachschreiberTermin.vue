<template>
	<div>
		<svws-ui-modal v-model:show="showModalTerminLoeschen" size="small" class="hidden">
			<template #modalTitle>
				Klausurtermin löschen
			</template>
			<template #modalContent>
				Diesem Termin sind Nachschreiber zugewiesen. Soll er wirklich gelöscht werden?
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="showModalTerminLoeschen = false">Abbrechen</svws-ui-button>
				<svws-ui-button type="primary" @click="loescheTerminBestaetigt">Löschen</svws-ui-button>
			</template>
		</svws-ui-modal>
		<s-gost-klausurplanung-termin-card :termin="termin()"
			:selected="terminSelected"
			:drop-state
			:drop-allowed="isDropZone(termin())"
			:conflict-count
			:title-placeholder="titlePlaceholderText"
			:title-disabled="termin().istHaupttermin"
			:quartal-disabled="termin().istHaupttermin"
			:show-jahrgang="state.zeigeAlleJahrgaenge"
			:goto-kalenderdatum
			:goto-raumzeit-termin
			@click="emit('click', $event)"
			@drop="onDrop(termin())">
			<s-gost-klausurplanung-termin :termin="termin()"
				:draggable
				:on-drag
				:show-kursschiene="true"
				:klausur-css-classes="nachschreiberTerminKlausurCssClasses"
				:show-schuelerklausuren
				:schuelerklausuren-nachschreiber-ansicht="true">
				<template #loeschen>
					<svws-ui-button :disabled="!hatKompetenzUpdate" v-if="termin !== undefined" type="trash" size="small" @click.stop="loescheTermin" />
				</template>
			</s-gost-klausurplanung-termin>
		</s-gost-klausurplanung-termin-card>
	</div>
</template>


<script setup lang="ts">

	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import { isGostNachschreiberDragData } from "./SGostKlausurplanung";
	import type { GostHalbjahr, GostKlausurtermin } from "@core";
	import { BenutzerKompetenz, Arrays, GostKursklausur, GostSchuelerklausurtermin } from "@core";
	import { useBenutzerState, useGostKlausurplanungState } from "@ui";
	import { computed, ref, type HTMLAttributes } from 'vue';
	import { klausurplanungDropState } from "./SGostKlausurplanungDragUtils";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";

	const props = defineProps<{
		termin: () => GostKlausurtermin;
		klausurCssClasses: (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => HTMLAttributes["class"];
		dragData: GostKlausurplanungDragData;
		onDrag: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		draggable: (data: GostKlausurplanungDragData) => boolean;
		terminSelected?: boolean;
		showSchuelerklausuren?: boolean;
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
	}>();

	const emit = defineEmits<{
		click: [event: MouseEvent];
	}>();

	const benutzerState = useBenutzerState();
	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);

	const showModalTerminLoeschen = ref<boolean>(false);

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));
	const titlePlaceholderText = computed<string>(() => {
		const bezeichnung = props.termin().bezeichnung;
		if ((bezeichnung !== null) && (bezeichnung.trim().length > 0)) {
			return "Klausurtermin";
		}
		return state.manager.kursklausurGetMengeByTermin(props.termin()).size() > 0 ? presenter.terminTitelShort(props.termin()) : "Neuer Nachschreibtermin";
	});
	const bestehendeKonflikte = computed<number>(() => state.manager.konflikteAnzahlGetByTermin(props.termin()));
	const conflictCount = computed<number>(() => konflikteTerminDragKlausur.value ?? bestehendeKonflikte.value);

	function isDropZone(termin: GostKlausurtermin): boolean {
		if (isGostNachschreiberDragData(props.dragData)) {
			return state.manager.schuelerklausurterminePassenInNachschreibtermin(termin, props.dragData.items);
		}
		return false;
	}

	function nachschreiberTerminKlausurCssClasses(klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined): HTMLAttributes["class"] {
		const classes = props.klausurCssClasses(klausur, termin);
		if ((klausur instanceof GostSchuelerklausurtermin) && schuelerklausurterminHatBestehendenKonflikt(klausur)) {
			return [classes, "bg-ui-danger text-ui-ondanger"];
		}
		if (!(klausur instanceof GostKursklausur)) {
			return classes;
		}
		if (kursklausurHatBestehendenNachschreiberKonflikt(klausur) || kursklausurHatDragKonflikt(klausur)) {
			return [classes, "bg-ui-danger text-ui-ondanger"];
		}
		return [classes, "opacity-65"];
	}

	function schuelerklausurterminHatBestehendenKonflikt(schuelerklausurtermin: GostSchuelerklausurtermin): boolean {
		return state.manager.hatKonfliktByTerminAndSchuelerklausurtermin(props.termin(), schuelerklausurtermin);
	}

	function kursklausurHatBestehendenNachschreiberKonflikt(klausur: GostKursklausur): boolean {
		for (const item of state.manager.schuelerklausurterminAktuellNtGetMengeByTermin(props.termin())) {
			if (state.manager.konfliktZuKursklausurBySchuelerklausur(item, klausur)) {
				return true;
			}
		}
		return false;
	}

	function kursklausurHatDragKonflikt(klausur: GostKursklausur): boolean {
		if (!isGostNachschreiberDragData(props.dragData)) {
			return false;
		}
		for (const item of props.dragData.items) {
			if (state.manager.konfliktZuKursklausurBySchuelerklausur(item, klausur)) {
				return true;
			}
		}
		return false;
	}

	function loescheTermin(): void {
		if (state.manager.schuelerklausurterminNtGetMengeByTermin(props.termin()).size() > 0) {
			showModalTerminLoeschen.value = true;
			return;
		}
		loescheTerminBestaetigt();
	}

	function loescheTerminBestaetigt(): void {
		showModalTerminLoeschen.value = false;
		if (props.termin().istHaupttermin) {
			void state.patchKlausurtermin(props.termin().id, { nachschreiberZugelassen: false });
			return;
		}
		void state.loescheKlausurtermine(Arrays.asList([props.termin()]));
	}

	const dropState = computed(() => klausurplanungDropState({
		hasDragData: props.dragData !== undefined,
		canDrop: isDropZone(props.termin()),
		hasConflict: (konflikteTerminDragKlausur.value ?? 0) > 0,
	}));

	const konflikteTerminDragKlausur = computed<number | undefined>(() => {
		if (isGostNachschreiberDragData(props.dragData)) {
			if (!state.manager.schuelerklausurterminePassenInNachschreibtermin(props.termin(), props.dragData.items)) {
				return 0;
			}
			return state.manager.konfliktPaarGetMengeTerminAndSchuelerklausurtermine(props.termin(), props.dragData.items).size();
		}
		return undefined;
	});

</script>
