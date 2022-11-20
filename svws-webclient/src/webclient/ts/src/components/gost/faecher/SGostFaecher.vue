<template>
	<div v-if="app.dataFaecher?.daten && visible" class="app-container">
		<s-card-gost-faecher />
		<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" />
		<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	import { GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core-ts";

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.gostFaecher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
