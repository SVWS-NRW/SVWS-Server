<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show">
		<template #modalTitle>Standardsortierung anwenden</template>
		<template #modalContent>
			Soll die Reihenfolge der Sortierung anhand der Jahrgangsortierung angepasst werden? Dabei werden unter Umständen bereits festgelegte Sortierreihenfolgen der Klassen unwiderruflich verändert.
			<br>Die Standardsortierung orientiert sich dabei an der Jahrgangsortierung und -Parallelität und wird im Katalog der Jahrgänge festgelegt.
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="sortiere"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	import { useKlassenState } from "~/states/klassen/KlassenState";

	const klassenState = useKlassenState();

	const show = ref<boolean>(false);

	function openModal() {
		show.value = true;
	}

	async function sortiere() {
		show.value = false;
		await klassenState.setzeDefaultSortierung();
	}

</script>
