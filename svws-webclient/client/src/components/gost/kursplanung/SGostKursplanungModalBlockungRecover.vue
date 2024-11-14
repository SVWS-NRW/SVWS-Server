<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small" class="hidden">
		<template #modalTitle>Blockung Wiederherstellen</template>
		<template #modalDescription>
			Soll eine Blockung aus bestehenden Leistungsdaten wiederhergestellt werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_recovery">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const props = defineProps<{
		restoreBlockung: () => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	async function activate_recovery() {
		show.value = false;
		await props.restoreBlockung();
	}

	const openModal = () => {
		show.value = true;
	}

</script>
