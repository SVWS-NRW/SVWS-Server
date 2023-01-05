<template>
	<div v-if="item?.id">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ kuerzel }}</span>
				<svws-ui-badge type="light">{{ "ID: " + item?.id }}</svws-ui-badge>
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

	import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKurse } from "~/router/apps/RouteKurse";

	const { item, mapLehrer } = defineProps<{ 
		id?: number;
		item?: KursListeEintrag;
		schule: DataSchuleStammdaten;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
		routename: string;
	}>();

	const selectedRoute = routeKurse.getChildRouteSelector();

	const kuerzel: ComputedRef<string> = computed(() => item?.kuerzel.toString() || "");

	const inputFachlehrer: ComputedRef<string> = computed(() => {
		const id = routeKurse.data.auswahl.ausgewaehlt?.lehrer;
		const leer = "kein Lehrer festgelegt";
		if (!id) 
			return leer;
		const lehrer = mapLehrer.get(id);
		return lehrer?.kuerzel.toString() || leer;
	});

</script>
