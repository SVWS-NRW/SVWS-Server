<template>
	<div v-if="visible" class="app-container">
		<s-card-foerderschwerpunkt-daten :data="props.data" />
	</div>
</template>

<script setup lang="ts">

	import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataFoerderschwerpunkt } from "~/apps/kataloge/foerderschwerpunkt/DataFoerderschwerpunkt";
	import { routeKatalogFoerderschwerpunkteDaten } from "~/router/apps/foerderschwerpunkte/RouteKatalogFoerderschwerpunkteDaten";

	const props = defineProps<{ id?: number, item?: FoerderschwerpunktEintrag, data: DataFoerderschwerpunkt, routename: string }>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeKatalogFoerderschwerpunkteDaten.hidden);
	});

</script>
