<template>
	<div v-if="visible" class="app-container">
		<s-card-jahrgang-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeKatalogJahrgaengeDaten } from "~/router/apps/jahrgaenge/RouteKatalogJahrgaengeDaten";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { JahrgangDatenProps } from "./SJahrgangDatenProps";

	const props = defineProps<JahrgangDatenProps>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeKatalogJahrgaengeDaten.hidden();
	});

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
