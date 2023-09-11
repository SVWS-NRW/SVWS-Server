<template>
	<div class="page--content">
		<svws-ui-content-card title="Abschlüsse und Berechtigungen an aktueller Schule" class="opacity-50">
			<svws-ui-input-wrapper>
				<div class="opacity-50">
					Hier werden zunkünftig die Informationen zu den Abschlüssen und Berechtigungen an der aktuellen Schule angezeigt
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachenfolge" class="opacity-50">
			<svws-ui-data-table :items="sprachbelegungen()" :columns="colsSprachenfolge">
				<!-- -->
			</svws-ui-data-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachprüfungen" class="opacity-50">
			<svws-ui-input-wrapper>
				<svws-ui-data-table :items="sprachpruefungen()" :columns="colsSprachpruefungen">
					<!-- -->
				</svws-ui-data-table>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerLaufbahninfoProps } from './SchuelerLaufbahninfoProps';
	import type { DataTableColumn } from "@ui";

	const props = defineProps<SchuelerLaufbahninfoProps>();

	const colsSprachenfolge: Array<DataTableColumn> = [
		{ key: "sprache", label: "Sp", tooltip: "Kürzel der Sprache", align: 'center', fixedWidth: 4 },
		{ key: "reihenfolge", label: "RF", tooltip: "Reihenfolge", align: 'center', fixedWidth: 4 },
		{ key: "belegungVonJahrgang", label: "ab Jg", tooltip: "belegt ab Jahrgang", align: 'center', fixedWidth: 4 },
		{ key: "belegungVonAbschnitt", label: "ab Hj", tooltip: "belegt ab Abschnitt", align: 'center', fixedWidth: 4 },
		{ key: "belegungBisJahrgang", label: "bis Jg", tooltip: "belegt bis Jahrgang", align: 'center', fixedWidth: 4 },
		{ key: "belegungBisAbschnitt", label: "bis Hj", tooltip: "belegt bis Abschnitt", align: 'center', fixedWidth: 4 },
		{ key: "referenzniveau", label: "Niveau", tooltip: "das Referenzniveau", align: 'center', fixedWidth: 6 },
	];

	const colsSprachpruefungen: Array<DataTableColumn> = [
		{ key: "sprache", label: "Sp", tooltip: "Kürzel der Sprache", align: 'center', fixedWidth: 4 },
		{ key: "jahrgang", label: "Jg", tooltip: "im Jahrgang", align: 'center', fixedWidth: 4 },
		{ key: "anspruchsniveauId", label: "Anspruchsniveau", tooltip: "das Anspruchsniveau", align: 'center', fixedWidth: 4 },
		{ key: "pruefungsdatum", label: "Prüfungsdatum", tooltip: "Prüfungsdatum", align: 'center', fixedWidth: 6 },
		{ key: "sprache", label: "Ers-Sp", tooltip: "Die durch die Prüfung ersetzte Sprache", align: 'center', fixedWidth: 4 },
		{ key: "istHSUPruefung", label: "HSU", tooltip: "Ist eine HSU-Prüfung", align: 'center', fixedWidth: 4 },
		{ key: "istFeststellungspruefung", label: "Festst", tooltip: "Ist eine Feststellungsprüfung", align: 'center', fixedWidth: 4 },
		{ key: "kannErstePflichtfremdspracheErsetzen", label: "1. FS", tooltip: "Kann die erster Fremdsprache ersetzen", align: 'center', fixedWidth: 4 },
		{ key: "kannZweitePflichtfremdspracheErsetzen", label: "2. FS", tooltip: "Kann die zweite Fremdsprache ersetzen", align: 'center', fixedWidth: 4 },
		{ key: "kannWahlpflichtfremdspracheErsetzen", label: "WP FS", tooltip: "Kann die Wahlpflicht-Fremdsprache ersetzen", align: 'center', fixedWidth: 4 },
		{ key: "kannBelegungAlsFortgefuehrteSpracheErlauben", label: "Fortgef.", tooltip: "Kann die Belegung als fortgeführte Fremdsprache erlauben", align: 'center', fixedWidth: 4 },
		{ key: "referenzniveau", label: "Niveau", tooltip: "das Referenzniveau", align: 'center', fixedWidth: 6 },
		{ key: "note", label: "Note", tooltip: "die Prüfungsnote", align: 'center', fixedWidth: 6 },
	];

</script>
