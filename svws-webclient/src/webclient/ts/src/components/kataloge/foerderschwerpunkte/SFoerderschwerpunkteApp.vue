<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ item.value?.text }}</span>
				<svws-ui-badge type="light" title="ID">
					<i-ri-fingerprint-line/>
					{{ item.value?.id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-50">{{ item.value?.kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKatalogFoerderschwerpunkte.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ShallowRef } from "vue";
	import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import { routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";

	const props = defineProps<{
		item: ShallowRef<FoerderschwerpunktEintrag | undefined>;
	}>();

	const selectedRoute = routeKatalogFoerderschwerpunkte.childRouteSelector;
	const children_hidden = routeKatalogFoerderschwerpunkte.children_hidden();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKatalogFoerderschwerpunkte.hidden()) && (props.item.value !== undefined);
	});

</script>
