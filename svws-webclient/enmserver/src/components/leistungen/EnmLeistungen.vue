<template>
	<div class="page--content h-full w-full overflow-hidden">
		<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle">
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
				<template v-for="schueler of manager.lerngruppenAuswahlGetSchueler()" :key="schueler.id">
					<template v-for="leistung of manager.leistungenGetOfSchueler(schueler.id)" :key="leistung.id">
						<tr class="svws-ui-tr" role="row" :class="{ 'svws-clicked': manager.auswahlLeistung?.id === leistung.id }" @click.capture.exact="setAuswahlLeistung(leistung)">
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
								<!-- TODO click.stop für Kontrollelement zum Setzen der auswahlLeistung -->
								{{ leistung.noteQuartal }}
							</td>
							<td class="svws-ui-td" role="cell">
								<!-- TODO click.stop für Kontrollelement zum Setzen der auswahlLeistung -->
								{{ leistung.note }}
							</td>
							<td class="svws-ui-td" role="cell">
								{{ leistung.istGemahnt }}
							</td>
							<td class="svws-ui-td" role="cell">
								<!-- TODO click.stop für Kontrollelement zum Setzen der auswahlLeistung -->
								{{ leistung.fehlstundenFach }}
							</td>
							<td class="svws-ui-td" role="cell">
								<!-- TODO click.stop für Kontrollelement zum Setzen der auswahlLeistung -->
								{{ leistung.fehlstundenUnentschuldigtFach }}
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
	import type { ENMLeistung } from '@core';

	const props = defineProps<EnmLeistungenProps>();

	function setAuswahlLeistung(leistung: ENMLeistung) {
		props.manager.auswahlLeistung = leistung;
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