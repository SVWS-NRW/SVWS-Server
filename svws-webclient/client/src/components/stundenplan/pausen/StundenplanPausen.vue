<template>
	<div class="page page-flex-row select-none">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
				<div class="ml-4 flex gap-0.5 items-center leading-none select-none">
					<div class="text-button font-bold mr-1 -mt-px">Klasse:</div>
					<svws-ui-select headless title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeAsList()" :item-text="i => i.kuerzel" autocomplete
						:item-filter="(i, text) => i.filter(k=>k.kuerzel.includes(text.toLocaleLowerCase()))" :item-sort="() => 0" type="transparent" removable focus-class-sub-nav />
				</div>
			</svws-ui-sub-nav>
		</Teleport>
		<div v-if="hatUpdateKompetenz" class="min-w-72 h-full overflow-y-auto w-72 border-2 rounded-xl relative" :class="[dragFromPausenzeit === undefined ? 'border-ui-neutral' : 'border-ui-danger ring-4 ring-ui-danger border-dashed']" @drop.stop="onDrop" @dragover.prevent="dropZone = true" @dragleave="setDragLeave">
			<div class="fixed flex items-center justify-center h-3/4 w-64 z-20 pointer-events-none"><span :class="dragFromPausenzeit === undefined ? '':'icon-lg icon-ui-danger-secondary i-ri-delete-bin-line scale-[4]'" /></div>
			<div class="svws-ui-table" style="scrollbar-gutter: stable; scrollbar-width: thin; scrollbar-color: rgba(0,0,0,0.2) transparent;">
				<div class="svws-ui-tbody">
					<div v-for="lehrer in stundenplanManager().lehrerGetMengeAsList()" :key="lehrer.id" class="svws-ui-tr w-full" :class="dragLehrer ? 'cursor-grabbing' : 'cursor-grab'">
						<div class="svws-ui-td border-0!">
							<div class="svws-ui-badge select-none group flex place-items-center w-full" @dragstart="onDrag(lehrer)" @dragend="dragEnd" draggable="true">
								<span class="icon i-ri-draggable icon-ui-75 opacity-60 group-hover:opacity-100 group-hover:icon-ui-75 rounded-xs" />
								<span class="truncate grow p-1"> {{ lehrer.kuerzel }} ({{ lehrer.vorname[0] }}. {{ lehrer.nachname }})</span>
								<span class="rounded-lg bg-ui-brand text-ui-onbrand px-1 py-0.5 text-sm break-normal" v-if="mapLehrerPausenaufsichten.get(lehrer.id)?.size()">
									<svws-ui-tooltip>
										{{ stundenplanManager().lehrerGetPausenaufsichtMinuten(lehrer.id, -1) }}/{{ stundenplanManager().lehrerGetPausenaufsichtAnzahl(lehrer.id, -1) }}
										<template #content>
											Gesamtdauer der Aufsichten in Minuten: {{ stundenplanManager().lehrerGetPausenaufsichtMinuten(lehrer.id, -1) }}
											<br>Anzahl der Aufsichten: {{ stundenplanManager().lehrerGetPausenaufsichtAnzahl(lehrer.id, -1) }}
										</template>
									</svws-ui-tooltip>
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="grow min-w-fit h-full overflow-y-auto">
			<div v-if="stundenplanManager().pausenzeitGetMengeAsList().size()" class="svws-ui-stundenplan">
				<!-- Die Überschriften des Stundenplan -->
				<div class="svws-ui-stundenplan--head">
					<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
					<div v-for="wochentag in stundenplanManager().pausenzeitGetWochentageAlsEnumRange()" :key="wochentag.id" class="font-bold text-center inline-flex items-center justify-center">
						<div> {{ wochentag.beschreibung }} </div>
					</div>
				</div>
				<!-- Die Daten des Stundenplans -->
				<div class="svws-ui-stundenplan--body">
					<!-- Zeige die Unterrichte und Pausenaufsichten des Stundenplans -->
					<div v-for="wochentag in stundenplanManager().pausenzeitGetWochentageAlsEnumRange()" :key="wochentag.id">
						<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
						<div v-for="pause in getPausenzeitenWochentag(wochentag).value" :key="pause.hashCode()" class="border rounded-md my-2 mx-1" :style="posPause(pause)">
							<div class="font-bold px-2 py-1" :class="{'bg-ui-brand': lehrerAufsichten.get(pause.id)}"> {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitBeginn(pause.id) }} – {{ stundenplanManager().pausenzeitGetByIdStringOfUhrzeitEnde(pause.id) }} {{ pause.bezeichnung }} </div>
							<div v-if="!pause.klassen.isEmpty()" class="text-sm px-2 mb-2 font-bold"> {{ [...pause.klassen].map(k => " " + stundenplanManager().klasseGetByIdOrException(k).kuerzel).toString() }} </div>
							<!-- Zeige Wochentypenübersicht nur an, wenn mehr als jede Woche vorhanden ist -->
							<div v-if="wochentypen.size() > 1" class="grid p-0.5 font-bold h-full" :style="`grid-template-columns: 4rem repeat(${wochentypen.size()}, minmax(min-content, 1fr))`">
								<div>Bereich</div>
								<div v-for="typ in wochentypen" :key="typ" class="h-full rounded-xs text-center" :class="{'bg-ui-success text-ui-onsuccess': pause.id === dragOverPausenzeit?.pauseID && typ === dragOverPausenzeit.typ && !bereichGesperrt(pause.id, dragOverPausenzeit.aufsichtsbereichID).value}">
									{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(typ) }}
								</div>
							</div>
							<div v-for="aufsichtsbereich in stundenplanManager().aufsichtsbereichGetMengeAsList()" :key="aufsichtsbereich.id" class="svws-ui-stundenplan--pausen-aufsicht"
								:class="{
									'bg-ui-selected text-ui-onselected': !isDraggingOver(pause.id, aufsichtsbereich.id).value && mapAufsichtBereichTyp.containsKey1AndKey2(lehrerAufsichten.get(pause.id)?.id || -1, aufsichtsbereich.id),
									'bg-ui-danger text-ui-ondanger': bereichGesperrt(pause.id, aufsichtsbereich.id).value,
									'bg-ui-success text-ui-onsuccess': isDraggingOver(pause.id, aufsichtsbereich.id).value && !bereichGesperrt(pause.id, aufsichtsbereich.id).value}"
								:style="`grid-template-columns: 4rem repeat(${wochentypen.size()}, minmax(min-content, 1fr))`">
								<div> {{ aufsichtsbereich.kuerzel }} </div>
								<div v-for="typ in wochentypen" :key="typ"
									@drop.stop="onDrop" class="h-full rounded-xs" @dragover.prevent="setDragOver(pause.id, aufsichtsbereich.id, typ)" @dragleave="setDragLeave"
									:class="{'bg-ui-selected-secondary text-ui-onselected-secondary': mapAufsichtBereichTyp.getOrNull(lehrerAufsichten.get(pause.id)?.id || -1, aufsichtsbereich.id, typ)}">
									<div v-for="lehrer in stundenplanManager().lehrerGetMengeByPausenzeitIdAndAufsichtsbereichIdAndWochentypAndInklusive(pause.id, aufsichtsbereich.id, typ, false)"
										:key="lehrer.id" class="rounded-md group flex place-items-center" :class="{'cursor-grab': !dragLehrer && hatUpdateKompetenz, 'hover:bg-ui-25 hover:text-ui-75': hatUpdateKompetenz}"
										@dragstart.stop="onDrag(lehrer, {pauseID: pause.id, aufsichtsbereichID: aufsichtsbereich.id, typ})" :draggable="hatUpdateKompetenz" @dragend="dragEnd">
										<span v-if="hatUpdateKompetenz" class="icon i-ri-draggable icon-ui-75 opacity-60 group-hover:opacity-100 group-hover:icon-ui-75 rounded-xs"
											:class="{'icon-ui-onsuccess': isDraggingOver(pause.id, aufsichtsbereich.id).value || bereichGesperrt(pause.id, aufsichtsbereich.id).value}" />
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
	import type { Wochentag, List, StundenplanPausenzeit, StundenplanKlasse, StundenplanPausenaufsicht, StundenplanLehrer} from "@core";
	import { StundenplanPausenaufsichtBereich, StundenplanPausenaufsichtBereichUpdate, HashMap3D, ArrayList, BenutzerKompetenz } from "@core";
	import { useRegionSwitch } from "@ui";
	import type { StundenplanPausenProps } from "./StundenplanPausenProps";

	type PausenzeitBereichTyp = {pauseID: number; aufsichtsbereichID: number; typ: number, lehrerID?: number};

	const props = defineProps<StundenplanPausenProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const _klasse = ref<StundenplanKlasse>();
	const dragLehrer = ref<StundenplanLehrer>();
	const dragFromPausenzeit = ref<PausenzeitBereichTyp>();
	const dragOverPausenzeit = ref<PausenzeitBereichTyp>();
	const dropZone = ref<boolean>(false);

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.STUNDENPLAN_AENDERN));

	function onDrag(data: StundenplanLehrer|undefined, fromPausenzeit?: PausenzeitBereichTyp) {
		dragLehrer.value = data;
		dragFromPausenzeit.value = fromPausenzeit;
	}

	function setDragOver(pauseID: number, aufsichtsbereichID: number, typ: number) {
		dropZone.value = true;
		if (dragOverPausenzeit.value !== undefined) {
			const { pauseID: pID, aufsichtsbereichID: aID, typ: t } = dragOverPausenzeit.value;
			if (pauseID === pID && aufsichtsbereichID === aID && typ === t)
				return;
		}
		dragOverPausenzeit.value = { pauseID, aufsichtsbereichID, typ };
	}

	function setDragLeave() {
		dropZone.value = false;
		dragOverPausenzeit.value = undefined;
	}

	const isDraggingOver = (pauseID: number, aufsichtsbereichID: number, typ?: number) => computed(() => {
		if (dragOverPausenzeit.value === undefined)
			return false;
		const { pauseID: pID, aufsichtsbereichID: aID, typ: t } = dragOverPausenzeit.value;
		if (typ !== undefined)
			return (pauseID === pID && aufsichtsbereichID === aID && typ === t);
		return (pauseID === pID && aufsichtsbereichID === aID);
	})

	const bereichGesperrt = (pauseID: number, bereichID: number) => computed(() => {
		if (!isDraggingOver(pauseID, bereichID).value || (dragOverPausenzeit.value === undefined))
			return false;
		const aufsicht = lehrerAufsichten.value.get(pauseID);
		if (aufsicht === undefined)
			return false;
		if ((dragFromPausenzeit.value !== undefined) && (dragFromPausenzeit.value.aufsichtsbereichID === bereichID) && (dragFromPausenzeit.value.pauseID === pauseID)) {
			if (dragFromPausenzeit.value.typ === dragOverPausenzeit.value.typ)
				return true;
			if (mapAufsichtBereichTyp.value.getNonNullValuesOfMap3AsList(aufsicht.id, bereichID).size() < 2)
				return false;
			if ((dragFromPausenzeit.value.aufsichtsbereichID === dragOverPausenzeit.value.aufsichtsbereichID) && (dragOverPausenzeit.value.typ === 0))
				return true;
			for (const b of mapAufsichtBereichTyp.value.getNonNullValuesOfMap3AsList(aufsicht.id, bereichID))
				if (b.wochentyp === dragOverPausenzeit.value.typ && b.idAufsichtsbereich === dragOverPausenzeit.value.aufsichtsbereichID)
					return true;
			return false;
		}
		const typ = dragOverPausenzeit.value.typ;
		const typX = mapAufsichtBereichTyp.value.getOrNull(aufsicht.id, bereichID, typ);
		if (typX)
			return true;
		const typ0 = mapAufsichtBereichTyp.value.getOrNull(aufsicht.id, bereichID, 0);
		if (typ0)
			return true;
		return ((isDraggingOver(pauseID, bereichID, 0).value) && (mapAufsichtBereichTyp.value.containsKey1AndKey2(aufsicht.id, bereichID)))
	})

	function dragEnd() {
		if (!dropZone.value)
			dragReset();
	}

	function dragReset() {
		dragOverPausenzeit.value = undefined;
		dragFromPausenzeit.value = undefined;
		dragLehrer.value = undefined;
	}

	async function onDrop() {
		if (dragOverPausenzeit.value && bereichGesperrt(dragOverPausenzeit.value.pauseID, dragOverPausenzeit.value.aufsichtsbereichID).value)
			return dragReset();
		const update = new StundenplanPausenaufsichtBereichUpdate()
		if (dragFromPausenzeit.value !== undefined) {
			const { aufsichtsbereichID, pauseID, typ } = dragFromPausenzeit.value;
			const aufsichtFrom = lehrerAufsichten.value.get(pauseID);
			if (aufsichtFrom === undefined)
				return dragReset();
			for (const b of aufsichtFrom.bereiche)
				if (b.idAufsichtsbereich === aufsichtsbereichID && b.wochentyp === typ)
					update.listEntfernen.add(b);
		}
		if ((dragOverPausenzeit.value !== undefined) && (dragLehrer.value !== undefined)) {
			const { aufsichtsbereichID, pauseID, typ } = dragOverPausenzeit.value;
			const bereichNeu = new StundenplanPausenaufsichtBereich();
			bereichNeu.idAufsichtsbereich = aufsichtsbereichID;
			bereichNeu.wochentyp = typ;
			const aufsicht = lehrerAufsichten.value.get(pauseID);
			bereichNeu.idPausenaufsicht = aufsicht?.id ?? -1;
			update.listHinzuzufuegen.add(bereichNeu);
		}
		await props.updateAufsichtBereich(update, dragOverPausenzeit.value?.pauseID, dragLehrer.value?.id);
		return dragReset();
	}

	const wochentypen = computed<List<number>>(() => {
		let modell = props.stundenplanManager().getWochenTypModell();
		if (modell <= 1)
			modell = 0;
		const result = new ArrayList<number>();
		for (let n = 0; n <= modell; n++)
			result.add(n);
		return result;
	})

	const klasse = computed<StundenplanKlasse | undefined>({
		get: () => {
			if (_klasse.value !== undefined)
				return props.stundenplanManager().klasseGetByIdOrException(_klasse.value.id);
			return _klasse.value;
		},
		set: (value) => _klasse.value = value,
	});

	const lehrerAufsichten = computed<Map<number, StundenplanPausenaufsicht>>(() => {
		const map = new Map<number, StundenplanPausenaufsicht>();
		if (dragLehrer.value === undefined)
			return map;
		for (const aufsicht of props.stundenplanManager().pausenaufsichtGetMengeByLehrerId(dragLehrer.value.id))
			map.set(aufsicht.idPausenzeit, aufsicht);
		return map;
	})

	const mapAufsichtBereichTyp = computed<HashMap3D<number, number, number, StundenplanPausenaufsichtBereich>>(() => {
		const map = new HashMap3D<number, number, number, StundenplanPausenaufsichtBereich>();
		for (const aufsicht of lehrerAufsichten.value.values())
			for (const e of aufsicht.bereiche)
				map.put(e.idPausenaufsicht, e.idAufsichtsbereich, e.wochentyp, e);
		return map;
	})

	const getPausenzeitenWochentag = (wochentag: Wochentag) => computed<StundenplanPausenzeit[]>(() => {
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

	const mapLehrerPausenaufsichten = computed(() => {
		const map = new Map<number, List<StundenplanPausenaufsicht>>();
		for (const l of props.stundenplanManager().lehrerGetMengeAsList())
			map.set(l.id, new ArrayList<StundenplanPausenaufsicht>());
		for (const aufsicht of props.stundenplanManager().pausenaufsichtGetMengeAsList())
			map.get(aufsicht.idLehrer)?.add(aufsicht);
		return map;
	})

</script>
