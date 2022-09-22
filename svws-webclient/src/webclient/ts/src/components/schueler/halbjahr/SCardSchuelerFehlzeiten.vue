<template>
	<svws-ui-content-card title="Fehlzeiten"> Inhalte </svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const id: ComputedRef<number | undefined> = computed(() => {
		return app.stammdaten.daten?.id;
	});
</script>
