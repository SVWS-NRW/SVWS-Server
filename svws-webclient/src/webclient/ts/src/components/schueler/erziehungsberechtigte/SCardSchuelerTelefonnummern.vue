<template>
	<svws-ui-content-card title="Telefonnummern">
		Inhalte
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { Ref, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const id: Ref<number | undefined> = ref(app.stammdaten.daten?.id);
</script>
