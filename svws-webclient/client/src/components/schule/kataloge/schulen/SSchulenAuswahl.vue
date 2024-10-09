<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="gotoSchule">Schule</a>
				<span>Schulen</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="schulen" :columns selectable v-model="liste" scroll-into-view>
				<template #cell(kurzbezeichnung)="{ rowData }">
					<div class="text-ellipsis overflow-hidden whitespace-nowrap" :title="`${rowData.plz || ''} ${rowData.ort || ''}${rowData.ort ? ',': ''} ${rowData.name}`">{{ rowData.kurzbezeichnung }}</div>
				</template>
				<template #actions>
					<svws-ui-button @click="remove" type="trash" :disabled="liste.length === 0" />
					<s-schulen-neu-modal v-slot="{ openModal }" :add-eintrag>
						<svws-ui-button type="icon" @click="openModal()">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</s-schulen-neu-modal>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { ref, computed } from 'vue';
	import type { SchulenAuswahlProps } from './SSchulenAuswahlProps';

	const props = defineProps<SchulenAuswahlProps>();

	const liste = ref([]);

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "kurzbezeichnung", label: "Bezeichnung", sortable: true, span: 4 }
	];

	async function remove() {
		await props.removeEintraege(liste.value);
		liste.value = [];
	}

	const schulen = computed(() => {
		const kuerzel = [];
		const keinkeurzel = [];
		for (const schule of props.mapKatalogeintraege().values())
			if (schule.kuerzel !== null)
				kuerzel.push(schule);
			else
				keinkeurzel.push(schule);
		return kuerzel.concat(keinkeurzel);
	})

</script>
