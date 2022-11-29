<template>
	<div v-if="app.dataFaecher?.daten && visible" class="s-gost-faecher--wrapper">
		<div class="w-128">
			<s-card-gost-faecher />
		</div>
		<div>
			<s-card-gost-zusatzkurse />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.VERBOTEN" />
			<s-card-gost-fachkombinationen :typ="GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH" />
		</div>
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

<style>
	.s-gost-faecher--wrapper {
		@apply grid grid-cols-1 gap-8 gap-x-8 gap-y-4 2xl:grid-cols-2;
	}
</style>