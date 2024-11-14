<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show">
		<template #modalTitle>Standardsortierung anwenden</template>
		<template #modalContent>
			Sollen alle Klassen nach der Standardsortierung sortiert werden, die sich an der Klassenbezeichnung orientiert? Dabei geht die aktuell hinterlegt Sortierreihenfolge verloren.
			<br>Die Standardsortierung orientiert sich an der Jahrgang-Sortierung und Parallelität. Die Jahrgangssortierung wird im Katalog der Jahrgänge festgelegt.
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
		setzeDefaultSortierung: () => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	function openModal() {
		show.value = true;
	}

	async function sortiere() {
		show.value = false;
		await props.setzeDefaultSortierung();
	}

</script>
