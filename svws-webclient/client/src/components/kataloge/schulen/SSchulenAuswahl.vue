<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span>Schulen</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="schulen" :columns="cols" selectable v-model="liste">
				<template #cell(kurzbezeichnung)="{ rowData }">
					<div class="text-ellipsis overflow-hidden whitespace-nowrap" :title="`${rowData.plz || ''} ${rowData.ort || ''}${rowData.ort ? ',': ''} ${rowData.name}`">{{ rowData.kurzbezeichnung }}</div>
				</template>
				<template #actions>
					<svws-ui-button @click="remove" type="trash" :disabled="liste.length === 0" />
					<svws-ui-button @click="add" type="icon" title="Eine neue Schule hinzufügen"><i-ri-add-line /></svws-ui-button>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { ref, computed } from 'vue';
	import type { SchulenAuswahlProps } from './SSchulenAuswahlProps';
	import { SchulEintrag } from '@core';

	const props = defineProps<SchulenAuswahlProps>();

	const liste = ref([]);

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "kurzbezeichnung", label: "Bezeichnung", sortable: true, span: 4 }
	];

	async function remove() {
		await props.removeEintraege(liste.value);
		liste.value = [];
	}

	async function add() {
		const data = new SchulEintrag();
		await props.addEintrag(data);
	}

	const schulen = computed(()=>{
		const kuerzel = [];
		const keinkeurzel = [];
		for (const schule of props.mapKatalogeintraege().values())
			if (schule.kuerzel)
				kuerzel.push(schule);
			else
				keinkeurzel.push(schule);
		kuerzel.sort((a, b) => a.schulnummer > b.schulnummer ? 1 : -1);
		keinkeurzel.sort((a, b) => a.schulnummer > b.schulnummer ? 1 : -1);
		return kuerzel.concat(keinkeurzel);
	})

</script>
