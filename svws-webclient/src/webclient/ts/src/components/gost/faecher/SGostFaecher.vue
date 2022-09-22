<template>
	<div v-if="app.dataFaecher?.daten && visible" class="app-container">
		<s-card-gost-faecher />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.gostFaecher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
