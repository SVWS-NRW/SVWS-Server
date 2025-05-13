<template>
	<table class="svws-ui-table svws-clickable overflow-y-auto" role="table" aria-label="Tabelle"
		@keydown.down.prevent.stop="manager.auswahlLeistungNaechste()" @keydown.up.prevent.stop="manager.auswahlLeistungVorherige()"
		@keydown.right.prevent="nextColumn" @keydown.left.prevent="prevColumn">
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
			<template v-for="(schueler, indexSchueler) of manager.lerngruppenAuswahlGetSchuelerMitLeistungsdaten()" :key="schueler">
				<template v-for="(leistung, indexLeistung) of manager.leistungenGetOfSchueler(schueler.id)" :key="leistung">
					<tr class="svws-ui-tr h-10" role="row" :class="{ 'svws-clicked': manager.auswahlLeistung.leistung === leistung }"
						@click.capture.exact="setAuswahlLeistung({ indexSchueler, indexLeistung, leistung })"
						@keydown.tab="handleTabEvent($event, leistung.id)" :ref="el => rowRefs.set(leistung.id, {
							element: (el as HTMLElement),
							isLastRow: (manager.lerngruppenAuswahlGetSchuelerMitLeistungsdaten().getLast().id === schueler.id)
								&& (manager.leistungenGetOfSchueler(schueler.id).getLast().id === leistung.id) })">
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Klasse') ?? true">
							{{ manager.schuelerGetKlasse(schueler.id).kuerzelAnzeige }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Name') ?? true">
							{{ schueler.nachname }}, {{ schueler.vorname }} ({{ schueler.geschlecht }})
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Fach') ?? true">
							{{ manager.lerngruppeGetFachkuerzel(leistung.lerngruppenID) }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Kurs') ?? true">
							{{ manager.lerngruppeGetKursbezeichnung(leistung.lerngruppenID) }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Kursart') ?? true">
							{{ manager.leistungGetKursartAsString(leistung) }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Lehrer') ?? true">
							{{ manager.lerngruppeGetFachlehrerOrNull(leistung.lerngruppenID) }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Quartal') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('Quartal') }">
							<input type="text" v-if="manager.auswahlLeistung?.leistung === leistung && manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" class="w-full column-focussable" v-model="leistung.noteQuartal"
								@focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)" :class="{ contentFocusField: (manager.auswahlLeistung?.leistung === leistung && (currentColumn === colsFocussable.indexOf('Quartal')) || currentColumn === -1)}"
								@change="() => doPatchLeistungNote(leistung, Note.fromKuerzel(leistung.noteQuartal).daten(props.manager.schuljahr)?.kuerzel,{ noteQuartal: (Note.fromKuerzel(leistung.noteQuartal).daten(props.manager.schuljahr)?.kuerzel ?? null) })">
							<div v-else class="column-focussable w-full h-full" tabindex="0" @focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)"
								:class="{ contentFocusField: (!manager.currentListContainsAuswahl(manager.auswahlLeistung) && indexSchueler === 0) }">
								{{ leistung.noteQuartal ?? "-" }}
							</div>
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Note') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('Note') }">
							<input type="text" v-if="manager.auswahlLeistung?.leistung === leistung && manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" class="w-full column-focussable" v-model="leistung.note" @focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)"
								@change="() => doPatchLeistungNote(leistung, Note.fromKuerzel(leistung.note).daten(props.manager.schuljahr)?.kuerzel,{ note: (Note.fromKuerzel(leistung.note).daten(props.manager.schuljahr)?.kuerzel ?? null) })"
								:class="{ contentFocusField: manager.auswahlLeistung?.leistung === leistung && currentColumn === colsFocussable.indexOf('Note') }">
							<div v-else class="column-focussable w-full h-full" tabindex="0" @focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)">
								{{ leistung.note ?? "-" }}
							</div>
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Mahnung') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('Mahnung') }">
							<template v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)">
								<input type="checkbox" class="accent-ui-danger" :class="{'column-focussable': leistung.mahndatum === null, contentFocusField: manager.auswahlLeistung?.leistung === leistung && currentColumn === colsFocussable.indexOf('Mahnung')}" tabindex="0"
									:disabled="leistung.mahndatum !== null" v-model="leistung.istGemahnt" @change="() => doPatchLeistung(leistung, { istGemahnt: leistung.istGemahnt })" @focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)">
								<span v-if="leistung.mahndatum !== null" class="column-focussable" tabindex="0"> {{ leistung.mahndatum }} </span>
							</template>
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('FS') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('FS') }">
							<input v-if="manager.fehlstundenFachbezogen(schueler)" v-model="leistung.fehlstundenFach" class="column-focussable w-full"
								:class="{ contentFocusField: manager.auswahlLeistung?.leistung === leistung && currentColumn === colsFocussable.indexOf('FS') }"
								@focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)"
								@change="doPatchLeistung(leistung, { fehlstundenFach: (isNaN(Number(leistung.fehlstundenFach)) ? 0 : Number(leistung.fehlstundenFach)) })">
							<span v-else class="w-full column-focussable" tabindex="0">—</span>
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('FSU') ?? true" :class="{ 'bg-ui-selected': currentColumn === colsFocussable.indexOf('FSU') }">
							<input v-if="manager.fehlstundenFachbezogen(schueler)" v-model="leistung.fehlstundenUnentschuldigtFach" class="column-focussable w-full"
								:class="{ contentFocusField: manager.auswahlLeistung?.leistung === leistung && currentColumn === colsFocussable.indexOf('FSU') }"
								@focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)"
								@change="doPatchLeistung(leistung, { fehlstundenUnentschuldigtFach: (isNaN(Number(leistung.fehlstundenUnentschuldigtFach)) ? 0 : Number(leistung.fehlstundenUnentschuldigtFach)) })">
							<span v-else class="w-full column-focussable" tabindex="0">—</span>
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Bemerkung') ?? true"
							:class="{ 'bg-ui-selected-secondary text-ui-onselected-secondary': floskelEditorVisible && (manager.auswahlLeistung.leistung === leistung), 'bg-ui-selected': currentColumn === colsFocussable.indexOf('Bemerkung') }">
							<span class="text-ellipsis overflow-hidden whitespace-nowrap column-focussable w-full" tabindex="0" @keydown.enter.prevent="focusFloskelEditor"
								:class="{ contentFocusField: manager.auswahlLeistung?.leistung === leistung && currentColumn === colsFocussable.indexOf('Bemerkung') }"
								@focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)">{{ leistung.fachbezogeneBemerkungen ?? "-" }}</span>
						</td>
						<td class="svws-ui-td" role="cell" />
					</tr>
				</template>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed, onMounted, nextTick, ref, watch } from 'vue';
	import type { EnmLeistungenProps } from './EnmLeistungenProps';
	import type { EnmLeistungAuswahl } from './EnmManager';
	import type { ENMLeistung } from '@core/core/data/enm/ENMLeistung';
	import { Note } from '@core/asd/types/Note';

	const props = defineProps<EnmLeistungenProps>();
	const rowRefs = ref(new Map<number, { element: HTMLElement | null, isLastRow: boolean }>());
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

	const columnsComputed = computed<HTMLElement[]>(
		() => {
			if (props.manager.auswahlLeistung.leistung) {
				const htmlElement = rowRefs.value.get(props.manager.auswahlLeistung.leistung.id)!.element;
				if (htmlElement !== null)
					return Array.from(htmlElement.querySelectorAll(".column-focussable"));
				else
					return [];
			} else
				return [];
		}
	);

	function setAuswahlLeistung(value: EnmLeistungAuswahl) {
		if (currentColumn.value === -1)
			currentColumn.value = 0;
		props.manager.auswahlLeistung = value;
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
		props.manager.update();
	}

	async function doPatchLeistungNote(leistung: ENMLeistung, newValue: string | null | undefined, patchObject: Partial<ENMLeistung>) {
		if (newValue === null || newValue === undefined || newValue === "")
			return;
		else
			await doPatchLeistung(leistung, patchObject);
	}

	function tabToUnselectedLeistung(indexSchueler: number, indexLeistung: number, leistung: ENMLeistung, ele: EventTarget | null) {
		const newRowHtml = rowRefs.value.get(leistung.id)?.element;
		if (newRowHtml === null || newRowHtml === undefined)
			return;
		const newRowArray = Array.from(newRowHtml.querySelectorAll(".column-focussable"));
		const columnIndex = newRowArray.indexOf(ele as HTMLElement);
		if (columnIndex !== -1)
			currentColumn.value = columnIndex;
		if (!props.manager.compareAuswahlLeistung({ indexSchueler, indexLeistung, leistung }, props.manager.auswahlLeistung))
			setAuswahlLeistung({ indexSchueler, indexLeistung, leistung });
		if(ele)
			selectInputContent(ele);
	}

	function handleTabEvent(eve: KeyboardEvent, idLeistung: number) {
		if (eve.shiftKey) {
			if (currentColumn.value === 0) {
				if(props.manager.auswahlLeistung.indexSchueler === 0 && props.manager.auswahlLeistung.indexLeistung === 0)
					return;
				eve.preventDefault();
				props.manager.auswahlLeistungVorherige();
				currentColumn.value = columnsComputed.value.length - 1;
				columnsComputed.value[currentColumn.value].focus();
			} else
				currentColumn.value -= 1;
		} else {
			if (currentColumn.value === columnsComputed.value.length - 1) {
				if(rowRefs.value.get(idLeistung)?.isLastRow ?? false)
					return;
				eve.preventDefault();
				props.manager.auswahlLeistungNaechste();
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
		() => props.manager.auswahlLeistung,
		async () => {
			if (currentColumn.value !== -1) {
				await nextTick(() => columnsComputed.value[currentColumn.value].focus())
			}
		}
	)

	watch(() => props.manager.lerngruppenAuswahlAlleLeistungen(),() => currentColumn.value = -1)

	onMounted(() => currentColumn.value = -1)

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

</style>
