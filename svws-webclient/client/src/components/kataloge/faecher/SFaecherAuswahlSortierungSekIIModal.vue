<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="()=>ref(showModal)">
		<template #modalTitle>Standardsortierung für die Sekundarstufe II anwenden</template>
		<template #modalContent>
			Sollen alle Fächer nach der Standardsortierung für die Sekundarstufe II sortiert werden? Dabei geht die aktuell hinterlegt Sortierreihenfolge verloren.
			<br>Die Standardsortierung orientiert an den Fachbereichen der Gymnasialen Oberstufe. Die Sortierung von Fächern, die nicht einem Fachbereich der gymnasialen Oberstufe
			zugeordnet werden können, bleibt erhalten.
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
		setzeDefaultSortierungSekII: () => Promise<void>;
	}>();

	const showModal = ref<boolean>(false);

	const openModal = () => {
		showModal.value = true;
	}

	async function sortiere() {
		showModal.value = false;
		await props.setzeDefaultSortierungSekII();
	}

</script>
