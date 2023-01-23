<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ item.value?.bezeichnung }}</span>
				<svws-ui-badge variant="light">{{ "ID: " + item.value?.id }}</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-50">{{ item.value?.kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKatalogJahrgaenge.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";

	const props = defineProps<{
		item: ShallowRef<JahrgangsListeEintrag | undefined>;
		schule: DataSchuleStammdaten;
	}>();

	const selectedRoute = routeKatalogJahrgaenge.childRouteSelector;
	const children_hidden = routeKatalogJahrgaenge.children_hidden();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKatalogJahrgaenge.hidden()) && (props.item.value !== undefined);
	});

</script>
