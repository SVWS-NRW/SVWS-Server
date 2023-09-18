<template>
	<div class="page--content">
		<svws-ui-content-card title="Abschlüsse und Berechtigungen an aktueller Schule" class="opacity-50">
			<svws-ui-input-wrapper>
				<div class="opacity-50">
					Hier werden zunkünftig die Informationen zu den Abschlüssen und Berechtigungen an der aktuellen Schule angezeigt
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachenfolge" class="">
			<svws-ui-table :items="sprachbelegungen()" :columns="colsSprachenfolge">
				<template #cell(belegungVonAbschnitt)="{ rowData }">
					<svws-ui-text-input type="number" :valid="abschnitt" :model-value="rowData.belegungVonAbschnitt" @change="belegungVonAbschnitt => patchSprachbelegung({belegungVonAbschnitt: Number(belegungVonAbschnitt), sprache: rowData.sprache})" headless />
				</template>
				<template #cell(belegungVonJahrgang)="{ rowData }">
					<svws-ui-text-input type="text" :valid="jahrgang" :model-value="rowData.belegungVonJahrgang" @change="belegungVonJahrgang => patchSprachbelegung({belegungVonJahrgang, sprache: rowData.sprache})" headless />
				</template>
				<template #cell(belegungBisAbschnitt)="{ rowData }">
					<svws-ui-text-input type="number" :valid="abschnitt" :model-value="rowData.belegungBisAbschnitt" @change="belegungBisAbschnitt => patchSprachbelegung({belegungBisAbschnitt: Number(belegungBisAbschnitt), sprache: rowData.sprache})" headless />
				</template>
				<template #cell(belegungBisJahrgang)="{ rowData }">
					<svws-ui-text-input type="text" :valid="jahrgang" :model-value="rowData.belegungBisJahrgang" @change="belegungBisJahrgang => patchSprachbelegung({belegungBisJahrgang, sprache: rowData.sprache})" headless />
				</template>
				<template #cell(referenzniveau)="{ rowData }">
					<svws-ui-checkbox v-if="rowData.sprache === 'G'" v-model="hatGraecum" headless title="Graecum">Graecum</svws-ui-checkbox>
					<svws-ui-checkbox v-else-if="rowData.sprache === 'H'" v-model="hatHebraicum" headless title="Hebraicum">Hebraicum</svws-ui-checkbox>
					<template v-else-if="rowData.sprache === 'L'">
						<svws-ui-checkbox v-model="hatKleinesLatinum" headless title="Kleines Latinum">Kleines Latinum</svws-ui-checkbox>
						<svws-ui-checkbox v-model="hatLatinum" headless title="Latinum">Latinum</svws-ui-checkbox>
					</template>
					<template v-else>
						<svws-ui-multi-select title="Referenzniveau" headless :model-value="Sprachreferenzniveau.getByKuerzel(rowData.referenzniveau)" @change="(referenzniveau: Sprachreferenzniveau) => patchSprachbelegung({referenzniveau: referenzniveau.name(), sprache: rowData.sprache})" :items="Sprachreferenzniveau.values()" :item-text="(i: Sprachreferenzniveau)=>i.name()" />
					</template>
				</template>
				<!--  -->
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachprüfungen" class="col-span-full">
			<svws-ui-input-wrapper>
				<svws-ui-table :items="sprachpruefungen()" :columns="colsSprachpruefungen">
					<!-- -->
				</svws-ui-table>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerLaufbahninfoProps } from './SchuelerLaufbahninfoProps';
	import type { DataTableColumn, InputDataType } from "@ui";
	import { computed } from 'vue';
	import { Sprachreferenzniveau } from '@core';

	const props = defineProps<SchuelerLaufbahninfoProps>();

	const colsSprachenfolge: Array<DataTableColumn> = [
		{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache" },
		{ key: "reihenfolge", label: "Reihenfolge", tooltip: "Reihenfolge", divider: true },
		{ key: "belegungVonJahrgang", label: "ab Jg", tooltip: "belegt ab Jahrgang" },
		{ key: "belegungVonAbschnitt", label: "Halbjahr", tooltip: "belegt ab Abschnitt", divider: true },
		{ key: "belegungBisJahrgang", label: "bis Jg", tooltip: "belegt bis Jahrgang" },
		{ key: "belegungBisAbschnitt", label: "Halbjahr", tooltip: "belegt bis Abschnitt", divider: true },
		{ key: "referenzniveau", label: "Qualifikation", tooltip: "die erreichte Qualifikation", span: 2 },
	];

	const colsSprachpruefungen: Array<DataTableColumn> = [
		{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache", minWidth: 4 },
		{ key: "jahrgang", label: "Jahrgang", tooltip: "Im Jahrgang", minWidth: 4 },
		{ key: "anspruchsniveauId", label: "Anspruch", tooltip: "Anspruchsniveau", minWidth: 4 },
		{ key: "pruefungsdatum", label: "Prüfungsdatum", tooltip: "Prüfungsdatum", minWidth: 6, span: 1.5 },
		{ key: "sprache", label: "Ersetzt", tooltip: "Die durch die Prüfung ersetzte Sprache", minWidth: 4 },
		{ key: "istHSUPruefung", label: "HSU", tooltip: "Ist eine HSU-Prüfung", align: 'center', minWidth: 4, span: 0.5 },
		{ key: "istFeststellungspruefung", label: "Feststellungsprüfung", tooltip: "Ist eine Feststellungsprüfung", align: 'center', minWidth: 4, span: 0.5 },
		{ key: "kannErstePflichtfremdspracheErsetzen", label: "1. FS", tooltip: "Kann die erster Fremdsprache ersetzen", align: 'center', minWidth: 4, span: 0.5 },
		{ key: "kannZweitePflichtfremdspracheErsetzen", label: "2. FS", tooltip: "Kann die zweite Fremdsprache ersetzen", align: 'center', minWidth: 4, span: 0.5 },
		{ key: "kannWahlpflichtfremdspracheErsetzen", label: "WP FS", tooltip: "Kann die Wahlpflicht-Fremdsprache ersetzen", align: 'center', minWidth: 4, span: 0.5 },
		{ key: "kannBelegungAlsFortgefuehrteSpracheErlauben", label: "Fortgeführt", tooltip: "Kann die Belegung als fortgeführte Fremdsprache erlauben", align: 'center', minWidth: 4, span: 0.5 },
		{ key: "referenzniveau", label: "Niveau", tooltip: "Referenzniveau", minWidth: 6 },
		{ key: "note", label: "Note", tooltip: "Prüfungsnote", minWidth: 6 },
	];

	function abschnitt(item: InputDataType) {
		if (typeof item !== 'number')
			return false;
		return (item < 1 || item > 2)
	}

	function jahrgang(item: InputDataType) {
		if (typeof item !== 'string')
			return false;
		return (Number(item) < 0 || Number(item) > 13)
	}

	const hatKleinesLatinum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen())
				if (sprache.sprache === 'L')
					return sprache.hatKleinesLatinum;
			return false;
		},
		set: (hatKleinesLatinum) => void props.patchSprachbelegung({hatKleinesLatinum, sprache: 'L'})
	});

	const hatLatinum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen())
				if (sprache.sprache === 'L')
					return sprache.hatLatinum;
			return false;
		},
		set: (hatLatinum) => void props.patchSprachbelegung({hatLatinum, sprache: 'L'})
	});

	const hatGraecum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen())
				if (sprache.sprache === 'G')
					return sprache.hatGraecum;
			return false;
		},
		set: (hatGraecum) => void props.patchSprachbelegung({hatGraecum, sprache: 'G'})
	});

	const hatHebraicum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen())
				if (sprache.sprache === 'H')
					return sprache.hatHebraicum;
			return false;
		},
		set: (hatHebraicum) => void props.patchSprachbelegung({hatHebraicum, sprache: 'H'})
	});

</script>
