<template>
	<div class="svws-ui-stundenplan">
		<!-- Die Überschriften des Stundenplan -->
		<div class="svws-ui-stundenplan--head">
			<!-- Das Feld links in der Überschrift beinhaltet den ausgewählten Wochentyp -->
			<div class="inline-flex gap-1 items-center pl-2" :class="{'opacity-50 font-normal print:invisible': wochentyp() === 0, 'font-bold text-headline-md inline-flex items-center gap-1 pb-0.5': wochentyp() !== 0}">
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
			<div class="svws-ui-stundenplan--zeitraster svws-einheiten">
				<i-ri-time-line class="svws-time-icon" />
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
						<div> {{ (pause.ende! - pause.beginn!) }} Minuten </div>
					</div>
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
							<div v-if="(mode === 'lehrer' || mode === 'schueler') && getUnterrichtWochentypSpeziell(wochentag, stunde, wochentyp()).size() > 1" class="svws-ui-stundenplan--unterricht--warning">
								<span class="text-sm font-bold"><i-ri-alert-line class="inline-block -mt-0.5" /> Mehrere Kurse parallel </span>
								<svws-ui-button type="secondary" size="small" @click="$event.target.parentNode.classList.toggle('svws-show')"> Einblenden </svws-ui-button>
							</div>
							<div v-for="unterricht in getUnterrichtWochentypSpeziell(wochentag, stunde, wochentyp())" :key="unterricht.id"
								class="svws-ui-stundenplan--unterricht"
								:style="`background-color: ${getBgColor(manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id).split('-')[0])}`"
								:draggable="isDraggable()" @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)">
								<div v-if="unterricht.wochentyp !== 0" class="col-span-full text-sm mb-0.5 hidden"> {{ manager().stundenplanGetWochenTypAsString(unterricht.wochentyp) }} </div>
								<div class="font-bold" :class="{'col-span-2': mode === 'lehrer'}" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
								<div v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
								<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
							</div>
						</template>
						<template v-else>
							<!-- Allgemeiner Wochentyp ausgewählt -->
							<div v-if="(mode === 'lehrer' || mode === 'schueler') && getUnterrichtWochentypAllgemein(wochentag, stunde, 0).size() > 1" class="svws-ui-stundenplan--unterricht--warning">
								<span class="text-sm font-bold"><i-ri-alert-line class="inline-block -mt-0.5" /> Mehrere Kurse parallel </span>
								<svws-ui-button type="secondary" size="small" @click="$event.target.parentNode.classList.toggle('svws-show')"> Einblenden </svws-ui-button>
							</div>
							<!-- zunächst die Darstellung des allgemeinen Unterrichtes -->
							<div v-for="unterricht in getUnterrichtWochentypAllgemein(wochentag, stunde, 0)" :key="unterricht.id"
								class="svws-ui-stundenplan--unterricht"
								:style="`background-color: ${getBgColor(manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id).split('-')[0])}`"
								:draggable="isDraggable()" @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)">
								<div class="font-bold" :class="{'col-span-2': mode === 'lehrer'}" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
								<div v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
								<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
							</div>
							<!-- dann die Darstellung des speziellen Unterrichtes der Wochentypen -->
							<div v-if="manager().zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(wochentag, stunde)"
								class="svws-multiple" :style="`grid-template-columns: repeat(${manager().stundenplanGetWochenTypModell()}, minmax(0, 1fr)`">
								<template v-for="wt in manager().getWochenTypModell()" :key="wt">
									<div v-for="unterricht in getUnterrichtWochentypAllgemein(wochentag, stunde, wt)" :key="unterricht.id"
										class="svws-ui-stundenplan--unterricht svws-compact"
										:style="`background-color: ${getBgColor(manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id).split('-')[0])}; grid-column-start: ${wt}`"
										:draggable="isDraggable()" @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)">
										<div class="col-span-full text-sm mb-0.5"> {{ manager().stundenplanGetWochenTypAsString(wt) }} </div>
										<div class="font-bold" :class="{'col-span-2': mode === 'lehrer'}" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
										<div v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
										<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
									</div>
								</template>
							</div>
						</template>
					</div>
				</template>
				<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
				<template v-for="pause in getPausenzeitenWochentag(wochentag)" :key="pause">
					<div class="svws-ui-stundenplan--pause" :style="posPause(pause)" @dragover="checkDropZonePausenzeit($event, pause)" @drop="onDrop(pause)">
						<template v-for="pausenaufsicht in getPausenaufsichtenPausenzeit(pause)" :key="pausenaufsicht.id">
							<div class="svws-ui-stundenplan--pausen-aufsicht" :class="{'svws-lehrkraft': mode === 'lehrer'}"
								:draggable="isDraggable()" @dragstart="onDrag(pausenaufsicht)" @dragend="onDrag(undefined)">
								<div class="font-bold"> {{ pause.bezeichnung === 'Pause' && mode === 'lehrer' ? 'Aufsicht' : pause.bezeichnung }} </div>
								<div> <span v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel }} </span> </div>
								<div title="Aufsichtsbereiche"> {{ aufsichtsbereiche(pausenaufsicht) }}</div>
							</div>
						</template>
					</div>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ArrayList, type List, type Wochentag, StundenplanPausenaufsicht, type StundenplanPausenzeit, ZulaessigesFach, type StundenplanUnterricht, StundenplanKurs, StundenplanKlassenunterricht } from "@core";
	import { computed } from "vue";
	import { type StundenplanAnsichtDragData, type StundenplanAnsichtDropZone, type StundenplanAnsichtProps } from "./StundenplanAnsichtProps";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = withDefaults(defineProps<StundenplanAnsichtProps>(), {
		mode: 'schueler',
		ignoreEmpty: false,
		useDragAndDrop: false,
		dragAndDropData: () => undefined,
		onDrag: (data: StundenplanAnsichtDragData) => {},
		onDrop: (data: StundenplanAnsichtDropZone) => {},
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
		// TODO pausenzeitGetMengeByKlasseAsList, pausenzeitGetMengeByLehrerAsList, pausenzeitGetMengeBySchuelerAsList
		return props.manager().pausenzeitGetMengeAsList();
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

	function getUnterrichtWochentypSpeziell(wochentag: Wochentag, stunde: number, wochentyp: number) : List<StundenplanUnterricht> {
		// TODO Nutze Versionen für Filter mit Schüler-, Lehrer- und Klassen-ID
		return props.manager().unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(wochentag, stunde, wochentyp, true);
	}

	function getUnterrichtWochentypAllgemein(wochentag: Wochentag, stunde: number, wochentyp: number) : List<StundenplanUnterricht> {
		// TODO Nutze Versionen für Filter mit Schüler-, Lehrer- und Klassen-ID
		return props.manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, wochentyp);
	}

	function getPausenzeitenWochentag(wochentag: Wochentag) : List<StundenplanPausenzeit> {
		// TODO Nutze Versionen für Filter mit Schüler-, Lehrer- und Klassen-ID
		return props.manager().pausenzeitGetMengeByWochentagOrEmptyList(wochentag.id);
	}

	function getPausenaufsichtenPausenzeit(pause: StundenplanPausenzeit): List<StundenplanPausenaufsicht> {
		// TODO ersetzen mit entsprechender Methode pausenaufsichtGetMengeByPausenzeitOrEmptyList im Manager und nutze Versionen für Filter mit Schüler-, Lehrer- und Klassen-ID
		const allePausenaufsichten = props.manager().pausenaufsichtGetMengeAsList();
		const result = new ArrayList<StundenplanPausenaufsicht>();
		for (const p of allePausenaufsichten)
			if (p.idPausenzeit === pause.id)
				result.add(p);
		return result;
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

	function getBgColor(fach: string): string {
		return ZulaessigesFach.getByKuerzelASD(fach).getHMTLFarbeRGB();
	}

	function isDraggable() : boolean {
		return props.useDragAndDrop && (props.dragAndDropData() === undefined);
	}

	function isDropZoneZeitraster(wochentag: Wochentag, stunde: number) : boolean {
		const data = props.dragAndDropData();
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
		const data = props.dragAndDropData();
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
		grid-template-columns: 8rem repeat(auto-fit, minmax(8rem, 1fr));
	}

	.svws-ui-stundenplan--head {
		@apply bg-white dark:bg-black py-1 text-button;
		@apply h-[2.75rem] sticky -top-px z-10;
		@apply border border-black/25 dark:border-white/10;
	}

	.svws-ui-stundenplan--body {
		@apply flex-grow border-x border-black/25 dark:border-white/10 bg-white dark:bg-black -mt-px print:mt-0 relative;
	}

	.svws-ui-stundenplan--zeitraster {
		@apply grid grid-cols-1;
		grid-template-rows: repeat(var(--zeitrasterRows), minmax(0.6rem, 1fr));

		&.svws-einheiten {
			@apply print:hidden absolute h-full w-4 -ml-4 border-b border-r border-transparent;

			.svws-time-icon {
				@apply absolute -top-7 right-0 w-3.5 h-3.5 opacity-50;
			}
		}
	}

	.svws-ui-stundenplan--stunde,
	.svws-ui-stundenplan--pause {
		@apply bg-white dark:bg-black tabular-nums w-full h-full p-1 leading-tight flex flex-col overflow-y-auto;
		@apply border border-l-0 border-black/25 dark:border-white/10;

		.svws-ui-stundenplan--zeitraster:last-child & {
			@apply border-r-0;
		}

		.svws-multiple {
			@apply grid gap-1 h-full grid-flow-col;
			grid-template-columns: repeat(auto-fit, minmax(0, 1fr));
		}
	}

	.svws-ui-stundenplan--pause {
		@apply bg-light dark:bg-white/5 text-black/50 dark:text-white/50;
	}

	.svws-ui-stundenplan--unterricht,
	.svws-ui-stundenplan--pausen-aufsicht {
		@apply rounded grid grid-cols-3 gap-x-1 flex-grow w-full border border-black/10 px-2 py-1 content-center leading-none dark:text-black;

		&.svws-compact {
			@apply grid-cols-2;
		}

		+ .svws-ui-stundenplan--unterricht,
		+ .svws-ui-stundenplan--pausen-aufsicht {
			@apply rounded-t-none;

			.svws-multiple & {
				@apply rounded-t;
			}
		}

		&:not(:last-child) {
			@apply rounded-b-none;

			.svws-multiple & {
				@apply rounded-b;
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
			@apply bg-black/75 dark:bg-white/75 text-white dark:text-black;
		}
	}

	.svws-ui-stundenplan--einheit {
		@apply border-t border-black/50 dark:border-white/50 h-full w-1/2 pt-0.5 py-0.5 opacity-50 ml-auto;
		font-size: 0.66rem;
		writing-mode: vertical-lr;

		&.svws-small {
			@apply w-1/2 opacity-50;
		}

		&.svws-extended {
			@apply w-full;
		}
	}

</style>
