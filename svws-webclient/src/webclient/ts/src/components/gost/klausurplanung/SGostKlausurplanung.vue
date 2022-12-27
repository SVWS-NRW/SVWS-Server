<template>
	<div v-if="visible" class="flex h-full flex-row" >
		<div class="flex w-full flex-col">
			<svws-ui-router-vertical-tab-bar :routes="routeGostKlausurplanung.children_records" :hidden="routeGostKlausurplanung.children_hidden" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-vertical-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { routeGostKlausurplanung } from "~/router/apps/gost/RouteGostKlausurplanung";

	const props = defineProps<{ id?: number; item?: GostJahrgang, routename: string }>();

	const selectedRoute = routeGostKlausurplanung.getChildRouteSelector();

	const visible: ComputedRef<boolean> = computed(() => {
		//TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
