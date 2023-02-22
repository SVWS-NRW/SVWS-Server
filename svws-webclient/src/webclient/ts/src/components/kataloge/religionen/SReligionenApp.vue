<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3 capitalize">{{ auswahl?.text }}</span>
				<svws-ui-badge type="light" title="ID">
					<i-ri-fingerprint-line />
					{{ auswahl?.id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-50">{{ auswahl?.kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKatalogReligion.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligionen";
	import { ReligionenAppProps } from "./SReligionenAppProps";

	const props = defineProps<ReligionenAppProps>();

	const selectedRoute = routeKatalogReligion.childRouteSelector;
	const children_hidden = routeKatalogReligion.children_hidden();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKatalogReligion.hidden()) && (props.auswahl !== undefined);
	});

</script>
