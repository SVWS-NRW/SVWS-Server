<template>
	<div class="page--content page--content--full">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<div class="ml-4 flex gap-0.5 items-center leading-none">
					<div class="text-button font-bold mr-1 -mt-px">Klasse:</div>
					<svws-ui-select headless title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeSichtbarAsList()" :item-text="i => i.kuerzel" autocomplete
						:item-filter="(i, text)=> i.filter(k=>k.kuerzel.includes(text.toLocaleLowerCase()))" :item-sort="() => 0" type="transparent" />
					<div class="text-button font-bold mr-1 -mt-px">Wochentyp:</div>
					<svws-ui-select headless title="Wochentyp" v-model="wochentypAnzeige" :items="wochentypen()" class="print:hidden" type="transparent"
						:disabled="wochentypen().size() <= 0" :item-text="wt => stundenplanManager().stundenplanGetWochenTypAsString(wt)" />
				</div>
			</svws-ui-sub-nav>
		</Teleport>
		<div>
			<svws-ui-table :items="stundenplanManager().lehrerGetMengeAsList()" :columns="[{key: 'nachname', label: 'Name'}]">
				<template #cell(nachname)="{value}">{{ value }}</template>
			</svws-ui-table>
		</div>
		<div v-if="stundenplanManager().pausenzeitGetMengeAsList().size()" class="svws-ui-stundenplan">
			<!-- Die Überschriften des Stundenplan -->
			<div class="svws-ui-stundenplan--head">
				<!-- Das Feld links in der Überschrift beinhaltet den ausgewählten Wochentyp -->
				<div class="inline-flex items-center justify-center print:pl-2 print:justify-start" :class="{'opacity-50 print:invisible': wochentyp() === 0, 'font-bold text-headline-md pb-0.5': wochentyp() !== 0}" />
				<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
				<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="font-bold text-center inline-flex items-center justify-center">
					<div> {{ wochentage[wochentag.id] }} </div>
				</div>
			</div>
			<!-- Die Daten des Stundenplans -->
			<div class="svws-ui-stundenplan--body">
				<!-- Die Zeitachse des Stundenplans auf der linken Seite -->
				<div />
				<!-- Zeige die Unterrichte und Pausenaufsichten des Stundenplans -->
				<div v-for="wochentag in wochentagRange" :key="wochentag.id">
					<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
					<div v-for="pause in getPausenzeitenWochentag(wochentag).value" :key="pause.id" class="border rounded-md my-2 mx-1 cursor-pointer" :class="{'bg-green-400/50': selectedPausenzeit?.id === pause.id}" :style="posPause(pause)" @click="selectPausenzeit(pause)">
						<div class="font-bold m-1"> {{ pause.bezeichnung }} </div>
						<div class="font-bold m-1"> {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitBeginn(pause.id) }} – {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitEnde(pause.id) }} </div>
						<div v-for="aufsichtsbereich in stundenplanManager().aufsichtsbereichGetMengeAsList()" :key="aufsichtsbereich.id" class="svws-ui-stundenplan--pausen-aufsicht flex-grow" :class="{'bg-green-400': hatAufsichtsbereich(pause.id, aufsichtsbereich.id)}" @click.stop="selectPausenaufsicht(pause.id, aufsichtsbereich.id)">
							<div class="flex gap-3">
								<div>{{ aufsichtsbereich.kuerzel }}</div>
								<div>
									<div v-for="lehrer in hatAufsicht(pause.id, aufsichtsbereich.id).value" :key="lehrer.id" class="flex gap-3"><span>{{ lehrer.kuerzel }}</span> <span v-if="hatAufsichtsbereich(pause.id, aufsichtsbereich.id)" class="icon-sm icon-error i-ri-delete-bin-line inline-block" @click="delAufsicht(lehrer.id, aufsichtsbereich.id)" /></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div v-else class="svws-ui-stundenplan">Es wurden noch keine Zeitraster für diesen Stundenplan angelegt.</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref } from "vue";
	import type { Wochentag, List, StundenplanPausenaufsicht, StundenplanAufsichtsbereich, StundenplanPausenzeit, StundenplanLehrer, StundenplanKlasse } from "@core";
	import { ArrayList, DateUtils } from "@core";
	import type { StundenplanPausenProps } from "./StundenplanPausenProps";

	const props = defineProps<StundenplanPausenProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const _klasse = ref<StundenplanKlasse | undefined>(undefined);
	const zeit = ref<StundenplanPausenzeit | undefined>();
	const selectedPausenzeit = ref<StundenplanPausenzeit|undefined>();
	const selectedPausenaufsichtSet = ref<Set<number>>(new Set());
	const wochentypAnzeige = ref<number>(0);

	function wochentypen(): List<number> {
		let modell = props.stundenplanManager().getWochenTypModell();
		if (modell <= 1)
			modell = 0;
		const result = new ArrayList<number>();
		for (let n = 0; n <= modell; n++)
			result.add(n);
		return result;
	}

	const klasse = computed<StundenplanKlasse>({
		get: () : StundenplanKlasse => {
			if (_klasse.value !== undefined)
				try {
					return props.stundenplanManager().klasseGetByIdOrException(_klasse.value.id);
				} catch (e) { /* empty */ }
			return props.stundenplanManager().klasseGetMengeSichtbarAsList().get(0);
		},
		set: (value : StundenplanKlasse) => _klasse.value = value
	});

	function selectPausenzeit(pause: StundenplanPausenzeit) {
		for (const a of selectedPausenaufsichtSet.value)
			if (props.stundenplanManager().pausenaufsichtGetByIdOrException(a).idPausenzeit === pause.id) {
				selectedPausenaufsichtSet.value.clear();
				break;
			}
		if (selectedPausenzeit.value?.id === pause.id) {
			selectedPausenzeit.value = undefined;
			selectedPausenaufsichtSet.value.clear();
		} else
			selectedPausenzeit.value = pause;
	}

	const hatAufsicht = (pauseID: number, aufsichtsbereichID: number) => computed(() => {
		const aufsichten = props.stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(pauseID);
		let lehrer = new ArrayList<StundenplanLehrer>();
		for (const a of aufsichten)
			for (const b of a.bereiche)
				if (b === aufsichtsbereichID)
					lehrer.add(props.stundenplanManager().lehrerGetByIdOrException(a.idLehrer));
		return lehrer;
	})

	async function delAufsicht(lehrerID: number, aufsichtsbereichID: number) {
		for (const a of selectedPausenaufsichtSet.value) {
			const aufsicht = props.stundenplanManager().pausenaufsichtGetByIdOrException(a);
			if (aufsicht.idLehrer === lehrerID) {
				if (aufsicht.bereiche.size() === 1)
					await props.removeAufsicht(aufsicht.id);
				else {
					const bereiche = new ArrayList<number>()
					bereiche.addAll(aufsicht.bereiche);
					const i = bereiche.indexOf(aufsichtsbereichID);
					bereiche.removeElementAt(i);
					await props.patchAufsicht({bereiche}, aufsicht.id)
				}
			}
		}
	}

	function selectPausenaufsicht(pauseID: number, aufsichtsbereichID: number) {
		if (selectedPausenzeit.value?.id !== pauseID)
			selectedPausenzeit.value = props.stundenplanManager().pausenzeitGetByIdOrException(pauseID);
		const aufsichten = props.stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(pauseID);
		const setA = new Set();
		for (const a of aufsichten)
			for (const b of a.bereiche)
				if (b === aufsichtsbereichID) {
					if (selectedPausenaufsichtSet.value.has(a.id)) {
						selectedPausenaufsichtSet.value.delete(a.id)
					}
					else {
						selectedPausenaufsichtSet.value.add(a.id);
					}
				}
	}

	function hatAufsichtsbereich(pauseID: number, aufsichtsbereichID: number) {
		for (const a of selectedPausenaufsichtSet.value) {
			const aufsicht = props.stundenplanManager().pausenaufsichtGetByIdOrException(a);
			if (aufsicht.idPausenzeit === pauseID && aufsicht.bereiche.contains(aufsichtsbereichID))
				return true;
		}
		return false;
	}

	const items = computed(() => [...props.stundenplanManager().pausenzeitGetMengeAsList()]);

	const colsPausenzeiten = [
		{key: 'wochentag', label: 'Wochentag', span: 1},
		{key: 'beginn', label: 'Beginn', span: 1}, {key: 'ende', label: 'Ende', span: 1}
	]

	async function patchPausenBeginn(minuten: string, id: number) {
		const beginn = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({beginn}, id);
	}

	async function patchPausenEnde(minuten: string, id: number) {
		const ende = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({ende}, id);
	}

	const listPausenzeitenRest = computed(() => {
		const moeglich = new ArrayList<StundenplanPausenzeit>();
		for (const e of props.listPausenzeiten())
			if (!props.stundenplanManager().pausenzeitExistsByWochentagAndBeginnAndEnde(e.wochentag, e.beginn, e.ende))
				moeglich.add(e);
		return moeglich;
	})

	const bereich = ref<StundenplanAufsichtsbereich | undefined>();
	const selectedAufsichtsbereiche = ref<StundenplanAufsichtsbereich[]>([]);

	const listAufsichtsbereiche = computed(() => [...props.stundenplanManager().aufsichtsbereichGetMengeAsList()]);

	const colsAufsichtsbereiche = [
		{key: 'kuerzel', label: 'Kürzel', span: 1},
		{key: 'beschreibung', label: 'Beschreibung', span: 3}
	]

	const listAufsichtsbereicheRest = computed(() => {
		const moeglich = new ArrayList<StundenplanAufsichtsbereich>();
		for (const e of props.listAufsichtsbereiche())
			if (!props.stundenplanManager().aufsichtsbereichExistsByKuerzel(e.kuerzel))
				moeglich.add(e);
		return moeglich;
	})

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];
	const wochentagRange = computed(() => {
		const p = props.stundenplanManager().pausenzeitGetWochentageAlsEnumRange();
		return p;
	});

	const zeitrasterRange = computed(() => {
		return props.stundenplanManager().zeitrasterGetStundenRangeOhneLeere();
	});

	const zeitrasterRows = computed(() => {
		// TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist
		// props.manager().pausenzeitGetMengeByWochentagOrEmptyList(1).size() || 0
		return zeitrasterRange.value.length;
	});

	const getPausenzeitenWochentag = (wochentag: Wochentag) => computed<List<StundenplanPausenzeit>>(() => {
		const zeiten = props.stundenplanManager().pausenzeitGetMengeByWochentagOrEmptyList(wochentag.id);
		return zeiten
	})


	const getPausenaufsichtenPausenzeit = (pause: StundenplanPausenzeit) => computed<List<StundenplanPausenaufsicht>>(() => {
		const a = props.stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(pause.id);
		return a
	})

	const beginn = computed(() => {
		return props.stundenplanManager().pausenzeitUndZeitrasterGetMinutenMinOhneLeere();
	});

	const ende = computed(() => {
		return props.stundenplanManager().pausenzeitUndZeitrasterGetMinutenMaxOhneLeere();
	});

	function posPause(pause: StundenplanPausenzeit): string {
		const pzeit = props.stundenplanManager().pausenzeitGetByIdOrException(pause.id);
		let rowStart = 0;
		let rowEnd = 10;
		if ((pzeit.beginn !== null) && (pzeit.ende !== null)) {
			rowStart = (pzeit.beginn - beginn.value) / 5;
			rowEnd = (pzeit.ende - beginn.value) / 5;
		}
		return "grid-row-start: " + (Math.round(rowStart) + 1) + "; grid-row-end: " + (Math.round(rowEnd) + 1) + "; grid-column: 1;";
	}

	function aufsichtsbereiche(pausenaufsicht: StundenplanPausenaufsicht): string {
		let result = "";
		for (const idBereich of pausenaufsicht.bereiche) {
			const bereich = props.stundenplanManager().aufsichtsbereichGetByIdOrException(idBereich);
			if (result !== "")
				result += ",";
			result += bereich.kuerzel;
		}
		return result;
	}

	function getStringAufsichten(pause: StundenplanPausenzeit) {
		const pausenaufsichten = getPausenaufsichtenPausenzeit(pause);
		let text = "";
		for (const pausenaufsicht of pausenaufsichten.value) {
			if (text !== '')
				text += ', ';
			text += props.stundenplanManager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel;
		}
		return text;
	}


</script>

<style lang="postcss" scoped>

	.page--content {
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 lg:gap-x-8;
		/* grid-auto-rows: 100%; */
		grid-template-columns: 15rem 1fr;
		/* grid-auto-columns: max-content; */
	}

</style>
