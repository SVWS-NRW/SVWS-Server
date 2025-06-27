<template>
	{{ gridManager.focusColumnLast }}
	{{ gridManager.focusRowLast }}
	<ui-table-grid name="Klassenleitung" :header-count="1" :footer-count="0" :data="daten"
		:cell-format="cellFormat" :get-key="(row: PairNN<ENMKlasse, ENMSchueler>) => `${row.a.id}_${row.b.id}`"
		:row-selected="gridManager.focusRow" @row-clicked="(_row: any, rowIndex: number) => gridManager.doFocusRowIfNotFocussed(rowIndex)">
		<template #header>
			<template v-for="col of cols" :key="col.name">
				<th v-if="columnsVisible().get(col.kuerzel) ?? true">
					<svws-ui-tooltip v-if="col.kuerzel !== col.name">
						{{ col.kuerzel }}
						<template #content>{{ col.name }}</template>
					</svws-ui-tooltip>
					<span v-else>{{ col.kuerzel }}</span>
				</th>
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
		</template>
		<template #default="{ row: pair, index }">
			<td v-if="colsVisible.get('Klasse') ?? true">
				{{ pair.a.kuerzelAnzeige }}
			</td>
			<td v-if="colsVisible.get('Name') ?? true" class="text-left">
				{{ pair.b.nachname }}, {{ pair.b.vorname }} ({{ pair.b.geschlecht }})
			</td>
			<template v-if="columnsVisible().get('FS') ?? true">
				<td :ref="inputFehlstunden(pair, 1, index)" class="ui-table-grid-input"
					:class="{ 'bg-ui-selected': (gridManager.focusColumn === 1) }" />
			</template>
			<template v-if="columnsVisible().get('FSU') ?? true">
				<td :ref="inputFehlstundenUnendschuldigt(pair, 2, index)" class="ui-table-grid-input"
					:class="{ 'bg-ui-selected': (gridManager.focusColumn === 2) }" />
			</template>
			<template v-if="columnsVisible().get('ASV') ?? true">
				<td :ref="inputASV(pair, 3, index)" class="ui-table-grid-button"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === 3),
						'bg-ui-selected text-ui-onselected': floskelEditorVisible && ((gridManager.focusColumnLast === 3) && (gridManager.focusRowLast === index)),
					}">
					<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ pair.b.bemerkungen.ASV ?? "-" }}</span>
				</td>
			</template>
			<template v-if="columnsVisible().get('AUE') ?? true">
				<td :ref="inputAUE(pair, 4, index)" class="ui-table-grid-button"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === 4),
						'bg-ui-selected text-ui-onselected': floskelEditorVisible && ((gridManager.focusColumnLast === 4) && (gridManager.focusRowLast === index)),
					}">
					<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ pair.b.bemerkungen.AUE ?? "-" }}</span>
				</td>
			</template>
			<template v-if="columnsVisible().get('ZB') ?? true">
				<td :ref="inputZB(pair, 5, index)" class="ui-table-grid-button"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === 5),
						'bg-ui-selected text-ui-onselected': floskelEditorVisible && ((gridManager.focusColumnLast === 5) && (gridManager.focusRowLast === index)),
					}">
					<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ pair.b.bemerkungen.ZB ?? "-" }}</span>
				</td>
			</template>
			<td />
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import type { ComponentPublicInstance} from 'vue';
	import { computed, watchEffect } from 'vue';
	import type { ENMSchueler } from "../../../../core/src/core/data/enm/ENMSchueler";
	import type { EnmKlassenleitungUebersichtProps } from './EnmKlassenleitungUebersichtProps';
	import { PairNN } from '../../../../core/src/asd/adt/PairNN';
	import { GridManager } from '../../ui/controls/tablegrid/GridManager';
	import type { CellFormat } from '../../ui/controls/tablegrid/UiTableGrid.vue';
	import type { List } from '../../../../core/src/java/util/List';
	import { ArrayList } from '../../../../core/src/java/util/ArrayList';
	import type { ENMKlasse } from '../../../../core/src/core/data/enm/ENMKlasse';
	import type { GridInput } from '../../ui/controls/tablegrid/GridInput';
	import type { ENMLernabschnitt } from '../../../../core/src/core/data/enm/ENMLernabschnitt';
	import type { GridInputIntegerDiv } from '../../ui/controls/tablegrid/GridInputIntegerDiv';

	const props = defineProps<EnmKlassenleitungUebersichtProps>();
	defineExpose({ focusGrid });

	const cols = [
		{ kuerzel: "Klasse", name: "Klasse", width: "4rem" },
		{ kuerzel: "Name", name: "Name, Vorname", width: "16rem" },
		{ kuerzel: "FS", name: "Fehlstunden", width: "4rem" },
		{ kuerzel: "FSU", name: "Fehlstunden (unentschuldigt)", width: "4rem" },
		{ kuerzel: "ASV", name: "Arbeits- und Sozialverhalten", width: "16rem" },
		{ kuerzel: "AUE", name: "Außerunterrichtliches Engagement", width: "16rem" },
		{ kuerzel: "ZB", name: "Zeugnisbemerkung", width: "16rem" },
	];

	const colsVisible = computed<Map<string, boolean|null>>({
		get: () => props.columnsVisible(),
		set: (value) => void props.setColumnsVisible(value),
	});

	const gridManager = new GridManager<string>();
	gridManager.onFocusInput = (input: GridInput<any, any> | null) => {
		if ((input === null) || (input.row >= daten.value.size()))
			return;
		const pair = daten.value.get(input.row);
		void props.focusFloskelEditor(null, pair.b, pair.a, false);
	}
	function focusGrid() {
		gridManager.doFocus(true);
	}
	const cellFormat = computed<CellFormat>(() => {
		const result = <CellFormat>{ widths: [] };
		for (const col of cols) {
			const visible = props.columnsVisible().get(col.kuerzel) ?? true;
			if (visible)
				result.widths.push(col.width);
		}
		result.widths.push('3.5rem');
		return result;
	});

	const daten = computed<List<PairNN<ENMKlasse, ENMSchueler>>>(() => {
		const result = new ArrayList<PairNN<ENMKlasse, ENMSchueler>>();
		for (const klasse of props.auswahl()) {
			const listSchueler = props.enmManager().mapKlassenSchueler.get(klasse.id);
			if ((listSchueler === null))
				continue;
			for (const schueler of listSchueler)
				result.add(new PairNN<ENMKlasse, ENMSchueler>(klasse, schueler));
		}
		return result;
	});


	function inputFehlstunden(pair: PairNN<ENMKlasse, ENMSchueler>, col: number, index: number) {
		const key = 'Fehlstunden_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : number | null) => {
			const patch = <Partial<ENMLernabschnitt>>{ fehlstundenGesamt: value };
			const inputFSU = gridManager.getInputByKey('FehlstundenUnendschuldigt_' + pair.a.id + "_" + pair.b.id);
			if (inputFSU !== null) {
				const inputFSUTyped = inputFSU as GridInputIntegerDiv<string>;
				inputFSUTyped.max = value ?? 0;
				if ((patch.fehlstundenGesamtUnentschuldigt ?? 0) > (value ?? 0))
					patch.fehlstundenGesamtUnentschuldigt = (value ?? 0);
			}
			void props.patchLernabschnitt(pair.b.lernabschnitt, { fehlstundenGesamt: value });
		};
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputIntegerDiv(key, col, index, element, 999, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, pair.b.lernabschnitt.fehlstundenGesamt));
		};
	}

	function inputFehlstundenUnendschuldigt(pair: PairNN<ENMKlasse, ENMSchueler>, col: number, index: number) {
		const key = 'FehlstundenUnendschuldigt_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : number | null) => void props.patchLernabschnitt(pair.b.lernabschnitt, { fehlstundenGesamtUnentschuldigt: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputIntegerDiv(key, col, index, element, pair.b.lernabschnitt.fehlstundenGesamt ?? 0, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, pair.b.lernabschnitt.fehlstundenGesamtUnentschuldigt));
		};
	}

	function inputASV(pair: PairNN<ENMKlasse, ENMSchueler>, col: number, index: number) {
		const key = 'ASV_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : boolean) => void props.focusFloskelEditor('ASV', pair.b, pair.a, true);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null) {
				gridManager.update(key, false);
				gridManager.setNavigationOnEnter(key, null);
			}
		};
	}

	function inputAUE(pair: PairNN<ENMKlasse, ENMSchueler>, col: number, index: number) {
		const key = 'AUE_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : boolean) => void props.focusFloskelEditor('AUE', pair.b, pair.a, true);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null) {
				gridManager.update(key, false);
				gridManager.setNavigationOnEnter(key, null);
			}
		};
	}

	function inputZB(pair: PairNN<ENMKlasse, ENMSchueler>, col: number, index: number) {
		const key = 'ZB_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : boolean) => void props.focusFloskelEditor('ZB', pair.b, pair.a, true);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null) {
				gridManager.update(key, false);
				gridManager.setNavigationOnEnter(key, null);
			}
		};
	}

</script>
