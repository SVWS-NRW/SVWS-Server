<template>
	<div v-if="props.id !== undefined">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ props.item?.bezeichnung }}</span>
				<svws-ui-badge type="light">{{ "ID: " + props.id }}</svws-ui-badge>
			</div>
			<span class="opacity-50">{{ props.item?.kuerzel }}</span>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKatalogFaecher.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line/>
	</div>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { RouteDataKatalogFaecher, routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";

	const props = defineProps<{ id?: number; item?: FaecherListeEintrag, routename: string }>();

	const data: RouteDataKatalogFaecher = routeKatalogFaecher.data;
	const selectedRoute = routeKatalogFaecher.getChildRouteSelector();
	const children_hidden = routeKatalogFaecher.children_hidden();

</script>
