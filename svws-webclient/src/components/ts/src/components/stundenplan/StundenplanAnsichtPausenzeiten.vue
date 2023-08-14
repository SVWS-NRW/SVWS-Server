<template>
	<div class="svws-ui-stundenplan">
		<div class="svws-ui-stundenplan--head">
			<div class="inline-flex gap-1 items-center pl-2" :class="{'opacity-50 font-normal print:invisible': wochentyp() === 0, 'font-bold text-headline-md inline-flex items-center gap-1 pb-0.5': wochentyp() !== 0}">
				{{ manager().stundenplanGetWochenTypAsString(wochentyp()) }}
			</div>
			<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id"
				class="font-bold text-center inline-flex items-center w-full justify-center">
				<div> {{ wochentage[wochentag.id] }}</div>
			</div>
		</div>
		<div class="svws-ui-stundenplan--body" :style="{'--zeitrasterRows': zeitrasterRows}">
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
			<div class="svws-ui-stundenplan--zeitraster">
				<div v-for="stunde in manager().zeitrasterGetStundenRange()" :key="stunde"
					class="svws-ui-stundenplan--stunde text-center justify-center"
					:style="posZeitraster(undefined, stunde)">
					<div class="text-headline-sm">{{ stunde }}. Stunde</div>
					<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten" class="font-bold text-sm">
						{{ zeiten.replace(' Uhr', '') }}
					</div>
				</div>
				<template v-for="pause in manager().pausenzeitGetMengeAsList()" :key="pause">
					<div class="svws-ui-stundenplan--pause text-sm font-bold text-center justify-center"
						:style="posPause(undefined, pause)">
						<div>{{ pause.bezeichnung }}</div>
						<div>{{ (pause.ende! - pause.beginn!) }} Minuten</div>
					</div>
				</template>
			</div>
			<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id"
				class="svws-ui-stundenplan--zeitraster">
				<template v-for="stunde in manager().zeitrasterGetStundenRange()" :key="stunde">
					<div class="svws-ui-stundenplan--stunde"
						:style="posZeitraster(wochentag, stunde)">
						<template v-if="(wochentyp() !== 0) && (manager().zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(wochentag, stunde, 0) || manager().zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(wochentag, stunde, wochentyp()))">
							<div v-if="(mode === 'lehrer' || mode === 'schueler') && manager().unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(wochentag, stunde, wochentyp(), true).size() > 1" class="svws-ui-stundenplan--unterricht--warning">
								<span class="text-sm font-bold"><i-ri-alert-line class="inline-block -mt-0.5" /> Mehrere Kurse parallel</span>
								<svws-ui-button type="secondary" size="small" @click="$event.target.parentNode.classList.toggle('svws-show')">Einblenden</svws-ui-button>
							</div>
							<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(wochentag, stunde, wochentyp(), true)"
								:key="unterricht.id"
								class="svws-ui-stundenplan--unterricht"
								:style="`background-color: ${getBgColor(manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id).split('-')[0])}`">
								<div v-if="unterricht.wochentyp !== 0" class="col-span-full text-sm mb-0.5 hidden"> {{ manager().stundenplanGetWochenTypAsString(unterricht.wochentyp) }}</div>
								<div class="font-bold" :class="{'col-span-2': mode === 'lehrer'}" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</div>
								<div v-if="mode !== 'lehrer'" title="Lehrkraft">{{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }}</div>
								<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }}</div>
							</div>
						</template>
						<template v-if="(wochentyp() === 0) && manager().zeitrasterHatUnterrichtMitWochentyp0ByWochentagAndStunde(wochentag, stunde)">
							<div v-if="(mode === 'lehrer' || mode === 'schueler') && manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, 0).size() > 1" class="svws-ui-stundenplan--unterricht--warning">
								<span class="text-sm font-bold"><i-ri-alert-line class="inline-block -mt-0.5" /> Mehrere Kurse parallel</span>
								<svws-ui-button type="secondary" size="small" @click="$event.target.parentNode.classList.toggle('svws-show')">Einblenden</svws-ui-button>
							</div>
							<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, 0)"
								:key="unterricht.id"
								class="svws-ui-stundenplan--unterricht"
								:style="`background-color: ${getBgColor(manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id).split('-')[0])}`">
								<div class="font-bold" :class="{'col-span-2': mode === 'lehrer'}" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</div>
								<div v-if="mode !== 'lehrer'" title="Lehrkraft">{{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }}</div>
								<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }}</div>
							</div>
						</template>
						<div v-else-if="(wochentyp() === 0) && manager().zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(wochentag, stunde)"
							class="svws-multiple" :style="`grid-template-columns: repeat(${manager().stundenplanGetWochenTypModell()}, minmax(0, 1fr)`">
							<div v-if="(mode === 'lehrer' || mode === 'schueler') && manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, 1).size() > 1" class="svws-ui-stundenplan--unterricht--warning col-span-full">
								<span class="text-sm font-bold"><i-ri-alert-line class="inline-block -mt-0.5" /> Mehrere Kurse parallel</span>
								<svws-ui-button type="secondary" size="small" @click="$event.target.parentNode.classList.toggle('svws-show')">Einblenden</svws-ui-button>
							</div>
							<template v-for="wt in manager().getWochenTypModell()" :key="wt">
								<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, wt)"
									:key="unterricht.id"
									class="svws-ui-stundenplan--unterricht svws-compact"
									:style="`background-color: ${getBgColor(manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id).split('-')[0])}; grid-column-start: ${wt}`">
									<div class="col-span-full text-sm mb-0.5">{{ manager().stundenplanGetWochenTypAsString(wt) }}</div>
									<div class="font-bold" :class="{'col-span-2': mode === 'lehrer'}" title="Unterricht"> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</div>
									<div v-if="mode !== 'lehrer'" title="Lehrkraft">{{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }}</div>
									<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }}</div>
								</div>
							</template>
						</div>
					</div>
				</template>
				<template v-for="pause in manager().pausenzeitGetMengeAsList()" :key="pause">
					<div class="svws-ui-stundenplan--pause"
						:style="posPause(wochentag, pause)" />
				</template>
				<template v-for="pausenaufsicht in pausenaufsichtGetMengeByWochentagOrEmptyList(wochentag)"
					:key="pausenaufsicht.id">
					<div class="svws-ui-stundenplan--pause" :style="posPausenaufsicht(pausenaufsicht)">
						<div class="svws-ui-stundenplan--pausen-aufsicht" :class="{'svws-lehrkraft': mode === 'lehrer'}">
							<div class="font-bold"> {{ pausenzeit(pausenaufsicht).bezeichnung === 'Pause' && mode === 'lehrer' ? 'Aufsicht' : pausenzeit(pausenaufsicht).bezeichnung }} </div>
							<div><span v-if="mode !== 'lehrer'" title="Lehrkraft">{{ manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel }}</span></div>
							<div title="Aufsichtsbereiche"> {{ aufsichtsbereiche(pausenaufsicht) }}</div>
						</div>
					</div>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ArrayList, type List, type Wochentag, type StundenplanPausenaufsicht, type StundenplanPausenzeit, ZulaessigesFach } from "@core";
	import { computed } from "vue";
	import { type StundenplanAnsichtProps } from "./StundenplanAnsichtProps";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = withDefaults(defineProps<StundenplanAnsichtProps>(), {
		mode: 'schueler',
		ignoreEmpty: false,
	});

	const beginn = computed(() => {
		// TODO Methoden im Kommentar noch fehlerhaft, verwenden sobald der Bug behoben ist...
		// if (props.ignoreEmpty)
		// 	return props.manager().pausenzeitUndZeitrasterGetMinutenMinOhneLeere();
		return props.manager().pausenzeitUndZeitrasterGetMinutenMin();
	});

	const ende = computed(() => {
		// TODO Methoden im Kommentar noch fehlerhaft, verwenden sobald der Bug behoben ist...
		// if (props.ignoreEmpty)
		// 	return props.manager().pausenzeitUndZeitrasterGetMinutenMaxOhneLeere();
		return props.manager().pausenzeitUndZeitrasterGetMinutenMax();
	});

	const gesamtzeit = computed(() => {
		const tmp = ende.value - beginn.value;
		return tmp <= 0 ? 360 : tmp;
	});

	const zeitrasterRows = computed(() => {
		// Für alle 5 Minuten eine Grid Row
		return Math.round(gesamtzeit.value / 5);
	});

	function pausenzeit(pausenaufsicht: StundenplanPausenaufsicht): StundenplanPausenzeit {
		return props.manager().pausenzeitGetByIdOrException(pausenaufsicht.idPausenzeit);
	}

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

	// TODO ersetzen mit entsprechender Methode im Manager
	function pausenaufsichtGetMengeByWochentagOrEmptyList(wochentag : Wochentag): List<StundenplanPausenaufsicht> {
		const allePausenaufsichten = props.manager().pausenaufsichtGetMengeAsList();
		const result = new ArrayList<StundenplanPausenaufsicht>();
		for (const p of allePausenaufsichten) {
			if (pausenzeit(p).wochentag === wochentag.id)
				result.add(p);
		}
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
		let top = 0;
		let height = 10;
		let rowStart = 0;
		let rowEnd = 10;
		if ((zbeginn === null) || (zende === null)) {
			const sb = props.manager().zeitrasterGetStundeMin();
			const se = props.manager().zeitrasterGetStundeMax();
			const stunden = se - sb + 1;
			top = ((stunde - sb) / stunden) * 100;
			height = 100 / stunden;
		} else {
			top = ((zbeginn - beginn.value) / gesamtzeit.value) * 100;
			height = ((zende - zbeginn) / gesamtzeit.value) * 100;
			rowStart = (zbeginn - beginn.value) / 5;
			rowEnd = (zende - beginn.value) / 5;
		}
		// return "top: " + top + "%; height: " + height + "%;";
		return "grid-row-start: " + (rowStart + 1) + "; grid-row-end: " + (rowEnd + 1) + "; grid-column: 1;";
	}

	function posPause(wochentag: Wochentag | undefined, pause: StundenplanPausenzeit): string {
		if (pause.wochentag !== (wochentag?.id || wochentag === undefined ? 1 : wochentag.id))
			return 'display: none;';
		const p = props.manager().pausenzeitGetByIdOrException(pause.id);
		let rowStart = 0;
		let rowEnd = 10;
		if ((p.beginn === null) || (p.ende === null)) {
			rowStart = 1;
		} else {
			rowStart = (p.beginn - beginn.value) / 5;
			rowEnd = (p.ende - beginn.value) / 5;
		}
		return "grid-row-start: " + (rowStart + 1) + "; grid-row-end: " + (rowEnd + 1) + "; grid-column: 1;";
	}

	function posPausenaufsicht(p : StundenplanPausenaufsicht) : string {
		const pzeit = pausenzeit(p);
		let top = 0;
		let height = 10;
		let rowStart = 0;
		let rowEnd = 10;
		if ((pzeit.beginn === null) || (pzeit.ende === null)) {
			const sb = props.manager().zeitrasterGetStundeMin();
			const se = props.manager().zeitrasterGetStundeMax();
			const stunden = se - sb + 1;
			top = 0;
			height = 100 / stunden;
		} else {
			top = ((pzeit.beginn - beginn.value) / gesamtzeit.value) * 100;
			height = ((pzeit.ende - pzeit.beginn) / gesamtzeit.value) * 100;
			rowStart = (pzeit.beginn - beginn.value) / 5;
			rowEnd = (pzeit.ende - beginn.value) / 5;
		}
		// return "top: " + top + "%; height: " + height + "%;";
		return "grid-row-start: " + (rowStart + 1) + "; grid-row-end: " + (rowEnd + 1) + "; grid-column: 1;";
	}

	function getBgColor(fach: string): string {
		return ZulaessigesFach.getByKuerzelASD(fach).getHMTLFarbeRGB();
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
