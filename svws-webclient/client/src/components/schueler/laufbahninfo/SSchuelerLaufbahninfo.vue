<template>
	<div class="page--content">
		<svws-ui-todo title="Abschlüsse und Berechtigungen an aktueller Schule">
			Hier werden zukünftig die Informationen zu den Abschlüssen und Berechtigungen an der aktuellen Schule angezeigt.
		</svws-ui-todo>
		<svws-ui-content-card title="Sprachenfolge" class="">
			<svws-ui-table :items="sprachbelegungen()" :columns="colsSprachenfolge" selectable v-model="auswahl">
				<template #cell(belegungVonAbschnitt)="{ rowData }">
					<div class="flex items-center gap-0.5 border border-black/25 border-dashed hover:border-black/50 hover:border-solid hover:bg-white my-auto p-[0.1rem] rounded cursor-pointer" @click="patchSprachbelegung({belegungVonAbschnitt: rowData.belegungVonAbschnitt === 1 ? 2 : 1, sprache: rowData.sprache})">
						<span :class="{ 'opacity-100 font-bold': rowData.belegungVonAbschnitt === 1, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungVonAbschnitt === 2}">1</span>
						<span class="opacity-50">/</span>
						<span :class="{ 'opacity-100 font-bold': rowData.belegungVonAbschnitt === 2, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungVonAbschnitt === 1}">2</span>
					</div>
				</template>
				<template #cell(belegungVonJahrgang)="{ rowData }">
					<svws-ui-text-input type="text" :valid="jahrgang" :model-value="rowData.belegungVonJahrgang" @change="belegungVonJahrgang => patchSprachbelegung({belegungVonJahrgang, sprache: rowData.sprache})" headless />
				</template>
				<template #cell(belegungBisAbschnitt)="{ rowData }">
					<div class="flex items-center gap-0.5 border border-black/25 border-dashed hover:border-black/50 hover:border-solid hover:bg-white my-auto p-[0.1rem] rounded cursor-pointer" @click="patchSprachbelegung({belegungBisAbschnitt: rowData.belegungVonAbschnitt === 1 ? 2 : 1, sprache: rowData.sprache})">
						<span :class="{ 'opacity-100 font-bold': rowData.belegungBisAbschnitt === 1, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungBisAbschnitt === 2}">1</span>
						<span class="opacity-50">/</span>
						<span :class="{ 'opacity-100 font-bold': rowData.belegungBisAbschnitt === 2, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungBisAbschnitt === 1}">2</span>
					</div>
				</template>
				<template #cell(belegungBisJahrgang)="{ rowData }">
					<svws-ui-text-input type="text" :valid="jahrgang" :model-value="rowData.belegungBisJahrgang" @change="belegungBisJahrgang => patchSprachbelegung({belegungBisJahrgang, sprache: rowData.sprache})" headless />
				</template>
				<template #cell(referenzniveau)="{ rowData }">
					<svws-ui-checkbox v-if="rowData.sprache === 'G'" v-model="hatGraecum" headless title="Graecum">Graecum</svws-ui-checkbox>
					<svws-ui-checkbox v-else-if="rowData.sprache === 'H'" v-model="hatHebraicum" headless title="Hebraicum">Hebraicum</svws-ui-checkbox>
					<template v-else-if="rowData.sprache === 'L'">
						<svws-ui-select headless :items="sprachen" :model-value="latinum" :item-text="i => i.text" @update:model-value="patchLatinum" :removable="true" />
					</template>
					<template v-else>
						<svws-ui-select title="Referenzniveau" headless :removable="true" :model-value="Sprachreferenzniveau.getByKuerzel(rowData.referenzniveau)" @update:model-value="referenzniveau => patchSprachbelegung({referenzniveau: referenzniveau?.name(), sprache: rowData.sprache})" :items="Sprachreferenzniveau.values()" :item-text="itemText" />
					</template>
				</template>
				<!--  -->
				<template #actions>
					<svws-ui-button @click="remove" type="trash" :disabled="auswahl.length === 0" />
					<svws-ui-button @click="suchen" type="icon" title="Diese Sprache in den Leistungsdaten suchen und Beginn und Ende aktualisieren" :disabled="auswahl.length === 0"> <i-ri-search-line /></svws-ui-button>
					<svws-ui-button @click="ermitteln" type="icon" title="Das GER/Latinum anhand aller Daten ermitteln" :disabled="auswahl.length === 0"><ri-calculator-line /></svws-ui-button>
				</template>
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
	import { computed, ref } from 'vue';
	import { Sprachreferenzniveau } from '@core';

	const props = defineProps<SchuelerLaufbahninfoProps>();

	const auswahl = ref([]);

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

	function itemText(item: Sprachreferenzniveau) {
		const name = item.name();
		if (name.length === 2)
			return name;
		return name.slice(0, 2) + "/" + name.slice(2);
	}

	function jahrgang(item: InputDataType) {
		if (typeof item !== 'string')
			return false;
		return (Number(item) < 0 || Number(item) > 13)
	}

	const sprachen = [{text: 'Kleines Latinum'}, {text: 'Latinum'}];
	const latinum = computed(()=> {
		if (hatKleinesLatinum.value)
			return sprachen[0];
		if (hatLatinum.value)
			return sprachen[1];
		return
	})

	async function patchLatinum(item:any) {
		console.log(item)
		if (item === undefined)
			await props.patchSprachbelegung({hatKleinesLatinum: false, hatLatinum: false, sprache: 'L'});
		if (item === sprachen[0])
			await props.patchSprachbelegung({hatKleinesLatinum: true, hatLatinum: false, sprache: 'L'});
		if (item === sprachen[1])
			await props.patchSprachbelegung({hatKleinesLatinum: false, hatLatinum: true, sprache: 'L'});
	}

	const hatKleinesLatinum = computed<boolean>(() => {
		for (const sprache of props.sprachbelegungen())
			if (sprache.sprache === 'L')
				return sprache.hatKleinesLatinum;
		return false;
	})

	const hatLatinum = computed<boolean>(() => {
		for (const sprache of props.sprachbelegungen())
			if (sprache.sprache === 'L')
				return sprache.hatLatinum;
		return false;
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

	async function remove() {
		for (const sprache of auswahl.value)
			await props.removeSprachbelegung(sprache);
	}
	async function suchen() {
		//lösche Sprache
	}
	async function ermitteln() {
		//lösche Sprache
	}
</script>
