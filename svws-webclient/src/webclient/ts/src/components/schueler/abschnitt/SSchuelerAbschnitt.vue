<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-halbjahr />
		<s-card-schueler-zeugnis-abschluss />
		<s-card-schueler-abschluss-berechnung />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerAbschnitt.hidden);
	});

</script>
