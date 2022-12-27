<template>
	<div v-if="app?.stammdaten.daten" class="flex w-full flex-col h-full">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-16 mr-4 -ml-2">
					<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto ? 'Foto ' + vorname + ' ' + nachname : ''" />
				</div>
				<div>
					<span class="inline-block mr-3"> {{ vorname }} {{ nachname }} </span>
					<svws-ui-badge variant="light"> {{ props.id }} </svws-ui-badge>
					<br/>
					<span class="opacity-50"> {{ inputKlasse }} </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeSchueler.children_records" :hidden="routeSchueler.children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-briefcase-line/>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";
	import { routeSchueler, RouteDataSchueler } from "~/router/apps/RouteSchueler";
	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, routename: string }>();

	const data: RouteDataSchueler = routeSchueler.data;
	const selectedRoute = routeSchueler.getChildRouteSelector();


	const foto: ComputedRef<String | undefined> = computed(() => {
		return data.stammdaten.daten?.foto || undefined;
	});

	const nachname: ComputedRef<string | undefined> = computed(() => {
		return props.item?.nachname.toString();
	});

	const vorname: ComputedRef<string | undefined> = computed(() => {
		return props.item?.vorname.toString();
	});

	const inputKlasse: ComputedRef<string | false> = computed(() => {
		if (props.item === undefined)
			return false;
		const id = props.item.idKlasse;
		const klasse = main.apps.klassen.auswahl.liste.find(k => k.id === id);
		return klasse?.kuerzel?.toString() || false;
	});

</script>
