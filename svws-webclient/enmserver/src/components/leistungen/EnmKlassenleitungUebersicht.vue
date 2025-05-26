<template>
	<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle"
		@keydown.down.prevent.stop="auswahlmanager.linkedListNext"
		@keydown.up.prevent.stop="auswahlmanager.linkedListPrevious"
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
		<tbody class="svws-ui-tbody h-full overflow-y-auto" role="rowgroup" aria-label="Tabelleninhalt">
			<template v-for="schueler of auswahlmanager.liste" :key="schueler">
				<tr class="svws-ui-tr h-10" role="row" :class="{ 'svws-clicked': auswahlmanager.auswahl === schueler }"
					@keydown.tab="handleTabEvent"
					@click.capture.exact="auswahlmanager.auswahl = schueler"
					:ref="el => rowRefs.set(schueler, el as HTMLElement)">
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Klasse') ?? true">
						{{ manager.schuelerGetKlasse(schueler.id).kuerzelAnzeige }}
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Name') ?? true">
						{{ schueler.nachname }}, {{ schueler.vorname }} ({{ schueler.geschlecht }})
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('FS') ?? true">
						<svws-ui-input-number @focusin="switchToUnselectedSchueler(schueler, $event.target)" :model-value="schueler.lernabschnitt.fehlstundenGesamt" headless hide-stepper min="0" max="999"
							@change="fehlstundenGesamt => doPatchLernabschnitt(schueler.lernabschnitt, { fehlstundenGesamt, id: schueler.lernabschnitt.id })"
							:class="{ 'contentFocusField': auswahlmanager.auswahl === schueler }" />
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('FSU') ?? true">
						<svws-ui-input-number :model-value="schueler.lernabschnitt.fehlstundenGesamtUnentschuldigt" headless hide-stepper min="0" @focusin="switchToUnselectedSchueler(schueler, $event.target)"
							:max="schueler.lernabschnitt.fehlstundenGesamt" @change="fehlstundenGesamtUnentschuldigt => doPatchLernabschnitt(schueler.lernabschnitt, { fehlstundenGesamtUnentschuldigt, id: schueler.lernabschnitt.id })" />
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('ASV') ?? true" @click="emitBemerkung('ASV')" @keydown.enter.prevent="focusFloskelEditor('ASV')"
						:class="{ 'bg-ui-selected-secondary text-ui-onselected-secondary': floskelEditorVisible && (auswahlmanager.auswahl === schueler) && (hauptgruppe === 'ASV') }">
						<span class="text-ellipsis overflow-hidden whitespace-nowrap column-focussable" @focusin="switchToUnselectedSchueler(schueler, $event.target)" tabindex="0">{{ schueler.bemerkungen.ASV }}</span>
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('AUE') ?? true" @click="emitBemerkung('AUE')" @keydown.enter.prevent="focusFloskelEditor('AUE')"
						:class="{ 'bg-ui-selected-secondary text-ui-onselected-secondary': floskelEditorVisible && (auswahlmanager.auswahl === schueler) && (hauptgruppe === 'AUE') }">
						<span class="text-ellipsis overflow-hidden whitespace-nowrap column-focussable" @focusin="switchToUnselectedSchueler(schueler, $event.target)" tabindex="0">{{ schueler.bemerkungen.AUE }}</span>
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('ZB') ?? true" @click="emitBemerkung('ZB')" @keydown.enter.prevent="focusFloskelEditor('ZB')"
						:class="{ 'bg-ui-selected-secondary text-ui-onselected-secondary': floskelEditorVisible && (auswahlmanager.auswahl === schueler) && (hauptgruppe === 'ZB') }">
						<span class="text-ellipsis overflow-hidden whitespace-nowrap column-focussable" @focusin="switchToUnselectedSchueler(schueler, $event.target)" tabindex="0">{{ schueler.bemerkungen.ZB }}</span>
					</td>
					<td class="svws-ui-td" role="cell" />
				</tr>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed, nextTick, ref, watch } from 'vue';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';
	import type { BemerkungenHauptgruppe } from './EnmManager';
	import type { ENMLernabschnitt } from '@core/core/data/enm/ENMLernabschnitt';
	import type { ENMSchueler } from "@core/core/data/enm/ENMSchueler";

	const props = defineProps<EnmKlassenleitungProps>();
	const rowRefs = ref(new Map<ENMSchueler, HTMLElement>());
	const currentColumn = ref(0);

	const emit = defineEmits<{
		"hauptgruppe": [value: BemerkungenHauptgruppe];
	}>();

	const cols = [
		{ kuerzel: "Klasse", name: "Klasse", width: "1fr" },
		{ kuerzel: "Name", name: "Name, Vorname", width: "3fr" },
		{ kuerzel: "FS", name: "Fehlstunden", width: "2fr" },
		{ kuerzel: "FSU", name: "Fehlstunden (unentschuldigt)", width: "2fr" },
		{ kuerzel: "ASV", name: "Arbeits- und Sozialverhalten", width: "5fr" },
		{ kuerzel: "AUE", name: "Außerunterrichtliches Engagement", width: "5fr" },
		{ kuerzel: "ZB", name: "Zeugnisbemerkung", width: "5fr" },
	];

	const hauptgruppe = ref<BemerkungenHauptgruppe>('ZB');

	function emitBemerkung(ev: BemerkungenHauptgruppe) {
		if (hauptgruppe.value === ev)
			return;
		hauptgruppe.value = ev;
		emit('hauptgruppe', hauptgruppe.value)
	}

	const colsVisible = computed<Map<string, boolean|null>>({
		get: () => props.columnsVisible(),
		set: (value) => void props.setColumnsVisible(value),
	});

	const columnsComputed = computed<HTMLElement[]>(() => {
		const row = rowRefs.value.get(props.auswahlmanager.auswahl);
		if (row === undefined)
			return []
		return Array.from(row.querySelectorAll("input, .column-focussable"));
	});

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

	async function focusFloskelEditor(kuerzelBermerkung: BemerkungenHauptgruppe) {
		emitBemerkung(kuerzelBermerkung);
		await props.setFloskelEditorVisible(true).then(() => (document.getElementsByClassName("floskel-input")[0] as HTMLElement).focus());
	}

	async function doPatchLernabschnitt(lernabschnitt: ENMLernabschnitt, patch: Partial<ENMLernabschnitt>) {
		const success = await props.patchLernabschnitt(patch);
		if (success)
			Object.assign(lernabschnitt, patch);
	}

	function selectInputContent(ele: EventTarget) {
		if (ele instanceof HTMLInputElement)
			ele.select();
	}

	function switchToUnselectedSchueler(schueler: ENMSchueler, ele: EventTarget | null) {
		const newRowHtml = rowRefs.value.get(schueler);
		if (newRowHtml === undefined)
			return;
		const newRowArray = Array.from(newRowHtml.querySelectorAll("input, .column-focussable"));
		const columnIndex = newRowArray.indexOf(ele as HTMLElement);
		if (columnIndex !== -1)
			currentColumn.value = columnIndex;
		props.auswahlmanager.auswahl = schueler;
		if(ele)
			selectInputContent(ele);
	}

	function handleTabEvent(eve: KeyboardEvent) {
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

	const gridTemplateColumnsComputed = computed<string>(() => cols.filter(c => colsVisible.value.get(c.kuerzel) ?? true).map(c => c.width).join(" ") + " 5em");

	watch(
		() => props.auswahlmanager.auswahl,
		async () => {
			await nextTick(() => columnsComputed.value[currentColumn.value].focus())
		}
	)

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

</style>
