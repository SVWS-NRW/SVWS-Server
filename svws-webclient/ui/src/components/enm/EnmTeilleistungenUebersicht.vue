<template>
	<ui-table-grid name="Leistungsdaten" :header-count="1" :footer-count="0" :manager="() => gridManager">
		<template #header>
			<template v-for="col of gridManager.cols.values()" :key="col.name">
				<template v-if="col.kuerzel !== ''">
					<th v-if="gridManager.isColVisible(col.kuerzel)">
						<template v-if="!colsValidationTooltip.has(col.kuerzel)">
							<svws-ui-tooltip>
								{{ col.kuerzel }}
								<template #content>
									{{ col.name }}
									<ul class="mt-2">
										<li v-for="n in notenKuerzel" :key="n"> {{ n }} </li>
									</ul>
								</template>
							</svws-ui-tooltip>
						</template>
						<template v-else>{{ col.kuerzel }}</template>
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
			<template v-for="(idArt, indexArt) of setTeilleistungsarten" :key="idArt">
				<template v-for="teilleistung of enmManager().mapLeistungTeilleistungsartTeilleistung.getOrNull(pair.a.id, idArt) !== null ? [enmManager().mapLeistungTeilleistungsartTeilleistung.getOrNull(pair.a.id, idArt)!] : [ null ]" :key="teilleistung">
					<template v-if="gridManager.isColVisible(enmManager().mapTeilleistungsarten.get(idArt)?.bezeichnung ?? '???') ?? true">
						<td v-if="teilleistung === null" class="bg-ui-disabled" />
						<td v-else-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)" :ref="inputNoteTeilleistung(pair, teilleistung, indexArt + 1, index)" class="ui-table-grid-input"
							:class="{
								'bg-ui-selected': (gridManager.focusColumn === indexArt + 1),
								'text-ui-danger': Note.fromKuerzel(teilleistung.note).istDefizitSekII(),
							}" />
						<td v-else :class="{ 'text-ui-danger': Note.fromKuerzel(teilleistung.note).istDefizitSekII() }">{{ teilleistung.note ?? "-" }}</td>
					</template>
				</template>
			</template>
			<template v-if="gridManager.isColVisible('Quartal') ?? true">
				<td v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)" :ref="inputNoteQuartal(pair, setTeilleistungsarten.size() + 1, index)" class="ui-table-grid-input"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === setTeilleistungsarten.size() + 1),
						'text-ui-danger': Note.fromKuerzel(pair.a.noteQuartal).istDefizitSekII(),
					}" />
				<td v-else :class="{ 'text-ui-danger': Note.fromKuerzel(pair.a.noteQuartal).istDefizitSekII() }">{{ pair.a.noteQuartal ?? "-" }}</td>
			</template>
			<template v-if="gridManager.isColVisible('Note') ?? true">
				<td v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)" :ref="inputNote(pair, setTeilleistungsarten.size() + 2, index)" class="ui-table-grid-input"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === setTeilleistungsarten.size() + 2),
						'text-ui-danger': Note.fromKuerzel(pair.a.note).istDefizitSekII(),
					}" />
				<td v-else :class="{ 'text-ui-danger': Note.fromKuerzel(pair.a.note).istDefizitSekII() }">{{ pair.a.note ?? "-" }}</td>
			</template>
			<td />
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import type { ComponentPublicInstance} from 'vue';
	import { computed, watch } from 'vue';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';
	import type { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';
	import type { PairNN } from '../../../../core/src/asd/adt/PairNN';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import type { ENMTeilleistung } from '../../../../core/src/core/data/enm/ENMTeilleistung';
	import { HashSet } from '../../../../core/src/java/util/HashSet';
	import { GridManager } from '../../ui/controls/tablegrid/GridManager';
	import type { List } from '../../../../core/src/java/util/List';
	import { ArrayList } from '../../../../core/src/java/util/ArrayList';
	import { Note } from '../../../../core/src/asd/types/Note';

	const props = defineProps<EnmTeilleistungenProps>();

	const colsValidationTooltip = new Set(['Fach', 'Lehrer', 'Kurs', 'Kursart']);
	const notenKuerzel = computed(() => Note.values().map(e => e.daten(props.enmManager().schuljahr)?.kuerzel).filter(e => e !== ""));

	const setTeilleistungsarten = computed(() => {
		const result = new HashSet<number>();
		for (const lerngruppe of props.auswahl()) {
			const arten = props.enmManager().mapLerngruppeTeilleistungsarten.get(lerngruppe.id);
			if (arten !== null)
				result.addAll(arten);
		}
		return result;
	});

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
		colsVisible: computed<Map<string, boolean|null>>({
			get: () => props.columnsVisible(),
			set: (value) => void props.setColumnsVisible(value),
		}),
	});
	watch(setTeilleistungsarten, (teilleistungsarten) => {
		const cols = [
			{ kuerzel: "Klasse", name: "Klasse", width: "4rem", hideable: false },
			{ kuerzel: "Name", name: "Name, Vorname", width: "16rem", hideable: false },
			{ kuerzel: "Fach", name: "Fach", width: "4rem", hideable: false },
			{ kuerzel: "Kurs", name: "Kurs", width: "6rem", hideable: true },
			{ kuerzel: "Kursart", name: "Kursart", width: "4rem", hideable: true },
			{ kuerzel: "Lehrer", name: "Fachlehrer", width: "4rem", hideable: true },
		];
		for (const idArt of teilleistungsarten) {
			const art = props.enmManager().mapTeilleistungsarten.get(idArt);
			if (art === null)
				continue;
			cols.push({ kuerzel: art.bezeichnung ?? "???", name: art.bezeichnung ?? "???", width: "4rem", hideable: true });
		}
		cols.push({ kuerzel: "Quartal", name: "Quartalsnote", width: "6rem", hideable: true });
		cols.push({ kuerzel: "Note", name: "Note", width: "6rem", hideable: true });
		cols.push({ kuerzel: "", name: "", width: "3.25rem", hideable: false });
		gridManager.setColumns(cols);
	}, { immediate: true });


	function inputNoteTeilleistung(pair: PairNN<ENMLeistung, ENMSchueler>, teilleistung: ENMTeilleistung, col: number, index: number) {
		const key = 'Teilleistung_' + teilleistung.id + '_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : string | null) => void props.patchTeilleistung(teilleistung, { note: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputNote(key, col, index, element, setter, props.enmManager().schuljahr);
			if (input !== null)
				gridManager.update(key, teilleistung.note);
		};
	}

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

</script>
