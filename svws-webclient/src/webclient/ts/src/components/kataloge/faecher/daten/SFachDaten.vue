<template>
	<div v-if="visible" class="app-container">
		<s-card-fach-basisdaten :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { FachDaten } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { routeFaecherDaten } from "~/router/apps/faecher/RouteKatalogFaecherDaten";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<{
		patch: (data : Partial<FachDaten>) => Promise<void>;
		data: FachDaten;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeFaecherDaten.hidden();
	});

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
