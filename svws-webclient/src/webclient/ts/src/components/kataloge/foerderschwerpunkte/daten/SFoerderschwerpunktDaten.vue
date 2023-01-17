<template>
	<div v-if="visible" class="app-container">
		<s-card-foerderschwerpunkt-daten :data="data" />
	</div>
</template>

<script setup lang="ts">

	import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataFoerderschwerpunkt } from "~/apps/kataloge/foerderschwerpunkt/DataFoerderschwerpunkt";
	import { routeKatalogFoerderschwerpunkteDaten } from "~/router/apps/foerderschwerpunkte/RouteKatalogFoerderschwerpunkteDaten";

	const props = defineProps<{
		item: ShallowRef<FoerderschwerpunktEintrag | undefined>;
		data: DataFoerderschwerpunkt;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeKatalogFoerderschwerpunkteDaten.hidden());
	});

</script>
