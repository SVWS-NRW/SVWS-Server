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
			<svws-ui-table :items="stundenplanManager().lehrerGetMengeAsList()" :columns="[{key: 'nachname', label: 'Name'}]" disable-header :no-data="false" type="navigation">
				<template #cell(nachname)="{rowData: lehrer}">
					<div class="svws-ui-badge select-none group flex place-items-center w-full"
						@dragstart="onDrag(lehrer)" @dragover.prevent="() => true"
						:class="dragLehrer ? 'cursor-grabbing' : 'cursor-grab'" draggable="true">
						<span class="icon i-ri-draggable inline-block icon-dark opacity-60 group-hover:opacity-100 group-hover:icon-dark rounded-sm" />
						<span class="truncate"> {{ lehrer.kuerzel }} ({{ lehrer.vorname[0] }}. {{ lehrer.nachname }})</span>
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
						<div v-for="pause in getPausenzeitenWochentag(wochentag).value" :key="pause.hashCode()" class="border rounded-md my-2 mx-1" :style="posPause(pause)">
							<div class="font-bold px-2 py-1" :class="{'bg-primary/10': dragOverPausenzeit?.pauseID === pause.id}"> {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitBeginn(pause.id) }} – {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitEnde(pause.id) }} {{ pause.bezeichnung }} </div>
							<div v-if="!pause.klassen.isEmpty()" class="text-sm px-2 mb-2 opacity-70 font-bold"> {{ [...pause.klassen].map(k => " " + stundenplanManager().klasseGetByIdOrException(k).kuerzel).toString() }} </div>
							<!-- Zeige Wochentypenübersicht nur an, wenn mehr als jede Woche vorhanden ist -->
							<div v-if="wochentypen().size() > 1" class="svws-ui-stundenplan--pausen-aufsicht flex-grow font-bold place-items-center">
								<div>Bereich</div>
								<div v-for="typ in wochentypen()" :key="typ">{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(typ) }}</div>
							</div>
							<div v-for="aufsichtsbereich in stundenplanManager().aufsichtsbereichGetMengeAsList()" :key="aufsichtsbereich.id" class="svws-ui-stundenplan--pausen-aufsicht flex-grow">
								<div> {{ aufsichtsbereich.kuerzel }} </div>
								<div v-for="typ in wochentypen()" :key="typ"
									@drop="onDrop" class="rounded-md" @dragover.prevent="setDragOver(pause.id, aufsichtsbereich.id, typ)" @dragleave.stop="dragOverPausenzeit = undefined"
									:class="{'bg-green-400': isDraggingOver(pause.id, aufsichtsbereich.id, typ).value && !bereichGesperrtTyp, 'bg-red-400/50': isDraggingOver(pause.id, aufsichtsbereich.id, typ).value && bereichGesperrtTyp}">
									<div v-for="lehrer in hatAufsicht(pause.id, aufsichtsbereich.id, typ).value" :key="lehrer.id" class="hover:bg-slate-100 rounded-md group flex place-items-center" :class="{'bg-red-400 cursor-grabbing': lehrer.id === dragLehrer?.id, 'cursor-grab': !dragLehrer}"
										@dragstart.stop="onDrag(lehrer, {pauseID: pause.id, aufsichtsbereichID: aufsichtsbereich.id, typ})" @dragend="dragFromPausenzeit = undefined" draggable="true">
										<span class="icon i-ri-draggable inline-block icon-dark opacity-60 group-hover:opacity-100 group-hover:icon-dark rounded-sm" />
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
	import type { Wochentag, List, StundenplanPausenzeit, StundenplanKlasse, StundenplanPausenaufsicht, StundenplanLehrer } from "@core";
	import { ArrayList } from "@core";

	type PausenzeitBereichTyp = {pauseID: number; aufsichtsbereichID: number; typ: number, lehrerID?: number};

	const props = defineProps<StundenplanPausenProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const _klasse = ref<StundenplanKlasse | undefined>(undefined);
	const dragLehrer = ref<StundenplanLehrer|undefined>(undefined);
	const dragFromPausenzeit = ref<PausenzeitBereichTyp>();
	const dragOverPausenzeit = ref<PausenzeitBereichTyp>();

	function onDrag(data: StundenplanLehrer|undefined, fromPausenzeit?: PausenzeitBereichTyp) {
		dragLehrer.value = data;
		dragFromPausenzeit.value = fromPausenzeit;
	}

	function setDragOver(pauseID: number, aufsichtsbereichID: number, typ: number) {
		if (dragOverPausenzeit.value !== undefined) {
			const { pauseID: pID, aufsichtsbereichID: aID, typ: t } = dragOverPausenzeit.value;
			if (pauseID === pID && aufsichtsbereichID === aID && typ === t)
				return;
		}
		dragOverPausenzeit.value = { pauseID, aufsichtsbereichID, typ };
	}

	const dragOverAufsichten = computed<List<StundenplanPausenaufsicht>>(() => {
		if (dragOverPausenzeit.value !== undefined)
			return props.stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(dragOverPausenzeit.value.pauseID);
		return new ArrayList();
	});

	const dragOverAufsichtenBereich = computed<List<StundenplanPausenaufsicht>>(() => {
		const list = new ArrayList<StundenplanPausenaufsicht>();
		for (const a of dragOverAufsichten.value)
			for (const b of a.bereiche)
				if (b === dragOverPausenzeit.value?.aufsichtsbereichID)
					list.add(a);
		return list;
	})

	const dragOverAufsichtenBereichTyp = computed<List<StundenplanPausenaufsicht>>(() => {
		const list = new ArrayList<StundenplanPausenaufsicht>();
		for (const a of dragOverAufsichtenBereich.value)
			if (a.wochentyp === dragOverPausenzeit.value?.typ)
				list.add(a);
		return list;
	})

	const bereichGesperrt = computed<boolean>(() => {
		if (dragFromPausenzeit.value?.pauseID === dragOverPausenzeit.value?.pauseID)
			return false;
		for (const a of dragOverAufsichtenBereich.value)
			if (a.idLehrer === dragLehrer.value?.id && a.wochentyp === 0)
				return true;
		return false;
	})

	const bereichGesperrtTyp = computed<boolean>(() => {
		if (bereichGesperrt.value === true)
			return true;
		for (const a of dragOverAufsichtenBereichTyp.value)
			if (a.idLehrer === dragLehrer.value?.id && a.wochentyp === dragOverPausenzeit.value?.typ)
				return true;
		return false;
	})

	const isDraggingOver = (pauseID: number, aufsichtsbereichID: number, typ: number) => computed(() => {
		if (dragOverPausenzeit.value === undefined)
			return false;
		const { pauseID: pID, aufsichtsbereichID: aID, typ: t } = dragOverPausenzeit.value;
		return (pauseID === pID && aufsichtsbereichID === aID && typ === t);
	})

	function dragReset() {
		dragOverPausenzeit.value = undefined;
		dragFromPausenzeit.value = undefined;
		dragLehrer.value = undefined;
	}

	async function onDrop() {
		if (bereichGesperrtTyp.value || dragOverPausenzeit.value === undefined || dragLehrer.value === undefined)
			return dragReset();
		const { aufsichtsbereichID, pauseID, typ } = dragOverPausenzeit.value;
		if (dragFromPausenzeit.value !== undefined)
			await delAufsicht(dragLehrer.value.id, dragFromPausenzeit.value.aufsichtsbereichID, dragFromPausenzeit.value.typ, dragFromPausenzeit.value.pauseID);
		const bereiche = new ArrayList<number>();
		bereiche.add(aufsichtsbereichID);
		for (const aufsicht of dragOverAufsichten.value)
			if (aufsicht.idLehrer === dragLehrer.value.id) {
				bereiche.addAll(aufsicht.bereiche);
				await props.patchAufsicht({bereiche}, aufsicht.id);
				return dragReset();
			}
		await props.addAufsicht({idLehrer: dragLehrer.value.id, idPausenzeit: pauseID, bereiche, wochentyp: typ});
		return dragReset();
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

	.svws-ui-tr {
		@apply w-full;
	}

</style>
