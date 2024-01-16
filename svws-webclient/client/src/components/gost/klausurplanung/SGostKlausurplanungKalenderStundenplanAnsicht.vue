<template>
	<div class="svws-ui-stundenplan svws-hat-zeitachse svws-zeitraster-5">
		<!-- Die Überschriften des Stundenplan -->
		<div class="svws-ui-stundenplan--head">
			<slot name="kwAuswahl">
				<div class="col-span-2 inline-flex gap-1 items-center justify-center print:pl-2 print:justify-start font-bold text-headline-md pb-0.5">
					KW {{ kwAuswahl?.kw || '–' }}
				</div>
			</slot>
			<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="font-bold my-auto w-full inline-flex items-center justify-center tabular-nums">
				<span class="opacity-50 uppercase mr-2">{{ wochentage[wochentag.id].slice(0, 2) }}</span> {{ DateUtils.gibDatumGermanFormat(manager().datumGetByKwzAndWochentag(kwAuswahl, wochentag)) }}
			</div>
		</div>
		<!-- Die Daten des Stundenplans -->
		<div class="svws-ui-stundenplan--body" :style="{'--zeitrasterRows': zeitrasterRows}">
			<!-- Die Zeitachse des Stundenplans auf der linken Seite -->
			<div class="svws-ui-stundenplan--zeitraster svws-zeitachse">
				<template v-for="n in zeitrasterRows" :key="n">
					<span v-if="n % 3 === 2" class="svws-ui-stundenplan--einheit" :class="{'svws-extended': n % 4 === 2, 'svws-small': n % 4 === 1 || n % 4 === 3}" :style="`grid-row: ${ n-1 } / ${n+2}; grid-column: 1`">
						<template v-if="n % 4 === 2">
							{{ beginn / 60 + Math.floor((n * 5) / 60) }}:00
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
					<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten" class="font-bold text-sm">
						{{ zeiten.replace(' Uhr', '') }}
					</div>
				</div>
				<!-- Die Pausenzeiten -->
				<template v-for="pause in pausenzeiten" :key="pause">
					<div class="svws-ui-stundenplan--pause text-sm font-bold text-center justify-center" :style="posPause(pause)">
						<div> {{ pause.bezeichnung }} </div>
						<div> {{ ((pause.ende || 0) - (pause.beginn || 0)) }} Minuten </div>
					</div>
				</template>
			</div>
			<!-- Zeige die Unterrichte und Pausenaufsichten des Stundenplans -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="svws-ui-stundenplan--zeitraster">
				<!-- Darstellung des Unterrichtes in dem Zeitraster -->
				<template v-for="stunde in zeitrasterRange" :key="stunde">
					<div class="svws-ui-stundenplan--stunde flex-row relative" :style="posZeitraster(wochentag, stunde)"
						@dragover="checkDropZoneZeitraster($event, manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde))" @drop="onDrop(manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde))">
						<div v-if="kurseGefiltert(wochentag, stunde).size()" class="svws-ui-stundenplan--unterricht border-dashed border-black/50 flex absolute inset-1 w-auto bg-white/80 z-20">
							<div class="flex flex-col items-start justify-between mx-auto font-normal w-full opacity-75">
								<span class="text-button">{{ [...kurseGefiltert(wochentag, stunde)].map(kurs => kursInfos(kurs)).join(", ") }}</span>
								<span v-if="dragData !== undefined && sumSchreiber(wochentag, stunde) > 0" class="inline-flex gap-0.5 text-button font-normal"><i-ri-group-line class="text-sm" />{{ sumSchreiber(wochentag, stunde) }}</span>
							</div>
						</div>
					</div>
				</template>
				<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
				<template v-for="pause in getPausenzeitenWochentag(wochentag)" :key="pause">
					<div class="svws-ui-stundenplan--pause" :style="posPause(pause)">
						<template v-for="pausenaufsicht in getPausenaufsichtenPausenzeit(pause)" :key="pausenaufsicht.id">
							<div class="svws-ui-stundenplan--pausen-aufsicht" :class="{'svws-lehrkraft': mode === 'lehrer'}">
								<div class="font-bold"> {{ pause.bezeichnung === 'Pause' && mode === 'lehrer' ? 'Aufsicht' : pause.bezeichnung }} </div>
								<div title="Aufsichtsbereiche"> {{ aufsichtsbereiche(pausenaufsicht) }}</div>
							</div>
						</template>
					</div>
				</template>
				<template v-for="termin in kMan().terminGetMengeByDatum(manager().datumGetByKwzAndWochentag(kwAuswahl, wochentag))" :key="termin.id">
					<div class="svws-ui-stundenplan--unterricht flex flex-grow cursor-grab p-[2px] text-center z-10 border-transparent"
						:style="posKlausurtermin(termin)"
						:data="termin"
						:draggable="true"
						@dragstart="onDrag(termin)"
						@dragend="onDrag(undefined)">
						<div class="bg-white dark:bg-black border w-full h-full rounded-md overflow-hidden flex items-center justify-center relative group"
							:class="dragData !== undefined ? 'bg-light border-black/25 dark:border-white/25' : 'shadow border-black/10 dark:border-white/10'">
							<i-ri-draggable class="absolute top-1 left-0 z-10 text-sm opacity-50 group-hover:opacity-100" />
							<div class="absolute inset-0 flex w-full flex-col pointer-events-none" :style="{background: getBgColors(termin.id)}" />
							<svws-ui-tooltip :hover="false" class="cursor-pointer">
								<span class="z-10 relative p-2 leading-tight font-medium">{{ termin.bezeichnung === null ? ([...kMan().kursklausurGetMengeByTerminid(termin.id)].map(k => props.kMan().kursKurzbezeichnungByKursklausur(k)).slice(0, 3).join(', ') + '...' || 'Neuer Termin') : 'Klausurtermin' }}</span>
								<template #content>
									<div class="-mx-3">
										<s-gost-klausurplanung-termin :termin="termin"
											:k-man="kMan"
											:map-schueler="mapSchueler">
											<template #datum><span /></template>
										</s-gost-klausurplanung-termin>
									</div>
								</template>
							</svws-ui-tooltip>
						</div>
					</div>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostKlausurtermin, Wochentag, StundenplanPausenaufsicht} from "@core";
	import {type List, type StundenplanPausenzeit, DeveloperNotificationException, DateUtils, ZulaessigesFach} from "@core";
	import { computed } from "vue";
	import type { SGostKlausurplanungKalenderStundenplanAnsichtProps } from "./SGostKlausurplanungKalenderStundenplanAnsichtProps";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = withDefaults(defineProps<SGostKlausurplanungKalenderStundenplanAnsichtProps>(), {
		mode: 'schueler',
		ignoreEmpty: false,
		useDragAndDrop: false,
		dragData: () => undefined,
	});

	const kursInfos = (idKurs: number) => {
		return props.kMan().getKursManager().get(idKurs)?.kuerzel;
		//return test?.kuerzel || '' /* + " " + props.kMan().getKursklausurByTerminKurs(dragTermin.value.id, idKurs)!.schuelerIds.size() + "/??"*/;
	}

	const beginn = computed(() => {
		if (props.ignoreEmpty)
			return props.manager().pausenzeitUndZeitrasterGetMinutenMinOhneLeere();
		return props.manager().pausenzeitUndZeitrasterGetMinutenMin();
	});

	const ende = computed(() => {
		if (props.ignoreEmpty)
			return props.manager().pausenzeitUndZeitrasterGetMinutenMaxOhneLeere();
		return props.manager().pausenzeitUndZeitrasterGetMinutenMax();
	});

	const wochentagRange = computed(() => {
		return props.manager().zeitrasterGetWochentageAlsEnumRange();
	});

	const zeitrasterRange = computed(() => {
		if (props.ignoreEmpty)
			return props.manager().zeitrasterGetStundenRangeOhneLeere();
		return props.manager().zeitrasterGetStundenRange();
	});

	const pausenzeiten = computed(() => {
		if (props.mode === 'schueler')
			return props.manager().pausenzeitGetMengeBySchuelerIdAsList(props.id);
		if (props.mode === 'lehrer')
			return props.manager().pausenzeitGetMengeByLehrerIdAsList(props.id);
		if (props.mode === 'klasse')
			return props.manager().pausenzeitGetMengeByKlasseIdAsList(props.id);
		throw new DeveloperNotificationException("const pausenzeiten: Unbekannter Mode " + props.mode);
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
		for (const idBereich of pausenaufsicht.bereiche) {
			const bereich = props.manager().aufsichtsbereichGetByIdOrException(idBereich);
			if (result !== "")
				result += ",";
			result += bereich.kuerzel;
		}
		return result;
	}


	function getPausenzeitenWochentag(wochentag: Wochentag) : List<StundenplanPausenzeit> {
		if (props.mode === 'schueler')
			return props.manager().pausenzeitGetMengeBySchuelerIdAndWochentagAsList(props.id, wochentag.id);
		if (props.mode === 'lehrer')
			return props.manager().pausenzeitGetMengeByLehrerIdAndWochentagAsList(props.id, wochentag.id);
		if (props.mode === 'klasse')
			return props.manager().pausenzeitGetMengeByKlasseIdAndWochentagAsList(props.id, wochentag.id);
		throw new DeveloperNotificationException("function getPausenzeitenWochentag: Unbekannter Mode " + props.mode);
	}

	function getPausenaufsichtenPausenzeit(pause: StundenplanPausenzeit): List<StundenplanPausenaufsicht> {
		// TODO Pausenaufsicht zusätzlich pro "wochentyp" UND "inklWoche0=true"
		if (props.mode === 'schueler')
			return props.manager().pausenaufsichtGetMengeBySchuelerIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
		if (props.mode === 'lehrer')
			return props.manager().pausenaufsichtGetMengeByLehrerIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
		if (props.mode === 'klasse')
			return props.manager().pausenaufsichtGetMengeByKlasseIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
		throw new DeveloperNotificationException("function getPausenaufsichtenPausenzeit: Unbekannter Mode " + props.mode);
	}

	function posZeitraster(wochentag: Wochentag | undefined, stunde: number): string {
		let zbeginn =  props.manager().zeitrasterGetMinutenMinDerStunde(stunde);
		let zende =  props.manager().zeitrasterGetMinutenMaxDerStunde(stunde);
		if (wochentag !== undefined) {
			const z = props.manager().zeitrasterGetByWochentagAndStundeOrNull(wochentag.id, stunde);
			if (z !== null) {
				if (z.stundenbeginn !== null)
					zbeginn = z.stundenbeginn;
				if (z.stundenende !== null)
					zende = z.stundenende;
			}
		}
		let rowStart = 0;
		let rowEnd = 10;
		if ((zbeginn !== null) && (zende !== null)) {
			rowStart = (zbeginn - beginn.value) / 5;
			rowEnd = (zende - beginn.value) / 5;
		}
		return "grid-row-start: " + (rowStart + 1) + "; grid-row-end: " + (rowEnd + 1) + "; grid-column: 1;";
	}

	function posPause(pause: StundenplanPausenzeit): string {
		const pzeit = props.manager().pausenzeitGetByIdOrException(pause.id);
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
		const terminBeginn = props.kMan().minKursklausurstartzeitByTerminid(termin.id);
		const terminEnde = props.kMan().maxKursklausurendzeitByTerminid(termin.id);
		if ((terminBeginn !== -1) && (terminEnde !== -1)) {
			rowStart = (terminBeginn - beginn.value) / 5;
			rowEnd = (terminEnde - beginn.value) / 5;
		}
		return "grid-row-start: " + (rowStart + 1) + "; grid-row-end: " + (rowEnd + 1) + "; grid-column: 1;";
	}


	function isDraggable() : boolean {
		return props.useDragAndDrop && (props.dragData() === undefined);
	}

	function getBgColors(termin: number | null) {
		const klausuren = [...props.kMan().kursklausurGetMengeByTerminid(termin)].map(k => props.kMan().kursKurzbezeichnungByKursklausur(k).split('-')[0])
		const colors = klausuren.map(kuerzel => ZulaessigesFach.getByKuerzelASD(kuerzel || null).getHMTLFarbeRGBA(1.0));

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
