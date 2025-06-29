<template>
	<ui-table-grid name="Leistungsdaten" :header-count="1" :footer-count="0" :manager="() => gridManager" :cell-format="cellFormat">
		<template #header>
			<template v-for="col of cols" :key="col.name">
				<th v-if="colsVisible.get(col.kuerzel) ?? true">
					<svws-ui-tooltip v-if="col.kuerzel !== col.name">
						{{ col.kuerzel }}
						<template #content>{{ col.name }}</template>
					</svws-ui-tooltip>
					<span v-else>{{ col.kuerzel }}</span>
				</th>
			</template>
			<th>
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
			</th>
		</template>
		<template #default="{ row: pair, index }">
			<td v-if="colsVisible.get('Klasse') ?? true">
				{{ enmManager().mapKlassen.get(pair.b.klasseID)?.kuerzelAnzeige ?? '—' }}
			</td>
			<td v-if="colsVisible.get('Name') ?? true" class="text-left">
				{{ pair.b.nachname }}, {{ pair.b.vorname }} ({{ pair.b.geschlecht }})
			</td>
			<td v-if="colsVisible.get('Fach') ?? true">
				{{ enmManager().lerngruppeGetFachkuerzel(pair.a.lerngruppenID) }}
			</td>
			<td v-if="colsVisible.get('Kurs') ?? true">
				{{ enmManager().lerngruppeGetKursbezeichnung(pair.a.lerngruppenID) }}
			</td>
			<td v-if="colsVisible.get('Kursart') ?? true">
				{{ enmManager().leistungGetKursartAsString(pair.a) }}
			</td>
			<td v-if="colsVisible.get('Lehrer') ?? true">
				{{ enmManager().lerngruppeGetFachlehrerOrNull(pair.a.lerngruppenID) }}
			</td>
			<template v-for="(idArt, indexArt) of setTeilleistungsarten" :key="idArt">
				<template v-for="teilleistung of enmManager().mapLeistungTeilleistungsartTeilleistung.getOrNull(pair.a.id, idArt) !== null ? [enmManager().mapLeistungTeilleistungsartTeilleistung.getOrNull(pair.a.id, idArt)!] : []" :key="teilleistung">
					<template v-if="colsVisible.get(props.enmManager().mapTeilleistungsarten.get(idArt)?.bezeichnung ?? '???') ?? true">
						<td v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)" :ref="inputNoteTeilleistung(pair, teilleistung, indexArt + 1, index)" class="ui-table-grid-input"
							:class="{
								'bg-ui-selected': (gridManager.focusColumn === indexArt + 1),
								'text-ui-danger': Note.fromKuerzel(teilleistung.note).istDefizitSekII(),
							}" />
						<td v-else :class="{ 'text-ui-danger': Note.fromKuerzel(teilleistung.note).istDefizitSekII() }">{{ teilleistung.note ?? "-" }}</td>
					</template>
				</template>
			</template>
			<template v-if="colsVisible.get('Quartal') ?? true">
				<td v-if="enmManager().lerngruppeIstFachlehrer(pair.a.lerngruppenID)" :ref="inputNoteQuartal(pair, setTeilleistungsarten.size() + 1, index)" class="ui-table-grid-input"
					:class="{
						'bg-ui-selected': (gridManager.focusColumn === setTeilleistungsarten.size() + 1),
						'text-ui-danger': Note.fromKuerzel(pair.a.noteQuartal).istDefizitSekII(),
					}" />
				<td v-else :class="{ 'text-ui-danger': Note.fromKuerzel(pair.a.noteQuartal).istDefizitSekII() }">{{ pair.a.noteQuartal ?? "-" }}</td>
			</template>
			<template v-if="colsVisible.get('Note') ?? true">
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
	import { computed, watchEffect } from 'vue';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';
	import type { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';
	import type { PairNN } from '../../../../core/src/asd/adt/PairNN';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import type { ENMTeilleistung } from '../../../../core/src/core/data/enm/ENMTeilleistung';
	import { HashSet } from '../../../../core/src/java/util/HashSet';
	import { GridManager } from '../../ui/controls/tablegrid/GridManager';
	import type { CellFormat } from '../../ui/controls/tablegrid/UiTableGrid.vue';
	import type { List } from '../../../../core/src/java/util/List';
	import { ArrayList } from '../../../../core/src/java/util/ArrayList';
	import { Note } from '../../../../core/src/asd/types/Note';

	const props = defineProps<EnmTeilleistungenProps>();

	const setTeilleistungsarten = computed(() => {
		const result = new HashSet<number>();
		for (const lerngruppe of props.auswahl()) {
			const arten = props.enmManager().mapLerngruppeTeilleistungsarten.get(lerngruppe.id);
			if (arten !== null)
				result.addAll(arten);
		}
		return result;
	});

	const cols = computed(() => {
		const result = [
			{ kuerzel: "Klasse", name: "Klasse", width: "4rem" },
			{ kuerzel: "Name", name: "Name, Vorname", width: "16rem" },
			{ kuerzel: "Fach", name: "Fach", width: "4rem" },
			{ kuerzel: "Kurs", name: "Kurs", width: "6rem" },
			{ kuerzel: "Kursart", name: "Kursart", width: "4rem" },
			{ kuerzel: "Lehrer", name: "Fachlehrer", width: "4rem" },
		];
		for (const idArt of setTeilleistungsarten.value) {
			const art = props.enmManager().mapTeilleistungsarten.get(idArt);
			if (art === null)
				continue;
			result.push({ kuerzel: art.bezeichnung ?? "???", name: "Teilleistung: " + (art.bezeichnung ?? "???"), width: "4rem" });
		}
		result.push({ kuerzel: "Quartal", name: "Quartalsnote", width: "6rem" });
		result.push({ kuerzel: "Note", name: "Note", width: "6rem" });
		return result;
	});

	const colsVisible = computed<Map<string, boolean|null>>({
		get: () => props.columnsVisible(),
		set: (value) => void props.setColumnsVisible(value),
	});


	const daten = computed<List<PairNN<ENMLeistung, ENMSchueler>>>(() => {
		const result = new ArrayList<PairNN<ENMLeistung, ENMSchueler>>();
		for (const lerngruppenAuswahl of props.auswahl()) {
			const leistungen = props.enmManager().mapLerngruppeLeistungen.get(lerngruppenAuswahl.id);
			if ((leistungen === null))
				continue;
			result.addAll(leistungen);
		}
		return result;
	});

	const gridManager = new GridManager<string, PairNN<ENMLeistung, ENMSchueler>, List<PairNN<ENMLeistung, ENMSchueler>>>({
		daten: daten,
		getRowKey: row => `${row.a.id}_${row.b.id}`,
	});

	const cellFormat = computed<CellFormat>(() => {
		const result = <CellFormat>{ widths: [] };
		for (const col of cols.value) {
			const visible = props.columnsVisible().get(col.kuerzel) ?? true;
			if (visible)
				result.widths.push(col.width);
		}
		result.widths.push('3.5rem');
		return result;
	});


	function inputNoteTeilleistung(pair: PairNN<ENMLeistung, ENMSchueler>, teilleistung: ENMTeilleistung, col: number, index: number) {
		const key = 'Teilleistung_' + teilleistung.id + '_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : string | null) => void props.patchTeilleistung(teilleistung, { note: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputNote(key, col, index, element, setter, props.enmManager().schuljahr);
			if (input !== null)
				watchEffect(() => gridManager.update(key, teilleistung.note));
		};
	}

	function inputNoteQuartal(pair: PairNN<ENMLeistung, ENMSchueler>, col: number, index: number) {
		const key = 'Quartalsnote_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : string | null) => void props.patchLeistung(pair.a, { noteQuartal: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputNote(key, col, index, element, setter, props.enmManager().schuljahr);
			if (input !== null)
				watchEffect(() => gridManager.update(key, pair.a.noteQuartal));
		};
	}

	function inputNote(pair: PairNN<ENMLeistung, ENMSchueler>, col: number, index: number) {
		const key = 'Note_' + pair.a.id + "_" + pair.b.id;
		const setter = (value : string | null) => void props.patchLeistung(pair.a, { note: value });
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputNote(key, col, index, element, setter, props.enmManager().schuljahr);
			if (input !== null)
				watchEffect(() => gridManager.update(key, pair.a.note));
		};
	}

</script>
