<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3 capitalize">{{ item.value?.text }}</span>
				<svws-ui-badge type="light" title="ID">
					<i-ri-fingerprint-line />
					{{ item.value?.id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-50">{{ item.value?.kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKatalogReligion.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ShallowRef } from "vue";
	import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
	import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligion";

	const props = defineProps<{
		item: ShallowRef<ReligionEintrag | undefined>;
	}>();

	const selectedRoute = routeKatalogReligion.childRouteSelector;
	const children_hidden = routeKatalogReligion.children_hidden();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKatalogReligion.hidden()) && (props.item.value !== undefined);
	});

</script>
