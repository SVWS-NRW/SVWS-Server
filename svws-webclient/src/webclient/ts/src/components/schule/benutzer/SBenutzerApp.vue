<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ anzeigename }}</span>
				<svws-ui-badge title="ID" variant="light">{{ id }}</svws-ui-badge>
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

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";

	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";

	const props = defineProps<{
		item: ShallowRef<BenutzerListeEintrag | undefined>;
	}>();

	const selectedRoute = routeSchuleBenutzer.childRouteSelector;
	const children_hidden = routeSchuleBenutzer.children_hidden();

	const id: ComputedRef<string> = computed(() => props.item.value?.id.toString() || "?");
	const anzeigename: ComputedRef<string> = computed(() => props.item.value?.anzeigename.toString() || "---");
	const name: ComputedRef<string> = computed(() => props.item.value?.name.toString() || "---");

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeSchuleBenutzer.hidden()) && (props.item.value !== undefined);
	});

</script>
