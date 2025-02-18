<template>
	<div v-if="manager().getListZeitraster().size()" class="svws-ui-stundenplan svws-ui-stundenplan--mode-planung" :class="`${showZeitachse ? 'svws-hat-zeitachse' : 'svws-ohne-zeitachse'}`">
		<div class="svws-ui-stundenplan--head">
			<span class="icon i-ri-time-line svws-time-icon print:!hidden" v-if="showZeitachse" />
			<!-- Das Feld links in der Überschrift beinhaltet den ausgewählten Wochentyp -->
			<div class="inline-flex gap-1 items-center justify-center print:!pl-2 print:!justify-start opacity-50 text-sm font-bold pb-0.5" />
			<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" @click="updateSelected(wochentag)" class="svws-wochentag-label" :class="{'svws-selected': selected===wochentag}">
				<span class="px-2 py-1 rounded-xs"> {{ wochentag.beschreibung }}</span>
			</div>
		</div>
		<!-- Die Daten des Stundenplans -->
		<div class="svws-ui-stundenplan--body" :style="{'--zeitrasterRows': zeitrasterRows}">
			<!-- Die Zeitachse des Stundenplans auf der linken Seite -->
			<div class="svws-ui-stundenplan--zeitraster svws-zeitachse" v-if="showZeitachse">
				<template v-for="n in zeitrasterRows" :key="n">
					<span v-if="n % 3 === 2" class="svws-ui-stundenplan--einheit" :class="{'svws-extended': n % 4 === 2, 'svws-small': n % 4 === 1 || n % 4 === 3}" :style="`grid-row: ${ n-1 } / ${n+2}; grid-column: 1`">
						<template v-if="n % 4 === 2">
							{{ Math.floor((beginn + (n * 5)) / 60) }}:00
						</template>
					</span>
				</template>
			</div>
			<div class="svws-ui-stundenplan--zeitraster">
				<!-- Die Zeitraster-Einträge -->
				<div v-for="stunde in zeitrasterRange" :key="stunde" @click="updateSelected(stunde)" class="svws-ui-stundenplan--stunde svws-label text-center justify-center cursor-pointer" :style="posZeitraster(undefined, stunde)" :class="{'svws-selected-stunde': selected===stunde}">
					<div class="text-headline-sm">
						{{ stunde }}.&nbsp;Stunde
					</div>
					<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten" class="font-bold text-sm">
						{{ zeiten.replace(' Uhr', '') }}
					</div>
				</div>
			</div>
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="svws-ui-stundenplan--zeitraster" :class="{'svws-selected': selected === wochentag}">
				<template v-for="zeitrasterEintrag in manager().getListZeitrasterZuWochentag(wochentag)" :key="zeitrasterEintrag.id">
					<div class="svws-ui-stundenplan--stunde cursor-pointer" @click="updateSelected(zeitrasterEintrag)" :style="posZeitraster(wochentag, zeitrasterEintrag.unterrichtstunde)" :class="{'svws-selected': (selected === zeitrasterEintrag) || (selected === zeitrasterEintrag.unterrichtstunde), 'bg-ui-caution': manager().zeitrasterGetIstZustandProblematisch(zeitrasterEintrag)}">
						<div class="svws-ui-stundenplan--unterricht">
							<span>{{ zeitrasterEintrag.unterrichtstunde }}</span>
							<div class="flex content-start">
								{{ manager().zeitrasterGetByIdStringOfUhrzeitBeginn(zeitrasterEintrag.id) }}–{{ manager().zeitrasterGetByIdStringOfUhrzeitEnde(zeitrasterEintrag.id) }}
							</div>
						</div>
					</div>
				</template>
				<template v-for="pause in manager().pausenzeitGetMengeByWochentagOrEmptyList(wochentag.id)" :key="pause.id">
					<div class="svws-ui-stundenplan--pause cursor-pointer" @click="updateSelected(pause)" :style="posPause(wochentag, pause)" :class="{'svws-selected': selected === pause}">
						<div v-if="selected===pause" class="svws-ui-stundenplan--pausen-aufsicht">
							<span class="icon i-ri-cup-line" />
						</div>
						<div v-else class="svws-ui-stundenplan--pausen-aufsicht">
							<span class="icon i-ri-cup-line icon-ui-contrast-25" />
						</div>
					</div>
				</template>
			</div>
		</div>
	</div>
	<div v-else class="svws-ui-stundenplan">Es wurden noch keine Zeitraster für diesen Stundenplan angelegt.</div>
	<div class="sticky top-8 flex flex-col gap-5 h-full overflow-y-hidden">
		<div class="flex gap-3 flex-wrap justify-stretch">
			<svws-ui-button class="grow" type="secondary" @click="addStunde">
				<span class="icon i-ri-calendar-event-line" />
				<span class="icon i-ri-add-line -ml-1" />{{ manager().zeitrasterGetStundeMax() + (manager().getListZeitraster().size() === 0 ? 0:1) }}. Stunde
			</svws-ui-button>
			<svws-ui-button class="grow" type="secondary" @click="addWochentag" v-if="manager().zeitrasterGetWochentagMax() < 7">
				<span class="icon i-ri-calendar-event-line" />
				<span class="icon i-ri-add-line -ml-1" />{{ Wochentag.fromIDorException(manager().zeitrasterGetWochentagMaxEnum().id + (manager().getListZeitraster().size() === 0 ? 0:1)) }}
			</svws-ui-button>
		</div>
		<ui-card icon="i-ri-add-line" title="Alle Zeitraster erstellen" :is-open="actionZeitraster" @update:is-open="(isOpen) => actionZeitraster = isOpen">
			<stundenplan-zeitraster-einstellungen :manager :set-settings-defaults>
				<svws-ui-button type="secondary" @click="addBlock" :title="`Alle Zeitraster Montag - Freitag, 1.-${Schulform.G === schulform ? '6':'9'}. Stunde erstellen`">
					<span class="icon i-ri-calendar-event-line" />
					<span class="icon i-ri-add-line -ml-1" />Mo-Fr / 1.-{{ Schulform.G === schulform ? '6':'9' }}. erstellen
				</svws-ui-button>
			</stundenplan-zeitraster-einstellungen>
		</ui-card>
		<template v-if="importZeitraster !== undefined">
			<stundenplan-zeitraster-import-modal :stundenplan-manager="manager" :import-zeitraster :remove-zeitraster v-slot="{ openModal }">
				<svws-ui-button class="mb-5" type="secondary" @click="openModal()"><span class="icon i-ri-archive-line" /> Aus Katalog importieren</svws-ui-button>
			</stundenplan-zeitraster-import-modal>
		</template>
		<div class="flex gap-3 flex-wrap justify-stretch">
			<svws-ui-button class="grow" type="secondary" @click="exportJSON" title="Alle Zeitraster als JSON-Datei exportieren">
				<span class="icon i-ri-upload-2-line" />Exportieren
			</svws-ui-button>
			<stundenplan-zeitraster-json-import-modal :stundenplan-manager="manager" :add-zeitraster :remove-zeitraster v-slot="{ openModal }">
				<svws-ui-button class="grow" type="secondary" @click="openModal" title="Zeitraster aus einer JSON-Datei importieren"> <span class="icon i-ri-download-2-line" /> Importieren… </svws-ui-button>
			</stundenplan-zeitraster-json-import-modal>
		</div>
		<slot />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { StundenplanAnsichtPlanungProps } from "./StundenplanAnsichtPlanungProps";
	import { StundenplanZeitraster } from "../../../../core/src/core/data/stundenplan/StundenplanZeitraster";
	import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";
	import { Wochentag } from "../../../../core/src/core/types/Wochentag";
	import { Schulform } from "../../../../core/src/asd/types/schule/Schulform";

	const props = defineProps<StundenplanAnsichtPlanungProps>();
	const showZeitachse = true;
	const actionZeitraster = ref<boolean>(false);

	function updateSelected(event: Wochentag|number|StundenplanZeitraster|StundenplanPausenzeit) {
		if (event === props.selected)
			props.setSelection(undefined);
		else
			props.setSelection(event);
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
		const z = props.manager().zeitrasterGetWochentageAlsEnumRange();
		const p = props.manager().pausenzeitGetWochentageAlsEnumRange();
		return z.length > p.length ? z : p;
	});

	const zeitrasterRange = computed(() => {
		// TODO warten bis die Methode implementiert ist und dann mit den beiden TODOs oben aktivieren
		// if (props.ignoreEmpty)
		// 	const stunden =  props.manager().zeitrasterGetStundenRangeOhneLeere();
		const stunden = props.manager().zeitrasterGetStundenRange();
		// Prüfe noch, ob in den Stunden überhaupt Zeitraster-Einträge vorliegen
		return stunden.filter((stunde) => !props.manager().getListZeitrasterZuStunde(stunde).isEmpty());
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
		let zbeginn = props.manager().zeitrasterGetMinutenMinDerStunde(stunde);
		let zende = props.manager().zeitrasterGetMinutenMaxDerStunde(stunde);
		if (wochentag !== undefined) {
			const z = props.manager().zeitrasterGetByWochentagAndStundeOrNull(wochentag.id, stunde);
			if (z !== null) {
				if (z.stundenbeginn !== null)
					zbeginn = z.stundenbeginn;
				if (z.stundenende !== null)
					zende = z.stundenende;
			}
		}
		const rowStart = (zbeginn - beginn.value) / 5;
		const rowEnd = (zende - beginn.value) / 5;
		return "grid-row-start: " + Math.round(rowStart + 1).toString() + "; grid-row-end: " + Math.round(rowEnd + 1).toString() + ";";
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
		return "grid-row-start: " + Math.round(rowStart + 1).toString() + "; grid-row-end: " + Math.round(rowEnd + 1).toString() + ";";
	}

	async function addWochentag() {
		const letzerWochentag = props.manager().zeitrasterGetWochentagMaxEnum();
		const list = props.manager().zeitrasterGetDummyListe(letzerWochentag.id, letzerWochentag.id + (props.manager().getListZeitraster().size() > 0 ? 1 : 0), 1, 1);
		await props.addZeitraster(list);
	}

	async function addStunde() {
		const letzteStunde = props.manager().zeitrasterGetStundeMax();
		const wochentagMin = props.manager().zeitrasterGetWochentagMin();
		const wochentagMax = props.manager().zeitrasterGetWochentagMax();
		const list = props.manager().zeitrasterGetDummyListe(wochentagMin, wochentagMax, letzteStunde, letzteStunde + (props.manager().getListZeitraster().size() > 0 ? 1 : 0));
		await props.addZeitraster(list);
	}

	async function addBlock() {
		const letzteStunde = Schulform.G === props.schulform ? 6 : 9;
		const list = props.manager().zeitrasterGetDummyListe(1, 5, 1, letzteStunde);
		await props.addZeitraster(list);
	}

	async function exportJSON() {
		const arr = [];
		for (const e of props.manager().getListZeitraster())
			arr.push(StundenplanZeitraster.transpilerToJSON(e));
		const blob = new Blob(['['+arr.toString()+']'], { type: "application/json" });
		const link = document.createElement("a");
		link.href = URL.createObjectURL(blob);
		link.download = "ExportZeitraster.json";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
