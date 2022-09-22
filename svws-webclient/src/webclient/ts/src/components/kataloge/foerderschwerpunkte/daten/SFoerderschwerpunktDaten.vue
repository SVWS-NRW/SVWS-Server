<template>
	<div v-if="visible" class="app-container">
		<s-card-foerderschwerpunkt-daten />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, defineAsyncComponent } from "vue";

	const SCardFoerderschwerpunktDaten = defineAsyncComponent(
		() => import("~/components/kataloge/foerderschwerpunkte/daten/SCardFoerderschwerpunktDaten.vue")
	);

	const visible: ComputedRef<boolean> = computed(() => {
		// TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
