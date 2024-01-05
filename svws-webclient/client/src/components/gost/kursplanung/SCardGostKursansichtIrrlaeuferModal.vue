<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Ungültige Kurszuordnungen</template>
		<template #modalDescription>
			Sollen folgende fehlerhafte Kurs-Schüler-Zuordnungen entfernt werden?
			<svws-ui-table selectable v-model="auswahl" :items="zuordnungen" />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_ergebnis">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungsergebnisManager } from '@core';
	import { computed, ref } from 'vue';

	const props = defineProps<{
		// TODO reiche das Array mit den Elementen durch
		getErgebnismanager: () => GostBlockungsergebnisManager;
		removeKursSchuelerZuordnung: (id: number, kurs: number) => Promise<boolean>;
	}>();

	const zuordnungen = computed(()=>{
		const arr = [];
		for (const i of props.getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().values())
			arr.push(i);
		return arr;
	})

	const auswahl = ref([]);
	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function activate_ergebnis() {
		showModal().value = false;
		//props.removeKursSchuelerZuordnung()
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
