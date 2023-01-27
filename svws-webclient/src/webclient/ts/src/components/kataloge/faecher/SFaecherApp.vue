<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ item.value?.bezeichnung }}</span>
				<svws-ui-badge type="light" title="ID">
					<i-ri-fingerprint-line/>
					{{ item.value?.id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-50">{{ item.value?.kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKatalogFaecher.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line />
	</div>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { RouterView } from "vue-router";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";

	const props = defineProps<{
		item: ShallowRef<FaecherListeEintrag | undefined>;
		schule: DataSchuleStammdaten;
	}>();

	const selectedRoute = routeKatalogFaecher.childRouteSelector;
	const children_hidden = routeKatalogFaecher.children_hidden();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKatalogFaecher.hidden()) && (props.item.value !== undefined);
	});

</script>
