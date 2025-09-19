<template>
	<ui-table-grid name="Leistungsdaten" :header-count="1" :footer-count="0" :manager="() => gridManager">
		<template #header>
			<template v-for="col of gridManager.cols.values()" :key="col.name">
				<template v-if="col.kuerzel !== ''">
					<th v-if="gridManager.isColVisible(col.kuerzel) ?? true" class="flex h-10">
						<div class="h-full content-center">
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
										<template v-if="(col.kuerzel === 'Quartal') || (col.kuerzel === 'Note')">
											<ul>
												<li v-for="n in notenKuerzel" :key="n"> {{ n }} </li>
											</ul>
										</template>
										<template v-else-if="col.kuerzel === 'FS'">
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
						</div>
					</th>
				</template>
				<template v-else>
					<th>
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
				{{ enmManager().mapKlassen.get(pair.b.klasseID)?.kuerzelAnzeige ?? '—' }}
			</td>
			<td v-if="gridManager.isColVisible('Name') ?? true" class="text-left">
				{{ pair.b.nachname }}, {{ pair.b.vorname }} ({{ pair.b.geschlecht }})
			</td>
			<td v-if="gridManager.isColVisible('Fach') ?? true">
				{{ enmManager().lerngruppeGetFachkuerzel(pair.a.lerngruppenID) }}
			</td>
			<td v-if="gridManager.isColVisible('Kurs') ?? true">
				{{ enmManager().lerngruppeGetKursbezeichnung(pair.a.lerngruppenID) }}
			</td>
			<td v-if="gridManager.isColVisible('Kursart') ?? true">
				{{ enmManager().leistungGetKursartAsString(pair.a) }}
			</td>
			<td v-if="gridManager.isColVisible('Lehrer') ?? true">
				{{ enmManager().lerngruppeGetFachlehrerOrNull(pair.a.lerngruppenID) }}
			</td>
			<template v-if="gridManager.isColVisible('Quartal') ?? true">
				<td v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)" :ref="inputNoteQuartal(pair, 1, index)" class="ui-table-grid-input"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === 1),
						'text-ui-danger': Note.fromKuerzel(pair.a.noteQuartal).istDefizitSekII(),
					}" />
				<td v-else :class="{ 'text-ui-danger': Note.fromKuerzel(pair.a.noteQuartal).istDefizitSekII() }">{{ pair.a.noteQuartal ?? "-" }}</td>
			</template>
			<template v-if="gridManager.isColVisible('Note') ?? true">
				<td v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)" :ref="inputNote(pair, 2, index)" class="ui-table-grid-input"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === 2),
						'text-ui-danger': Note.fromKuerzel(pair.a.note).istDefizitSekII(),
					}" />
				<td v-else :class="{ 'text-ui-danger': Note.fromKuerzel(pair.a.note).istDefizitSekII() }">{{ pair.a.note ?? "-" }}</td>
			</template>
			<template v-if="gridManager.isColVisible('Mahnung') ?? true">
				<template v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)">
					<td :ref="inputMahnung(pair, 3, index)" class="ui-table-grid-button"
						:class="{ 'bg-ui-selected': (gridManager.focusColumn === 3) }">
						<span v-if="pair.a.mahndatum !== null" class="icon-sm align-middle i-ri-checkbox-line bg-ui-50" />
						<span v-else-if="pair.a.istGemahnt === true" class="icon-sm align-middle i-ri-checkbox-line bg-ui-danger" />
						<span v-else class="icon-sm align-middle i-ri-checkbox-blank-line" />
					</td>
				</template>
				<template v-else>
					<td>
						<span v-if="pair.a.mahndatum !== null" class="icon-sm align-middle i-ri-checkbox-line bg-ui-50" />
						<span v-else-if="pair.a.istGemahnt === true" class="icon-sm align-middle i-ri-checkbox-line bg-ui-danger" />
						<span v-else class="icon-sm align-middle i-ri-checkbox-blank-line" />
					</td>
				</template>
			</template>
			<template v-if="gridManager.isColVisible('FS') ?? true">
				<td v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)"
					:ref="inputFehlstunden(pair, 4, index)" class="ui-table-grid-input"
					:class="{ 'bg-ui-selected': (gridManager.focusColumn === 4) }" />
				<td v-else>{{ pair.a.fehlstundenFach ?? "—" }}</td>
			</template>
			<template v-if="gridManager.isColVisible('FSU') ?? true">
				<td v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)"
					:ref="inputFehlstundenUnendschuldigt(pair, 5, index)" class="ui-table-grid-input"
					:class="{ 'bg-ui-selected': (gridManager.focusColumn === 5) }" />
				<td v-else>{{ pair.a.fehlstundenUnentschuldigtFach ?? "-" }}</td>
			</template>
			<template v-if="gridManager.isColVisible('Bemerkung') ?? true">
				<td :ref="inputBemerkung(pair, 6, index)" class="ui-table-grid-button"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === 6),
						'bg-ui-selected text-ui-onselected': ((gridManager.focusColumnLast === 6) && (gridManager.focusRowLast === index)),
					}">
					<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ pair.a.fachbezogeneBemerkungen ?? "-" }}</span>
				</td>
			</template>
			<td />
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import type { ComponentPublicInstance} from 'vue';
	import { computed, watchEffect } from 'vue';
	import type { EnmLeistungenUebersichtProps } from './EnmLeistungenUebersichtProps';
	import type { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';
	import type { PairNN } from '../../../../core/src/asd/adt/PairNN';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import { Note } from '../../../../core/src/asd/types/Note';
	import type { List } from '../../../../core/src/java/util/List';
	import { ArrayList } from '../../../../core/src/java/util/ArrayList';
	import type { GridInput } from '../../ui/controls/tablegrid/GridInput';
	import type { GridInputIntegerDiv } from '../../ui/controls/tablegrid/GridInputIntegerDiv';
	import { GridManager } from '../../ui/controls/tablegrid/GridManager';

	const props = defineProps<EnmLeistungenUebersichtProps>();

	const colsValidationTooltip = new Set(["Quartal", "Note", "FS", "FSU"]);

	const gridManager = new GridManager<string, PairNN<ENMLeistung, ENMSchueler>, List<PairNN<ENMLeistung, ENMSchueler>>>({
		daten: computed<List<PairNN<ENMLeistung, ENMSchueler>>>(() => {
			const result = new ArrayList<PairNN<ENMLeistung, ENMSchueler>>();
			for (const lerngruppenAuswahl of props.auswahl()) {
				const leistungen = props.enmManager().mapLerngruppeLeistungen.get(lerngruppenAuswahl.id);
				if ((leistungen === null))
					continue;
				result.addAll(leistungen);
			}
			return result;
		}),
		getRowKey: row => `${row.a.id}_${row.b.id}`,
		columns: [
			{ kuerzel: "Klasse", name: "Klasse", width: "4rem", hideable: false },
			{ kuerzel: "Name", name: "Name, Vorname", width: "16rem", hideable: false },
			{ kuerzel: "Fach", name: "Fach", width: "4rem", hideable: false },
			{ kuerzel: "Kurs", name: "Kurs", width: "6rem", hideable: true },
			{ kuerzel: "Kursart", name: "Kursart", width: "4rem", hideable: true },
			{ kuerzel: "Lehrer", name: "Fachlehrer", width: "4rem", hideable: true },
			{ kuerzel: "Quartal", name: "Quartalsnote", width: "6rem", hideable: true },
			{ kuerzel: "Note", name: "Note", width: "6rem", hideable: true },
			{ kuerzel: "Mahnung", name: "Mahnung", width: "5rem", hideable: true },
			{ kuerzel: "FS", name: "Fehlstunden", width: "4rem", hideable: true },
			{ kuerzel: "FSU", name: "Fehlstunden (unentschuldigt)", width: "4rem", hideable: true },
			{ kuerzel: "Bemerkung", name: "Fachbezogene Bemerkungen", width: "16rem", hideable: true },
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
		void props.focusFloskelEditor(pair.b, pair.a, input.row, false);
	}
	defineExpose({ gridManager });



	const notenKuerzel = computed(() => Note.values().map(e => e.daten(props.enmManager().schuljahr)?.kuerzel).filter(e => e !== ""));

	function inputNoteQuartal(pair: PairNN<ENMLeistung, ENMSchueler>, col: number, index: number) {
		const key = 'Quartalsnote_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : string | null) => void props.patchLeistung(pair.a, { noteQuartal: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputNote(key, col, index, element, setter, props.enmManager().schuljahr);
			if (input !== null)
				gridManager.update(key, pair.a.noteQuartal);
		};
	}

	function inputNote(pair: PairNN<ENMLeistung, ENMSchueler>, col: number, index: number) {
		const key = 'Note_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : string | null) => void props.patchLeistung(pair.a, { note: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputNote(key, col, index, element, setter, props.enmManager().schuljahr);
			if (input !== null)
				gridManager.update(key, pair.a.note);
		};
	}

	function inputMahnung(pair: PairNN<ENMLeistung, ENMSchueler>, col: number, index: number) {
		const key = 'Mahnung_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : boolean) => void props.patchLeistung(pair.a, { istGemahnt: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, pair.a.istGemahnt ?? false));
		};
	}

	function inputFehlstunden(pair: PairNN<ENMLeistung, ENMSchueler>, col: number, index: number) {
		const key = 'Fehlstunden_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : number | null) => {
			const patch = <Partial<ENMLeistung>>{ fehlstundenFach: value };
			const inputFSU = gridManager.getInputByKey('FehlstundenUnendschuldigt_' + pair.a.id + "_" + pair.b.id);
			if (inputFSU !== null) {
				const inputFSUTyped = inputFSU as GridInputIntegerDiv<string>;
				inputFSUTyped.max = value ?? 0;
				if ((patch.fehlstundenUnentschuldigtFach ?? 0) > (value ?? 0))
					patch.fehlstundenUnentschuldigtFach = (value ?? 0);
			}
			void props.patchLeistung(pair.a, { fehlstundenFach: value });
		};
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputIntegerDiv(key, col, index, element, 999, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, pair.a.fehlstundenFach));
		};
	}

	function inputFehlstundenUnendschuldigt(pair: PairNN<ENMLeistung, ENMSchueler>, col: number, index: number) {
		const key = 'FehlstundenUnendschuldigt_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : number | null) => void props.patchLeistung(pair.a, { fehlstundenUnentschuldigtFach: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputIntegerDiv(key, col, index, element, pair.a.fehlstundenFach ?? 0, setter);
			if (input !== null)
				watchEffect(() => gridManager.update(key, pair.a.fehlstundenUnentschuldigtFach));
		};
	}


	function inputBemerkung(pair: PairNN<ENMLeistung, ENMSchueler>, col: number, index: number) {
		const key = 'Bemerkung_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : boolean) => void props.focusFloskelEditor(pair.b, pair.a, index, true);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null) {
				gridManager.update(key, false);
				gridManager.setNavigationOnEnter(key, null);
			}
		};
	}

</script>
