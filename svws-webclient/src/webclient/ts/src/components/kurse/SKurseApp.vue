<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ kuerzel }}</span>
				<svws-ui-badge type="light" title="ID">
					<i-ri-fingerprint-line />
					{{ auswahl?.id }}
				</svws-ui-badge>
			</div>
			<div v-if="inputFachlehrer">
				<span class="opacity-50">{{ inputFachlehrer }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKurse.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-slideshow-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeKurse } from "~/router/apps/RouteKurse";
	import { KurseAppProps } from "./SKurseAppProps";

	const props = defineProps<KurseAppProps>();

	const selectedRoute = routeKurse.childRouteSelector;
	const children_hidden = routeKurse.children_hidden();

	const kuerzel: ComputedRef<string> = computed(() => props.auswahl?.kuerzel ?? "");

	const inputFachlehrer: ComputedRef<string> = computed(() => {
		const id = props.auswahl?.lehrer;
		const leer = "kein Lehrer festgelegt";
		if (!id)
			return leer;
		const lehrer = props.mapLehrer.get(id);
		return lehrer?.kuerzel ?? leer;
	});

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
