<template>
	<div class="flex h-full flex-row">
		<div v-if="visible" class="flex w-full flex-col">
			<svws-ui-header>
				<div class="flex items-center">
					<span class="inline-block mr-3">{{ item.value?.text }}</span>
					<svws-ui-badge type="light">{{ "ID: " + item.value?.id }}</svws-ui-badge>
				</div>
				<span class="opacity-50">{{ item.value?.kuerzel }}</span>
			</svws-ui-header>
			<svws-ui-router-tab-bar :routes="routeKatalogFoerderschwerpunkte.children_records" :hidden="children_hidden" v-model="selectedRoute">
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
	const children_hidden = routeKatalogFoerderschwerpunkte.children_hidden();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKatalogFoerderschwerpunkte.hidden()) && (item.value !== undefined);
	});

</script>
