<template>
	<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle"
		@keydown.down.prevent.stop="manager.auswahlLeistungNaechste()" @keydown.up.prevent.stop="manager.auswahlLeistungVorherige()">
		<thead class="svws-ui-thead cursor-pointer mb-1" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr" role="row">
				<template v-for="col of cols" :key="col.name">
					<template v-if="col.name === 'Teilleistung'">
						<template v-for="art of manager.lerngruppenAuswahlGetTeilleistungsarten()" :key="art.id">
							<td class="svws-ui-td" role="columnheader"> {{ art.bezeichnung ?? '???' }} </td>
						</template>
					</template>
					<td v-else-if="col.sichtbar ?? true" class="svws-ui-td" role="columnheader">
						<svws-ui-tooltip v-if="col.kuerzel !== col.name">
							{{ col.kuerzel }}
							<template #content>{{ col.name }}</template>
						</svws-ui-tooltip>
						<span v-else>{{ col.kuerzel }}</span>
					</td>
				</template>
				<td class="svws-ui-td" role="columnheader">
					<svws-ui-tooltip :hover="false" :show-arrow="false" position="top" class="h-full w-full">
						<span class="icon inline-block" :class="cols.some(c => c.sichtbar === false) ? 'i-ri-layout-column-fill' : 'i-ri-layout-column-line'" />
						<template #content>
							<ul class="min-w-[10rem] flex flex-col gap-0.5 pt-1">
								<template v-for="col of cols" :key="col.name">
									<li v-if="col.sichtbar !== null">
										<svws-ui-checkbox :model-value="col.sichtbar" @update:model-value="value => col.sichtbar = value">
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
			<template v-for="(schueler, indexSchueler) of manager.lerngruppenAuswahlGetSchueler()" :key="schueler">
				<template v-for="(leistung, indexLeistung) of manager.leistungenGetOfSchueler(schueler.id)" :key="leistung">
					<tr class="svws-ui-tr" role="row" :class="{ 'svws-clicked': manager.auswahlLeistung.leistung === leistung }" @click.capture.exact="setAuswahlLeistung({ indexSchueler, indexLeistung, leistung })">
						<td class="svws-ui-td" role="cell" v-if="colKlasse.sichtbar ?? true">
							{{ manager.schuelerGetKlasse(schueler.id).kuerzelAnzeige }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colName.sichtbar ?? true">
							{{ schueler.nachname }}, {{ schueler.vorname }} ({{ schueler.geschlecht }})
						</td>
						<td class="svws-ui-td" role="cell" v-if="colFach.sichtbar ?? true">
							{{ manager.lerngruppeGetFachkuerzel(leistung.lerngruppenID) }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colKurs.sichtbar ?? true">
							{{ manager.lerngruppeGetKursbezeichnung(leistung.lerngruppenID) }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colKursart.sichtbar ?? true">
							{{ (leistung.abiturfach === null) ? manager.lerngruppeGetKursartAsString(leistung.lerngruppenID) : ((leistung.abiturfach < 3) ? "LK" + leistung.abiturfach : "AB" + leistung.abiturfach) }}
						</td>
						<td class="svws-ui-td" role="cell" v-if="colLehrer.sichtbar ?? true">
							{{ manager.lerngruppeGetFachlehrerOrNull(leistung.lerngruppenID) }}
						</td>
						<template v-for="art of manager.lerngruppenAuswahlGetTeilleistungsarten()" :key="art.id">
							<td class="svws-ui-td" role="cell">
								<template v-if="manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id) !== null">
									<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full"
										:items="Note.values()" :item-text="item => item.daten(manager.schuljahr)?.kuerzel ?? '—'"
										:model-value="Note.fromKuerzel(manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.note)"
										@update:model-value="value => doPatchTeilleistung(manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!, { note: value?.daten(manager.schuljahr)?.kuerzel ?? null })" />
									<div v-else>{{ manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.note }}</div>
								</template>
							</td>
						</template>
						<td class="svws-ui-td" role="cell" v-if="colQuartal.sichtbar ?? true">
							<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full"
								:items="Note.values()" :item-text="item => item.daten(manager.schuljahr)?.kuerzel ?? '—'"
								:model-value="Note.fromKuerzel(leistung.noteQuartal)"
								@update:model-value="value => doPatchLeistung(leistung, { noteQuartal: value?.daten(manager.schuljahr)?.kuerzel ?? null })" />
							<div v-else>{{ leistung.noteQuartal }}</div>
						</td>
						<td class="svws-ui-td" role="cell" v-if="colNote.sichtbar ?? true">
							<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full"
								:items="Note.values()" :item-text="item => item.daten(manager.schuljahr)?.kuerzel ?? '—'"
								:model-value="Note.fromKuerzel(leistung.note)"
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

	import { computed, ref } from 'vue';
	import type { EnmLeistungAuswahl } from './EnmManager';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';
	import type { ENMLeistung } from '@core/core/data/enm/ENMLeistung';
	import type { ENMTeilleistung } from '@core/core/data/enm/ENMTeilleistung';
	import { Note } from '@core/asd/types/Note';

	const props = defineProps<EnmTeilleistungenProps>();

	const colKlasse = { kuerzel: "Klasse", name: "Klasse", sichtbar: null, width: "1fr" };
	const colName =	{ kuerzel: "Name, Vorname", name: "Name, Vorname", sichtbar: null, width: "3fr" };
	const colFach =	{ kuerzel: "Fach", name: "Fach", sichtbar: null, width: "1fr" };
	const colKurs =	{ kuerzel: "Kurs", name: "Kurs", sichtbar: true, width: "1fr" };
	const colKursart = { kuerzel: "Kursart", name: "Kursart", sichtbar: true, width: "1fr" };
	const colLehrer = { kuerzel: "Lehrer", name: "Fachlehrer", sichtbar: true, width: "1fr" };
	const colTeilleistung = { kuerzel: "Teilleistung", name: "Teilleistung", sichtbar: null, width: "" };
	const colQuartal = { kuerzel: "Quartal", name: "Quartalsnote", sichtbar: true, width: "1fr" };
	const colNote = { kuerzel: "Note", name: "Note", sichtbar: null, width: "1fr" };

	const cols = ref([ colKlasse, colName, colFach, colKurs, colKursart, colLehrer, colTeilleistung, colQuartal, colNote ]);

	function setAuswahlLeistung(value: EnmLeistungAuswahl) {
		props.manager.auswahlLeistung = value;
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

	const gridTemplateColumnsComputed = computed<string>(() => {
		let frs = " ";
		for (const art of props.manager.lerngruppenAuswahlGetTeilleistungsarten())
			frs += "1fr ";
		return cols.value.filter(c => c.sichtbar ?? true).map(c => c.width).join(" ") + frs + "5em";
	});

</script>

<style lang="postcss" scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

</style>