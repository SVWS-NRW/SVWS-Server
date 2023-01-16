<template>
	<div v-if="bezeichnung_abiturjahr" class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header>
				<span class="inline-block mr-3">{{ bezeichnung_abiturjahr }}</span>
				<br>
				<span class="opacity-50">{{ jahrgang ? jahrgang : '–' }}</span>
			</svws-ui-header>
			<svws-ui-router-tab-bar :routes="routeGost.children_records" :hidden="children_hidden" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-tab-bar>
		</div>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1.2em" height="1.2em">
			<g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
				stroke-width="2">
				<path
					d="m2.573 8.463l8.659-4.329a.6.6 0 0 1 .536 0l8.659 4.33a.6.6 0 0 1 0 1.073l-8.659 4.329a.6.6 0 0 1-.536 0l-8.659-4.33a.6.6 0 0 1 0-1.073Z" />
				<path
					d="M22.5 13V9.5l-2-1m-16 2v5.412a2 2 0 0 0 1.142 1.807l5 2.374a2 2 0 0 0 1.716 0l5-2.374a2 2 0 0 0 1.142-1.807V10.5" />
			</g>
		</svg>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { RouterView } from "vue-router";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

	import { routeGost } from "~/router/apps/RouteGost";

	const { item } = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
	}>();

	const selectedRoute = routeGost.getChildRouteSelector();
	const children_hidden = routeGost.children_hidden();


	const jahrgang: ComputedRef<string | undefined> = computed(() => {
		return item.value?.jahrgang?.toString();
	});

	const bezeichnung_abiturjahr: ComputedRef<string | undefined> = computed(() => {
		return item.value?.bezeichnung?.toString();
	});

</script>
