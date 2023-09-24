<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small">
		<template #modalTitle>Blockung löschen</template>
		<template #modalDescription>
			Soll die Blockung mit allen Ergebnissen gelöscht werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button @click="remove_blockung">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const props = defineProps<{
		removeBlockung: () => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function remove_blockung() {
		showModal().value = false;
		await props.removeBlockung();
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>