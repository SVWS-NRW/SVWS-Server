<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header :badge="id" badge-type="light" badge-size="normal">
				<span> {{ anzeigename }} </span>
			</svws-ui-header>
			<svws-ui-router-tab-bar class="w-full" :routes="routeSchuleBenutzer.children_records" :hidden="routeSchuleBenutzer.children_hidden" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";

	const props = defineProps<{ id?: number; item?: BenutzerListeEintrag, routename: string }>();

	const selectedRoute = routeSchuleBenutzer.getChildRouteSelector();

	const id: ComputedRef<string> = computed(() => "ID: " + props.id);
	const anzeigename: ComputedRef<string> = computed(() => props.item?.anzeigename.toString() || "");

</script>
