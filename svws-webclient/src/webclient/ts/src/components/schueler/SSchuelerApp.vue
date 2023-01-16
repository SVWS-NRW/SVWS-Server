<template>
	<div v-if="visible" class="flex w-full flex-col h-full">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-16 mr-4 -ml-2">
					<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto ? 'Foto ' + vorname + ' ' + nachname : ''" />
				</div>
				<div>
					<span class="inline-block mr-3"> {{ vorname }} {{ nachname }} </span>
					<svws-ui-badge type="light"> {{ item.value?.id }} </svws-ui-badge>
					<br>
					<span class="opacity-50"> {{ inputKlasse ? inputKlasse : '–' }} </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeSchueler.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-briefcase-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ShallowRef } from "vue";

	import { routeSchueler } from "~/router/apps/RouteSchueler";
	import { JahrgangsListeEintrag, KlassenListeEintrag, KursListeEintrag, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { ListKurse } from "~/apps/kurse/ListKurse";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
	import { ListKlassen } from "~/apps/klassen/ListKlassen";

	const { item, stammdaten, mapKlassen } = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten;
		schule: DataSchuleStammdaten;
		listKlassen: ListKlassen;
		mapKlassen: Map<Number, KlassenListeEintrag>;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listKurse: ListKurse;
		mapKurs: Map<Number, KursListeEintrag>;
	}>();

	const selectedRoute = routeSchueler.childRouteSelector;
	const children_hidden = routeSchueler.children_hidden();


	const foto: ComputedRef<String | undefined> = computed(() => {
		return stammdaten.daten?.foto || undefined;
	});

	const nachname: ComputedRef<string | undefined> = computed(() => {
		return item.value?.nachname.toString();
	});

	const vorname: ComputedRef<string | undefined> = computed(() => {
		return item.value?.vorname.toString();
	});

	const inputKlasse: ComputedRef<string | false> = computed(() => {
		if (item.value === undefined)
			return false;
		return mapKlassen.get(item.value?.idKlasse)?.kuerzel?.toString() || false;
	});

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchueler.hidden) && (item !== undefined);
	});

</script>
