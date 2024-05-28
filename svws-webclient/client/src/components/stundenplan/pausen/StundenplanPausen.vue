<template>
	<div class="page--content page--content--full">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<div class="ml-4 flex gap-0.5 items-center leading-none">
					<div class="text-button font-bold mr-1 -mt-px">Klasse:</div>
					<svws-ui-select headless title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeSichtbarAsList()" :item-text="i => i.kuerzel" autocomplete
						:item-filter="(i, text)=> i.filter(k=>k.kuerzel.includes(text.toLocaleLowerCase()))" :item-sort="() => 0" type="transparent" removable />
					<!-- <div class="text-button font-bold mr-1 -mt-px">Wochentyp:</div>
					<svws-ui-select headless title="Wochentyp" v-model="wochentypAnzeige" :items="wochentypen()" class="print:hidden" type="transparent"
						:disabled="wochentypen().size() <= 0" :item-text="wt => stundenplanManager().stundenplanGetWochenTypAsString(wt)" /> -->
				</div>
			</svws-ui-sub-nav>
		</Teleport>
		<div>
			<svws-ui-table :items="stundenplanManager().lehrerGetMengeAsList()" :columns="[{key: 'nachname', label: 'Name'}]">
				<template #cell(nachname)="{rowData: lehrer}">
					<div class="svws-ui-badge select-none group"
						@dragstart="onDrag(lehrer)" @dragend="onDrag(undefined)"
						:class="dragData ? 'cursor-grabbing' : 'cursor-grab'" draggable="true">
						<span class="rounded-sm ">
							<span class="icon i-ri-draggable inline-block -ml-1 icon-dark opacity-60 group-hover:opacity-100 group-hover:icon-dark" />
						</span>
						<span class="my-0.5">{{ lehrer.nachname }}</span>
					</div>
				</template>
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
					<div v-for="pause in getPausenzeitenWochentag(wochentag).value" :key="pause.hashCode()" class="border rounded-md my-2 mx-1" :style="posPause(pause)"
						@dragover="setDragOverPausenzeit(pause)" @dragleave="unsetDragOverPausenzeit"
						@click="selectPausenzeit(pause)">
						<div class="font-bold px-2 py-1" :class="{'bg-green-400/50': dragOverPausenzeit?.id === pause.id}"> {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitBeginn(pause.id) }} – {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitEnde(pause.id) }} {{ pause.bezeichnung }} </div>
						<!-- Zeige Wochentypenübersicht nur an, wenn mehr als jede Woche vorhanden ist -->
						<div v-if="wochentypen().size() > 1" class="svws-ui-stundenplan--pausen-aufsicht flex-grow font-bold">
							<div>Bereich</div>
							<div v-for="typ in wochentypen()" :key="typ">{{ stundenplanManager().stundenplanGetWochenTypAsString(typ) }}</div>
						</div>
						<div v-for="aufsichtsbereich in stundenplanManager().aufsichtsbereichGetMengeAsList()" :key="aufsichtsbereich.id" class="svws-ui-stundenplan--pausen-aufsicht flex-grow"
							@dragover.prevent="setDragOverBereich(aufsichtsbereich)" @dragleave.stop="dragOverBereich = undefined">
							<div> {{ aufsichtsbereich.kuerzel }} </div>
							<div v-for="typ in wochentypen()" :key="typ" @drop="onDrop" class="rounded-md"
								@dragover.prevent="dragOverWochentyp = typ" @dragleave.stop="dragOverWochentyp = undefined"
								:class="{'bg-green-400': (dragOverPausenzeit?.id === pause.id) && (dragOverBereich?.id === aufsichtsbereich.id) && (!dragOverBereichGesperrt) && (dragOverWochentyp === typ), 'bg-red-400/50': (dragOverPausenzeit?.id === pause.id) && (dragOverBereich?.id === aufsichtsbereich.id) && dragOverBereichGesperrt && dragOverWochentyp === typ}">
								<div v-for="lehrer in hatAufsicht(pause.id, aufsichtsbereich.id, typ).value" :key="lehrer.id" class="flex gap-3 group hover:bg-slate-100 px-2 rounded-md" :class="{'bg-red-400': lehrer.id === dragData?.id}">
									<span>{{ lehrer.kuerzel }}</span>
									<span class="icon-sm icon-error i-ri-delete-bin-line inline-block opacity-0 group-hover:opacity-100 cursor-pointer" @click="delAufsicht(lehrer.id, aufsichtsbereich.id, typ, pause.id)" />
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
	const dragOverAufsichten = ref<List<StundenplanPausenaufsicht>>(new ArrayList());
	const dragOverPausenzeit = ref<StundenplanPausenzeit>();
	const dragOverBereich = ref<StundenplanAufsichtsbereich>();
	const dragOverBereichGesperrt = ref<boolean>(false);
	const dragOverWochentyp = ref<number>();

	function onDrag(data: StundenplanLehrer|undefined) {
		dragData.value = data;
	}

	function setDragOverPausenzeit(pause: StundenplanPausenzeit) {
		if (pause.id === dragOverPausenzeit.value?.id)
			return;
		dragOverPausenzeit.value = pause;
		dragOverAufsichten.value = props.stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(pause.id);
	}

	function unsetDragOverPausenzeit() {
		dragOverPausenzeit.value = undefined;
		dragOverAufsichten.value = new ArrayList();
	}

	function setDragOverBereich(bereich: StundenplanAufsichtsbereich) {
		if ((dragOverBereich.value?.id === bereich.id) || (dragData.value === undefined))
			return;
		dragOverBereich.value = bereich;
		for (const aufsicht of dragOverAufsichten.value)
			if ((aufsicht.idLehrer === dragData.value.id) && (aufsicht.bereiche.contains(bereich.id)) && ((dragOverWochentyp.value === aufsicht.wochentyp) || (aufsicht.wochentyp === 0) || ((aufsicht.wochentyp) !== 0 && (dragOverWochentyp.value === 0)))) {
				dragOverBereichGesperrt.value = true;
				return;
			}
		dragOverBereichGesperrt.value = false;
	}

	async function onDrop() {
		if (dragOverBereichGesperrt.value || (dragData.value === undefined) || (dragOverBereich.value === undefined) || (dragOverPausenzeit.value === undefined) || (dragOverWochentyp.value === undefined))
			return unsetDragOverPausenzeit();
		const bereiche = new ArrayList<number>();
		bereiche.add(dragOverBereich.value.id);
		for (const aufsicht of dragOverAufsichten.value)
			if ((aufsicht.idLehrer === dragData.value.id) && (dragOverWochentyp.value === aufsicht.wochentyp)) {
				bereiche.addAll(aufsicht.bereiche);
				unsetDragOverPausenzeit();
				await props.patchAufsicht({bereiche}, aufsicht.id);
				return;
			}
		await props.addAufsicht({idLehrer: dragData.value.id, idPausenzeit: dragOverPausenzeit.value.id, bereiche, wochentyp: dragOverWochentyp.value});
		unsetDragOverPausenzeit();
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
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 lg:gap-x-8;
		/* grid-auto-rows: 100%; */
		grid-template-columns: 15rem 1fr;
		/* grid-auto-columns: max-content; */
	}

</style>
