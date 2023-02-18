<template>
	<div v-if="visible" class="app-container">
		<s-card-jahrgang-basisdaten :data="data" :list-jahrgaenge="listJahrgaenge" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsDaten, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { routeKatalogJahrgaengeDaten } from "~/router/apps/jahrgaenge/RouteKatalogJahrgaengeDaten";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<{
		patch: (data : Partial<JahrgangsDaten>) => Promise<void>;
		data: JahrgangsDaten,
		listJahrgaenge: JahrgangsListeEintrag[];
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeKatalogJahrgaengeDaten.hidden();
	});

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
