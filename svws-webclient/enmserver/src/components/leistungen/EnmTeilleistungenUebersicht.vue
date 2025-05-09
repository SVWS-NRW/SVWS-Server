<template>
	<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle"
		@keydown.down.prevent.stop="manager.auswahlLeistungNaechste()" @keydown.up.prevent.stop="manager.auswahlLeistungVorherige()"
		@keydown.right.prevent="nextColumn" @keydown.left.prevent="prevColumn">
		<thead class="svws-ui-thead cursor-pointer mb-1" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr" role="row">
				<template v-for="col of cols" :key="col.name">
					<template v-if="col.name === 'Teilleistung'">
						<template v-for="art of manager.lerngruppenAuswahlGetTeilleistungsarten()" :key="art.id">
							<td class="svws-ui-td" role="columnheader"> {{ art.bezeichnung ?? '???' }} </td>
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
			<template v-for="(schueler, indexSchueler) of manager.lerngruppenAuswahlGetSchuelerMitLeistungsdaten()" :key="schueler">
				<template v-for="(leistung, indexLeistung) of manager.leistungenGetOfSchueler(schueler.id)" :key="leistung">
					<tr class="svws-ui-tr" role="row" :class="{ 'svws-clicked': manager.auswahlLeistung.leistung === leistung }"
						@click.capture.exact="setAuswahlLeistung({ indexSchueler, indexLeistung, leistung })"
						@keydown.tab="handleTabEvent($event, leistung.id)" :ref="el => rowRefs.set(leistung.id, {
							element: (el as HTMLElement),
							isLastRow: manager.lerngruppenAuswahlGetSchuelerMitLeistungsdaten().getLast().id === schueler.id
								&& manager.leistungenGetOfSchueler(schueler.id).getLast().id === leistung.id })">
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
						<template v-for="art of manager.lerngruppenAuswahlGetTeilleistungsarten()" :key="art.id">
							<td class="svws-ui-td" role="cell">
								<template v-if="manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id) !== null">
									<svws-ui-text-input :id="manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.id" v-if="(manager.auswahlLeistung.leistung === leistung) && manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" class="w-full"
										:model-value="manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.note" :class="{ contentFocusField: (manager.auswahlLeistung.leistung === leistung) && art.id === 5 }" @focusin="switchToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)"
										@change="note => doPatchLeistungNote(manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id), Note.fromKuerzel(note).daten(props.manager.schuljahr)?.kuerzel,{ note: (Note.fromKuerzel(note).daten(props.manager.schuljahr)?.kuerzel ?? null) })" />
									<div v-else @focusin="switchToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)" class="grade-field column-focussable" tabindex="0">
										{{ manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.note ?? "-" }}
									</div>
								</template>
							</td>
						</template>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Quartal') ?? true">
							<svws-ui-text-input v-if="(manager.auswahlLeistung.leistung === leistung) && manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" class="w-full" :model-value="leistung.noteQuartal"
								:class="{ contentFocusField: (manager.auswahlLeistung.leistung === leistung) }" @focusin="switchToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)"
								@change="noteQuartal => doPatchLeistungNote(leistung, Note.fromKuerzel(noteQuartal).daten(props.manager.schuljahr)?.kuerzel,{ noteQuartal: (Note.fromKuerzel(noteQuartal).daten(props.manager.schuljahr)?.kuerzel ?? null) })" />
							<div v-else class="grade-field column-focussable" tabindex="0" @focusin="switchToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)">
								{{ leistung.noteQuartal ?? "-" }}
							</div>
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Note') ?? true">
							<svws-ui-text-input v-if="(manager.auswahlLeistung.leistung === leistung) && manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" class="w-full" :model-value="leistung.note"
								:class="{ contentFocusField: (manager.auswahlLeistung.leistung === leistung) }" @focusin="switchToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)"
								@change="note => doPatchLeistungNote(leistung, Note.fromKuerzel(note).daten(props.manager.schuljahr)?.kuerzel,{ note: (Note.fromKuerzel(note).daten(props.manager.schuljahr)?.kuerzel ?? null) })" />
							<div v-else class="text-ellipsis overflow-hidden whitespace-nowrap grade-field column-focussable" tabindex="0" @focusin="switchToUnselectedLeistung(indexSchueler, indexLeistung, leistung, $event.target)">
								{{ leistung.note ?? "-" }}
							</div>
						</td>
						<td class="svws-ui-td" role="cell" />
					</tr>
				</template>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed, nextTick, onMounted, ref, watch, toRaw } from 'vue';
	import type { EnmLeistungAuswahl } from './EnmManager';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';
	import type { ENMLeistung } from '@core/core/data/enm/ENMLeistung';
	import { ENMTeilleistung } from '@core/core/data/enm/ENMTeilleistung';
	import { Note } from '@core/asd/types/Note';

	const props = defineProps<EnmTeilleistungenProps>();
	const rowRefs = ref(new Map<number, { element: HTMLElement | null, isLastRow: boolean }>());
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

	const columnsComputed = computed<HTMLElement[]>(
		() => {
			if (props.manager.auswahlLeistung.leistung) {
				const htmlElement = rowRefs.value.get(props.manager.auswahlLeistung.leistung.id)!.element;
				if (htmlElement !== null)
					return Array.from(htmlElement.querySelectorAll("input, .column-focussable"));
				else
					return [];
			} else
				return [];
		}
	);

	const listContainsLeistung = computed<boolean>(
		() => {
			const id = props.manager.auswahlLeistung.leistung?.id
			if (id !== undefined)
				return toRaw(rowRefs.value.has(id) && rowRefs.value.get(id)?.element !== null);
			return false;
		}
	)

	function setAuswahlLeistung(value: EnmLeistungAuswahl) {
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

	async function doPatchLeistungNote(leistung: ENMLeistung | ENMTeilleistung | null, newValue: string | null | undefined, patchObject: Partial<ENMLeistung> | Partial<ENMTeilleistung>) {
		if (leistung === null || newValue === null || newValue === undefined || newValue === "")
			return;
		else {
			if (leistung instanceof ENMTeilleistung)
				await doPatchTeilleistung(leistung, patchObject);
			else
				await doPatchLeistung(leistung, patchObject);
		}
	}

	async function doPatchLeistung(leistung: ENMLeistung, patch: Partial<ENMLeistung>) {
		patch.id = leistung.id;
		const success = await props.patchLeistung(patch);
		if (success)
			Object.assign(leistung, patch);
		props.manager.update();
	}

	async function doPatchTeilleistung(teilleistung: ENMTeilleistung, patch: Partial<ENMTeilleistung>) {
		patch.id = teilleistung.id;
		const success = await props.patchTeilleistung(patch);
		if (success)
			Object.assign(teilleistung, patch);
		props.manager.update();
	}

	function switchToUnselectedLeistung(indexSchueler: number, indexLeistung: number, leistung: ENMLeistung, ele: EventTarget | null) {
		const newRowHtml = rowRefs.value.get(leistung.id)?.element;
		if (newRowHtml === null || newRowHtml === undefined)
			return;
		const newRowArray = Array.from(newRowHtml.querySelectorAll("input, .column-focussable"));
		const columnIndex = newRowArray.indexOf(ele as HTMLElement);
		if (columnIndex !== -1)
			currentColumn.value = columnIndex;
		if (!listContainsLeistung.value || !props.manager.compareAuswahlLeistung({ indexSchueler, indexLeistung, leistung }, props.manager.auswahlLeistung))
			setAuswahlLeistung({ indexSchueler, indexLeistung, leistung });
		if(ele)
			selectInputContent(ele);
	}

	function handleTabEvent(eve: KeyboardEvent, idLeistung: number) {
		if (eve.shiftKey) {
			if (currentColumn.value === 0) {
				if(props.manager.auswahlLeistung.indexSchueler === 0)
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
		let frs = " ";
		for (const art of props.manager.lerngruppenAuswahlGetTeilleistungsarten())
			frs += "1fr ";
		return cols.filter(c => colsVisible.value.get(c.kuerzel) ?? true).map(c => c.width).join(" ") + frs + " 5em";
	});

	watch(
		() => props.manager.auswahlLeistung,
		async () => await nextTick(() => columnsComputed.value[currentColumn.value].focus())
	)

	onMounted(() => currentColumn.value = 0);

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

	.grade-field {
		width: 100%;
	}

</style>
