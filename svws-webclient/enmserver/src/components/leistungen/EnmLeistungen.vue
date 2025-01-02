<template>
	<div class="page--content h-full w-full overflow-hidden">
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
					<td class="svws-ui-td" role="columnheader"> Quartalsnote </td>
					<td class="svws-ui-td" role="columnheader"> Note </td>
					<td class="svws-ui-td" role="columnheader"> Mahnung </td>
					<td class="svws-ui-td" role="columnheader"> Fehlstunden </td>
					<td class="svws-ui-td" role="columnheader"> Fehlstunden (unentschuldigt) </td>
					<td class="svws-ui-td" role="columnheader"> Fachbezogene Bemerkungen </td>
				</tr>
			</thead>
			<tbody class="svws-ui-tbody h-full overflow-y-auto" role="rowgroup" aria-label="Tabelleninhalt">
				<template v-for="(schueler, indexSchueler) of manager.lerngruppenAuswahlGetSchueler()" :key="schueler.id">
					<template v-for="(leistung, indexLeistung) of manager.leistungenGetOfSchueler(schueler.id)" :key="leistung.id">
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
							<td class="svws-ui-td" role="cell">
								{{ leistung.noteQuartal }}
							</td>
							<td class="svws-ui-td" role="cell">
								{{ leistung.note }}
							</td>
							<td class="svws-ui-td" role="cell">
								{{ leistung.istGemahnt }}
							</td>
							<td class="svws-ui-td" role="cell">
								<svws-ui-input-number v-if="manager.fehlstundenFachbezogen(schueler)" placeholder="Fehlstunden"
									:model-value="leistung.fehlstundenFach" headless hide-stepper min="0" max="999"
									@change="fehlstundenFach => patchLeistung(leistung, { fehlstundenFach })" />
								<span v-else>—</span>
							</td>
							<td class="svws-ui-td" role="cell">
								<svws-ui-input-number v-if="manager.fehlstundenFachbezogen(schueler)" placeholder="Fehlstunden (unentschuldigt)"
									:model-value="leistung.fehlstundenUnentschuldigtFach" headless hide-stepper min="0" max="999"
									@change="fehlstundenUnentschuldigtFach => patchLeistung(leistung, { fehlstundenUnentschuldigtFach })" />
								<span v-else>—</span>
							</td>
							<td class="svws-ui-td" role="cell">
								{{ leistung.fachbezogeneBemerkungen }}
							</td>
						</tr>
					</template>
				</template>
			</tbody>
		</table>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { EnmLeistungenProps } from './EnmLeistungenProps';
	import type { EnmLeistungAuswahl } from './EnmManager';
	import type { ENMLeistung } from '@core';

	const props = defineProps<EnmLeistungenProps>();

	function setAuswahlLeistung(value: EnmLeistungAuswahl) {
		props.manager.auswahlLeistung = value;
	}

	function patchLeistung(leistung: ENMLeistung, patch: Partial<ENMLeistung>) {
		console.log(leistung, patch);
	}

	// TODO
	const gridTemplateColumnsComputed = computed(() => 'repeat(auto-fit, minmax(0, 1fr))');

</script>

<style lang="postcss" scoped>

	.svws-ui-tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-height: auto;
	}

	.page--content {
		display: inline-block;
	}

</style>