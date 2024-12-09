<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>Blockung löschen</template>
		<template #modalDescription>
			Soll die Blockung mit allen Ergebnissen gelöscht werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button @click="remove_blockung">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const props = defineProps<{
		removeBlockung: () => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	async function remove_blockung() {
		show.value = false;
		await props.removeBlockung();
	}

	const openModal = () => {
		show.value = true;
	}

</script>