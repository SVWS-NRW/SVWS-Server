<template>
	<div class="svws-ui-stundenplan" :class="`${showZeitachse ? 'svws-hat-zeitachse' : 'svws-ohne-zeitachse'} svws-zeitraster-${zeitrasterSteps}`">
		<!-- Die Überschriften des Stundenplan -->
		<div class="svws-ui-stundenplan--head">
			<i-ri-time-line class="svws-time-icon print:hidden" v-if="showZeitachse" />
			<!-- Das Feld links in der Überschrift beinhaltet den ausgewählten Wochentyp -->
			<div class="inline-flex gap-1 items-center justify-center print:pl-2 print:justify-start" :class="{'opacity-50 print:invisible': wochentyp() === 0, 'font-bold text-headline-md pb-0.5': wochentyp() !== 0}">
				{{ manager().stundenplanGetWochenTypAsString(wochentyp()) }}
			</div>
			<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="font-bold text-center inline-flex items-center w-full justify-center">
				<div> {{ wochentage[wochentag.id] }} </div>
			</div>
		</div>
		<!-- Die Daten des Stundenplans -->
		<div class="svws-ui-stundenplan--body" :style="{'--zeitrasterRows': zeitrasterRows}">
			<!-- Die Zeitachse des Stundenplans auf der linken Seite -->
			<div class="svws-ui-stundenplan--zeitraster svws-zeitachse" v-if="showZeitachse">
				<!--TODO: Zeitraster dynamisch mit zeitrasterSteps darstellen-->
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
				<template v-if="showZeitachse">
					<!--TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist-->
					<template v-for="pause in pausenzeiten" :key="pause">
						<div class="svws-ui-stundenplan--pause text-sm text-center justify-center" :style="posPause(pause)">
							<div> {{ pause.bezeichnung }} </div>
							<div> {{ (pause.ende! - pause.beginn!) }} Minuten </div>
						</div>
					</template>
				</template>
			</div>
			<!-- Zeige die Unterrichte und Pausenaufsichten des Stundenplans -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="svws-ui-stundenplan--zeitraster">
				<!-- Darstellung des Unterrichtes in dem Zeitraster -->
				<template v-for="stunde in zeitrasterRange" :key="stunde">
					<div class="svws-ui-stundenplan--stunde" :style="posZeitraster(wochentag, stunde)"
						@dragover="checkDropZoneZeitraster($event, wochentag, stunde)" @drop="onDrop(manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde))">
						<!-- Passe die Darstellung je nach ausgewähltem Wochentyp an... -->
						<template v-if="(wochentyp() !== 0)">
							<!-- Spezieller Wochentyp ausgewählt: Darstellung des allgemeinen und des speziellen Unterrichts eines Wochentyps -->
							<div v-for="unterricht in getUnterrichtWochentypSpeziell(wochentag, stunde, wochentyp())" :key="unterricht.id"
								class="svws-ui-stundenplan--unterricht"
								:class="{'flex-grow': getUnterrichtWochentypSpeziell(wochentag, stunde, wochentyp()).size() === 1}"
								:style="`background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)}`"
								:draggable="isDraggable()" @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)">
								<div v-if="unterricht.wochentyp !== 0" class="col-span-full text-sm mb-0.5 hidden"> {{ manager().stundenplanGetWochenTypAsString(unterricht.wochentyp) }} </div>
								<div class="font-bold" :class="`${mode === 'lehrer' ? 'col-span-3' : 'col-span-2'}`" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
								<div v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
								<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
							</div>
						</template>
						<template v-else>
							<!-- Allgemeiner Wochentyp ausgewählt -->
							<!-- zunächst die Darstellung des allgemeinen Unterrichtes -->
							<div v-for="unterricht in getUnterrichtWochentypAllgemein(wochentag, stunde, 0)" :key="unterricht.id"
								class="svws-ui-stundenplan--unterricht"
								:class="{'flex-grow': getUnterrichtWochentypAllgemein(wochentag, stunde, 0).size() === 1}"
								:style="`background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)}`"
								:draggable="isDraggable()" @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)">
								<div class="font-bold" :class="`${mode === 'lehrer' ? 'col-span-3' : 'col-span-2'}`" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
								<div v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
								<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
							</div>
							<!-- dann die Darstellung des speziellen Unterrichtes der Wochentypen -->
							<div v-if="manager().zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(wochentag, stunde)"
								class="svws-multiple" :style="`grid-template-columns: repeat(${manager().stundenplanGetWochenTypModell()}, minmax(0, 1fr)`">
								<template v-for="wt in manager().getWochenTypModell()" :key="wt">
									<div class="border-r border-black/25 p-1 last:border-r-0 flex flex-col" :style="`grid-column-start: ${wt}`">
										<template v-if="getUnterrichtWochentypAllgemein(wochentag, stunde, wt).size() > 0">
											<div class="col-span-full text-sm font-bold text-center mb-1 py-1 print:mb-0"> {{ manager().stundenplanGetWochenTypAsString(wt) }}</div>
										</template>
										<div v-for="unterricht in getUnterrichtWochentypAllgemein(wochentag, stunde, wt)" :key="unterricht.id"
											class="svws-ui-stundenplan--unterricht svws-compact"
											:class="{'flex-grow': getUnterrichtWochentypAllgemein(wochentag, stunde, wt).size() === 1}"
											:style="`background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)};`"
											:draggable="isDraggable()" @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)">
											<div class="font-bold col-span-full" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
											<div v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
											<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
										</div>
									</div>
								</template>
							</div>
						</template>
					</div>
				</template>
				<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
				<template v-if="showZeitachse && (modePausenaufsichten !== 'aus')">
					<!--TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist-->
					<!-- TODO Modi 'normal', 'kurz, 'tooltip' -->
					<template v-for="pause in getPausenzeitenWochentag(wochentag)" :key="pause">
						<div class="svws-ui-stundenplan--pause" :style="posPause(pause)" @dragover="checkDropZonePausenzeit($event, pause)" @drop="onDrop(pause)">
							<template v-for="pausenaufsicht in getPausenaufsichtenPausenzeit(pause)" :key="pausenaufsicht.id">
								<div class="svws-ui-stundenplan--pausen-aufsicht flex-grow" :class="{'svws-lehrkraft': mode === 'lehrer'}"
									:draggable="isDraggable()" @dragstart="onDrag(pausenaufsicht)" @dragend="onDrag(undefined)">
									<div class="font-bold"> {{ pause.bezeichnung === 'Pause' && mode === 'lehrer' ? 'Aufsicht' : pause.bezeichnung }} </div>
									<div> <span v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel }} </span> </div>
									<div title="Aufsichtsbereiche"> {{ aufsichtsbereiche(pausenaufsicht) }}</div>
								</div>
							</template>
						</div>
					</template>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Wochentag} from "@core";
	import { type List, StundenplanPausenaufsicht, type StundenplanPausenzeit, ZulaessigesFach, type StundenplanUnterricht, StundenplanKurs, StundenplanKlassenunterricht, DeveloperNotificationException } from "@core";
	import { computed } from "vue";
	import { type StundenplanAnsichtDragData, type StundenplanAnsichtDropZone, type StundenplanAnsichtProps } from "./StundenplanAnsichtProps";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = withDefaults(defineProps<StundenplanAnsichtProps>(), {
		mode: 'schueler',
		modePausenaufsichten: 'normal',
		showZeitachse: true,
		zeitrasterSteps: 5,
		ignoreEmpty: false,
		useDragAndDrop: false,
		dragData: () => undefined,
		onDrag: (data: StundenplanAnsichtDragData) => {},
		onDrop: (zone: StundenplanAnsichtDropZone) => {},
	});

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
		if(!props.showZeitachse) {
			// TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist
			// props.manager().pausenzeitGetMengeByWochentagOrEmptyList(1).size() || 0
			return zeitrasterRange.value.length;
		}

		// Für alle x Minuten eine Grid Row (zeitrasterSteps)
		return Math.round(gesamtzeit.value / props.zeitrasterSteps);
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

	function getUnterrichtWochentypSpeziell(wochentag: Wochentag, stunde: number, wochentyp: number) : List<StundenplanUnterricht> {
		if (props.mode === 'schueler')
			return props.manager().unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, true);
		if (props.mode === 'lehrer')
			return props.manager().unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, true);
		if (props.mode === 'klasse')
			return props.manager().unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, true);
		throw new DeveloperNotificationException("function getUnterrichtWochentypSpeziell: Unbekannter Mode " + props.mode);
	}

	function getUnterrichtWochentypAllgemein(wochentag: Wochentag, stunde: number, wochentyp: number) : List<StundenplanUnterricht> {
		if (props.mode === 'schueler')
			return props.manager().unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, false);
		if (props.mode === 'lehrer')
			return props.manager().unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, false);
		if (props.mode === 'klasse')
			return props.manager().unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, false);
		throw new DeveloperNotificationException("function getUnterrichtWochentypAllgemein: Unbekannter Mode " + props.mode);
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
			rowStart = (zbeginn - beginn.value) / props.zeitrasterSteps;
			rowEnd = (zende - beginn.value) / props.zeitrasterSteps;
		}
		if (!props.showZeitachse) {
			rowStart = stunde - 1;
			rowEnd = stunde;
		}

		return "grid-row-start: " + (Math.round(rowStart) + 1) + "; grid-row-end: " + (Math.round(rowEnd) + 1) + "; grid-column: 1;";
	}

	function posPause(pause: StundenplanPausenzeit): string {
		const pzeit = props.manager().pausenzeitGetByIdOrException(pause.id);
		let rowStart = 0;
		let rowEnd = 10;
		if ((pzeit.beginn !== null) && (pzeit.ende !== null)) {
			rowStart = (pzeit.beginn - beginn.value) / props.zeitrasterSteps;
			rowEnd = (pzeit.ende - beginn.value) / props.zeitrasterSteps;
		}
		return "grid-row-start: " + (Math.round(rowStart) + 1) + "; grid-row-end: " + (Math.round(rowEnd) + 1) + "; grid-column: 1;";
	}

	function getBgColor(fach: string): string {
		return ZulaessigesFach.getByKuerzelASD(fach).getHMTLFarbeRGB();
	}

	function isDraggable() : boolean {
		return props.useDragAndDrop && (props.dragData() === undefined);
	}

	function isDropZoneZeitraster(wochentag: Wochentag, stunde: number) : boolean {
		const data = props.dragData();
		if ((data === undefined) || (data instanceof StundenplanPausenaufsicht))
			return false;
		if ((data instanceof StundenplanKurs) || (data instanceof StundenplanKlassenunterricht))
			return true;
		const z = props.manager().zeitrasterGetByIdOrException(data.idZeitraster);
		return !((z.wochentag === wochentag.id) && (z.unterrichtstunde === stunde));
	}

	function checkDropZoneZeitraster(event: DragEvent, wochentag: Wochentag, stunde: number) : void {
		if (isDropZoneZeitraster(wochentag, stunde))
			event.preventDefault();
	}

	function isDropZonePausenzeit(pause : StundenplanPausenzeit) : boolean {
		const data = props.dragData();
		if ((data === undefined) || (!(data instanceof StundenplanPausenaufsicht)))
			return false;
		if (pause.id === data.idPausenzeit)
			return false;
		return true;
	}

	function checkDropZonePausenzeit(event: DragEvent, pause : StundenplanPausenzeit) {
		if (isDropZonePausenzeit(pause))
			event.preventDefault();
	}

</script>

<style lang="postcss">

	.svws-ui-stundenplan {
		@apply flex flex-col h-full min-w-max flex-grow;
		--zeitrasterRows: 0;
	}

	.svws-ui-stundenplan--head,
	.svws-ui-stundenplan--body {
		@apply grid grid-flow-col;
		grid-template-columns: 8rem repeat(auto-fit, minmax(10rem, 1fr));

		.svws-hat-zeitachse & {
			@media screen {
				grid-template-columns: 2rem 8rem repeat(auto-fit, minmax(10rem, 1fr));
			}
		}
	}

	.svws-ui-stundenplan--head {
		@apply bg-white dark:bg-black py-1 text-button;
		@apply h-[2.75rem] sticky -top-px z-10;
		@apply border border-black/25 dark:border-white/10;

		.svws-hat-zeitachse & {
			@media screen {
				@apply border-l-0;
			}
		}

		.svws-time-icon {
			@apply opacity-25 text-center self-center w-full;
		}
	}

	.svws-ui-stundenplan--body {
		@apply flex-grow border-x border-black/25 dark:border-white/10 bg-white dark:bg-black -mt-px print:mt-0 relative;

		.svws-hat-zeitachse & {
			@media screen {
				@apply border-l-0;
			}
		}
	}

	.svws-ui-stundenplan--zeitraster {
		@apply grid grid-cols-1;
		grid-template-rows: repeat(var(--zeitrasterRows), minmax(0.6rem, 1fr));

	  .svws-zeitraster-1 & {
        grid-template-rows: repeat(var(--zeitrasterRows), minmax(0.1rem, 1fr));
	  }

		&.svws-zeitachse {
			@apply print:hidden h-full border-b-0 border-r border-black/25 dark:border-white/25;
		}
	}

	.svws-ui-stundenplan--stunde,
	.svws-ui-stundenplan--pause {
		@apply bg-white dark:bg-black tabular-nums w-full h-full p-1 leading-tight flex flex-col overflow-hidden;
		@apply border border-l-0 border-black/25 dark:border-white/10;

		.svws-ui-stundenplan--zeitraster:last-child & {
			@apply border-r-0;
		}

	  .svws-ohne-zeitachse & {
		+ .svws-ui-stundenplan--stunde {
		  @apply border-t-0;
		}
	  }

		.svws-multiple {
			@apply grid h-full grid-flow-col -m-1 flex-grow;
			grid-template-columns: repeat(auto-fit, minmax(0, 1fr));
		}

    .svws-ui-stundenplan--mode-planung & {
      &:hover,
      &:focus-visible {
        .svws-ui-stundenplan--unterricht,
        .svws-ui-stundenplan--pausen-aufsicht,
        &.svws-label {
          @apply bg-light dark:bg-white/5;
        }

      }

      &.svws-selected-stunde {
        @apply text-svws;
      }
    }
	}

	.svws-ui-stundenplan--pause {
		@apply border-y-0;

    .svws-ui-stundenplan--mode-planung &:not(.svws-no-hover) {
      &:hover,
      &:focus-visible {
          @apply bg-light dark:bg-white/5;
      }
    }
	}

	.svws-ui-stundenplan--unterricht,
	.svws-ui-stundenplan--pausen-aufsicht {
		@apply rounded grid grid-cols-4 gap-x-2 w-full border border-black/10 px-2 py-2 content-center leading-none dark:text-black;

		&.svws-compact {
			@apply grid-cols-1 py-1;
		}

    .svws-ui-stundenplan--mode-planung & {
      @apply flex flex-col gap-1 items-center flex-grow justify-center;
    }

		+ .svws-ui-stundenplan--unterricht,
		+ .svws-ui-stundenplan--pausen-aufsicht {
			@apply rounded-t-none;
		}

		&:not(:last-child) {
			@apply rounded-b-none;
		}
	}

  .svws-ui-stundenplan--mode-planung {
    .svws-wochentag-label {
      @apply font-bold text-center inline-flex items-center w-full justify-center cursor-pointer;

      &:hover,
      &:focus-visible {
        span {
          @apply bg-light dark:bg-white/5;
        }
      }

      &.svws-selected {
        span {
          @apply bg-svws/5 text-svws font-bold border-svws/25;
        }
      }
    }

    .svws-ui-stundenplan--zeitraster.svws-selected,
    .svws-ui-stundenplan--stunde.svws-selected,
    .svws-ui-stundenplan--pause.svws-selected {
      .svws-ui-stundenplan--unterricht,
      .svws-ui-stundenplan--pausen-aufsicht {
        @apply bg-svws/5 text-svws font-bold;
      }
    }
  }

	.svws-ui-stundenplan--unterricht--warning {
		@apply flex flex-col gap-2 items-center justify-center text-center bg-error text-white rounded p-2 flex-grow print:hidden;

		~ .svws-ui-stundenplan--unterricht {
			@apply flex-grow-0 min-h-[2rem] hidden print:grid;

			&.svws-compact {
				@apply min-h-[5rem];
			}
		}

		&.svws-show {
			@apply hidden;

			~ .svws-ui-stundenplan--unterricht {
				@apply grid;
			}
		}
	}

	.svws-ui-stundenplan--pausen-aufsicht {
		&.svws-lehrkraft {
			@apply bg-white dark:bg-black border-black/25 dark:border-white/25;
		}
	}

	.svws-ui-stundenplan--einheit {
		@apply border-t border-black/50 dark:border-white/50 w-1/2 pr-1 opacity-50 ml-auto text-right h-px;
		font-size: 0.66rem;
	  	letter-spacing: -0.08em;

		&.svws-small {
			@apply w-1/2;
		}

		&.svws-extended {
			@apply w-full opacity-50;
		}
	}

</style>
