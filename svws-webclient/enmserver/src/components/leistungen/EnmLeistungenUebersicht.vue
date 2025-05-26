<template>
	<table class="svws-ui-table svws-clickable overflow-y-auto" role="table" aria-label="Tabelle"
		@keydown.down.prevent.stop="auswahlmanager.linkedListNext"
		@keydown.up.prevent.stop="auswahlmanager.linkedListPrevious"
		@keydown.right.prevent="nextColumn"
		@keydown.left.prevent="prevColumn">
		<thead class="svws-ui-thead cursor-pointer" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr" role="row">
				<template v-for="col of cols" :key="col.name">
					<td class="svws-ui-td" role="columnheader" v-if="colsVisible.get(col.kuerzel) ?? true">
						<svws-ui-tooltip v-if="col.kuerzel !== col.name">
							{{ col.kuerzel }}
							<template #content>{{ col.name }}</template>
						</svws-ui-tooltip>
						<span v-else>{{ col.kuerzel }}</span>
					</td>
				</template>
				<td class="svws-ui-td" role="columnheader">
					<svws-ui-tooltip :hover="false" :show-arrow="false" position="top" class="h-full w-full">
						<span class="icon" :class="[...colsVisible.values()].some(c => c === false) ? 'i-ri-layout-column-fill' : 'i-ri-layout-column-line'" /><span class="icon i-ri-arrow-down-s-line" />
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
		<tbody class="svws-ui-tbody h-full" role="rowgroup" aria-label="Tabelleninhalt">
			<template v-for="pair of auswahlmanager.liste" :key="pair">
				<tr class="svws-ui-tr h-10" role="row" :class="{ 'svws-clicked': auswahlmanager.auswahl === pair }"
					@click="setAuswahlLeistung(pair)"
					@keydown.tab="handleTabEvent($event, pair)" :ref="el => rowRefs.set(pair, el as HTMLElement)">
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
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Quartal') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('Quartal') }">
						<input v-if="auswahlmanager.auswahl === pair && manager.lerngruppeIstFachlehrer(pair.a.lerngruppenID)"
							type="text"
							class="w-full column-focussable"
							v-model="pair.a.noteQuartal"
							@focusin="tabToUnselectedLeistung(pair, $event.target)"
							:class="{ contentFocusField: (auswahlmanager.auswahl === pair && (currentColumn === colsFocussable.indexOf('Quartal')) || currentColumn === -1)}"
							@change="() => doPatchLeistungNote(pair.a, Note.fromKuerzel(pair.a.noteQuartal).daten(props.manager.schuljahr)?.kuerzel,{ noteQuartal: (Note.fromKuerzel(pair.a.noteQuartal).daten(props.manager.schuljahr)?.kuerzel ?? null) })">
						<div v-else class="column-focussable w-full h-full contentFocusField"
							tabindex="0"
							@focusin="tabToUnselectedLeistung(pair, $event.target)">
							{{ pair.a.noteQuartal ?? "-" }}
						</div>
					</td>
					<td v-if="colsVisible.get('Note') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('Note') }" class="svws-ui-td" role="cell">
						<input v-if="auswahlmanager.auswahl === pair && manager.lerngruppeIstFachlehrer(pair.a.lerngruppenID)"
							type="text"
							class="w-full column-focussable"
							:class="{ contentFocusField: auswahlmanager.auswahl === pair && currentColumn === colsFocussable.indexOf('Note') }"
							v-model="pair.a.note" @focusin="tabToUnselectedLeistung(pair, $event.target)"
							@change="() => doPatchLeistungNote(pair.a, Note.fromKuerzel(pair.a.note).daten(props.manager.schuljahr)?.kuerzel,{ note: (Note.fromKuerzel(pair.a.note).daten(props.manager.schuljahr)?.kuerzel ?? null) })">
						<div v-else class="column-focussable w-full h-full"
							tabindex="0"
							@focusin="tabToUnselectedLeistung(pair, $event.target)">
							{{ pair.a.note ?? "-" }}
						</div>
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Mahnung') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('Mahnung') }">
						<template v-if="manager.lerngruppeIstFachlehrer(pair.a.lerngruppenID)">
							<input type="checkbox" class="accent-ui-danger" :class="{'column-focussable': pair.a.mahndatum === null, contentFocusField: (auswahlmanager.auswahl === pair) && (currentColumn === colsFocussable.indexOf('Mahnung'))}" tabindex="0"
								:disabled="pair.a.mahndatum !== null" v-model="pair.a.istGemahnt" @change="() => doPatchLeistung(pair.a, { istGemahnt: pair.a.istGemahnt })" @focusin="tabToUnselectedLeistung(pair, $event.target)">
							<span v-if="pair.a.mahndatum !== null" class="column-focussable" tabindex="0"> {{ pair.a.mahndatum }} </span>
						</template>
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('FS') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('FS') }">
						<input v-if="manager.fehlstundenFachbezogen(pair.b)" v-model="pair.a.fehlstundenFach" class="column-focussable w-full"
							:class="{ contentFocusField: auswahlmanager.auswahl === pair && currentColumn === colsFocussable.indexOf('FS') }"
							@focusin="tabToUnselectedLeistung(pair, $event.target)"
							@change="doPatchLeistung(pair.a, { fehlstundenFach: (isNaN(Number(pair.a.fehlstundenFach)) ? 0 : Number(pair.a.fehlstundenFach)) })">
						<span v-else class="w-full column-focussable" tabindex="0">—</span>
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('FSU') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('FSU') }">
						<input v-if="manager.fehlstundenFachbezogen(pair.b)" v-model="pair.a.fehlstundenUnentschuldigtFach" class="column-focussable w-full"
							:class="{ contentFocusField: auswahlmanager.auswahl === pair && currentColumn === colsFocussable.indexOf('FSU') }"
							@focusin="tabToUnselectedLeistung(pair, $event.target)"
							@change="doPatchLeistung(pair.a, { fehlstundenUnentschuldigtFach: (isNaN(Number(pair.a.fehlstundenUnentschuldigtFach)) ? 0 : Number(pair.a.fehlstundenUnentschuldigtFach)) })">
						<span v-else class="w-full column-focussable" tabindex="0">—</span>
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Bemerkung') ?? true"
						:class="{ 'bg-ui-selected-secondary text-ui-onselected-secondary': floskelEditorVisible && (auswahlmanager.auswahl === pair), 'bg-ui-selected': currentColumn === colsFocussable.indexOf('Bemerkung') }">
						<span class="text-ellipsis overflow-hidden whitespace-nowrap column-focussable w-full" tabindex="0" @keydown.enter.prevent="focusFloskelEditor"
							:class="{ contentFocusField: (auswahlmanager.auswahl === pair) && (currentColumn === colsFocussable.indexOf('Bemerkung')) }"
							@focusin="tabToUnselectedLeistung(pair, $event.target)">{{ pair.a.fachbezogeneBemerkungen ?? "-" }}</span>
					</td>
					<td class="svws-ui-td" role="cell" />
				</tr>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed, onMounted, nextTick, ref, watch } from 'vue';
	import type { EnmLeistungenProps } from './EnmLeistungenProps';
	import type { ENMLeistung } from '@core/core/data/enm/ENMLeistung';
	import type { PairNN } from '@core/asd/adt/PairNN';
	import type { ENMSchueler } from '@core/core/data/enm/ENMSchueler';
	import { Note } from '@core/asd/types/Note';

	const props = defineProps<EnmLeistungenProps>();
	const rowRefs = ref(new Map<PairNN<ENMLeistung, ENMSchueler>, HTMLElement>());
	const currentColumn = ref(-1);

	const cols = [
		{ kuerzel: "Klasse", name: "Klasse", width: "1fr" },
		{ kuerzel: "Name", name: "Name, Vorname", width: "3fr" },
		{ kuerzel: "Fach", name: "Fach", width: "1fr" },
		{ kuerzel: "Kurs", name: "Kurs", width: "1fr" },
		{ kuerzel: "Kursart", name: "Kursart", width: "1fr" },
		{ kuerzel: "Lehrer", name: "Fachlehrer", width: "1fr" },
		{ kuerzel: "Quartal", name: "Quartalsnote", width: "1fr" },
		{ kuerzel: "Note", name: "Note", width: "1fr" },
		{ kuerzel: "Mahnung", name: "Mahnung", width: "1fr" },
		{ kuerzel: "FS", name: "Fehlstunden", width: "1fr" },
		{ kuerzel: "FSU", name: "Fehlstunden (unentschuldigt)", width: "1fr" },
		{ kuerzel: "Bemerkung", name: "Fachbezogene Bemerkungen", width: "3fr" },
	];

	const colsFocussable = ["Quartal", "Note", "Mahnung", "FS", "FSU", "Bemerkung"];

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

	async function focusFloskelEditor() {
		await props.setFloskelEditorVisible(true).then(() => (document.getElementsByClassName("floskel-input")[0] as HTMLElement).focus());
	}

	async function doPatchLeistung(leistung: ENMLeistung, patch: Partial<ENMLeistung>) {
		patch.id = leistung.id;
		const success = await props.patchLeistung(patch);
		if (success)
			Object.assign(leistung, patch);
	}

	async function doPatchLeistungNote(leistung: ENMLeistung, newValue: string | null | undefined, patchObject: Partial<ENMLeistung>) {
		if (newValue === null || newValue === undefined || newValue === "")
			return;
		else
			await doPatchLeistung(leistung, patchObject);
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

	const gridTemplateColumnsComputed = computed<string>(() => {
		return cols.filter(c => colsVisible.value.get(c.kuerzel) ?? true).map(c => c.width).join(" ") + " 3.5em";
	});

	watch(
		() => props.auswahlmanager.auswahl,
		async () => {
			if (currentColumn.value !== -1) {
				await nextTick(() => columnsComputed.value[currentColumn.value].focus())
			}
		}
	)

	onMounted(() => currentColumn.value = -1)

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

</style>
