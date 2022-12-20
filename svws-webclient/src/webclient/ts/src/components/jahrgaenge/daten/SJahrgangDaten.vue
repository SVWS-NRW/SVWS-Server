<template>
	<div v-if="visible" class="app-container">
		<s-card-jahrgang-basisdaten />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";

	const SCardJahrgangBasisdaten = defineAsyncComponent(
		() => import("~/components/jahrgaenge/daten/SCardJahrgangBasisdaten.vue")
	);

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.afaecher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
