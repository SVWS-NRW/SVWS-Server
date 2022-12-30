<template>
	<div class="flex h-full flex-row">
		<div v-if="app.dataFoerderschwerpunkt.daten" class="flex w-full flex-col">
			<svws-ui-header :badge="props.id" badge-variant="light" badge-size="normal"><span>{{ props.item?.text }}</span>
				<svws-ui-badge variant="highlight" size="normal"> {{ props.item?.kuerzel }} </svws-ui-badge>
			</svws-ui-header>
			<svws-ui-router-tab-bar :routes="routeKatalogFoerderschwerpunkte.children_records" :hidden="routeKatalogFoerderschwerpunkte.children_hidden" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";

	const props = defineProps<{ id?: number; item?: FoerderschwerpunktEintrag, routename: string }>();

	const main: Main = injectMainApp();

	const app = main.apps.foerderschwerpunkte;
	const selectedRoute = routeKatalogFoerderschwerpunkte.getChildRouteSelector();

</script>
