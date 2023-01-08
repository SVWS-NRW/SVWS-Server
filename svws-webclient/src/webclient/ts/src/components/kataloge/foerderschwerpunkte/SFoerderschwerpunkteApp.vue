<template>
	<div class="flex h-full flex-row">
		<div v-if="visible" class="flex w-full flex-col">
			<svws-ui-header :badge="item.value?.id.toString()" badge-type="light" badge-size="normal"><span>{{ item.value?.text }}</span>
				<svws-ui-badge type="highlight" size="normal"> {{ item.value?.kuerzel }} </svws-ui-badge>
			</svws-ui-header>
			<svws-ui-router-tab-bar :routes="routeKatalogFoerderschwerpunkte.children_records" :hidden="routeKatalogFoerderschwerpunkte.children_hidden" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ShallowRef } from "vue";
	import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import { routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";

	const { item } = defineProps<{ 
		item: ShallowRef<FoerderschwerpunktEintrag | undefined>;
	}>();

	const selectedRoute = routeKatalogFoerderschwerpunkte.childRouteSelector;

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKatalogFoerderschwerpunkte.hidden) && (item.value !== undefined);
	});

</script>
