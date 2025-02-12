<template>
	<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle"
		@keydown.down.prevent.stop="manager.auswahlLeistungNaechste()" @keydown.up.prevent.stop="manager.auswahlLeistungVorherige()">
		<thead class="svws-ui-thead cursor-pointer mb-1" role="rowgroup" aria-label="Tabellenkopf">
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
						<span class="icon inline-block" :class="[...colsVisible.values()].some(c => c === false) ? 'i-ri-layout-column-fill' : 'i-ri-layout-column-line'" /><span class="icon i-ri-arrow-down-s-line" />
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
			<template v-for="schueler of manager.klassenAuswahlGetSchueler()" :key="schueler">
				<tr class="svws-ui-tr" role="row" :class="{ 'svws-clicked': manager.auswahlSchueler?.id === schueler.id }" @click.capture.exact="manager.auswahlSchueler = schueler">
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Klasse') ?? true">
						{{ manager.schuelerGetKlasse(schueler.id).kuerzelAnzeige }}
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Name') ?? true">
						{{ schueler.nachname }}, {{ schueler.vorname }} ({{ schueler.geschlecht }})
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('FS') ?? true">
						<svws-ui-input-number :model-value="schueler.lernabschnitt.fehlstundenGesamt" headless hide-stepper min="0" max="999"
							@change="fehlstundenGesamt => doPatchLernabschnitt({ fehlstundenGesamt, id: schueler.lernabschnitt.id })" />
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('FSU') ?? true">
						<svws-ui-input-number :model-value="schueler.lernabschnitt.fehlstundenGesamtUnentschuldigt" headless hide-stepper min="0" :max="schueler.lernabschnitt.fehlstundenGesamt"
							@change="fehlstundenGesamtUnentschuldigt => doPatchLernabschnitt({ fehlstundenGesamtUnentschuldigt, id: schueler.lernabschnitt.id })" />
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('ASV') ?? true" @click="emitBemerkung('ASV')"
						:class="{ 'bg-ui-selected-secondary text-ui-onselected-secondary': floskelEditorVisible && (manager.auswahlSchueler?.id === schueler.id) && (hauptgruppe === 'ASV') }">
						<span class="text-ellipsis overflow-hidden whitespace-nowrap">{{ schueler.bemerkungen.ASV }}</span>
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('AUE') ?? true" @click="emitBemerkung('AUE')"
						:class="{ 'bg-ui-selected-secondary text-ui-onselected-secondary': floskelEditorVisible && (manager.auswahlSchueler?.id === schueler.id) && (hauptgruppe === 'AUE') }">
						<span class="text-ellipsis overflow-hidden whitespace-nowrap">{{ schueler.bemerkungen.AUE }}</span>
					</td>
					<td class="svws-ui-td" role="cell" v-if="colsVisible.get('ZB') ?? true" @click="emitBemerkung('ZB')"
						:class="{ 'bg-ui-selected-secondary text-ui-onselected-secondary': floskelEditorVisible && (manager.auswahlSchueler?.id === schueler.id) && (hauptgruppe === 'ZB') }">
						<span class="text-ellipsis overflow-hidden whitespace-nowrap">{{ schueler.bemerkungen.ZB }}</span>
					</td>
					<td class="svws-ui-td" role="cell" />
				</tr>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';
	import type { BemerkungenHauptgruppe } from './EnmManager';
	import type { ENMLernabschnitt } from '@core/core/data/enm/ENMLernabschnitt';

	const props = defineProps<EnmKlassenleitungProps>();

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

	async function doPatchLernabschnitt(patch: Partial<ENMLernabschnitt>) {
		await props.patchLernabschnitt(patch);
		props.manager.update();
	}

	const gridTemplateColumnsComputed = computed<string>(() => {
		return cols.filter(c => colsVisible.value.get(c.kuerzel) ?? true).map(c => c.width).join(" ") + " 5em";
	});

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

</style>