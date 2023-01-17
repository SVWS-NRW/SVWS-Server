<template>
	<div v-if="props.id !== undefined">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ anzeigename }}</span>
				<svws-ui-badge title="ID" variant="light">{{ id }}</svws-ui-badge>
			</div>
			<div v-if="name">
				<span class="opacity-50">{{ name }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeSchuleBenutzer.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-community-line/>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";

	const props = defineProps<{ id?: number; item?: BenutzerListeEintrag, routename: string }>();

	const selectedRoute = routeSchuleBenutzer.getChildRouteSelector();
	const children_hidden = routeSchuleBenutzer.children_hidden();

	const id: ComputedRef<string> = computed(() => "ID: " + props.id);
	const anzeigename: ComputedRef<string> = computed(() => props.item?.anzeigename.toString() || "");
	const name: ComputedRef<string> = computed(() => props.item?.name.toString() || "");

</script>
