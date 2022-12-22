<template>
	<div v-if="app.listAbschnitte.liste && visible" class="app-container">
		<s-card-abschnitte />
		<s-card-leistungsdaten />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	
	const visible = computed(() => {
		//return this.$app.stammdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
