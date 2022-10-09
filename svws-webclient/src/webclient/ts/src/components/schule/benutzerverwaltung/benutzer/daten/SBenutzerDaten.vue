<template>
	<div v-if="visible" class="app-container">
		<s-card-benutzer-daten />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";

	const SCardBenutzerDaten = defineAsyncComponent(
		() =>
			import(
				"~/components/schule/benutzerverwaltung/benutzer/daten/SCardBenutzerDaten.vue"
			)
	);

	const visible: ComputedRef<boolean> = computed(() => {
		// TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
