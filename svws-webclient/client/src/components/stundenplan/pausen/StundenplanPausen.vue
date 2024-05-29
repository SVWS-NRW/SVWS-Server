<template>
	<div class="page--content page--content--full select-none">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<div class="ml-4 flex gap-0.5 items-center leading-none select-none">
					<div class="text-button font-bold mr-1 -mt-px">Klasse:</div>
					<svws-ui-select headless title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeSichtbarAsList()" :item-text="i => i.kuerzel" autocomplete
						:item-filter="(i, text)=> i.filter(k=>k.kuerzel.includes(text.toLocaleLowerCase()))" :item-sort="() => 0" type="transparent" removable />
				</div>
			</svws-ui-sub-nav>
		</Teleport>
		<div class="h-full overflow-y-auto w-72 border-2 rounded-xl border-dashed relative" :class="[dragFromPausenzeit === undefined ? 'border-black/0' : 'border-error ring-4 ring-error/10']" @drop="onDrop">
			<div class="fixed flex items-center justify-center h-3/4 w-64 z-20 pointer-events-none"><span :class="dragFromPausenzeit === undefined ? '':'icon-lg icon-error opacity-50 i-ri-delete-bin-line scale-[4]'" /></div>
			<svws-ui-table :items="stundenplanManager().lehrerGetMengeAsList()" :columns="[{key: 'nachname', label: 'Name'}]" disable-header>
				<template #cell(nachname)="{rowData: lehrer}">
					<div class="svws-ui-badge select-none group"
						@dragstart="onDrag(lehrer)" @dragover.prevent="() => true"
						:class="dragData ? 'cursor-grabbing' : 'cursor-grab'" draggable="true">
						<span class="icon i-ri-draggable inline-block -ml-1 icon-dark opacity-60 group-hover:opacity-100 group-hover:icon-dark rounded-sm" />
						<span class="my-0.5 truncate"> {{ lehrer.kuerzel }} ({{ lehrer.vorname[0] }}. {{ lehrer.nachname }})</span>
					</div>
				</template>
			</svws-ui-table>
		</div>
		<div class="h-full overflow-y-auto w-full  ">
			<div v-if="stundenplanManager().pausenzeitGetMengeAsList().size()" class="svws-ui-stundenplan">
				<!-- Die Überschriften des Stundenplan -->
				<div class="svws-ui-stundenplan--head">
					<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
					<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="font-bold text-center inline-flex items-center justify-center">
						<div> {{ wochentage[wochentag.id] }} </div>
					</div>
				</div>
				<!-- Die Daten des Stundenplans -->
				<div class="svws-ui-stundenplan--body">
					<!-- Zeige die Unterrichte und Pausenaufsichten des Stundenplans -->
					<div v-for="wochentag in wochentagRange" :key="wochentag.id">
						<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
						<div v-for="pause in getPausenzeitenWochentag(wochentag).value" :key="pause.hashCode()" class="border rounded-md my-2 mx-1" :style="posPause(pause)"
							@dragover.prevent="dragOverPausenzeit = pause" @dragleave.stop="dragOverPausenzeit = undefined"
							@click="selectPausenzeit(pause)">
							<div class="font-bold px-2 py-1" :class="{'bg-primary/10': dragOverPausenzeit?.id === pause.id}"> {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitBeginn(pause.id) }} – {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitEnde(pause.id) }} {{ pause.bezeichnung }} </div>
							<div v-if="pause.klassen.size()" class="text-sm px-2 py-1"> {{ [...pause.klassen].map(k => stundenplanManager().klasseGetByIdOrException(k).kuerzel + " ") }} </div>
							<!-- Zeige Wochentypenübersicht nur an, wenn mehr als jede Woche vorhanden ist -->
							<div v-if="wochentypen().size() > 1" class="svws-ui-stundenplan--pausen-aufsicht flex-grow font-bold">
								<div>Bereich</div>
								<div v-for="typ in wochentypen()" :key="typ">{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(typ) }}</div>
							</div>
							<div v-for="aufsichtsbereich in stundenplanManager().aufsichtsbereichGetMengeAsList()" :key="aufsichtsbereich.id" class="svws-ui-stundenplan--pausen-aufsicht flex-grow"
								@dragover.prevent="setDragOverBereich(aufsichtsbereich)" @dragleave.stop="dragOverBereich = undefined">
								<div> {{ aufsichtsbereich.kuerzel }} </div>
								<div v-for="typ in wochentypen()" :key="typ"
									@drop="onDrop" class="rounded-md" @dragover.prevent="dragOverWochentyp = typ" @dragleave.stop="dragOverWochentyp = undefined"
									:class="{'bg-green-400': (dragOverPausenzeit?.id === pause.id) && (dragOverBereich?.id === aufsichtsbereich.id) && (!dragOverBereichGesperrt) && (dragOverWochentyp === typ), 'bg-red-400/50': (dragOverPausenzeit?.id === pause.id) && (dragOverBereich?.id === aufsichtsbereich.id) && dragOverBereichGesperrt && dragOverWochentyp === typ}">
									<div v-for="lehrer in hatAufsicht(pause.id, aufsichtsbereich.id, typ).value" :key="lehrer.id" class="hover:bg-slate-100 rounded-md" :class="{'bg-red-400 cursor-grabbing': lehrer.id === dragData?.id, 'cursor-grab': !dragData}"
										@dragstart.stop="onDrag(lehrer, {pauseID: pause.id, aufsichtsbereichID: aufsichtsbereich.id, typ})" @dragend="dragFromPausenzeit = undefined" draggable="true">
										<span>{{ lehrer.kuerzel }}</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div v-else class="svws-ui-stundenplan">Es wurden noch keine Pausenzeiten für diesen Stundenplan angelegt.</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref } from "vue";
	import type { StundenplanPausenProps } from "./StundenplanPausenProps";
	import type { Wochentag, List, StundenplanPausenzeit, StundenplanKlasse, StundenplanPausenaufsicht, StundenplanLehrer, StundenplanAufsichtsbereich } from "@core";
	import { ArrayList } from "@core";

	const props = defineProps<StundenplanPausenProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const _klasse = ref<StundenplanKlasse | undefined>(undefined);
	const selectedPausenzeit = ref<StundenplanPausenzeit|undefined>();
	const selectedPausenaufsichtSet = ref<Set<number>>(new Set());
	const wochentypAnzeige = ref<number>(0);
	const dragData = ref<StundenplanLehrer|undefined>(undefined);
	const dragFromPausenzeit = ref<{pauseID: number; aufsichtsbereichID: number; typ: number}|undefined>(undefined);
	const dragOverPausenzeit = ref<StundenplanPausenzeit>();
	const dragOverAufsichten = (pause: StundenplanPausenzeit) => computed<List<StundenplanPausenaufsicht>>(() => {
		if (dragOverPausenzeit.value !== undefined)
			return props.stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(dragOverPausenzeit.value.id)
		return new ArrayList();
	});
	const dragOverBereich = ref<StundenplanAufsichtsbereich>();
	const dragOverBereichGesperrt = ref<boolean>(false);
	const dragOverWochentyp = ref<number>();


	function onDrag(data: StundenplanLehrer|undefined, fromPausenzeit?: {pauseID: number; aufsichtsbereichID: number; typ: number}) {
		dragData.value = data;
		dragFromPausenzeit.value = fromPausenzeit;
	}

	function setDragOverBereich(bereich: StundenplanAufsichtsbereich) {
		if ((dragOverBereich.value?.id === bereich.id) || (dragData.value === undefined))
			return;
		dragOverBereich.value = bereich;
		for (const aufsicht of dragOverAufsichten(dragOverPausenzeit.value).value)
			if ((aufsicht.idLehrer === dragData.value.id) && (aufsicht.bereiche.contains(bereich.id))
				&& ((dragOverWochentyp.value === aufsicht.wochentyp) || (aufsicht.wochentyp === 0) || ((aufsicht.wochentyp) !== 0 && (dragOverWochentyp.value === 0)))) {
				dragOverBereichGesperrt.value = true;
				return;
			}
		dragOverBereichGesperrt.value = false;
	}

	async function onDrop() {
		if ((dragFromPausenzeit.value !== undefined) && (dragData.value !== undefined)) {
			const { aufsichtsbereichID, typ, pauseID } = dragFromPausenzeit.value;
			await delAufsicht(dragData.value.id, aufsichtsbereichID, typ, pauseID);
		}
		if (dragOverBereichGesperrt.value || (dragData.value === undefined) || (dragOverBereich.value === undefined) || (dragOverPausenzeit.value === undefined) || (dragOverWochentyp.value === undefined)) {
			dragOverPausenzeit.value = undefined;
			return;
		}
		const bereiche = new ArrayList<number>();
		bereiche.add(dragOverBereich.value.id);
		for (const aufsicht of dragOverAufsichten(dragOverPausenzeit.value).value)
			if ((aufsicht.idLehrer === dragData.value.id) && (dragOverWochentyp.value === aufsicht.wochentyp)) {
				bereiche.addAll(aufsicht.bereiche);
				dragOverPausenzeit.value = undefined;
				await props.patchAufsicht({bereiche}, aufsicht.id);
				return;
			}
		await props.addAufsicht({idLehrer: dragData.value.id, idPausenzeit: dragOverPausenzeit.value.id, bereiche, wochentyp: dragOverWochentyp.value});
		dragOverPausenzeit.value = undefined;
		dragData.value = undefined;
	}

	function wochentypen(): List<number> {
		let modell = props.stundenplanManager().getWochenTypModell();
		if (modell <= 1)
			modell = 0;
		const result = new ArrayList<number>();
		for (let n = 0; n <= modell; n++)
			result.add(n);
		return result;
	}

	const klasse = computed<StundenplanKlasse | undefined>({
		get: () => {
			if (_klasse.value !== undefined)
				return props.stundenplanManager().klasseGetByIdOrException(_klasse.value.id);
			return _klasse.value;
		},
		set: (value) => _klasse.value = value
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

	const hatAufsicht = (pauseID: number, aufsichtsbereichID: number, typ: number) => computed(() => {
		const aufsichten = props.stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(pauseID);
		let lehrer = new ArrayList<StundenplanLehrer>();
		for (const a of aufsichten)
			for (const b of a.bereiche)
				if (b === aufsichtsbereichID && typ === a.wochentyp)
					lehrer.add(props.stundenplanManager().lehrerGetByIdOrException(a.idLehrer));
		return lehrer;
	})

	async function delAufsicht(lehrerID: number, aufsichtsbereichID: number, wochentyp: number, pauseID: number) {
		const aufsichten = props.stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(pauseID);
		for (const aufsicht of aufsichten) {
			if ((aufsicht.idLehrer === lehrerID) && (aufsicht.wochentyp === wochentyp) && (aufsicht.bereiche.contains(aufsichtsbereichID))) {
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

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];
	const wochentagRange = computed(() => props.stundenplanManager().pausenzeitGetWochentageAlsEnumRange());

	const getPausenzeitenWochentag = (wochentag: Wochentag) => computed<Array<StundenplanPausenzeit>>(() => {
		if (klasse.value !== undefined)
			return [...props.stundenplanManager().pausenzeitGetMengeByKlasseIdAndWochentagAsList(klasse.value.id, wochentag.id)];
		else
			return [...props.stundenplanManager().pausenzeitGetMengeByWochentagOrEmptyList(wochentag.id)];
	})

	const beginn = computed(() => {
		return props.stundenplanManager().pausenzeitUndZeitrasterGetMinutenMinOhneLeere();
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

</script>

<style lang="postcss" scoped>

.page--content {
		@apply overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 flex flex-row gap-2
	}

	.svws-ui-stundenplan--head, .svws-ui-stundenplan--body {
		@apply  grid auto-cols-fr border-none pt-0;
		grid-template-columns: initial;
	}

</style>
