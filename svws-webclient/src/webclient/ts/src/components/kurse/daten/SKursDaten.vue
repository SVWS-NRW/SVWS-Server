<template>
	<div v-if="visible" class="app-container">
		<s-card-kurs-basisdaten/>
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";

	const SCardKursBasisdaten = defineAsyncComponent(
		() => import("~/components/kurse/daten/SCardKursBasisdaten.vue")
	);

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.afaecher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
