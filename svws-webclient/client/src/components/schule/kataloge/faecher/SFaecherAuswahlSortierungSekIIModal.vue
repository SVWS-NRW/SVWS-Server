<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show">
		<template #modalTitle>Standardsortierung für die Sekundarstufe II anwenden</template>
		<template #modalContent>
			Sollen alle Fächer nach der Standardsortierung für die Sekundarstufe II sortiert werden? Dabei geht die aktuell hinterlegt Sortierreihenfolge verloren.
			<br>Die Standardsortierung orientiert an den Fachbereichen der Gymnasialen Oberstufe. Die Sortierung von Fächern, die nicht einem Fachbereich der gymnasialen Oberstufe
			zugeordnet werden können, bleibt erhalten.
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="sortiere"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const props = defineProps<{
		setzeDefaultSortierungSekII: () => Promise<void>;
		setFilter: () => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	const openModal = () => {
		show.value = true;
	};

	async function sortiere() {
		show.value = false;
		await props.setzeDefaultSortierungSekII();
		await props.setFilter();
	}

</script>
