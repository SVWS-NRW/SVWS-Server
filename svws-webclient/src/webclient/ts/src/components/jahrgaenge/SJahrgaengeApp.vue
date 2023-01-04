<template>
	<div v-if="app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt !== null">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ props.item?.bezeichnung }}</span>
				<svws-ui-badge type="light">{{ "ID: " + props.id }}</svws-ui-badge>
				<br/>
				<span class="opacity-50">{{ props.item?.kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKatalogJahrgaenge.children_records" :hidden="routeKatalogJahrgaenge.children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line/>
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { RouteDataKatalogJahrgaenge, routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";

	const props = defineProps<{ id?: number; item?: JahrgangsListeEintrag, routename: string }>();

	const data: RouteDataKatalogJahrgaenge = routeKatalogJahrgaenge.data;
	const selectedRoute = routeKatalogJahrgaenge.getChildRouteSelector();

	const main: Main = injectMainApp();
	const app = main.apps.jahrgaenge;

</script>
