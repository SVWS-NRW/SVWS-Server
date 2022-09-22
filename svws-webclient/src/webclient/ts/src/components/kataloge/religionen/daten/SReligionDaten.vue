<template>
	<div v-if="visible" class="app-container">
		<s-card-religion-daten />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, defineAsyncComponent } from "vue";

	const SCardReligionDaten = defineAsyncComponent(
		() => import("~/components/kataloge/religionen/daten/SCardReligionDaten.vue")
	);

	const visible: ComputedRef<boolean> = computed(() => {
		// TODO: richtige Bedingung einpflegen
		return true;
	});

</script>