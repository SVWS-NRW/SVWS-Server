<template>
	<div v-if="visible" class="app-container">
		<s-card-foerderschwerpunkt-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { routeKatalogFoerderschwerpunkteDaten } from "~/router/apps/foerderschwerpunkte/RouteKatalogFoerderschwerpunkteDaten";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<{
		patch: (data : Partial<FoerderschwerpunktEintrag>) => Promise<void>;
		data: FoerderschwerpunktEintrag;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeKatalogFoerderschwerpunkteDaten.hidden());
	});

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
