<template>
	<div v-if="app.personaldaten.daten && visible" class="app-container">
		<s-card-lehrer-personaldaten-allgemein />
		<s-card-lehrer-personaldaten-lehraemter />
		<s-card-lehrer-personaldaten-beschaeftigung />
		<s-card-lehrer-personaldaten-anrechnung />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.lehrerPersonaldaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
