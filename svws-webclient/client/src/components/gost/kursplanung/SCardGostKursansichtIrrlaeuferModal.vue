<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Ungültige Kurszuordnungen</template>
		<template #modalDescription>
			Sollen folgende fehlerhafte Kurs-Schüler-Zuordnungen entfernt werden?
			<svws-ui-table selectable v-model="selected" :items="zuordnungen" disable-footer>
				<template #cell(name)="{value: id}"> {{ getErgebnismanager().getOfSchuelerNameVorname(id) }} </template>
				<template #cell(kurs)="{value: kurs}"> {{ getErgebnismanager().getOfKursName(kurs.id) }} </template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="removeZuordnung">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	type Item  = {
		name: number;
		kurs: GostBlockungsergebnisKurs;
	}

	import type { GostBlockungsergebnisManager, GostBlockungsergebnisKurs } from '@core';
	import { computed, ref } from 'vue';

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		removeKursSchuelerZuordnung: (id: number, kurs: number) => Promise<boolean>;
	}>();

	const zuordnungen = computed<Item[]>(()=>{
		const liste: Item[] = [];
		for (const es of props.getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().entrySet())
			for (const kurs of es.getValue())
				liste.push({name: es.getKey(), kurs});
		return liste;
	})

	const selected = ref<Item[]>(zuordnungen.value);
	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function removeZuordnung() {
		showModal().value = false;
		//TODO mit multiple-Aufruf ersetzen #1431
		for (const i of selected.value)
			await props.removeKursSchuelerZuordnung(i.name, i.kurs.id);
		selected.value = [];
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
