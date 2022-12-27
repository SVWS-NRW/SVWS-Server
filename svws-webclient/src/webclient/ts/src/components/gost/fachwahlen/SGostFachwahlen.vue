<template>
	<div v-if="inputAbiturjahr && inputAbiturjahr !== 'Allgemein' && app.dataFachwahlen.daten && visible" class="app-container">
		<s-card-gost-fachwahlen />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const inputAbiturjahr: ComputedRef<string | undefined> = computed(() => {
		return app.auswahl.ausgewaehlt?.bezeichnung?.toString();
	});

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.fachwahlen.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
