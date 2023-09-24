<template>
	<svws-ui-modal :show="showModal" size="small">
		<template #modalTitle>Ungültige Schüler-Kurszuordnungen</template>
		<template #modalDescription>
			Die folgenden Schüler haben ungültige Kurszuordnungen in dieser Blockung
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button @click="remove_blockung">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungsergebnisManager } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function remove_blockung() {
		showModal().value = false;
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>