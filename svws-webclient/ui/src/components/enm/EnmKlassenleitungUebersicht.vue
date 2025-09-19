<template>
	<ui-table-grid name="Klassenleitung" :header-count="1" :footer-count="0" :manager="() => gridManager">
		<template #header>
			<template v-for="col of gridManager.cols.values()" :key="col.name">
				<template v-if="col.kuerzel !== ''">
					<th v-if="gridManager.isColVisible(col.kuerzel) ?? true">
						<template v-if="col.kuerzel !== col.name">
							<svws-ui-tooltip>
								{{ col.kuerzel }}
								<template #content>{{ col.name }}</template>
							</svws-ui-tooltip>
						</template>
						<template v-else>{{ col.kuerzel }}</template>
						<template v-if="colsValidationTooltip.has(col.kuerzel)">
							<svws-ui-tooltip>
								<span class="icon-sm i-ri-question-line ml-0.5" />
								<template #content>
									<div class="font-bold">{{ col.name }}</div>
									<template v-if="col.kuerzel === 'FS'">
										<ul>
											<li>Keine negativen Werte</li>
											<li>Maximal 999</li>
											<li>Größer/gleich FSU</li>
										</ul>
									</template>
									<template v-else-if="col.kuerzel === 'FSU'">
										<ul>
											<li>Keine negativen Werte</li>
											<li>Maximal 999</li>
											<li>Kleiner/gleich FS</li>
										</ul>
									</template>
								</template>
							</svws-ui-tooltip>
						</template>
					</th>
				</template>
				<template v-else>
					<th role="columnheader">
						<svws-ui-tooltip :hover="false" :show-arrow="false" position="top" class="h-full w-full">
							<span class="icon" :class="gridManager.hasHiddenColumn ? 'i-ri-layout-column-fill' : 'i-ri-layout-column-line'" />
							<span class="icon i-ri-arrow-down-s-line" />
							<template #content>
								<ul class="min-w-[10rem] flex flex-col gap-0.5 pt-1">
									<template v-for="hideable of gridManager.hideableColumns" :key="hideable.name">
										<li>
											<svws-ui-checkbox :model-value="gridManager.isColVisible(hideable.kuerzel)" @update:model-value="value => gridManager.setColVisibility(hideable.kuerzel, value)">
												{{ hideable.kuerzel }}
											</svws-ui-checkbox>
										</li>
									</template>
								</ul>
							</template>
						</svws-ui-tooltip>
					</th>
				</template>
			</template>
		</template>
		<template #default="{ row: pair, index }">
			<td v-if="gridManager.isColVisible('Klasse') ?? true">
				{{ pair.a.kuerzelAnzeige }}
			</td>
			<td v-if="gridManager.isColVisible('Name') ?? true" class="text-left">
				{{ pair.b.nachname }}, {{ pair.b.vorname }} ({{ pair.b.geschlecht }})
			</td>
			<template v-if="gridManager.isColVisible('FS') ?? true">
				<td :ref="inputFehlstunden(pair, 1, index)" class="ui-table-grid-input"
					:class="{ 'bg-ui-selected': (gridManager.focusColumn === 1) }" />
			</template>
			<template v-if="gridManager.isColVisible('FSU') ?? true">
				<td :ref="inputFehlstundenUnendschuldigt(pair, 2, index)" class="ui-table-grid-input"
					:class="{ 'bg-ui-selected': (gridManager.focusColumn === 2) }" />
			</template>
			<template v-if="gridManager.isColVisible('ASV') ?? true">
				<td :ref="inputASV(pair, 3, index)" class="ui-table-grid-button"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === 3),
						'bg-ui-selected text-ui-onselected': floskelEditorVisible && ((gridManager.focusColumnLast === 3) && (gridManager.focusRowLast === index)),
					}">
					<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ pair.b.bemerkungen.ASV ?? "-" }}</span>
				</td>
			</template>
			<template v-if="gridManager.isColVisible('AUE') ?? true">
				<td :ref="inputAUE(pair, 4, index)" class="ui-table-grid-button"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === 4),
						'bg-ui-selected text-ui-onselected': floskelEditorVisible && ((gridManager.focusColumnLast === 4) && (gridManager.focusRowLast === index)),
					}">
					<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ pair.b.bemerkungen.AUE ?? "-" }}</span>
				</td>
			</template>
			<template v-if="gridManager.isColVisible('ZB') ?? true">
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
	import type { List } from '../../../../core/src/java/util/List';
	import { ArrayList } from '../../../../core/src/java/util/ArrayList';
	import type { ENMKlasse } from '../../../../core/src/core/data/enm/ENMKlasse';
	import type { GridInput } from '../../ui/controls/tablegrid/GridInput';
	import type { ENMLernabschnitt } from '../../../../core/src/core/data/enm/ENMLernabschnitt';
	import type { GridInputIntegerDiv } from '../../ui/controls/tablegrid/GridInputIntegerDiv';

	const props = defineProps<EnmKlassenleitungUebersichtProps>();

	const colsValidationTooltip = new Set(["FS", "FSU"]);

	const gridManager = new GridManager<string, PairNN<ENMKlasse, ENMSchueler>, List<PairNN<ENMKlasse, ENMSchueler>>>({
		daten: computed<List<PairNN<ENMKlasse, ENMSchueler>>>(() => {
			const result = new ArrayList<PairNN<ENMKlasse, ENMSchueler>>();
			for (const klasse of props.auswahl()) {
				const listSchueler = props.enmManager().mapKlassenSchueler.get(klasse.id);
				if ((listSchueler === null))
					continue;
				for (const schueler of listSchueler)
					result.add(new PairNN<ENMKlasse, ENMSchueler>(klasse, schueler));
			}
			return result;
		}),
		getRowKey: row => `${row.a.id}_${row.b.id}`,
		columns: [
			{ kuerzel: "Klasse", name: "Klasse", width: "4rem", hideable: false },
			{ kuerzel: "Name", name: "Name, Vorname", width: "16rem", hideable: false },
			{ kuerzel: "FS", name: "Fehlstunden", width: "4rem", hideable: true },
			{ kuerzel: "FSU", name: "Fehlstunden (unentschuldigt)", width: "4rem", hideable: true },
			{ kuerzel: "ASV", name: "Arbeits- und Sozialverhalten", width: "16rem", hideable: true },
			{ kuerzel: "AUE", name: "Außerunterrichtliches Engagement", width: "16rem", hideable: true },
			{ kuerzel: "ZB", name: "Zeugnisbemerkung", width: "16rem", hideable: true },
			{ kuerzel: "", name: "", width: "3.25rem", hideable: false },
		],
		colsVisible: computed<Map<string, boolean|null>>({
			get: () => props.columnsVisible(),
			set: (value) => void props.setColumnsVisible(value),
		}),
	});
	gridManager.onFocusInput = (input: GridInput<any, any> | null) => {
		if ((input === null) || (input.row >= gridManager.daten.size()))
			return;
		const pair = gridManager.daten.get(input.row);
		void props.focusFloskelEditor(null, pair.b, pair.a, input.row, false);
	}
	defineExpose({ gridManager });

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
		const setter = (value : boolean) => void props.focusFloskelEditor('ASV', pair.b, pair.a, index, true);
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
		const setter = (value : boolean) => void props.focusFloskelEditor('AUE', pair.b, pair.a, index, true);
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
		const setter = (value : boolean) => void props.focusFloskelEditor('ZB', pair.b, pair.a, index, true);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null) {
				gridManager.update(key, false);
				gridManager.setNavigationOnEnter(key, null);
			}
		};
	}

</script>
