<template>
	<div v-if="visible" class="app-container">
		<s-card-fach-basisdaten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeKatalogFaecherDaten } from "~/router/apps/faecher/RouteKatalogFaecherDaten";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";
	import { FachDatenProps } from "./SFachDatenProps";

	const props = defineProps<FachDatenProps>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeKatalogFaecherDaten.hidden();
	});

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
