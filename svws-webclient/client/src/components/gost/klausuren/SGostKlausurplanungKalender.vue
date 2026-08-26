<template>
	<template v-if="hatKalenderKontext">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-kalender-hilfe /> </svws-ui-modal-hilfe>
		</Teleport>
		<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
			<s-gost-klausurplanung-quartal-auswahl show-alle-jahrgaenge />
		</Teleport>
		<s-gost-klausurplanung-layout sidebar-title="In Planung"
			:sidebar-drop-enabled="(state.jahrgangsdaten?.abiturjahr !== -1) && (state.selectedTermin !== undefined) && (state.selectedTermin.datum !== null)"
			@sidebar-drop="onDrop(undefined)">
			<template #sidebar>
				<div class="flex flex-col">
					<div v-if="state.jahrgangsdaten?.abiturjahr !== -1" class="h-full">
						<s-gost-klausurplanung-sidebar-liste :empty="state.manager.terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal).isEmpty()">
							<template #empty>
								<span>Aktuell keine Klausuren zu planen.</span>
								<span class="opacity-50">Bereits geplante Einträge können hier zurückgelegt werden.</span>
							</template>
							<s-gost-klausurplanung-sidebar-eintrag-termin v-for="termin in state.manager.terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal)"
								:id="'termin' + termin.id"
								:key="termin.id"
								:termin
								:kursklausuren="state.manager.kursklausurGetMengeByTermin(termin)"
								:draggable="isDraggable(termin)"
								:show-quartal="state.quartal === 0"
								:expanded="(state.selectedTermin !== undefined) && (state.selectedTermin.id === termin.id)"
								:selected="(state.selectedTermin !== undefined) && (state.selectedTermin.id === termin.id)"
								@dragstart="onDrag(termin)"
								@dragend="onDrag(undefined)"
								@click="state.selectedTermin?.id === termin.id ? onDrag(undefined) : onDrag(termin);$event.stopPropagation()">
								<template #expanded>
									<s-gost-klausurplanung-termin :termin
										show-last-klausurtermin />
								</template>
							</s-gost-klausurplanung-sidebar-eintrag-termin>
						</s-gost-klausurplanung-sidebar-liste>
					</div>
				</div>
			</template>
			<template #workspace>
				<div class="svws-card-stundenplan h-full overflow-auto overflow-y-hidden">
					<template v-if="state.kalenderdatum">
						<s-gost-klausurplanung-kalender-stundenplan-ansicht :id="33"
							:abschnitt-id="state.abschnittOrException.id"
							:wochentyp="() => 0"
							:kurse-gefiltert
							:sum-schreiber
							:on-drop
							:on-drag
							:drag-data="() => state.selectedTermin"
							:check-drop-zone-zeitraster
							:goto-kalenderdatum
							:goto-raumzeit-termin
							:kalenderwoche
							:kursklausur-mouse-over="() => kursklausurMouseOver">
							<template #kwAuswahl>
								<div class="col-span-2 flex gap-0.5">
									<svws-ui-button type="icon" @click="navKalenderdatum(-7)" :disabled="!berechneKwzDatum(-7)"><span class="icon i-ri-arrow-left-s-line" /></svws-ui-button>
									<svws-ui-select title="Kalenderwoche" v-model="kalenderwochenauswahl" :items="kalenderwochen()" :item-text="kw => stundenplanManager().kalenderwochenzuordnungGetWocheAsString(kw)" headless />
									<svws-ui-button type="icon" @click="navKalenderdatum(+7)" :disabled="!berechneKwzDatum(+7)"><span class="icon i-ri-arrow-right-s-line" /></svws-ui-button>
								</div>
							</template>
						</s-gost-klausurplanung-kalender-stundenplan-ansicht>
					</template>
					<template v-else>
						<svws-ui-select title="Kalenderwoche" v-model="kalenderwochenauswahl" :items="kalenderwochen()"
							:item-text="kw => stundenplanManager().kalenderwochenzuordnungGetWocheAsString(kw)" />
					</template>
				</div>
			</template>
			<template #aside>
				<s-gost-klausurplanung-konflikte :termine="termineDerAusgewaehltenKalenderwoche"
					kontext="woche"
					:kw="kalenderwoche().kw"
					:highlight="pendingTerminDrop !== undefined"
					:pending-termin-drop
					@kursklausur-mouse-enter="kursklausurMouseOver = $event"
					@kursklausur-mouse-leave="kursklausurMouseOver = undefined" />
			</template>
		</s-gost-klausurplanung-layout>
	</template>
	<s-gost-klausurplanung-modal v-model:show="modalKlausurHatRaeume" text="Der Klausurtermin ist Teil einer jahrgangsübergreifenden Raumplanung. Die Aktion hat daher Auswirkungen auf andere Termine." :weiter="verschiebeKlausurTrotzRaumzuweisung" />
</template>

<script setup lang="ts">
	import { ref, onMounted, computed } from "vue";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type { Wochentag, StundenplanKalenderwochenzuordnung, List, GostKursklausur, GostHalbjahr } from "@core";
	import { GostKlausurtermin, DateUtils, ArrayList, BenutzerKompetenz, StundenplanZeitraster } from "@core";
	import { useBenutzerState, useGostKlausurplanungState } from "@ui";
	import { useKlausurplanungDragAndDrop } from "./SGostKlausurplanungDragUtils";

	const { gotoKalenderdatum, gotoRaumzeitTermin } = defineProps<{
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, idtermin: number | undefined) => Promise<void>;
	}>();
	const state = useGostKlausurplanungState();
	const benutzerState = useBenutzerState();
	const { scrollSelectedTerminIntoView } = useKlausurplanungDragAndDrop();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const hatKalenderKontext = computed<boolean>(() => {
		const abschnitt = state.abschnitt;
		if (abschnitt === undefined) {
			return false;
		}
		const datum = state.kalenderdatum;
		if (datum === undefined) {
			return false;
		}
		if (!state.manager.stundenplanManagerGeladenByAbschnitt(abschnitt.id)) {
			return false;
		}
		if (!state.manager.stundenplanManagerExistsByAbschnitt(abschnitt.id)) {
			return false;
		}
		return state.manager.stundenplanManagerGetByAbschnittAndDatumOrNull(abschnitt.id, datum) !== null;
	});

	const kalenderwoche = (datum?: string) => {
		const datumAktuell = datum ?? state.kalenderdatumOrException;
		return state.manager.stundenplanManagerGetByAbschnittAndDatumOrException(state.abschnittOrException.id, datumAktuell).kalenderwochenzuordnungGetByDatum(datumAktuell);
	};

	const stundenplanManager = () => {
		const kw = kalenderwoche();
		return state.manager.stundenplanManagerGetByAbschnittAndKwOrException(state.abschnittOrException.id, kw.jahr, kw.kw);
	};

	const kursklausurMouseOver = ref<GostKursklausur | undefined>(undefined);

	const kalenderwochenauswahl = computed<StundenplanKalenderwochenzuordnung>({
		get: () => {
			const datum = state.kalenderdatumOrException;
			return state.manager.stundenplanManagerGetByAbschnittAndDatumOrException(state.abschnittOrException.id, datum).kalenderwochenzuordnungGetByDatum(datum);
		},
		set: (value) => {
			gotoKalenderdatum(DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(value.jahr, value.kw), state.selectedTermin).catch(() => {});
		},
	});

	function kalenderwochen(): List<StundenplanKalenderwochenzuordnung> {
		return state.manager.stundenplanManagerKalenderwochenzuordnungenGetMengeByAbschnitt(state.abschnittOrException.id);
	}

	const modalKlausurHatRaeume = ref<boolean>(false);

	let klausurMoveDragData: GostKlausurtermin | undefined = undefined;
	let klausurMoveDropZone: GostKlausurplanungDropZone = undefined;

	async function verschiebeKlausurTrotzRaumzuweisung() {
		if (klausurMoveDragData) {
			if (isTerminDropUnveraendert(klausurMoveDragData, klausurMoveDropZone)) {
				state.setSelectedTermin(undefined);
				return;
			}
			if (klausurMoveDropZone === undefined) {
				await state.patchKlausurtermin(klausurMoveDragData.id, { datum: null, startzeit: null });
			} else if (klausurMoveDropZone instanceof StundenplanZeitraster) {
				const date = stundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche(), klausurMoveDropZone);
				await state.patchKlausurtermin(klausurMoveDragData.id, { datum: date, startzeit: klausurMoveDropZone.stundenbeginn });
			}
		}
		state.setSelectedTermin(undefined);

	}

	const zeitrasterSelected = ref<StundenplanZeitraster | undefined>(undefined);

	const pendingTerminDrop = computed(() => (state.selectedTermin !== undefined) && (zeitrasterSelected.value !== undefined)
		? { termin: state.selectedTermin, datum: stundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche(), zeitrasterSelected.value) }
		: undefined);

	const termineDerAusgewaehltenKalenderwoche = computed<GostKlausurtermin[]>(() => {
		const kw = kalenderwoche();
		return [...state.manager.terminGetMengeByJahrAndKwAndAbijahrMultijahrgang(kw.jahr, kw.kw, state.jahrgangsdaten.abiturjahr, state.zeigeAlleJahrgaenge)];
	});

	const berechneKwzDatum = (by: number) => {
		const datum = new Date(state.kalenderdatumOrException);
		datum.setDate(datum.getDate() + by);
		const datumStr = datum.getFullYear() + "-" + (datum.getMonth() + 1).toString().padStart(2, '0') + "-" + datum.getDate().toString().padStart(2, '0');
		const stundenplan = state.manager.stundenplanManagerGetByAbschnittAndKwOrNull(state.abschnittOrException.id, DateUtils.gibKwJahrDesDatumsISO8601(datumStr), DateUtils.gibKwDesDatumsISO8601(datumStr));
		if (stundenplan === null) {
			return undefined;
		}
		const kw = stundenplan.kalenderwochenzuordnungGetByDatum(datumStr);
		return DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kw.jahr, kw.kw);
	};

	async function navKalenderdatum(by: number) {
		await gotoKalenderdatum(berechneKwzDatum(by), state.selectedTermin);
	}

	function checkDropZoneZeitraster(event: DragEvent, zeitraster: StundenplanZeitraster | undefined): void {
		zeitrasterSelected.value = zeitraster;
		event.preventDefault();
	}

	function sumSchreiber(datum: string, day: Wochentag, stunde: number) {
		let summe = 0;
		for (const klausur of kurseGefiltert(datum, day, stunde)) {
			summe += state.manager.kursAnzahlSchuelerGesamtByKursklausur(klausur);
		}
		return summe;
	}

	function kurseGefiltert(datum: string, day: Wochentag, stunde: number): List<GostKursklausur> {
		return state.selectedTermin === undefined
			? new ArrayList<GostKursklausur>()
			: state.manager.kursklausurGetMengeMitUnterrichtByTerminAndDatumAndWochentagAndStunde(state.selectedTermin, datum, day, stunde);
	}

	function isDraggable(object: any): boolean {
		return hatKompetenzUpdate.value;
	}

	const onDrag = (data: GostKlausurplanungDragData) => {
		if (data instanceof GostKlausurtermin) {
			gotoKalenderdatum(undefined, data).catch(() => {});
			zeitrasterSelected.value = undefined;
		} else if (data === undefined) {
			gotoKalenderdatum(undefined, undefined).catch(() => {});
		}
	};

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (state.selectedTermin !== undefined) {
			if (isTerminDropUnveraendert(state.selectedTermin, zone)) {
				return;
			}
			klausurMoveDropZone = zone;
			klausurMoveDragData = state.selectedTermin;
			if (state.manager.isKlausurenInFremdraeumenByTermin(state.selectedTermin)) {
				modalKlausurHatRaeume.value = true;
			} else {
				await verschiebeKlausurTrotzRaumzuweisung();
			}
		}
	};

	function isTerminDropUnveraendert(termin: GostKlausurtermin, zone: GostKlausurplanungDropZone): boolean {
		if (zone === undefined) {
			return (termin.datum === null) && (termin.startzeit === null);
		}
		if (!(zone instanceof StundenplanZeitraster)) {
			return false;
		}
		const datum = stundenplanManager().datumGetByKwzAndZeitraster(kalenderwoche(), zone);
		return (termin.datum === datum) && (termin.startzeit === zone.stundenbeginn);
	}

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
		scrollSelectedTerminIntoView(state.selectedTermin);
	});

</script>

<!-- <style lang="postcss">

	@reference "../../../../../ui/src/assets/styles/index.css"

	.svws-kw-auswahl {
		@apply bg-ui-brand text-white rounded-md h-7 -my-1;

		.text-input--headless {
			@apply !px-4 !text-button;
		}

		.svws-dropdown-icon {
			@apply !hidden;
		}
	}

</style> -->
