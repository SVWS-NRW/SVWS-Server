<template>
	<div v-if="visible" class="flex" >
		<div class="flex flex-row flex-wrap gap-4">
			Hier entsteht die Klausurplanungsansicht.
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	
	const visible: ComputedRef<boolean> = computed(() => {
		//TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
