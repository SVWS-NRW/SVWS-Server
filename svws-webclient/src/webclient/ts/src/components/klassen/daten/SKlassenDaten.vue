<template>
	<div v-if="visible" class="app-container">
		<s-card-klasse-basisdaten/>
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";

	const SCardKlasseBasisdaten = defineAsyncComponent(
		() => import("~/components/klassen/daten/SCardKlasseBasisdaten.vue")
	);

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.afaecher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
