<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ anzeigename }}</span>
				<svws-ui-badge title="ID" variant="light">
					<i-ri-fingerprint-line />
					{{ id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-50">{{ name }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeSchuleBenutzer.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-community-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
	import { BenutzerAppProps } from "./SBenutzerAppProps";

	const props = defineProps<BenutzerAppProps>();

	const selectedRoute = routeSchuleBenutzer.childRouteSelector;
	const children_hidden = routeSchuleBenutzer.children_hidden();

	const id: ComputedRef<number | string> = computed(() => props.auswahl?.id ?? "?");
	const anzeigename: ComputedRef<string> = computed(() => props.auswahl?.anzeigename ?? "---");
	const name: ComputedRef<string> = computed(() => props.auswahl?.name ?? "---");

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
