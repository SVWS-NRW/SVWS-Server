<template>
	<div v-if="app.dataJahrgang.daten && visible" class="app-container">
		<s-card-gost-beratungslehrer v-if="enabled" />
		<div>
			<s-card-gost-text-beratungsbogen />
			<s-card-gost-text-mailversand />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const enabled: ComputedRef<boolean> = computed(() => {
		return (
			!!app.dataJahrgang.daten?.abiturjahr &&
			app.dataJahrgang.daten?.abiturjahr > 0
		);
	});

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.gostStammdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
