<template>
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Ungültige Schüler-Kurszuordnungen</template>
		<template #modalDescription>
			Die folgenden Schüler haben ungültige Kurszuordnungen in dieser Blockung
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal()">Abbrechen</svws-ui-button>
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

	const modal = ref();

	async function remove_blockung() {
		modal.value.closeModal()
	}

	const openModal = () => {
		modal.value.openModal();
	}

</script>