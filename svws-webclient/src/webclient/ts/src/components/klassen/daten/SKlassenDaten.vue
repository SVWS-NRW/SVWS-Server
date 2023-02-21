<template>
	<div v-if="visible" class="app-container">
		<s-card-klasse-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" @patch="doPatch" />
		<s-card-klasse-klassenleitungen :data="data" :map-lehrer="mapLehrer" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { KlassenDatenProps } from "./SKlassenDatenProps";

	const props = defineProps<KlassenDatenProps>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeKlassenDaten.hidden();
	});

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
