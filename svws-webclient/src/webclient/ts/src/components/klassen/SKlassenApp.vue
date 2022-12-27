<template>
	<div v-if="app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt !== null">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ inputKuerzel }}</span>
				<svws-ui-badge variant="light">{{ "ID: " + props.id }}</svws-ui-badge>
				<br/>
				<div class="separate-items--custom">
					<span v-for="(l, i) in inputKlassenlehrer" :key="i" class="opacity-50"> {{ l.kuerzel }} </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKlassen.children_records" :hidden="routeKlassen.children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-group-line/>
	</div>
</template>

<script setup lang="ts">

	import { KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { RouteDataKlassen, routeKlassen } from "~/router/apps/RouteKlassen";

	const props = defineProps<{ id?: number; item?: KlassenListeEintrag, routename: string }>();

	const data: RouteDataKlassen = routeKlassen.data;
	const selectedRoute = routeKlassen.getChildRouteSelector();

	const main: Main = injectMainApp();
	const app = main.apps.klassen;
	const appLehrer = main.apps.lehrer;

	const inputKuerzel: ComputedRef<string | null> = computed(() => {
		if (app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt !== null) {
			return String(app.auswahl.ausgewaehlt.kuerzel);
		} else if (app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt === null) {
			return null;
		}
		return "";
	});

	const inputKlassenlehrer: ComputedRef<Array<LehrerListeEintrag>> = computed(() => {
		const liste: Array<LehrerListeEintrag> = [];
		const ids = app.auswahl.ausgewaehlt?.klassenLehrer || [];
		for (const id of ids) {
			const lehrer = appLehrer.auswahl.liste.find(l => l.id === id);
			if (lehrer) 
				liste.push(lehrer);
		}
		return liste;
	});

</script>
