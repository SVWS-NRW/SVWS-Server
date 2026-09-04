<template>
	<template v-if="(state.abschnitt !== undefined) && state.manager.stundenplanManagerGeladenByAbschnitt(state.abschnitt.id)">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-raumzeit-hilfe /> </svws-ui-modal-hilfe>
		</Teleport>
		<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
			<s-gost-klausurplanung-quartal-auswahl show-alle-jahrgaenge />
		</Teleport>
		<s-gost-klausurplanung-layout sidebar-title="In Planung"
			:sidebar-drop-enabled="isSidebarDropTarget()"
			@sidebar-drop="(state.selectedTermin !== undefined) && onDrop(state.selectedTermin)">
			<template #sidebar>
				<s-gost-klausurplanung-sidebar-liste :empty="termine().isEmpty()">
					<template #empty>
						<span>Aktuell keine Klausuren zu planen.</span>
						<span class="opacity-50">Um Räume und Startzeiten festzulegen, müssen Klausuren einem Termin zugeordnet sein.</span>
					</template>
					<s-gost-klausurplanung-sidebar-eintrag-termin v-for="termin in termine()" :id="'termin' + termin.id" :key="termin.id"
						:termin
						:kursklausuren="state.manager.kursklausurMitNachschreibernGetMengeByTermin(termin, true)"
						:draggable="isDraggable(termin, termin)"
						:expanded="(state.selectedTermin !== undefined) && (state.selectedTermin.id === termin.id)"
						:selected="(state.selectedTermin !== undefined) && (state.selectedTermin.id === termin.id)"
						@dragstart="onDrag($event, termin)" @dragend="onDrag($event, undefined)"
						@dragover="checkDropZone($event)" @drop.stop="onDrop(termin)"
						@click="(state.manager.stundenplanManagerGetByTerminOrNull(termin) !== null) && chooseTermin(termin)">
						<template #expanded>
							<s-gost-klausurplanung-termin :termin
								:on-drag :draggable="isDraggable" :klausur-css-classes="calculateCssClassesKlausur"
								:show-kursklausuren-nachschreiber="true" />
						</template>
					</s-gost-klausurplanung-sidebar-eintrag-termin>
				</s-gost-klausurplanung-sidebar-liste>
			</template>
			<template #workspace>
				<template v-if="(state.selectedTermin === undefined) || (state.selectedTermin.datum === null)">
					<div class="shadow-inner rounded-lg h-full flex items-center justify-center py-8 px-3 text-center">
						<span class="opacity-50" v-if="termine().size() > 0">Zum Bearbeiten einen Klausurtermin aus der Planung auswählen.</span>
					</div>
				</template>
				<template v-else>
					<s-gost-klausurplanung-raumzeit-termin :termin="state.selectedTermin" :goto-termin
						:drag-data="() => dragData" :on-drag :on-drop />
				</template>
			</template>
		</s-gost-klausurplanung-layout>
	</template>
</template>


<script setup lang="ts">

	import { ref, onMounted, computed } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';
	import { useKlausurplanungDragAndDrop } from './SGostKlausurplanungDragUtils';
	import { GostKlausurraum } from '@core/core/data/gost/klausuren/GostKlausurraum';
	import { GostKlausurraumRich } from '@core/core/data/gost/klausuren/GostKlausurraumRich';
	import { GostKlausurtermin } from '@core/core/data/gost/klausuren/GostKlausurtermin';
	import { GostKursklausur } from '@core/core/data/gost/klausuren/GostKursklausur';
	import { GostSchuelerklausurtermin } from '@core/core/data/gost/klausuren/GostSchuelerklausurtermin';
	import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import { GostHalbjahr } from '@core/core/types/gost/GostHalbjahr';
	import { ListUtils } from '@core/core/utils/ListUtils';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { useBenutzerState } from '@ui/states/BenutzerState';
	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState';

	const { gotoKalenderdatum, gotoRaumzeitTermin: gotoTermin } = defineProps<{
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, idtermin: number | undefined) => Promise<void>;
	}>();
	const state = useGostKlausurplanungState();
	const benutzerState = useBenutzerState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const chooseTermin = async (termin: GostKlausurtermin) => {
		state.setRaumTermin(termin);
		await gotoTermin(termin.abiturjahrgang, GostHalbjahr.fromIDorException(termin.halbjahr), termin.id);
	};

	const termine = () => state.manager.terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);

	const calculateCssClassesKlausur = (kl: GostKlausurplanungDragData, termin: GostKlausurtermin | null = null) => {
		const klausur = kl as GostKursklausur;
		return {
			"text-ui-50": state.manager.isKursklausurAlleSchuelerklausurenVerplant(klausur, termin),
			"": !state.manager.isKursklausurAlleSchuelerklausurenVerplant(klausur, termin),
		};
	};


	const { dragData, onDrag, scrollSelectedTerminIntoView } = useKlausurplanungDragAndDrop();

	const isDraggable = (object: any, termin: GostKlausurtermin) => {
		if (!hatKompetenzUpdate.value) {
			return false;
		}
		if (!hatRaum(termin)) {
			return false;
		}
		if (object instanceof GostKursklausur) {
			return !state.manager.isKursklausurAlleSchuelerklausurenVerplant(object, termin);
		} else if ((object instanceof GostKlausurtermin) && state.selectedTermin) {
			return (object.id === state.selectedTermin.id) && (state.manager.schuelerklausurterminFuerRaumzuweisungGetMengeByTermin(state.selectedTermin).size() > 0);
		} else if (object instanceof GostSchuelerklausurtermin) {
			return true;
		}
		return false;
	};

	function hatRaum(termin: GostKlausurtermin): boolean {
		return state.manager.raumGetMengeByTerminIncludingFremdtermine(termin, state.zeigeAlleJahrgaenge || state.manager.isKlausurenInFremdraeumenByTermin(termin)).size() > 0;
	}

	function isSidebarDropTarget(): boolean {
		if (!hatKompetenzUpdate.value || (state.selectedTermin === undefined)) {
			return false;
		}
		if (dragData.value instanceof GostKursklausur) {
			return state.manager.hatRaumzuteilungByKursklausur(dragData.value);
		}
		if (dragData.value instanceof GostSchuelerklausurtermin) {
			return state.manager.raumGetBySchuelerklausurtermin(dragData.value) !== null;
		}
		return false;
	}

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		const schuelerklausurtermine = schuelerklausurtermineByDropZone(zone, dragData.value);
		if (schuelerklausurtermine.isEmpty()) {
			return;
		}
		const rRaum = zone instanceof GostKlausurraum ? new GostKlausurraumRich(zone, null) : new GostKlausurraumRich();
		rRaum.idsSchuelerklausurtermine = mapIDs(schuelerklausurtermine);
		await state.setzeRaumZuSchuelerklausuren(ListUtils.create1(rRaum), zone instanceof GostKlausurtermin);
	};

	function schuelerklausurtermineByDropZone(zone: GostKlausurplanungDropZone, data: GostKlausurplanungDragData): List<GostSchuelerklausurtermin> {
		if (state.selectedTermin === undefined) {
			return new ArrayList<GostSchuelerklausurtermin>();
		}
		if (zone instanceof GostKlausurraum) {
			if (data instanceof GostKursklausur) {
				return state.manager.schuelerklausurterminFuerRaumzuweisungGetMengeByTerminAndKursklausur(state.selectedTermin, data);
			}
			if (data instanceof GostKlausurtermin) {
				return state.manager.schuelerklausurterminFuerRaumzuweisungGetMengeByTermin(state.selectedTermin);
			}
			if (data instanceof GostSchuelerklausurtermin) {
				return state.manager.schuelerklausurterminFuerRaumzuweisungGetMengeBySchuelerklausurtermin(data);
			}
		} else if (zone instanceof GostKlausurtermin) {
			if (data instanceof GostKursklausur) {
				return state.manager.schuelerklausurterminFuerRaumzuweisungAufhebenGetMengeByTerminAndKursklausur(state.selectedTermin, data);
			}
			if (data instanceof GostSchuelerklausurtermin) {
				return state.manager.schuelerklausurterminFuerRaumzuweisungAufhebenGetMengeBySchuelerklausurtermin(data);
			}
		}
		return new ArrayList<GostSchuelerklausurtermin>();
	}

	function mapIDs(skts: List<GostSchuelerklausurtermin>): List<number> {
		const numList = new ArrayList<number>();
		for (const skt of skts) {
			numList.add(skt.id);
		}
		return numList;
	}

	function isDropZone(): boolean {
		return dragData.value !== undefined;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone()) {
			event.preventDefault();
		}
	}

	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
		scrollSelectedTerminIntoView(state.selectedTermin);
	});

</script>
