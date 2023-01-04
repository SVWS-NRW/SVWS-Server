<template>
	<div v-if="props.id !== undefined">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3 capitalize">{{ props.item?.text }}</span>
				<svws-ui-badge type="light">{{ "ID: " + props.id }}</svws-ui-badge>
				<br/>
				<span class="opacity-50">{{ props.item?.kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKatalogReligion.children_records" :hidden="routeKatalogReligion.children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line/>
	</div>
</template>

<script setup lang="ts">

	import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
	import { RouteDataKatalogReligion, routeKatalogReligion } from "~/router/apps/RouteKatalogReligion";

	const props = defineProps<{ id?: number; item?: ReligionEintrag, routename: string }>();

	const data: RouteDataKatalogReligion = routeKatalogReligion.data;
	const selectedRoute = routeKatalogReligion.getChildRouteSelector();

</script>
