<template>
	<div class="svws-ui-stundenplan svws-ui-stundenplan--mode-planung" :class="`${showZeitachse ? 'svws-hat-zeitachse' : 'svws-ohne-zeitachse'}`">
		<div class="svws-ui-stundenplan--head">
			<i-ri-time-line class="svws-time-icon print:hidden" v-if="showZeitachse" />
			<!-- Das Feld links in der Überschrift beinhaltet den ausgewählten Wochentyp -->
			<div class="inline-flex gap-1 items-center justify-center print:pl-2 print:justify-start opacity-50 text-sm font-bold pb-0.5" />
			<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" @click="updateSelected(wochentag)" class="svws-wochentag-label" :class="{'svws-selected': toRaw(selected)===wochentag}">
				<span class="px-2 py-1 rounded"> {{ wochentag.beschreibung }}</span>
			</div>
		</div>
		<!-- Die Daten des Stundenplans -->
		<div class="svws-ui-stundenplan--body" :style="{'--zeitrasterRows': zeitrasterRows}">
			<!-- Die Zeitachse des Stundenplans auf der linken Seite -->
			<div class="svws-ui-stundenplan--zeitraster svws-zeitachse" v-if="showZeitachse">
				<template v-for="n in zeitrasterRows" :key="n">
					<span v-if="n % 3 === 2" class="svws-ui-stundenplan--einheit" :class="{'svws-extended': n % 4 === 2, 'svws-small': n % 4 === 1 || n % 4 === 3}" :style="`grid-row: ${ n-1 } / ${n+2}; grid-column: 1`">
						<template v-if="n % 4 === 2">
							{{ beginn / 60 + Math.floor((n * 5) / 60) }}:00
						</template>
					</span>
				</template>
			</div>
			<div class="svws-ui-stundenplan--zeitraster">
				<!-- Die Zeitraster-Einträge -->
				<div v-for="stunde in zeitrasterRange" :key="stunde" @click="updateSelected(stunde)" class="svws-ui-stundenplan--stunde svws-label text-center justify-center cursor-pointer" :style="posZeitraster(undefined, stunde)" :class="{'svws-selected-stunde': toRaw(selected)===stunde}">
					<div class="text-headline-sm">
						{{ stunde }}.&nbsp;Stunde
					</div>
					<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten" class="font-bold text-sm">
						{{ zeiten.replace(' Uhr', '') }}
					</div>
				</div>
				<!-- Die Pausenzeiten -->
				<template v-for="pause in manager().pausenzeitGetMengeAsList()" :key="pause.id">
					<div class="svws-ui-stundenplan--pause text-sm text-center justify-center svws-no-hover" :style="posPause(undefined, pause)" :class="{'svws-selected': toRaw(selected)===pause}">
						<div>{{ ((pause.ende || 0) - (pause.beginn || 0)) }} Minuten</div>
					</div>
				</template>
			</div>
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="svws-ui-stundenplan--zeitraster" :class="{'svws-selected': toRaw(selected)===wochentag}">
				<template v-for="zeitrasterEintrag in manager().getListZeitrasterZuWochentag(wochentag)" :key="zeitrasterEintrag.id">
					<div class="svws-ui-stundenplan--stunde cursor-pointer" @click="updateSelected(zeitrasterEintrag)" :style="posZeitraster(wochentag, zeitrasterEintrag.unterrichtstunde)" :class="{'svws-selected': toRaw(selected)===zeitrasterEintrag || toRaw(selected) === zeitrasterEintrag.unterrichtstunde}">
						<div class="svws-ui-stundenplan--unterricht">
							<span>{{ zeitrasterEintrag.unterrichtstunde }}</span>
							<div class="flex content-start">
								{{ manager().zeitrasterGetByIdStringOfUhrzeitBeginn(zeitrasterEintrag.id) }}–{{ manager().zeitrasterGetByIdStringOfUhrzeitEnde(zeitrasterEintrag.id) }}
							</div>
						</div>
					</div>
				</template>
				<template v-for="pause in manager().pausenzeitGetMengeByWochentagOrEmptyList(wochentag.id)" :key="pause.id">
					<div class="svws-ui-stundenplan--pause cursor-pointer" @click="updateSelected(pause)" :style="posPause(wochentag, pause)" :class="{'svws-selected': toRaw(selected)===pause}">
						<div v-if="toRaw(selected)===pause" class="svws-ui-stundenplan--pausen-aufsicht">
							<i-ri-cup-line />
						</div>
					</div>
				</template>
			</div>
		</div>
	</div>
	<aside>
		<div class="sticky top-8 flex flex-col gap-5">
			<div class="flex gap-1 flex-wrap">
				<svws-ui-button type="secondary" @click="addStunde">
					<i-ri-calendar-event-line /><i-ri-add-line class="-ml-1" />{{ manager().zeitrasterGetStundeMax() + 1 }}. Stunde
				</svws-ui-button>
				<svws-ui-button type="secondary" @click="addWochentag" v-if="manager().zeitrasterGetWochentagMax() < 7">
					<i-ri-calendar-event-line /><i-ri-add-line class="-ml-1" />{{ Wochentag.fromIDorException(manager().zeitrasterGetWochentagMaxEnum().id + 1) }}
				</svws-ui-button>
			</div>
			<svws-ui-button type="secondary" @click="addBlock" title="Alle Zeitraster Montag - Freitag, 1.- 9. Stunde erstellen">
				<i-ri-calendar-event-line /><i-ri-add-line class="-ml-1" />Mo-Fr / 1.-9. erstellen
			</svws-ui-button>
			<template v-if="importZeitraster !== undefined">
				<stundenplan-zeitraster-import-modal :stundenplan-manager="manager" :import-zeitraster="importZeitraster" :remove-zeitraster="removeZeitraster" v-slot="{ openModal }">
					<svws-ui-button class="mb-5" type="secondary" @click="openModal()"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
				</stundenplan-zeitraster-import-modal>
			</template>
			<slot />
		</div>
	</aside>
</template>

<script setup lang="ts">

	import type { StundenplanAnsichtPlanungProps } from "./StundenplanAnsichtPlanungProps";
	import type { StundenplanPausenzeit, StundenplanZeitraster} from "@core";
	import { Wochentag } from "@core";
	import { computed, ref, toRaw } from "vue";

	const props = defineProps<StundenplanAnsichtPlanungProps>();

	const showZeitachse = true;

	const emit = defineEmits<{
		'selected:updated': [item: Wochentag|number|StundenplanZeitraster|StundenplanPausenzeit|undefined];
	}>();

	const selected = ref<Wochentag|number|StundenplanZeitraster|StundenplanPausenzeit|undefined>();

	function updateSelected(event: Wochentag|number|StundenplanZeitraster|StundenplanPausenzeit) {
		if (event === toRaw(selected.value)) {
			selected.value = undefined;
			emit('selected:updated', undefined);
		} else {
			selected.value = event;
			emit('selected:updated', event);
		}
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
		return "grid-row-start: " + Math.round(rowStart + 1) + "; grid-row-end: " + Math.round(rowEnd + 1) + "; grid-column: 1;";
	}

	function posPause(wochentag: Wochentag | undefined, pause: StundenplanPausenzeit): string {
		let rowStart = 0;
		let rowEnd = 10;
		if ((pause.beginn === null) || (pause.ende === null)) {
			rowStart = 1;
		} else {
			rowStart = (pause.beginn - beginn.value) / 5;
			rowEnd = (pause.ende - beginn.value) / 5;
		}
		return "grid-row-start: " + Math.round(rowStart + 1) + "; grid-row-end: " + Math.round(rowEnd + 1) + "; grid-column: 1;";
	}

	async function addWochentag() {
		const letzerWochentag = props.manager().zeitrasterGetWochentagMaxEnum();
		const list = props.manager().zeitrasterGetDummyListe(letzerWochentag.id, letzerWochentag.id +1, 1, 1);
		await props.addZeitraster(list);
	}

	async function addStunde() {
		const letzteStunde = props.manager().zeitrasterGetStundeMax();
		const wochentagMin = props.manager().zeitrasterGetWochentagMin();
		const wochentagMax = props.manager().zeitrasterGetWochentagMax();
		const list = props.manager().zeitrasterGetDummyListe(wochentagMin, wochentagMax, letzteStunde, letzteStunde + 1);
		await props.addZeitraster(list);
	}

	async function addBlock() {
		const list = props.manager().zeitrasterGetDummyListe(1, 5, 1, 9)
		await props.addZeitraster(list);
	}

</script>
