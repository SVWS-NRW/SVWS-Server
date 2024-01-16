<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="()=>ref(showModal)">
		<template #modalTitle>Standardsortierung anwenden</template>
		<template #modalContent>
			Sollen alle Klassen nach der Standardsortierung sortiert werden, die sich an der Klassenbezeichnung orientiert? Dabei geht die aktuell hinterlegt Sortierreihenfolge verloren.
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="sortiere"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const props = defineProps<{
		setzeDefaultSortierung: () => Promise<void>;
	}>();

	const showModal = ref<boolean>(false);

	const openModal = () => {
		showModal.value = true;
	}

	async function sortiere() {
		showModal.value = false;
		await props.setzeDefaultSortierung();
	}

</script>
