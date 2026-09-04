<template>
	<div class="svws-ui-stundenplan svws-hat-zeitachse svws-zeitraster-5 overflow-visible h-auto pb-8 w-full">
		<!-- Die Überschriften des Stundenplan -->
		<div class="svws-ui-stundenplan--head">
			<slot name="kwAuswahl">
				<div class="col-span-2 inline-flex gap-1 items-center justify-center print:!pl-2 print:!justify-start font-bold text-headline-md pb-0.5">
					KW {{ kalenderwoche().kw || '–' }}
				</div>
			</slot>
			<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.a.id" class="font-bold my-auto w-full inline-flex items-center justify-center tabular-nums">
				<span class="opacity-50 uppercase mr-2">{{ wochentage[wochentag.a.id].slice(0, 2) }}</span> {{ DateUtils.gibDatumGermanFormat(wochentag.b) }}
			</div>
		</div>
		<!-- Die Daten des Stundenplans -->
		<div class="svws-ui-stundenplan--body" :style="{'--zeitrasterRows': zeitrasterRows}">
			<!-- Die Zeitachse des Stundenplans auf der linken Seite -->
			<div class="svws-ui-stundenplan--zeitraster svws-zeitachse">
				<template v-for="n in zeitrasterRows" :key="n">
					<span v-if="(n % 3 === 2)" class="svws-ui-stundenplan--einheit" :class="{'svws-extended': (n % 4 === 2), 'svws-small': (n % 4 === 1) || (n % 4 === 3)}" :style="`grid-row: ${ n-1 } / ${n+2}; grid-column: 1`">
						<template v-if="n % 4 === 2">
							{{ Math.floor((beginn + n * 5) / 60) }}:00
						</template>
					</span>
				</template>
			</div>
			<!-- Zeige auf der linken Seite die Zeitraster- und Pausenzeiten-Einträge an der Zeitachse -->
			<div class="svws-ui-stundenplan--zeitraster">
				<!-- Die Zeitraster-Einträge -->
				<div v-for="stunde in zeitrasterRange" :key="stunde"
					class="svws-ui-stundenplan--stunde text-center justify-center"
					:style="posZeitraster(undefined, stunde)">
					<div class="text-headline-sm"> {{ stunde }}. Stunde </div>
					<div v-for="zeiten in stundenplanManagerAktuell().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten" class="font-bold text-sm">
						{{ zeiten.replace(' Uhr', '') }}
					</div>
				</div>
				<!-- Die Pausenzeiten -->
				<template v-for="pause in pausenzeiten" :key="pause.id">
					<div class="svws-ui-stundenplan--pause text-sm font-bold text-center justify-center" :style="posPause(pause)">
						<div> {{ pause.bezeichnung }} </div>
						<div> {{ ((pause.ende || 0) - (pause.beginn || 0)) }} Minuten </div>
					</div>
				</template>
			</div>
			<!-- Zeige die Unterrichte und Pausenaufsichten des Stundenplans -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.a.id" class="svws-ui-stundenplan--zeitraster">
				<!-- Darstellung des Unterrichtes in dem Zeitraster -->
				<template v-for="stunde in zeitrasterRange" :key="stunde">
					<template v-if="(state.manager.stundenplanManagerGetByAbschnittAndDatumOrNull(props.abschnittId, wochentag.b) !== null) && stundenplanManager(wochentag.b).zeitrasterGetByWochentagAndStundeOrNull(wochentag.a.id, stunde)">
						<div class="svws-ui-stundenplan--stunde flex-row relative"
							:class="{
								'z-20 svws-ui-stundenplan--stunde--klausurplan-opacity': dragData && (dragData() !== undefined),
							}"
							:style="posZeitraster(wochentag.a, stunde)"
							@dragover="checkDropZoneZeitraster($event, stundenplanManager(wochentag.b).zeitrasterGetByWochentagAndStundeOrException(wochentag.a.id, stunde))"
							@dragleave="checkDropZoneZeitraster($event, undefined)"
							@drop="onDrop(stundenplanManager(wochentag.b).zeitrasterGetByWochentagAndStundeOrException(wochentag.a.id, stunde))">
							<div v-if="kurseGefiltert(wochentag.b, wochentag.a, stunde).size()" class="svws-ui-stundenplan--unterricht border-dashed border-ui-50 flex absolute inset-1 w-auto bg-ui-75 z-30 pointer-events-none">
								<div class="flex flex-col items-start justify-between mx-auto font-normal w-full opacity-75">
									<span class="text-button">{{ [...kurseGefiltert(wochentag.b, wochentag.a, stunde)].map(kurs => state.manager.kursKurzbezeichnungByKursklausur(kurs)).join(", ") }}</span>
									<span v-if="(dragData !== undefined) && (sumSchreiber(wochentag.b, wochentag.a, stunde) > 0)" class="inline-flex gap-0.5 text-button font-normal"><span class="icon i-ri-group-line" />{{ sumSchreiber(wochentag.b, wochentag.a, stunde) }}</span>
								</div>
							</div>
						</div>
					</template>
					<div v-else class="svws-ui-stundenplan--stunde flex-row relative bg-ui-warning-secondary text-center justify-center" :style="posZeitraster(wochentag.a, stunde)">Stundenplan fehlt</div>
				</template>
				<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
				<template v-for="pause in getPausenzeitenWochentag(wochentag.a)" :key="pause.id">
					<div class="svws-ui-stundenplan--pause" :style="posPause(pause)">
						<template v-for="pausenaufsicht in getPausenaufsichtenPausenzeit(pause)" :key="pausenaufsicht.id">
							<div class="svws-ui-stundenplan--pausen-aufsicht" :class="{'svws-lehrkraft': mode === 'lehrer'}">
								<div class="font-bold"> {{ pause.bezeichnung === 'Pause' && mode === 'lehrer' ? 'Aufsicht' : pause.bezeichnung }} </div>
								<div title="Aufsichtsbereiche"> {{ aufsichtsbereiche(pausenaufsicht) }}</div>
							</div>
						</template>
					</div>
				</template>
				<template v-for="item in state.manager.terminGruppierteUeberschneidungenGetMengeByDatumAndAbijahr(wochentag.b, state.zeigeAlleJahrgaenge ? null : state.jahrgangsdaten.abiturjahr)">
					<template v-for="(termin, index) in item" :key="termin.id">
						<div class="svws-ui-stundenplan--unterricht flex grow cursor-grab p-[2px] relative text-center z-10 border-transparent"
							:style="posKlausurtermin(termin) +
								(item.size() > 1
									? (index === 0
										? ('width: calc(' + (100 / item.size()) + '% + 2px);left: calc(' + (100 / item.size() * index) + '% - 1px);')
										: (index === item.size() - 1
											? ('width: calc(' + (100 / item.size()) + '% + 3px);left: calc(' + (100 / item.size() * index) + '% - 3px);')
											: ('width: calc(' + (100 / item.size()) + '% + 4px);left: calc(' + (100 / item.size() * index) + '% - 3px);')
										)
									)
									: ''
								)"
							:data="termin"
							:draggable="(termin.abiturjahrgang === state.jahrgangsdaten.abiturjahr) && hatKompetenzUpdate"
							@dragstart="onDrag(termin)"
							@dragend="onDrag(undefined)">
							<div class="bg-ui-caution text-ui-oncaution border w-full h-full rounded-lg overflow-hidden flex items-center justify-center relative group"
								:class="{
									'bg-ui-neutral border-ui-25': dragData !== undefined,
									'shadow-sm border-ui-10': dragData === undefined,
								}">
								<span class="icon i-ri-draggable absolute top-1 left-0 z-10 opacity-50 group-hover:opacity-100" v-if="(termin.abiturjahrgang === state.jahrgangsdaten.abiturjahr) && hatKompetenzUpdate" />
								<div class="absolute inset-0 flex w-full flex-col pointer-events-none opacity-80 bg-ui" :style="{background: (kursklausurMouseOver() !== undefined) && (kursklausurMouseOver()!.idTermin === termin.id) ? 'none' : getBgColors(termin)}" />
								<span v-if="state.zeigeAlleJahrgaenge" class="absolute top-1.5 right-1.5 z-10 font-bold text-sm opacity-50">{{ GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(termin.abiturjahrgang, abschnittState.auswahl.schuljahr, state.halbjahr.halbjahr)?.jahrgang }}</span>
								<svws-ui-tooltip :hover="false" position="right-start" class="!items-start h-full mr-auto" :indicator="false" :class="{'!cursor-grab': termin.abiturjahrgang === state.jahrgangsdaten.abiturjahr, '!cursor-pointer': termin.abiturjahrgang !== state.jahrgangsdaten.abiturjahr}">
									<span class="z-10 relative p-1 leading-tight cursor-pointer font-medium text-left mt-6 pb-0 hyphens-auto">
										<span class="line-clamp-4 text-ui" :class="dragData && (dragData() !== undefined) ? 'opacity-0' : ''">{{ terminBezeichnung(termin) }}</span>
									</span>
									<template #content>
										<s-gost-klausurplanung-termin :termin in-tooltip />
									</template>
								</svws-ui-tooltip>
								<span class="absolute bottom-0 left-0 py-1.5 pl-1.5 text-sm opacity-50 hidden group-hover:block pointer-events-none text-uistatic">Details</span>
							</div>
						</div>
					</template>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SGostKlausurplanungKalenderStundenplanAnsichtProps } from "./SGostKlausurplanungKalenderStundenplanAnsichtProps";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";
	import type { GostKlausurtermin } from "@core/core/data/gost/klausuren/GostKlausurtermin";
	import type { StundenplanPausenaufsicht } from "@core/core/data/stundenplan/StundenplanPausenaufsicht";
	import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
	import type { Wochentag } from "@core/core/types/Wochentag";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import type { List } from "@core/java/util/List";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useGostKlausurplanungState } from "@ui/states/GostKlausurplanungState";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag'];

	const props = withDefaults(defineProps<SGostKlausurplanungKalenderStundenplanAnsichtProps>(), {
		mode: 'schueler',
		ignoreEmpty: false,
		useDragAndDrop: false,
		dragData: () => undefined,
	});

	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();
	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);

	const stundenplanManagerAktuell = () => state.manager.stundenplanManagerGetByAbschnittAndKwOrException(props.abschnittId, props.kalenderwoche().jahr, props.kalenderwoche().kw);

	const stundenplanManager = (datum: string) => state.manager.stundenplanManagerGetByAbschnittAndDatumOrException(props.abschnittId, datum);

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const terminBezeichnung = (termin: GostKlausurtermin) => presenter.terminTitel(termin);

	const beginn = computed(() => {
		if (props.ignoreEmpty) {
			return stundenplanManagerAktuell().pausenzeitUndZeitrasterGetMinutenMinOhneLeere();
		}
		return stundenplanManagerAktuell().pausenzeitUndZeitrasterGetMinutenMin();
	});

	const ende = computed(() => {
		if (props.ignoreEmpty) {
			return stundenplanManagerAktuell().pausenzeitUndZeitrasterGetMinutenMaxOhneLeere();
		}
		return stundenplanManagerAktuell().pausenzeitUndZeitrasterGetMinutenMax();
	});

	const wochentagRange = computed(() => {
		return DateUtils.gibDatenDerWochentageOfJahrAndKalenderwoche(props.kalenderwoche().jahr, props.kalenderwoche().kw, stundenplanManagerAktuell().zeitrasterGetWochentageAlsEnumRange());
	});

	const zeitrasterRange = computed(() => {
		if (props.ignoreEmpty) {
			return stundenplanManagerAktuell().zeitrasterGetStundenRangeOhneLeere();
		}
		return stundenplanManagerAktuell().zeitrasterGetStundenRange();
	});

	const pausenzeiten = computed(() => {
		if (props.mode === 'schueler') {
			return stundenplanManagerAktuell().pausenzeitGetMengeBySchuelerIdAsList(props.id);
		}
		if (props.mode === 'lehrer') {
			return stundenplanManagerAktuell().pausenzeitGetMengeByLehrerIdAsList(props.id);
		}
		return stundenplanManagerAktuell().pausenzeitGetMengeByKlasseIdAsList(props.id);
	});

	const gesamtzeit = computed(() => {
		const tmp = ende.value - beginn.value;
		return tmp <= 0 ? 360 : tmp;
	});

	const zeitrasterRows = computed(() => {
		// Für alle 5 Minuten eine Grid Row
		return Math.round(gesamtzeit.value / 5);
	});

	function aufsichtsbereiche(pausenaufsicht: StundenplanPausenaufsicht): string {
		let result = "";
		for (const aufsichtsbereich of pausenaufsicht.bereiche) {
			const bereich = stundenplanManagerAktuell().aufsichtsbereichGetByIdOrException(aufsichtsbereich.idAufsichtsbereich);
			if (result !== "") {
				result += ",";
			}
			result += bereich.kuerzel;
		}
		return result;
	}


	function getPausenzeitenWochentag(wochentag: Wochentag): List<StundenplanPausenzeit> {
		if (props.mode === 'schueler') {
			return stundenplanManagerAktuell().pausenzeitGetMengeBySchuelerIdAndWochentagAsList(props.id, wochentag.id);
		}
		if (props.mode === 'lehrer') {
			return stundenplanManagerAktuell().pausenzeitGetMengeByLehrerIdAndWochentagAsList(props.id, wochentag.id);
		}
		return stundenplanManagerAktuell().pausenzeitGetMengeByKlasseIdAndWochentagAsList(props.id, wochentag.id);
	}

	function getPausenaufsichtenPausenzeit(pause: StundenplanPausenzeit): List<StundenplanPausenaufsicht> {
		if (props.mode === 'schueler') {
			return stundenplanManagerAktuell().pausenaufsichtGetMengeBySchuelerIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
		}
		if (props.mode === 'lehrer') {
			return stundenplanManagerAktuell().pausenaufsichtGetMengeByLehrerIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
		}
		return stundenplanManagerAktuell().pausenaufsichtGetMengeByKlasseIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
	}

	function posZeitraster(wochentag: Wochentag | undefined, stunde: number): string {
		let zbeginn = stundenplanManagerAktuell().zeitrasterGetMinutenMinDerStunde(stunde);
		let zende = stundenplanManagerAktuell().zeitrasterGetMinutenMaxDerStunde(stunde);
		if (wochentag !== undefined) {
			const z = stundenplanManagerAktuell().zeitrasterGetByWochentagAndStundeOrNull(wochentag.id, stunde);
			if (z !== null) {
				if (z.stundenbeginn !== null) {
					zbeginn = z.stundenbeginn;
				}
				if (z.stundenende !== null) {
					zende = z.stundenende;
				}
			}
		}
		const rowStart = (zbeginn - beginn.value) / 5;
		const rowEnd = (zende - beginn.value) / 5;
		return "grid-row-start: " + (rowStart + 1) + "; grid-row-end: " + (rowEnd + 1) + "; grid-column: 1;";
	}

	function posPause(pause: StundenplanPausenzeit): string {
		const pzeit = stundenplanManagerAktuell().pausenzeitGetByIdOrException(pause.id);
		let rowStart = 0;
		let rowEnd = 10;
		if ((pzeit.beginn !== null) && (pzeit.ende !== null)) {
			rowStart = (pzeit.beginn - beginn.value) / 5;
			rowEnd = (pzeit.ende - beginn.value) / 5;
		}
		return "grid-row-start: " + (rowStart + 1) + "; grid-row-end: " + (rowEnd + 1) + "; grid-column: 1;";
	}

	function posKlausurtermin(termin: GostKlausurtermin): string {
		let rowStart = 0;
		let rowEnd = 10;
		const terminBeginn = (termin.startzeit === null) ? -1 : state.manager.minKlausurstartzeitByTermin(termin, true);
		let terminEnde = -1;
		if (terminBeginn !== -1) {
			if (state.manager.schuelerklausurterminAktuellGetMengeByTermin(termin).isEmpty()) {
				let dauer = state.manager.maxKlausurdauerGetByTermin(termin, true);
				if (dauer === 0) {
					dauer = GostHalbjahr.fromIDorException(termin.halbjahr).istEinfuehrungsphase() ? 90 : 135;
				}
				terminEnde = Math.ceil((terminBeginn + dauer) / 5) * 5;
			} else {
				terminEnde = Math.ceil(state.manager.maxKlausurendzeitByTermin(termin, true) / 5) * 5;
			}
		}
		if ((terminBeginn !== -1) && (terminEnde !== -1)) {
			rowStart = (terminBeginn - beginn.value) / 5;
			rowEnd = (terminEnde - beginn.value) / 5;
		}
		// Workaround für zu kurze Terminzeiten
		rowEnd += rowEnd - rowStart > 9 ? 0 : (9 - (rowEnd - rowStart));
		return "grid-row-start: " + (rowStart + 1) + "; grid-row-end: " + (rowEnd + 1) + "; grid-column: 1;";
	}


	function isDraggable(): boolean {
		return props.useDragAndDrop && (props.dragData() === undefined);
	}

	function getBgColors(termin: GostKlausurtermin) {
		if (termin.abiturjahrgang !== state.jahrgangsdaten.abiturjahr) {
			return "#f2f4f5";
		}

		const colors = [...state.manager.kursklausurGetMengeByTermin(termin)].map(klausur => presenter.kursBadge(klausur).farbe ?? "#f2f4f5");

		let gradient = '';

		const gradientPositions = [0, 20, 80, 100];

		for (let i = 0; i < colors.length; i++) {
			if (i > 0) {
				gradient += `radial-gradient(farthest-side at ${gradientPositions[i % 2]}% ${gradientPositions[i % 3]}%, ${colors[i]}, ${colors[i].replace('1)', '0)')}),`;
			}
		}

		gradient += `linear-gradient(${colors[0]}, ${colors[0]})`;

		return gradient;
	}

</script>

<style scoped>

	@reference "../../../../../ui/src/assets/styles/index.css"

	.svws-ui-stundenplan--stunde--klausurplan-opacity {
		/* CSS_TODO */
		@apply bg-opacity-20;
	}

	.svws-ui-stundenplan--unterricht {
		@apply flex px-1 py-0 border-none;
	}

</style>
