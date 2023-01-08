<template>
	<div v-if="item.value?.id">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ kuerzel }}</span>
				<svws-ui-badge type="light">{{ "ID: " + item.value?.id }}</svws-ui-badge>
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
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKurse } from "~/router/apps/RouteKurse";

	const { item, mapLehrer } = defineProps<{ 
		item: ShallowRef<KursListeEintrag | undefined>;
		schule: DataSchuleStammdaten;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
	}>();

	const selectedRoute = routeKurse.childRouteSelector;

	const kuerzel: ComputedRef<string> = computed(() => item.value?.kuerzel.toString() || "");

	const inputFachlehrer: ComputedRef<string> = computed(() => {
		const id = routeKurse.liste.ausgewaehlt?.lehrer;
		const leer = "kein Lehrer festgelegt";
		if (!id) 
			return leer;
		const lehrer = mapLehrer.get(id);
		return lehrer?.kuerzel.toString() || leer;
	});

</script>
