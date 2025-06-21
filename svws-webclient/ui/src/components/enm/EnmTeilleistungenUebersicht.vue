<template>
	<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle"
		@keydown.down.prevent.stop="auswahlmanager.linkedListNext"
		@keydown.up.prevent.stop="auswahlmanager.linkedListPrevious"
		@keydown.right.prevent="nextColumn"
		@keydown.left.prevent="prevColumn">
		<thead class="svws-ui-thead cursor-pointer" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr" role="row">
				<template v-for="col of cols" :key="col.name">
					<template v-if="col.name === 'Teilleistung'">
						<template v-for="art of setTeilleistungsarten" :key="art">
							<td class="svws-ui-td" role="columnheader"> {{ manager.mapTeilleistungsarten.get(art)?.bezeichnung ?? '???' }} </td>
						</template>
					</template>
					<td v-else-if="colsVisible.get(col.kuerzel) ?? true" class="svws-ui-td" role="columnheader">
						<svws-ui-tooltip v-if="col.kuerzel !== col.name">
							{{ col.kuerzel }}
							<template #content>{{ col.name }}</template>
						</svws-ui-tooltip>
						<span v-else>{{ col.kuerzel }}</span>
					</td>
				</template>
				<td class="svws-ui-td" role="columnheader">
					<svws-ui-tooltip :hover="false" :show-arrow="false" position="top" class="h-full w-full">
						<span class="icon" :class="[...colsVisible.values()].some(c => c === false) ? 'i-ri-layout-column-fill' : 'i-ri-layout-column-line'" />
						<span class="icon i-ri-arrow-down-s-line" />
						<template #content>
							<ul class="min-w-[10rem] flex flex-col gap-0.5 pt-1">
								<template v-for="col of cols" :key="col.name">
									<li v-if="colsVisible.get(col.kuerzel) !== null">
										<svws-ui-checkbox :model-value="colsVisible.get(col.kuerzel) ?? false" @update:model-value="value => setColumnsVisible(colsVisible.set(col.kuerzel, value))">
											{{ col.kuerzel }}
										</svws-ui-checkbox>
									</li>
								</template>
							</ul>
						</template>
					</svws-ui-tooltip>
				</td>
			</tr>
		</thead>
		<tbody class="svws-ui-tbody h-full overflow-y-auto" role="rowgroup" aria-label="Tabelleninhalt">
			<template v-for="pair of auswahlmanager.liste" :key="pair">
				<tr class="svws-ui-tr h-10" role="row" :class="{ 'svws-clicked': auswahlmanager.auswahl === pair }"
					@click="setAuswahlLeistung(pair)"
					@keydown.tab="handleTabEvent($event, pair)"
					:ref="el => rowRefs.set(pair, el as HTMLElement)">
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Klasse') ?? true">
						{{ manager.mapKlassen.get(pair.b.klasseID)?.kuerzelAnzeige ?? '—' }}
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Name') ?? true">
						{{ pair.b.nachname }}, {{ pair.b.vorname }} ({{ pair.b.geschlecht }})
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Fach') ?? true">
						{{ manager.lerngruppeGetFachkuerzel(pair.a.lerngruppenID) }}
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Kurs') ?? true">
						{{ manager.lerngruppeGetKursbezeichnung(pair.a.lerngruppenID) }}
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Kursart') ?? true">
						{{ manager.leistungGetKursartAsString(pair.a) }}
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Lehrer') ?? true">
						{{ manager.lerngruppeGetFachlehrerOrNull(pair.a.lerngruppenID) }}
					</td>
					<template v-for="art of setTeilleistungsarten" :key="art">
						<td class="svws-ui-td" role="cell">
							<template v-for="teilleistung of manager.mapLeistungTeilleistungsartTeilleistung.getOrNull(pair.a.id, art) !== null ? [manager.mapLeistungTeilleistungsartTeilleistung.getOrNull(pair.a.id, art)!] : []" :key="teilleistung">
								<input id="teilleistung.id"
									v-if="manager.lerngruppeIstFachlehrer(pair.a.lerngruppenID)"
									class="w-full column-focussable"
									v-model="teilleistung.note"
									:class="{ contentFocusField: (auswahlmanager.auswahl === pair) && (art === 5) }"
									@focusin="tabToUnselectedLeistung(pair, $event.target)"
									@change="doPatchLeistungNote(teilleistung, { note: (Note.fromKuerzel(teilleistung.note).daten(props.manager.schuljahr)?.kuerzel ?? null), id: teilleistung.id })">
								<div v-else class="grade-field column-focussable"
									tabindex="0"
									@focusin="tabToUnselectedLeistung(pair, $event.target)">
									{{ teilleistung.note ?? "-" }}
								</div>
							</template>
						</td>
					</template>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Quartal') ?? true">
						<input v-if="manager.lerngruppeIstFachlehrer(pair.a.lerngruppenID)"
							class="w-full column-focussable"
							v-model="pair.a.noteQuartal"
							@focusin="tabToUnselectedLeistung(pair, $event.target)"
							:class="{ contentFocusField: auswahlmanager.auswahl === pair}"
							@change="() => doPatchLeistungNote(pair.a, { noteQuartal: (Note.fromKuerzel(pair.a.noteQuartal).daten(props.manager.schuljahr)?.kuerzel ?? null), id: pair.a.id })">
						<div v-else class="column-focussable w-full h-full contentFocusField"
							tabindex="0"
							@focusin="tabToUnselectedLeistung(pair, $event.target)">
							{{ pair.a.noteQuartal ?? "-" }}
						</div>
					</td>
					<td v-if="colsVisible.get('Note') ?? true" class="svws-ui-td" role="cell">
						<input v-if="manager.lerngruppeIstFachlehrer(pair.a.lerngruppenID)"
							class="w-full column-focussable"
							:class="{ contentFocusField: auswahlmanager.auswahl === pair }"
							v-model="pair.a.note" @focusin="tabToUnselectedLeistung(pair, $event.target)"
							@change="() => doPatchLeistungNote(pair.a, { note: (Note.fromKuerzel(pair.a.note).daten(props.manager.schuljahr)?.kuerzel ?? null), id: pair.a.id })">
						<div v-else class="column-focussable w-full h-full"
							tabindex="0"
							@focusin="tabToUnselectedLeistung(pair, $event.target)">
							{{ pair.a.note ?? "-" }}
						</div>
					</td>
					<td class="svws-ui-td" role="cell" />
				</tr>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref, watch } from 'vue';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';
	import type { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';
	import type { PairNN } from '../../../../core/src/asd/adt/PairNN';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import { ENMTeilleistung } from '../../../../core/src/core/data/enm/ENMTeilleistung';
	import { Note } from '../../../../core/src/asd/types/Note';
	import { HashSet } from '../../../../core/src/java/util/HashSet';

	const props = defineProps<EnmTeilleistungenProps>();
	const rowRefs = ref(new Map<PairNN<ENMLeistung, ENMSchueler>, HTMLElement>());
	const currentColumn = ref(0);

	const cols = [
		{ kuerzel: "Klasse", name: "Klasse", width: "1fr" },
		{ kuerzel: "Name", name: "Name, Vorname", width: "3fr" },
		{ kuerzel: "Fach", name: "Fach", width: "1fr" },
		{ kuerzel: "Kurs", name: "Kurs", width: "1fr" },
		{ kuerzel: "Kursart", name: "Kursart", width: "1fr" },
		{ kuerzel: "Lehrer", name: "Fachlehrer", width: "1fr" },
		{ kuerzel: "Teilleistung", name: "Teilleistung", width: "" },
		{ kuerzel: "Quartal", name: "Quartalsnote", width: "1fr" },
		{ kuerzel: "Note", name: "Note", width: "1fr" },
	];

	const colsVisible = computed<Map<string, boolean|null>>({
		get: () => props.columnsVisible(),
		set: (value) => void props.setColumnsVisible(value),
	});

	const columnsComputed = computed<HTMLElement[]>(() => {
		const htmlElement = rowRefs.value.get(props.auswahlmanager.auswahl);
		if (htmlElement !== undefined)
			return Array.from(htmlElement.querySelectorAll(".column-focussable"));
		return [];
	});

	function setAuswahlLeistung(value: PairNN<ENMLeistung, ENMSchueler>) {
		if (currentColumn.value === -1)
			currentColumn.value = 0;
		props.auswahlmanager.auswahl = value;
	}

	function selectInputContent(ele: EventTarget) {
		if (ele instanceof HTMLInputElement)
			ele.select();
	}

	function nextColumn() {
		if (currentColumn.value === columnsComputed.value.length - 1)
			currentColumn.value = 0;
		else
			currentColumn.value += 1;
		columnsComputed.value[currentColumn.value].focus();
	}

	function prevColumn() {
		if (currentColumn.value === 0)
			currentColumn.value = columnsComputed.value.length - 1;
		else
			currentColumn.value -= 1;
		columnsComputed.value[currentColumn.value].focus();
	}

	async function doPatchLeistungNote(leistung: ENMLeistung | ENMTeilleistung, patchObject: Partial<ENMLeistung> | Partial<ENMTeilleistung>) {
		let success = false;
		if (leistung instanceof ENMTeilleistung)
			success = await props.patchTeilleistung(patchObject);
		else
			success = await props.patchLeistung(patchObject);
		if (success)
			Object.assign(leistung, patchObject);
	}

	function tabToUnselectedLeistung(leistung: PairNN<ENMLeistung, ENMSchueler>, ele: EventTarget | null) {
		const newRowHtml = rowRefs.value.get(leistung);
		if (newRowHtml === undefined)
			return;
		const newRowArray = Array.from(newRowHtml.querySelectorAll(".column-focussable"));
		const columnIndex = newRowArray.indexOf(ele as HTMLElement);
		if (columnIndex !== -1)
			currentColumn.value = columnIndex;
		if (!props.manager.compareAuswahlLeistung(leistung, props.auswahlmanager.auswahl))
			setAuswahlLeistung(leistung);
		if(ele)
			selectInputContent(ele);
	}

	function handleTabEvent(eve: KeyboardEvent, pair: PairNN<ENMLeistung, ENMSchueler>) {
		if (eve.shiftKey) {
			if (currentColumn.value === 0) {
				eve.preventDefault();
				props.auswahlmanager.linkedListPrevious();
				currentColumn.value = columnsComputed.value.length - 1;
				columnsComputed.value[currentColumn.value].focus();
			} else
				currentColumn.value -= 1;
		} else {
			if (currentColumn.value === columnsComputed.value.length - 1) {
				if (!props.auswahlmanager.linkedListHasNext())
					return;
				eve.preventDefault();
				props.auswahlmanager.linkedListNext();
				currentColumn.value = 0;
				columnsComputed.value[currentColumn.value].focus();
			} else
				currentColumn.value += 1;
		}
	}

	const setTeilleistungsarten = computed(() => {
		const set = props.manager.mapLerngruppeTeilleistungsarten.get(props.auswahlmanager.filter.id);
		if (set === null)
			return new HashSet<number>();
		return set;
	});

	const gridTemplateColumnsComputed = computed<string>(() => {
		let frs = " ";
		for (const art of setTeilleistungsarten.value)
			frs += "1fr ";
		return cols.filter(c => colsVisible.value.get(c.kuerzel) ?? true).map(c => c.width).join(" ") + frs + " 5em";
	});

	watch(
		() => props.auswahlmanager.auswahl,
		async () => {
			if (currentColumn.value !== -1)
				columnsComputed.value[currentColumn.value].focus();
		}
	);
	onMounted(() => currentColumn.value = -1);

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

</style>
