<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Blockung Wiederherstellen</template>
		<template #modalDescription>
			Soll eine Blockung aus bestehenden Leistungsdaten wiederhergestellt werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_recovery">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const props = defineProps<{
		restoreBlockung: () => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function activate_recovery() {
		showModal().value = false;
		await props.restoreBlockung();
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
