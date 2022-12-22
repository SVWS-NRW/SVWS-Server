<template>
	<div v-if="app.stammdaten.daten && visible" class="app-container">
		<s-card-schueler-basisdaten />
		<s-card-schueler-kontaktdaten />
		<s-card-schueler-staat-religion />
		<s-card-schueler-migrationshintergrund />
		<s-card-schueler-foerderbedarf />
		<s-card-schueler-statusdaten />
		<s-card-schueler-bemerkungen />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.stammdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
