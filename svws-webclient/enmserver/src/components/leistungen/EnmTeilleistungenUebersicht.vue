<template>
	<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle"
		@keydown.down.prevent.stop="manager.auswahlLeistungNaechste()" @keydown.up.prevent.stop="manager.auswahlLeistungVorherige()">
		<thead class="svws-ui-thead cursor-pointer mb-1" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr" role="row">
				<td class="svws-ui-td" role="columnheader"> Klasse </td>
				<td class="svws-ui-td" role="columnheader"> Name, Vorname </td>
				<td class="svws-ui-td" role="columnheader"> Fach </td>
				<td class="svws-ui-td" role="columnheader"> Kurs </td>
				<td class="svws-ui-td" role="columnheader"> Kursart </td>
				<td class="svws-ui-td" role="columnheader"> Fachlehrer </td>
				<template v-for="art of manager.lerngruppenAuswahlGetTeilleistungsarten()" :key="art.id">
					<td class="svws-ui-td" role="columnheader"> {{ art.bezeichnung ?? '???' }} </td>
				</template>
				<td class="svws-ui-td" role="columnheader"> Quartalsnote </td>
				<td class="svws-ui-td" role="columnheader"> Note </td>
			</tr>
		</thead>
		<tbody class="svws-ui-tbody h-full overflow-y-auto" role="rowgroup" aria-label="Tabelleninhalt">
			<template v-for="(schueler, indexSchueler) of manager.lerngruppenAuswahlGetSchueler()" :key="schueler">
				<template v-for="(leistung, indexLeistung) of manager.leistungenGetOfSchueler(schueler.id)" :key="leistung">
					<tr class="svws-ui-tr" role="row" :class="{ 'svws-clicked': manager.auswahlLeistung.leistung === leistung }" @click.capture.exact="setAuswahlLeistung({ indexSchueler, indexLeistung, leistung })">
						<td class="svws-ui-td" role="cell">
							{{ manager.schuelerGetKlasse(schueler.id).kuerzelAnzeige }}
						</td>
						<td class="svws-ui-td" role="cell">
							{{ schueler.nachname }}, {{ schueler.vorname }} ({{ schueler.geschlecht }})
						</td>
						<td class="svws-ui-td" role="cell">
							{{ manager.lerngruppeGetFachkuerzel(leistung.lerngruppenID) }}
						</td>
						<td class="svws-ui-td" role="cell">
							{{ manager.lerngruppeGetKursbezeichnung(leistung.lerngruppenID) }}
						</td>
						<td class="svws-ui-td" role="cell">
							{{ manager.lerngruppeGetKursartAsString(leistung.lerngruppenID) }}{{ (leistung.abiturfach === null) ? "" : ", Abi " + leistung.abiturfach }}
						</td>
						<td class="svws-ui-td" role="cell">
							{{ manager.lerngruppeGetFachlehrerOrNull(leistung.lerngruppenID) }}
						</td>
						<template v-for="art of manager.lerngruppenAuswahlGetTeilleistungsarten()" :key="art.id">
							<td class="svws-ui-td" role="cell">
								<template v-if="manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id) !== null">
									<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full"
										:items="Note.values()" :item-text="(item: Note) => item.daten(manager.schuljahr)?.kuerzel ?? '—'"
										:model-value="Note.fromKuerzel(manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.note)"
										@update:model-value="value => doPatchTeilleistung(manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!, { note: value?.daten(manager.schuljahr)?.kuerzel ?? null })" />
									<div v-else>{{ manager.lerngruppenAuswahlGetTeilleistungOrNull(leistung.id, art.id)!.note }}</div>
								</template>
							</td>
						</template>
						<td class="svws-ui-td" role="cell">
							<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full"
								:items="Note.values()" :item-text="(item: Note) => item.daten(manager.schuljahr)?.kuerzel ?? '—'"
								:model-value="Note.fromKuerzel(leistung.noteQuartal)"
								@update:model-value="value => doPatchLeistung(leistung, { noteQuartal: value?.daten(manager.schuljahr)?.kuerzel ?? null })" />
							<div v-else>{{ leistung.noteQuartal }}</div>
						</td>
						<td class="svws-ui-td" role="cell">
							<svws-ui-select v-if="manager.lerngruppeIstFachlehrer(leistung.lerngruppenID)" title="—" headless class="w-full"
								:items="Note.values()" :item-text="(item: Note) => item.daten(manager.schuljahr)?.kuerzel ?? '—'"
								:model-value="Note.fromKuerzel(leistung.note)"
								@update:model-value="value => doPatchLeistung(leistung, { note: value?.daten(manager.schuljahr)?.kuerzel ?? null })" />
							<div v-else>{{ leistung.note }}</div>
						</td>
					</tr>
				</template>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { EnmLeistungAuswahl } from './EnmManager';
	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';
	import type { ENMLeistung } from '@core/core/data/enm/ENMLeistung';
	import { Note } from '@core/asd/types/Note';
	import type { ENMTeilleistung } from '@core/core/data/enm/ENMTeilleistung';

	const props = defineProps<EnmTeilleistungenProps>();

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

	// TODO
	const gridTemplateColumnsComputed = computed(() => 'repeat(auto-fit, minmax(0, 1fr))');

</script>

<style lang="postcss" scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

</style>