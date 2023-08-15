<template>
	<div class="svws-ui-stundenplan">
		<div class="svws-ui-stundenplan--head">
			<div class="inline-flex gap-1 items-center pl-2">
				{{ }}
			</div>
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" @click="updateSelected(wochentag)" class="font-bold text-center inline-flex items-center w-full justify-center" :class="{'bg-slate-400': toRaw(selected)===wochentag}">
				<div> {{ wochentag.beschreibung }}<i-ri-delete-bin-line class="cursor-pointer" @click.stop="removeZeitraster([...manager().getListZeitrasterZuWochentag(wochentag)])" /></div>
			</div>
			<div @click="addWochentag"><i-ri-add-line class="cursor-pointer" /></div>
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
				<div v-for="stunde in zeitrasterRange" :key="stunde" @click="updateSelected(stunde)" class="svws-ui-stundenplan--stunde text-center justify-center" :style="posZeitraster(undefined, stunde)" :class="{'bg-slate-400': toRaw(selected)===stunde}">
					<div class="text-headline-sm">
						{{ stunde }}.&nbsp;Stunde <i-ri-delete-bin-line class="cursor-pointer" @click.stop="removeZeitraster([...manager().getListZeitrasterZuStunde(stunde)])" />
					</div>
					<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten" class="font-bold text-sm">
						{{ zeiten.replace(' Uhr', '') }}
					</div>
				</div>
				<template v-for="pause in manager().pausenzeitGetMengeAsList()" :key="pause">
					<div class="svws-ui-stundenplan--pause text-sm font-bold text-center justify-center" @click="updateSelected(pause)" :style="posPause(undefined, pause)" :class="{'bg-slate-400': toRaw(selected)===pause}">
						<div>{{ (pause.ende! - pause.beginn!) }} Minuten</div>
					</div>
				</template>
			</div>
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="svws-ui-stundenplan--zeitraster" :class="{'bg-slate-400': selected===wochentag}">
				<template v-for="zeitrasterEintrag in manager().getListZeitrasterZuWochentag(wochentag)" :key="zeitrasterEintrag">
					<div class="svws-ui-stundenplan--stunde" @click="updateSelected(zeitrasterEintrag)" :style="posZeitraster(wochentag, zeitrasterEintrag.unterrichtstunde)" :class="{'bg-slate-400': toRaw(selected)===zeitrasterEintrag || toRaw(selected) === zeitrasterEintrag.unterrichtstunde}">
						{{ zeitrasterEintrag.unterrichtstunde }}
						<div class="flex justify-between">
							<div class="flex content-start">
								{{ manager().zeitrasterGetByIdStringOfUhrzeitBeginn(zeitrasterEintrag.id) }} - {{ manager().zeitrasterGetByIdStringOfUhrzeitEnde(zeitrasterEintrag.id) }}
							</div> <i-ri-delete-bin-line class="cursor-pointer" @click.stop="removeZeitraster([zeitrasterEintrag])" />
						</div>
					</div>
				</template>
				<template v-for="pause in manager().pausenzeitGetMengeAsList()" :key="pause">
					<div class="svws-ui-stundenplan--pause" :style="posPause(wochentag, pause)" :class="{'bg-slate-400': selected===pause}" />
				</template>
			</div>
			<div />
		</div>
		<div @click="addStunde"><i-ri-add-line class="cursor-pointer" /></div>
	</div>
</template>
<script setup lang="ts">
	import type { StundenplanManager, StundenplanPausenzeit} from "@core";
	import type { StundenplanZeitraster, Wochentag } from "@core";
	import { computed, ref, toRaw } from "vue";

	const props = defineProps<{
		manager: () => StundenplanManager;
		patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
		addZeitraster: (wochentag: Wochentag | undefined, stunde : number | undefined) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

	const emit = defineEmits<{
		'selected:updated': [item: Wochentag|number|StundenplanZeitraster|StundenplanPausenzeit|undefined];
	}>();

	const selected = ref<Wochentag|number|StundenplanZeitraster|StundenplanPausenzeit|undefined>();

	function updateSelected(event: Wochentag|number|StundenplanZeitraster|StundenplanPausenzeit) {
		if (event === toRaw(selected.value))
			selected.value = undefined;
		else
			selected.value = event;
		emit('selected:updated', event);
	}

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

	const wochentagRange = computed(() => {
		return props.manager().zeitrasterGetWochentageAlsEnumRange();
	});

	const zeitrasterRange = computed(() => {
		// TODO warten bis die Methode implementiert ist und dann mit den beiden TODOs oben aktivieren
		// if (props.ignoreEmpty)
		// 	return props.manager().zeitrasterGetStundenRangeOhneLeere();
		return props.manager().zeitrasterGetStundenRange();
	})

	const gesamtzeit = computed(() => {
		const tmp = ende.value - beginn.value;
		return tmp <= 0 ? 360 : tmp;
	});

	const zeitrasterRows = computed(() => {
		// Für alle 5 Minuten eine Grid Row
		return Math.round(gesamtzeit.value / 5);
	});

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

	async function addWochentag() {
		await props.addZeitraster(props.manager().zeitrasterGetWochentagMaxEnum(), undefined);
	}

	async function addStunde() {
		await props.addZeitraster(undefined, props.manager().zeitrasterGetStundeMax());
	}

	async function addZeitrasterEintrag(wochentag: Wochentag, stunde : number) {
		await props.addZeitraster(wochentag, stunde);
	}

	async function patchAnfang(zeit: string) {
		console.log(zeit)
	}
	async function patchEnde(zeit: string) {
		console.log(zeit)
	}
	async function patchStunde(stunde: number) {
		console.log(stunde)
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
  grid-template-columns: 8rem repeat(auto-fit, minmax(8rem, 1fr)) 1rem;
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