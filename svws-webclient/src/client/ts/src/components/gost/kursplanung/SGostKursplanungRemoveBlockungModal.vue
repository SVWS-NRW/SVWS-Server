<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Blockung löschen</template>
		<template #modalDescription>
			Soll die Blockung mit allen Ergebnissen gelöscht werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal()">Abbrechen</svws-ui-button>
			<svws-ui-button @click="remove_blockung">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from 'vue';

	const props = defineProps<{
		removeBlockung: () => Promise<void>;
	}>();

	const modal = ref();

	async function remove_blockung() {
		modal.value.closeModal()
		await props.removeBlockung();
	}

	const openModal = () => {
		modal.value.openModal();
	}
</script>