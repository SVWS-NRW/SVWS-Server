<template>
	<div v-if="visible" class="app-container">
		<s-card-religion-daten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeKatalogReligionDaten } from "~/router/apps/religion/RouteKatalogReligionDaten";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { ReligionDatenProps } from "./SReligionDatenProps";

	const props = defineProps<ReligionDatenProps>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeKatalogReligionDaten.hidden();
	});

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)
</script>