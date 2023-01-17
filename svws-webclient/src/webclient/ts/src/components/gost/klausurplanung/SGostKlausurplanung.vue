<template>
	<div v-if="visible" class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-router-vertical-tab-bar :routes="routeGostKlausurplanung.children_records" :hidden="children_hidden" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-vertical-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { RouterView } from "vue-router";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeGostKlausurplanung } from "~/router/apps/gost/RouteGostKlausurplanung";

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
	}>();

	const selectedRoute = routeGostKlausurplanung.getChildRouteSelector();
	const children_hidden = routeGostKlausurplanung.children_hidden();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeGostKlausurplanung.hidden());
	});

</script>
