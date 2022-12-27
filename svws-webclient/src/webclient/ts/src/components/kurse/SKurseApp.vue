<template>
	<div v-if="appKurse.auswahl.ausgewaehlt">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ kuerzel }}</span>
				<svws-ui-badge variant="light">{{ "ID: " + props.id }}</svws-ui-badge>
				<span v-if="inputFachlehrer" class="opacity-50"><br/>{{ inputFachlehrer }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKurse.children_records" :hidden="routeKurse.children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-slideshow-line/>
	</div>
</template>

<script setup lang="ts">

	import { KursListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { RouteDataKurse, routeKurse } from "~/router/apps/RouteKurse";

	const props = defineProps<{ id?: number; item?: KursListeEintrag, routename: string }>();

	const data: RouteDataKurse = routeKurse.data;
	const selectedRoute = routeKurse.getChildRouteSelector();

	const main: Main = injectMainApp();

	const appKurse = main.apps.kurse;
	const appLehrer = main.apps.lehrer;

	const kuerzel: ComputedRef<string> = computed(() => props.item?.kuerzel.toString() || "");

	const inputFachlehrer: ComputedRef<string> = computed(() => {
		const id = appKurse.auswahl.ausgewaehlt?.lehrer;
		const leer = "kein Lehrer festgelegt";
		if (!id) 
			return leer;
		const lehrer = appLehrer.auswahl.liste.find(l => l.id === id);
		return lehrer?.kuerzel.toString() || leer;
	});

</script>
