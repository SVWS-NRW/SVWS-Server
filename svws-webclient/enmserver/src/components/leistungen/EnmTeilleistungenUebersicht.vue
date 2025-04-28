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
						<span class="icon" :class="[...colsVisible.values()].some(c => c === false) ? 'i-ri-layout-column-fill' : 'i-ri-layout-column-line'" /><span class="icon i-ri-arrow-down-s-line" />						<template #content>
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
									<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full" :items="Note.values()"
										:item-text="item => item.daten(manager.schuljahr)?.kuerzel ?? '—'" :focus-class-content="manager.auswahlLeistung.leistung === leistung && art.id === 5"
										:model-value="Note.fromKuerzel(manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.note)"
										@focusin="() => { if(art.id === 5) tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, 0) }"
										@update:model-value="value => doPatchTeilleistung(manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!, { note: value?.daten(manager.schuljahr)?.kuerzel ?? null })" />
									<div v-else>{{ manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.note }}</div>
								</template>
							</td>
						</template>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Quartal') ?? true">
							<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full"
								:items="Note.values()" :item-text="item => item.daten(manager.schuljahr)?.kuerzel ?? '—'"
								:model-value="Note.fromKuerzel(leistung.noteQuartal)"
								@update:model-value="value => doPatchLeistung(leistung, { noteQuartal: value?.daten(manager.schuljahr)?.kuerzel ?? null })" />
							<div v-else>{{ leistung.noteQuartal }}</div>
						</td>
						<td class="svws-ui-td" role="cell" v-if="colsVisible.get('Note') ?? true">
							<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full"
								:items="Note.values()" :item-text="item => item.daten(manager.schuljahr)?.kuerzel ?? '—'"
								:model-value="Note.fromKuerzel(leistung.note)" @focusin="tabToUnselectedLeistung(indexSchueler, indexLeistung, leistung, columnsComputed.length - 1)"
								@update:model-value="value => doPatchLeistung(leistung, { note: value?.daten(manager.schuljahr)?.kuerzel ?? null })" />
							<div v-else>{{ leistung.note }}</div>
						</td>
						<td class="svws-ui-td" role="cell" />
					</tr>
				</template>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from 'vue';
	import type { EnmLeistungAuswahl } from './EnmManager';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';
	import type { ENMLeistung } from '@core/core/data/enm/ENMLeistung';
	import type { ENMTeilleistung } from '@core/core/data/enm/ENMTeilleistung';
	import { Note } from '@core/asd/types/Note';

	const props = defineProps<EnmTeilleistungenProps>();
	const rowRefs = ref(new Map<number, { element: HTMLElement, isLastRow: boolean }>());
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
		() => Array.from(rowRefs.value.get(props.manager.auswahlLeistung.leistung!.id)!.element.querySelectorAll("input"))
	);

	function setAuswahlLeistung(value: EnmLeistungAuswahl) {
		props.manager.auswahlLeistung = value;
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

	function tabToUnselectedLeistung(indexSchueler: number, indexLeistung: number, leistung: ENMLeistung, columnIndex: number) {
		currentColumn.value = columnIndex;
		setAuswahlLeistung({ indexSchueler, indexLeistung, leistung });
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
		() => columnsComputed.value[currentColumn.value].focus()
	)

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

</style>
