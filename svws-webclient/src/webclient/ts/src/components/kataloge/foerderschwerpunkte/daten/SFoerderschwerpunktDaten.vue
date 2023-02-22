<template>
	<div v-if="visible" class="app-container">
		<s-card-foerderschwerpunkt-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeKatalogFoerderschwerpunkteDaten } from "~/router/apps/foerderschwerpunkte/RouteKatalogFoerderschwerpunkteDaten";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { FoerderschwerpunktDatenProps } from "./SFoerderschwerpunktDatenProps";

	const props = defineProps<FoerderschwerpunktDatenProps>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeKatalogFoerderschwerpunkteDaten.hidden());
	});

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
