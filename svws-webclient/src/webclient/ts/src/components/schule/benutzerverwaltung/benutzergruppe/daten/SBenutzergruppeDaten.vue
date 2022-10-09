<template>
	<div v-if="visible" class="app-container">
		<s-card-benutzer-gruppe-daten />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";

	const SCardBenutzerGruppeDaten = defineAsyncComponent(
		() =>
			import(
				"~/components/schule/benutzerverwaltung/benutzergruppe/daten/SCardBenutzergruppeDaten.vue"
			)
	);

	const visible: ComputedRef<boolean> = computed(() => {
		// TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
