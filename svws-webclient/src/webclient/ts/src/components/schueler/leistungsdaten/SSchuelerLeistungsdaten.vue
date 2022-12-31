<template>
	<div v-if="visible" class="app-container">
		<s-card-abschnitte />
		<s-card-leistungsdaten />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeSchuelerLeistungsdaten } from "~/router/apps/schueler/RouteSchuelerLeistungsdaten";
	
	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerLeistungsdaten.hidden);
	});

</script>
