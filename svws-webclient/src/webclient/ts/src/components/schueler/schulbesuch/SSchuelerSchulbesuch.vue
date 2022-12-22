<template>
	<div v-if="app.schulbesuchsdaten.daten && visible" class="app-container">
		<s-card-schueler-herkunft />
		<s-card-schueler-entlassung />
		<s-card-schueler-wechsel />
		<s-card-schueler-grundschulbesuch />
		<s-card-schueler-sekundarstufe1 />
		<s-card-schueler-merkmale />
		<s-card-schueler-schulen />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";
	import { Main, injectMainApp } from "~/apps/Main.js";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		//TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
