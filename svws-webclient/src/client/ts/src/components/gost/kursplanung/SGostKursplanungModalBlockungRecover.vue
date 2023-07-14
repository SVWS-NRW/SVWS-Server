<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" size="small" class="hidden">
		<template #modalTitle>Blockung Wiederherstellen</template>
		<template #modalDescription>
			Soll eine Blockung aus bestehenden Leistungsdaten wiederhergestellt werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal()">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_recovery">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from 'vue';


	const props = defineProps<{
		restoreBlockung: () => Promise<void>;
	}>();

	async function activate_recovery() {
		modal.value.closeModal();
		await props.restoreBlockung();
	}

	const modal = ref();
	const openModal = () => {
		modal.value.openModal();
	}
</script>
